/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leong.nimbus.clouds.dropbox.gui;

import com.dropbox.core.DbxEntry;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 *
 * @author henry
 */
public abstract class DropboxFileItemPanelMouseAdapter extends MouseAdapter
{
    protected final DbxEntry m_entry;

    public DropboxFileItemPanelMouseAdapter(DbxEntry entry)
    {
        m_entry = entry;
    }

    public abstract void onOpenFolder(DbxEntry item);

    @Override
    public void mouseClicked(MouseEvent e)
    {
        //Tools.logit("LocalFileItemPanelMouseAdapter.mouseClicked()");

        if (e.getClickCount() == 2)
        {
            //Tools.logit("LocalFileItemPanelMouseAdapter.mouseClicked() click count = 2");
            if (m_entry.isFolder())
            {
                onOpenFolder(m_entry);
            }
        }
    }

}
