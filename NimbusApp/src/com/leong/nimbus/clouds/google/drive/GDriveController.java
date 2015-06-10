/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leong.nimbus.clouds.google.drive;

import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.ParentReference;
import com.leong.nimbus.clouds.CloudType;
import com.leong.nimbus.clouds.interfaces.ICloudController;
import com.leong.nimbus.clouds.interfaces.ICloudTransfer;
import com.leong.nimbus.utils.Logit;
import com.leong.nimbus.utils.Tools;
import edu.stanford.ejalbert.BrowserLauncher;
import edu.stanford.ejalbert.exception.BrowserLaunchingInitializingException;
import edu.stanford.ejalbert.exception.UnsupportedOperatingSystemException;
import java.awt.Component;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;

// TODO: this class has an unchecked or unsafe operation

/**
 *
 * @author henry
 */
public class GDriveController implements ICloudController<com.google.api.services.drive.model.File>
{
    private static final Logit Log = Logit.create(GDriveController.class.getName());

    private final GDriveModel m_model = new GDriveModel();

    private final transient Comparator<File> m_comparatorFiles;
    private final transient Map<File, List<File>> m_cachedListFiles = new HashMap<>();
    private final transient Map<String, File> m_cachedFiles = new HashMap<>();

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

    public static GDriveController createInstance()
    {
        return new GDriveController();
    }

    private void writeObject(java.io.ObjectOutputStream out)
        throws java.io.IOException
    {
        out.defaultWriteObject();
    }

    private void readObject(java.io.ObjectInputStream in)
        throws java.io.IOException, ClassNotFoundException

    {
        in.defaultReadObject();
    }

    @Override
    public CloudType getCloudType()
    {
        return CloudType.GOOGLE_DRIVE;
    }

    @Override
    public boolean login(Component parentComponent, String userid)
    {
        Log.entering("login", new Object[]{"parentComponent", userid});

        if (m_model.login(userid))
        {
            Log.info("Login successful for '"+userid+"'");
            return true;
        }

        try
        {
            final String authUrl = m_model.getAuthUrl();

            // TODO: does not work with OSX -- i think...
            BrowserLauncher launcher = new BrowserLauncher();
            launcher.setNewWindowPolicy(true);

            Log.fine("Opening new browser to "+authUrl);
            launcher.openURLinBrowser(authUrl);
        }
        catch (BrowserLaunchingInitializingException | UnsupportedOperatingSystemException ex)
        {
            Log.throwing("login", ex);
        }

        String authCode = JOptionPane.showInputDialog(parentComponent, "Input the authentication code here");

        if (Tools.isNullOrEmpty(authCode))
        {
            Log.severe("Auth code not valid.");
            return false;
        }

        authCode = authCode.trim();
        Log.info("Auth code: "+authCode);

        return m_model.login(userid, authCode);
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

    @Override
    public File getRoot()
    {
        Log.entering("getRoot");

        File root = m_model.getRoot();
        return root;
    }

    @Override
    public File getItemById(String id, boolean useCache)
    {
        Log.entering("getItemById", new Object[]{id, useCache});

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
        Log.entering("getParent", (item != null ? item.getId() : "null"));

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
        Log.entering("getChildrenItems", new Object[]{(parent != null ? parent.getId() : "(parent.null)"), useCache});

        if (parent == null)
        {
            parent =  m_model.getRoot();
        }

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

    @Override
    public void transfer(ICloudTransfer transfer)
    {
        try
        {
            m_model.transfer(transfer);
        }
        finally
        {
            Tools.notifyAll(transfer);
        }
    }

    @Override
    public InputStream getDownloadStream(File downloadFile)
    {
        return m_model.getDownloadStream(downloadFile);
    }
    
}
