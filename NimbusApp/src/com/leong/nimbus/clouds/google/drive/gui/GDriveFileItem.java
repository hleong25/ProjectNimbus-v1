/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leong.nimbus.clouds.google.drive.gui;

import com.google.api.services.drive.model.File;
import com.leong.nimbus.gui.components.IFileItem;
import com.leong.nimbus.utils.Tools;
import javax.swing.ImageIcon;

/**
 *
 * @author henry
 */
public class GDriveFileItem implements IFileItem
{
    public final String MIME_TYPE_FOLDER = "application/vnd.google-apps.folder";

    protected File m_item;

    public GDriveFileItem(File item)
    {
        m_item = item;
    }

    @Override
    public ImageIcon getIcon()
    {
        String path;

        Tools.logit("Item="+m_item.getTitle()+" Mime="+m_item.getMimeType());

        if (m_item.getMimeType().equals(MIME_TYPE_FOLDER))
        {
            path = "resources/icons/icon-dir.png";
        }
        else
        {
            path = "resources/icons/icon-file.png";
        }

        ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource(path));

        return icon;
    }

    @Override
    public String getLabel()
    {
        return m_item.getTitle();
    }

}
