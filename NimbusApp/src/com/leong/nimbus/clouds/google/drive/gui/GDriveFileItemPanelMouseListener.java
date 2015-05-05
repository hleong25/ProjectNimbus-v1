/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leong.nimbus.clouds.google.drive.gui;

import com.google.api.services.drive.model.File;
import com.leong.nimbus.clouds.google.drive.GDriveConstants;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 *
 * @author henry
 */
public abstract class GDriveFileItemPanelMouseListener implements MouseListener
{
    protected File m_item;

    public GDriveFileItemPanelMouseListener(File item)
    {
        m_item = item;
    }

    public abstract void onOpenFolder(File item);

    @Override
    public void mouseClicked(MouseEvent e)
    {
        //Tools.logit("GDriveFileItemPanelMouseListener.mouseClicked()");

        if (e.getClickCount() == 2)
        {
            //Tools.logit("GDriveFileItemPanelMouseListener.mouseClicked() click count = 2");
            if (m_item.getMimeType().equals(GDriveConstants.MIME_TYPE_FOLDER))
            {
                onOpenFolder(m_item);
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e)
    {
        // nothing
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
        // nothing
    }

    @Override
    public void mouseEntered(MouseEvent e)
    {
        // nothing
    }

    @Override
    public void mouseExited(MouseEvent e)
    {
        // nothing
    }

}
