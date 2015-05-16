/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leong.nimbus.clouds.google.drive.gui;

import com.google.api.services.drive.model.File;
import com.leong.nimbus.clouds.google.drive.GDriveConstants;
import com.leong.nimbus.gui.components.IFileItem;
import com.leong.nimbus.utils.Logit;
import javax.swing.ImageIcon;

/**
 *
 * @author henry
 */
public class GDriveFileItem implements IFileItem
{
    private static final Logit Log = Logit.create(GDriveFileItem.class.getName());

    protected File m_item;

    public GDriveFileItem(File item)
    {
        m_item = item;
    }

    @Override
    public ImageIcon getIcon()
    {
        String path;

        //Tools.logit("Item="+m_item.getTitle()+" Mime="+m_item.getMimeType());

        if (m_item.getMimeType() == null)
        {
            Log.fine("Item="+m_item.getTitle()+" Mime=(null)");
            path = "resources/icons/google/drive-64.png";
        }
        else if (m_item.getMimeType().equals(GDriveConstants.MIME_TYPE_DOCUMENT))
        {
            path = "resources/icons/google/docs-64.png";
        }
        else if (m_item.getMimeType().equals(GDriveConstants.MIME_TYPE_FOLDER))
        {
            path = "resources/icons/google/Close-Folder-icon-64.png";
        }
        else if (m_item.getMimeType().equals(GDriveConstants.MIME_TYPE_PRESENTATION))
        {
            path = "resources/icons/google/presentations-64.png";
        }
        else if (m_item.getMimeType().equals(GDriveConstants.MIME_TYPE_SPREADSHEET))
        {
            path = "resources/icons/google/spreadsheets-64.png";
        }
        else if (m_item.getMimeType().equals(GDriveConstants.MIME_TYPE_VIDEO))
        {
            path = "resources/icons/google/video-64.gif";
        }
        else
        {
            Log.fine("Item="+m_item.getTitle()+" Mime="+m_item.getMimeType());
            path = "resources/icons/google/drive-64.png";
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
