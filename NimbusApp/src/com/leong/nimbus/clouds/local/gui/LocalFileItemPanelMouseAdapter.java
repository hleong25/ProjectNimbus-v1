/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leong.nimbus.clouds.local.gui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

/**
 *
 * @author henry
 */
public abstract class LocalFileItemPanelMouseAdapter extends MouseAdapter
{
    protected File m_item;

    public LocalFileItemPanelMouseAdapter(File item)
    {
        m_item = item;
    }

    public abstract void onOpenFolder(File item);

    @Override
    public void mouseClicked(MouseEvent e)
    {
        //Tools.logit("LocalFileItemPanelMouseAdapter.mouseClicked()");

        if (e.getClickCount() == 2)
        {
            //Tools.logit("LocalFileItemPanelMouseAdapter.mouseClicked() click count = 2");
            if (m_item.isDirectory())
            {
                onOpenFolder(m_item);
            }
        }
    }
}
