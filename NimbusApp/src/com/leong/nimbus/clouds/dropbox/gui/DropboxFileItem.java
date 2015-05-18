/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leong.nimbus.clouds.dropbox.gui;

import com.dropbox.core.DbxEntry;
import com.leong.nimbus.gui.components.IFileItem;
import javax.swing.ImageIcon;

/**
 *
 * @author henry
 */
public class DropboxFileItem implements IFileItem
{
    protected DbxEntry m_entry;

    public DropboxFileItem(DbxEntry entry)
    {
        m_entry = entry;
    }

    @Override
    public ImageIcon getIcon()
    {
        String path;

        if (m_entry.isFolder())
        {
            path = "resources/icons/google/Close-Folder-icon-64.png";
        }
        else
        {
            path = "resources/icons/google/docs-64.png";
        }

        ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource(path));

        return icon;
    }

    @Override
    public String getLabel()
    {
        return m_entry.name;
    }

}
