/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leong.nimbus.clouds.google.drive;

import com.google.api.client.googleapis.media.MediaHttpUploaderProgressListener;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.ParentReference;
import com.leong.nimbus.clouds.interfaces.ICloudController;
import com.leong.nimbus.utils.Tools;
import edu.stanford.ejalbert.BrowserLauncher;
import edu.stanford.ejalbert.exception.BrowserLaunchingInitializingException;
import edu.stanford.ejalbert.exception.UnsupportedOperatingSystemException;
import java.awt.Component;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;

/**
 *
 * @author henry
 */
public class GDriveController implements ICloudController
{
    private final GDriveModel m_model;

    private final Comparator<File> m_comparatorFiles;
    private final Map<String, List<File>> m_cachedListFiles;
    private final Map<String, File> m_cachedFiles;

    private String m_currentPathID = GDriveConstants.FOLDER_ROOT;

    public GDriveController()
    {
        Tools.logit("GDriveController.ctor()");

        m_model = new GDriveModel();

        m_comparatorFiles = new Comparator<File>()
        {
            @Override
            public int compare(File f1, File f2)
            {
                boolean f1_isdir = f1.getMimeType().equals(GDriveConstants.MIME_TYPE_FOLDER);
                boolean f2_isdir = f2.getMimeType().equals(GDriveConstants.MIME_TYPE_FOLDER);

                if (f1_isdir ^ f2_isdir)
                {
                    return f1_isdir ? -1 : 1;
                }

                return f1.getTitle().compareTo(f2.getTitle());
            }
        };

        m_cachedListFiles = new HashMap<>();
        m_cachedFiles = new HashMap<>();
    }

    public boolean login(Component parentComponent)
    {
        Tools.logit("GDriveController.login()");

        String authUrl = m_model.getAuthUrl();

        try
        {
            // TODO: does not work with OSX -- i think...
            BrowserLauncher launcher = new BrowserLauncher();
            launcher.setNewWindowPolicy(true);

            Tools.logit("Opening new browser to "+authUrl);
            launcher.openURLinBrowser(authUrl);
        }
        catch (BrowserLaunchingInitializingException e)
        {
            e.printStackTrace();
        }
        catch (UnsupportedOperatingSystemException e)
        {
            e.printStackTrace();
        }

        String authCode = JOptionPane.showInputDialog(parentComponent, "Input the authentication code here");

        if (Tools.isNullOrEmpty(authCode))
        {
            Tools.isNullOrEmpty("Auth code not valid");
            return false;
        }

        authCode = authCode.trim();
        Tools.logit("Auth code is "+authCode);

        return m_model.login(authCode);
    }

    public File getParentFile(String fileID)
    {
        File file = getFile(fileID);

        if (!file.getParents().isEmpty())
        {
            String parentID = file.getParents().get(0).getId();
            return getFile(parentID);
        }

        return null;
    }

    public File getFile(String fileID)
    {
        if (fileID.equals(GDriveConstants.FOLDER_ROOT))
        {
            fileID = m_model.getRootID();
        }

        if (m_cachedFiles.containsKey(fileID))
        {
            Tools.logit("GDriveController.getFile() Cache hit '"+fileID+"'");
            return m_cachedFiles.get(fileID);
        }

        File file = m_model.getFile(fileID);

        if (!Tools.isNullOrEmpty(fileID) && (file != null))
        {
            Tools.logit("GDriveController.getFile() Add cache '"+fileID+"'");
            m_cachedFiles.put(fileID, file);
        }

        return file;
    }

    public List<File> getFiles(String fileID, boolean forceRefresh)
    {
        if (fileID.equals(GDriveConstants.FOLDER_ROOT))
        {
            fileID = m_model.getRootID();
        }

        m_currentPathID = fileID;

        if (!forceRefresh && m_cachedListFiles.containsKey(fileID))
        {
            Tools.logit("GDriveController.getFiles() Cache hit '"+fileID+"'");
            return m_cachedListFiles.get(fileID);
        }

        List<File> files =  m_model.getFiles(fileID);

        Collections.sort(files, m_comparatorFiles);

        Tools.logit("GDriveController.getFiles() Add cache '"+fileID+"'");
        m_cachedListFiles.put(fileID, files);

        return files;
    }

    public String getCurrentPathID()
    {
        return m_currentPathID;
    }

    public File generateMetadata(String parentID, java.io.File content)
    {
        String mimeType = URLConnection.guessContentTypeFromName(content.getName());

        Tools.logit("GDriveController.generateMetadata() File="+content.getName()+" Mime="+mimeType);

        ParentReference parent = new ParentReference();
        parent.setId(parentID);

        File metadata = new File();
        metadata.setTitle(content.getName());
        metadata.setFileSize(content.length());
        metadata.setMimeType(mimeType);
        metadata.setParents(Arrays.asList(parent));

        return metadata;
    }

    public File uploadLocalFile(File metadata, java.io.File content, MediaHttpUploaderProgressListener progressListener)
    {
        InputStream input = null;

        try
        {
            input = new BufferedInputStream(new FileInputStream(content));

            File uploadedFile = m_model.uploadLocalFile(metadata, input, progressListener);
            return uploadedFile;
        }
        catch (FileNotFoundException ex)
        {
            //Logger.getLogger(GDriveController.class.getName()).log(Level.SEVERE, null, ex);
            Tools.logit("File not found: "+ex.toString());
        }
        finally
        {
            try
            {
                input.close();
            }
            catch (IOException ex)
            {
                //Logger.getLogger(GDriveController.class.getName()).log(Level.SEVERE, null, ex);
                Tools.logit("File fail to close. "+ex.toString());
            }
        }

        return null;
    }
}
