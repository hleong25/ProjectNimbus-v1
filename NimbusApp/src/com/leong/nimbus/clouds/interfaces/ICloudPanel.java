/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leong.nimbus.clouds.interfaces;

import com.leong.nimbus.gui.components.FileItemPanel;
import java.awt.Component;
import java.util.List;
import javax.swing.JPanel;

/**
 *
 * @author henry
 */
public interface ICloudPanel<T>
{
    void initPanel();

    String getAbsolutePath(T item);
    void setCurrentPath(T path);

    FileItemPanel createFileItemPanel(final T file);
    void responsiveShowFiles(final T path, final boolean useCache);

    JPanel getFilesPanel();
    List<Component> getFiles(final T parent, final boolean useCache);
    void showFiles(final T parent, final boolean useCache);
}
