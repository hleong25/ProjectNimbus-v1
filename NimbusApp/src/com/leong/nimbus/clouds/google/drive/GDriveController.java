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
import com.leong.nimbus.utils.Logit;
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
public class GDriveController implements ICloudController<com.google.api.services.drive.model.File>
{
    private static final Logit Log = Logit.create(GDriveController.class.getName());

    private final GDriveModel m_model = new GDriveModel();

    private final Comparator<File> m_comparatorFiles;
    private final Map<File, List<File>> m_cachedListFiles = new HashMap<>();
    private final Map<String, File> m_cachedFiles = new HashMap<>();

    private File m_currentPath;

    public GDriveController()
    {
        Log.entering("<init>");

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
    }

    public boolean login(Component parentComponent)
    {
        Log.entering("login", parentComponent);

        String authUrl = m_model.getAuthUrl();

        try
        {
            // TODO: does not work with OSX -- i think...
            BrowserLauncher launcher = new BrowserLauncher();
            launcher.setNewWindowPolicy(true);

            Log.fine("Opening new browser to "+authUrl);
            launcher.openURLinBrowser(authUrl);
        }
        catch (BrowserLaunchingInitializingException | UnsupportedOperatingSystemException ex)
        {
            Log.exception(ex);
        }

        String authCode = JOptionPane.showInputDialog(parentComponent, "Input the authentication code here");

        if (Tools.isNullOrEmpty(authCode))
        {
            Log.severe("Auth code not valid.");
            return false;
        }

        authCode = authCode.trim();
        Log.info("Auth code: "+authCode);

        return m_model.login(authCode);
    }

    public File getCurrentPath()
    {
        return m_currentPath;
    }

    public File generateMetadata(File parent, java.io.File content)
    {
        Log.entering("generateMetadata", new Object[]{parent, content});

        String mimeType = URLConnection.guessContentTypeFromName(content.getName());

        Log.fine("Mime: "+mimeType);

        ParentReference parentRef = new ParentReference();
        parentRef.setId(parent.getId());

        File metadata = new File();
        metadata.setTitle(content.getName());
        metadata.setFileSize(content.length());
        metadata.setMimeType(mimeType);
        metadata.setParents(Arrays.asList(parentRef));

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
            Log.exception(ex);
        }
        finally
        {
            try
            {
                input.close();
            }
            catch (IOException ex)
            {
                Log.exception(ex);
            }
        }

        return null;
    }

    @Override
    public File getRoot()
    {
        File root = m_model.getRoot();
        return root;
    }

    @Override
    public File getItemById(String id, boolean useCache)
    {
        if (useCache && m_cachedFiles.containsKey(id))
        {
            Log.info("Cache hit: "+id);
            return m_cachedFiles.get(id);
        }

        File file;

        if (id.equals(GDriveConstants.FOLDER_ROOT))
        {
            file = m_model.getRoot();
        }
        else
        {
            file = m_model.getItemById(id);
        }

        if (file != null)
        {
            Log.info("Add cache: "+file.getId());
            m_cachedFiles.put(file.getId(), file);
        }

        return file;
    }

    @Override
    public File getParent(File item)
    {
        if (item == null)
        {
            Log.warning("item is null");
            return null;
        }

        if (!item.getParents().isEmpty())
        {
            String parentID = item.getParents().get(0).getId();
            return getItemById(parentID, true);
        }

        return null;
    }

    @Override
    public List<File> getChildrenItems(File parent, boolean useCache)
    {
        if (parent == null)
        {
            parent =  m_model.getRoot();
        }

        m_currentPath = parent;

        if (useCache && m_cachedListFiles.containsKey(parent))
        {
            Log.info("Cache hit: "+parent.getId());
            return m_cachedListFiles.get(parent);
        }

        List<File> files =  m_model.getChildrenItems(parent);

        Collections.sort(files, m_comparatorFiles);

        Log.fine("Add cache: "+parent.getId());
        m_cachedListFiles.put(parent, files);

        return files;
    }
    
}
