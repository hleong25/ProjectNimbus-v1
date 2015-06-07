/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leong.nimbus.clouds.local;

import com.leong.nimbus.clouds.interfaces.ICloudController;
import com.leong.nimbus.clouds.interfaces.ICloudTransfer;
import com.leong.nimbus.utils.Logit;
import com.leong.nimbus.utils.Tools;
import java.awt.Component;
import java.io.File;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author henry
 */
public class LocalController implements ICloudController<java.io.File>
{
    private static final Logit Log = Logit.create(LocalController.class.getName());

    private final LocalModel m_model = new LocalModel();

    private final Comparator<File> m_comparatorFiles;
    private final Map<File, List<File>> m_cachedChildren = new HashMap<>();

    public LocalController()
    {
        m_comparatorFiles = new Comparator<File>()
        {
            @Override
            public int compare(File f1, File f2)
            {
                boolean f1_isdir = f1.isDirectory();
                boolean f2_isdir = f2.isDirectory();

                if (f1_isdir ^ f2_isdir)
                {
                    return f1_isdir ? -1 : 1;
                }

                return f1.getName().compareTo(f2.getName());
            }
        };

    }

    @Override
    public boolean login(Component parentComponent, String userid)
    {
        return true;
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
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public File getParent(File item)
    {
        return item.getParentFile();
    }

    @Override
    public List<File> getChildrenItems(File parent, boolean useCache)
    {
        if (parent == null)
        {
            parent = getRoot();
        }

        if (useCache && m_cachedChildren.containsKey(parent))
        {
            Log.fine(MessageFormat.format("Cache hit {0}'", parent.getAbsolutePath()));
            return m_cachedChildren.get(parent);
        }

        List<File> files =  m_model.getChildrenItems(parent);

        Collections.sort(files, m_comparatorFiles);

        Log.fine("Add cache: "+parent.getAbsolutePath());
        m_cachedChildren.put(parent, files);

        return files;
    }

    @Override
    public void transfer(ICloudTransfer<?,? super java.io.File> transfer)
    {
        m_model.transfer(transfer);

        Tools.notifyAll(transfer);
    }
}
