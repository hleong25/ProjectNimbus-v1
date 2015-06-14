/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leong.nimbus.clouds.interfaces;

import com.leong.nimbus.gui.components.FileItemPanel;
import com.leong.nimbus.gui.datatransfer.TransferableContainer;
import com.leong.nimbus.gui.helpers.XferHolder;
import com.leong.nimbus.gui.layout.AllCardsPanel;
import com.leong.nimbus.gui.layout.AllCardsPanel.ViewType;
import com.leong.nimbus.utils.GlobalCacheKey;
import java.awt.Component;
import java.util.List;

/**
 *
 * @author henry
 */
public interface ICloudPanel<T>
{
    void initPanel();
    void disposePanel();

    void setPanelView(ViewType type);

    boolean login(String userid);

    String getAbsolutePath(T item);
    void setCurrentPath(T path);

    AllCardsPanel getFilesPanel();
    FileItemPanel createFileItemPanel(final T file);

    List<Component> getFiles(final T parent, final boolean useCache);
    void showFiles(final T parent, final boolean useCache);
    void responsiveShowFiles(final T path, final boolean useCache);

    XferHolder<?, T> createXferHolder(GlobalCacheKey sourceCacheKey, Object input);
    List<XferHolder<?, T>> generateTransferList(TransferableContainer tc);
    boolean doTransferLoop(List<XferHolder<?, T>> list);

    boolean onAction_drop(TransferableContainer tc);

}
