/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leong.nimbus.clouds.interfaces;

import com.leong.nimbus.utils.GlobalCache;
import com.leong.nimbus.utils.Logit;
import com.leong.nimbus.utils.Tools;
import edu.stanford.ejalbert.BrowserLauncher;
import edu.stanford.ejalbert.exception.BrowserLaunchingInitializingException;
import edu.stanford.ejalbert.exception.UnsupportedOperatingSystemException;
import java.awt.Component;
import java.io.InputStream;
import java.io.OutputStream;
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
public abstract class CloudControllerAdapter<T>
    implements ICloudController<T>
{
    private static final Logit Log = Logit.create(CloudControllerAdapter.class.getName());

    protected final ICloudModel<T> m_model;

    protected final GlobalCache.IProperties m_gcprops;

    protected final Comparator<T> m_comparatorFiles;

    protected final Map<T, List<T>> m_cachedListFiles = new HashMap<>();
    protected final Map<String, T> m_cachedFiles = new HashMap<>();

    protected String m_rootFolder;

    protected CloudControllerAdapter(final String className, ICloudModel<T> model)
    {
        Log.entering("<init>");

        m_model = model;

        m_gcprops = new GlobalCache.IProperties()
        {
            @Override
            public String getPackageName()
            {
                return "controllers/"+className;
            }
        };

        m_comparatorFiles = new Comparator<T>()
        {
            @Override
            public int compare(T f1, T f2)
            {
                //boolean f1_isdir = f1.getMimeType().equals(GDriveConstants.MIME_TYPE_FOLDER);
                //boolean f2_isdir = f2.getMimeType().equals(GDriveConstants.MIME_TYPE_FOLDER);

                boolean f1_isdir = m_model.isFolder(f1);
                boolean f2_isdir = m_model.isFolder(f2);

                if (f1_isdir ^ f2_isdir)
                {
                    return f1_isdir ? -1 : 1;
                }

                String f1_name = m_model.getName(f1);
                String f2_name = m_model.getName(f2);
                return f1_name.compareTo(f2_name);

                // return f1.getTitle().compareTo(f2.getTitle());
            }
        };
    }

    @Override
    public boolean login(Component parentComponent, String userid)
    {
        Log.entering("login", new Object[]{"parentComponent", userid});

        if (m_model.login(userid))
        {
            Log.info("Login successful for '"+userid+"'");
            GlobalCache.getInstance().put(m_gcprops, userid, this);
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

        boolean successLogin = m_model.login(userid, authCode);
        if (successLogin)
        {
            GlobalCache.getInstance().put(m_gcprops, userid, this);
        }
        return successLogin;
    }

    @Override
    public T getRoot()
    {
        Log.entering("getRoot");

        T root = m_model.getRoot();
        return root;
    }

    @Override
    public T getItemById(String id, boolean useCache)
    {
        Log.entering("getItemById", new Object[]{id, useCache});

        if (useCache && m_cachedFiles.containsKey(id))
        {
            Log.info("Cache hit: "+id);
            return m_cachedFiles.get(id);
        }

        T file;

        if (id.equals(m_rootFolder))
        {
            file = m_model.getRoot();
        }
        else
        {
            file = m_model.getItemById(id);
        }

        if (file != null)
        {
            String fileId = m_model.getIdByItem(file);
            Log.info("Add cache: "+fileId);
            m_cachedFiles.put(fileId, file);
        }

        return file;
    }

    @Override
    public List<T> getChildrenItems(T parent, boolean useCache)
    {
        Log.entering("getChildrenItems", new Object[]{parent, useCache});

        if (parent == null)
        {
            parent =  m_model.getRoot();
        }

        if (useCache && m_cachedListFiles.containsKey(parent))
        {
            Log.info("Cache hit: "+m_model.getIdByItem(parent));
            return m_cachedListFiles.get(parent);
        }

        List<T> files =  m_model.getChildrenItems(parent);

        Collections.sort(files, m_comparatorFiles);

        Log.fine("Add cache: "+m_model.getIdByItem(parent));
        m_cachedListFiles.put(parent, files);

        return files;
    }

    @Override
    public void transfer(ICloudTransfer</*source*/?, /*target*/? super T> transfer)
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
    public InputStream getDownloadStream(T downloadFile)
    {
        return m_model.getDownloadStream(downloadFile);
    }

    @Override
    public OutputStream getUploadStream(T uploadFile)
    {
        return m_model.getUploadStream(uploadFile);
    }


}
