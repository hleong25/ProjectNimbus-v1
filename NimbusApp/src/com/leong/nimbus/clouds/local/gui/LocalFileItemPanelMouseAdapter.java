/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leong.nimbus.clouds.local.gui;

import com.leong.nimbus.gui.components.FileItemPanelMouseAdapter;
import java.io.File;

/**
 *
 * @author henry
 */
public abstract class LocalFileItemPanelMouseAdapter
    extends FileItemPanelMouseAdapter<File>
{
    public LocalFileItemPanelMouseAdapter(File item)
    {
        super(item);
    }

    @Override
    public boolean isFolder(final File item)
    {
        return item.isDirectory();
    }

}
