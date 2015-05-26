/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leong.nimbus.clouds.dropbox;

import com.dropbox.core.DbxEntry;
import com.leong.nimbus.clouds.interfaces.ICloudController;
import com.leong.nimbus.clouds.interfaces.ICloudTransfer;
import com.leong.nimbus.utils.Logit;
import com.leong.nimbus.utils.Tools;
import edu.stanford.ejalbert.BrowserLauncher;
import edu.stanford.ejalbert.exception.BrowserLaunchingInitializingException;
import edu.stanford.ejalbert.exception.UnsupportedOperatingSystemException;
import java.awt.Component;
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
public class DropboxController implements ICloudController<DbxEntry>
{
    private static final Logit Log = Logit.create(DropboxController.class.getName());

    private final DropboxModel m_model = new DropboxModel();

    private final Comparator<DbxEntry> m_comparatorFiles;
    private final Map<DbxEntry, List<DbxEntry>> m_cachedListFiles = new HashMap<>();
    private final Map<String, DbxEntry> m_cachedFiles = new HashMap<>();

    public DropboxController()
    {
        Log.entering("<init>");

        m_comparatorFiles = new Comparator<DbxEntry>()
        {
            @Override
            public int compare(DbxEntry f1, DbxEntry f2)
            {
                boolean f1_isdir = f1.isFolder();
                boolean f2_isdir = f2.isFolder();

                if (f1_isdir ^ f2_isdir)
                {
                    return f1_isdir ? -1 : 1;
                }

                return f1.name.compareTo(f2.name);
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

        return m_model.login(authCode);
    }

    @Override
    public DbxEntry getRoot()
    {
        Log.entering("getRoot");

        DbxEntry root = m_model.getRoot();
        return root;
    }

    @Override
    public DbxEntry getItemById(String id, boolean useCache)
    {
        Log.entering("getItemById", new Object[]{id, useCache});

        if (useCache && m_cachedFiles.containsKey(id))
        {
            Log.info("Cache hit: "+id);
            return m_cachedFiles.get(id);
        }

        DbxEntry entry;

        if (id.equals(DropboxConstants.FOLDER_ROOT))
        {
            entry = m_model.getRoot();
        }
        else
        {
            entry = m_model.getItemById(id);
        }

        if (entry != null)
        {
            Log.info("Add cache: "+entry.path);
            m_cachedFiles.put(entry.path, entry);
        }

        return entry;
    }

    @Override
    public DbxEntry getParent(DbxEntry item)
    {
        Log.entering("getParent", (item != null ? item.path : "null"));

        if (item == null)
        {
            Log.warning("item is null");
            return null;
        }

        String path = item.path;

        if (path.equals(DropboxConstants.FOLDER_ROOT))
        {
            return null;
        }

        int idxOffset = path.lastIndexOf('/');
        String parentPath = path.substring(0, idxOffset);

        if (Tools.isNullOrEmpty(parentPath))
        {
            parentPath = DropboxConstants.FOLDER_ROOT;
        }

        Log.fine("Parent: "+parentPath);
        return getItemById(parentPath, true);
    }

    @Override
    public List<DbxEntry> getChildrenItems(DbxEntry parent, boolean useCache)
    {
        Log.entering("getChildrenItems", new Object[]{parent.path, useCache});

        if (parent == null)
        {
            parent =  m_model.getRoot();
        }

        if (useCache && m_cachedListFiles.containsKey(parent))
        {
            Log.info("Cache hit: "+parent.path);
            return m_cachedListFiles.get(parent);
        }

        List<DbxEntry> files =  m_model.getChildrenItems(parent);

        Collections.sort(files, m_comparatorFiles);

        Log.fine("Add cache: "+parent.path);
        m_cachedListFiles.put(parent, files);

        return files;
    }

    @Override
    public void transfer(ICloudTransfer<?,?> transfer)
    {
        m_model.transfer(transfer);

        Tools.notifyAll(transfer);
    }

    public DbxEntry.File generateFile(String parent, java.io.File content)
    {
        String path = parent + "/" + content.getName();
        String iconName = null;
        boolean mightHaveThumbnail = true;
        long numBytes = content.length();
        String humanSize = String.valueOf(numBytes);
        java.util.Date lastModified = new java.util.Date(content.lastModified());
        java.util.Date clientMtime = lastModified;
        String rev = "1";

        return new DbxEntry.File(path, iconName, mightHaveThumbnail, numBytes, humanSize, lastModified, clientMtime, rev);
    }

}
