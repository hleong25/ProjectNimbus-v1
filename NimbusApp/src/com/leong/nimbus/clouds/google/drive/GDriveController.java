/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leong.nimbus.clouds.google.drive;

import com.google.api.services.drive.model.File;
import com.leong.nimbus.clouds.interfaces.ICloudController;
import com.leong.nimbus.utils.Tools;
import edu.stanford.ejalbert.BrowserLauncher;
import edu.stanford.ejalbert.exception.BrowserLaunchingInitializingException;
import edu.stanford.ejalbert.exception.UnsupportedOperatingSystemException;
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

    private final Map<String, List<File>> m_cachedListFiles;
    private final Map<String, File> m_cachedFiles;

    public GDriveController()
    {
        Tools.logit("GDriveController.ctor()");

        m_model = new GDriveModel();

        m_cachedListFiles = new HashMap<>();
        m_cachedFiles = new HashMap<>();
    }

    public boolean login()
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

        String authCode = JOptionPane.showInputDialog("Input the authentication code here");

        if (Tools.isNullOrEmpty(authCode))
        {
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
            m_cachedFiles.put(fileID, file);
        }

        return file;
    }

    public List<File> getFiles(String fileID)
    {
        if (fileID.equals(GDriveConstants.FOLDER_ROOT))
        {
            fileID = m_model.getRootID();
        }

        if (m_cachedListFiles.containsKey(fileID))
        {
            Tools.logit("GDriveController.getFiles() Cache hit '"+fileID+"'");
            return m_cachedListFiles.get(fileID);
        }

        List<File> files =  m_model.getFiles(fileID);

        Collections.sort(files, new Comparator<File>()
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
        });

        m_cachedListFiles.put(fileID, files);

        return files;
    }
}
