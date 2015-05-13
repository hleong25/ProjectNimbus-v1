/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leong.nimbus.clouds.local;

import com.leong.nimbus.clouds.interfaces.ICloudController;
import com.leong.nimbus.utils.Tools;
import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author henry
 */
public class LocalController implements ICloudController
{
    private final LocalModel m_model = new LocalModel();

    private final Comparator<File> m_comparatorFiles;
    private final Map<String, List<File>> m_cachedListFiles;
    private final Map<String, File> m_cachedFiles;

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

        m_cachedListFiles = new HashMap<>();
        m_cachedFiles = new HashMap<>();
    }

    public File getHomeFile()
    {
        File home = m_model.getHomeFile();
        m_cachedFiles.put(home.getAbsolutePath(), home);
        return home;
    }

    public File getParentFile(File file)
    {
        return file.getParentFile();
    }

    public File getParentFile(String file)
    {
        File currPath = m_model.getFile(file);
        File parentPath = currPath.getParentFile();
        return parentPath;
    }

    public List<File> getFiles(String path, boolean forceRefresh)
    {
        if (Tools.isNullOrEmpty(path))
        {
            path = getHomeFile().getAbsolutePath();
        }

        if (!forceRefresh && m_cachedListFiles.containsKey(path))
        {
            Tools.logit("LocalController.getFiles() Cache hit '"+path+"'");
            return m_cachedListFiles.get(path);
        }

        List<File> files =  m_model.getFiles(path);

        Collections.sort(files, m_comparatorFiles);

        Tools.logit("LocalController.getFiles() Add cache '"+path+"'");
        m_cachedListFiles.put(path, files);

        return files;
    }
}
