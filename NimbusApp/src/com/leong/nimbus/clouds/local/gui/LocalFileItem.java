package com.leong.nimbus.clouds.local.gui;


import com.leong.nimbus.clouds.CloudType;
import com.leong.nimbus.gui.components.IFileItem;
import java.io.File;
import javax.swing.ImageIcon;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author henry
 */
public class LocalFileItem implements IFileItem<File>
{
    protected final File m_item;

    public LocalFileItem(File item)
    {
        m_item = item;
    }

    @Override
    public CloudType getCloudType()
    {
        return CloudType.LOCAL_FILE_SYSTEM;
    }

    @Override
    public File getCloudObject()
    {
        return m_item;
    }

    @Override
    public ImageIcon getIcon()
    {
        String path;

        if (m_item.isDirectory())
        {
            //Tools.logit("Path="+m_item.getAbsolutePath());
            path = "resources/icons/google/Close-Folder-icon-64.png";
        }
        else
        {
            //Tools.logit("File="+m_item.getAbsolutePath());
            path = "resources/icons/google/docs-64.png";
        }

        ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource(path));

        return icon;
    }

    @Override
    public String getLabel()
    {
        return m_item.getName();
    }
}
