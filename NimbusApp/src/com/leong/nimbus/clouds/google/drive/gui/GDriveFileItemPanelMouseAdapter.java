/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leong.nimbus.clouds.google.drive.gui;

import com.google.api.services.drive.model.File;
import com.leong.nimbus.clouds.google.drive.GDriveConstants;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 *
 * @author henry
 */
public abstract class GDriveFileItemPanelMouseAdapter extends MouseAdapter
{
    protected final File m_item;

    public GDriveFileItemPanelMouseAdapter(File item)
    {
        m_item = item;
    }

    public abstract void onOpenFolder(File parent);

    @Override
    public void mouseClicked(MouseEvent e)
    {
        //Tools.logit("GDriveFileItemPanelMouseAdapter.mouseClicked()");

        if (e.getClickCount() == 2)
        {
            //Tools.logit("GDriveFileItemPanelMouseAdapter.mouseClicked() click count = 2");
            if (m_item.getMimeType().equals(GDriveConstants.MIME_TYPE_FOLDER))
            {
                onOpenFolder(m_item);
            }
        }
    }

}
