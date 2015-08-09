/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leong.nimbus.clouds.local;

import com.leong.nimbus.clouds.interfaces.CloudPanelAdapter;
import com.leong.nimbus.clouds.interfaces.ICloudController;
import com.leong.nimbus.clouds.interfaces.transferadapters.CloudFileUtils;
import com.leong.nimbus.clouds.interfaces.transferadapters.DropboxToLocalTransferAdapter;
import com.leong.nimbus.clouds.interfaces.transferadapters.GDriveToLocalTransferAdapter;
import com.leong.nimbus.clouds.interfaces.transferadapters.LocalToLocalTransferAdapter;
import com.leong.nimbus.clouds.local.gui.LocalFileItem;
import com.leong.nimbus.clouds.local.gui.LocalFileItemPanelMouseAdapter;
import com.leong.nimbus.gui.components.FileItemPanel;
import com.leong.nimbus.gui.datatransfer.TransferableContainer;
import com.leong.nimbus.gui.helpers.DefaultDropTargetAdapter;
import com.leong.nimbus.gui.helpers.XferHolder;
import com.leong.nimbus.gui.layout.AllCardsPanel;
import com.leong.nimbus.utils.GlobalCache;
import com.leong.nimbus.utils.GlobalCacheKey;
import com.leong.nimbus.utils.Logit;
import java.awt.Color;
import java.awt.dnd.DropTarget;
import java.io.File;

/**
 *
 * @author henry
 */
public class LocalPanel
    extends CloudPanelAdapter<File, LocalController>
{
    private static final Logit Log = Logit.create(LocalPanel.class.getName());

    /**
     * Creates new form LocalPanel
     */
    public LocalPanel()
    {
        super();

        Log.entering("<init>");
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        txtPath = new javax.swing.JTextField();
        pnlFiles = new com.leong.nimbus.gui.layout.AllCardsPanel();

        setLayout(new java.awt.BorderLayout(0, 2));

        txtPath.setText("Path");
        txtPath.setMargin(new java.awt.Insets(2, 2, 2, 2));
        add(txtPath, java.awt.BorderLayout.PAGE_START);
        add(pnlFiles, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.leong.nimbus.gui.layout.AllCardsPanel pnlFiles;
    private javax.swing.JTextField txtPath;
    // End of variables declaration//GEN-END:variables

    @Override
    public void initPanel(ICloudController<?> controller)
    //public void initPanel(LocalController controller)
    {
        Log.entering("initPanel");
        m_controller = (LocalController) controller;

        pnlFiles.setProxy(this);
        pnlFiles.setView(AllCardsPanel.ViewType.LARGE_ICONS);

        Log.fine("Showing root path");
        File root = m_controller.getRoot();
        responsiveShowFiles(root, false);

        new DropTarget(pnlFiles, new DefaultDropTargetAdapter()
        {
            @Override
            public boolean onAction_drop(TransferableContainer tc)
            {
                return LocalPanel.this.onAction_drop(tc);
            }
        });
    }

    @Override
    public String getAbsolutePath(File item)
    {
        Log.entering("getAbsolutePath", new Object[]{item});
        return item.getAbsolutePath();
    }

    @Override
    public void setCurrentPath(File path)
    {
        Log.entering("setCurrentPath", new Object[]{path});

        super.setCurrentPath(path);
        txtPath.setText(getAbsolutePath(path));
    }

    @Override
    public FileItemPanel createFileItemPanel(File file)
    {
        FileItemPanel pnl = new FileItemPanel(new LocalFileItem(m_controller, file));

        pnl.setBackground(Color.WHITE);

        pnl.addMouseListener(new LocalFileItemPanelMouseAdapter(file)
        {
            @Override
            public void onOpenFolder(final File item)
            {
                responsiveShowFiles(item, true);
            }
        });

        return pnl;
    }

    @Override
    public AllCardsPanel getFilesPanel()
    {
        return pnlFiles;
    }

    @Override
    public XferHolder<?, File> createXferHolder(GlobalCacheKey sourceCacheKey, Object input)
    {
        final GlobalCacheKey targetCacheKey = GlobalCache.getInstance().getKey(m_controller);
        //Log.finer("xferholder sourceCacheKey:"+sourceCacheKey+" targetCacheKey:"+targetCacheKey);
        final ICloudController genericInputController = (ICloudController) GlobalCache.getInstance().get(sourceCacheKey);
        //Log.finer("genericInputController:"+genericInputController);
        switch (genericInputController.getCloudType())
        {
            case LOCAL_FILE_SYSTEM:
            {
                final File inputFile = (File)input;
                final File outputFile = CloudFileUtils.convertToLocal(m_currentPath, inputFile);
                final FileItemPanel pnl = createFileItemPanel(outputFile);

                pnl.showProgress(true);

                XferHolder<File, File> holder = new XferHolder<>();
                holder.xfer = new LocalToLocalTransferAdapter(sourceCacheKey, inputFile, targetCacheKey, outputFile);
                holder.pnl = pnl;

                return holder;
            }

            case GOOGLE_DRIVE:
            {
                final com.google.api.services.drive.model.File inputFile = (com.google.api.services.drive.model.File)input;
                final File outputFile = CloudFileUtils.convertToLocal(m_currentPath, inputFile);
                final FileItemPanel pnl = createFileItemPanel(outputFile);

                pnl.showProgress(true);

                XferHolder<com.google.api.services.drive.model.File, java.io.File> holder = new XferHolder<>();
                holder.xfer = new GDriveToLocalTransferAdapter(sourceCacheKey, inputFile, targetCacheKey, outputFile);
                holder.pnl = pnl;

                return holder;
            }

            case DROPBOX:
            {
                final com.dropbox.core.DbxEntry inputFile = (com.dropbox.core.DbxEntry)input;
                final File outputFile = CloudFileUtils.convertToLocal(m_currentPath, inputFile);
                final FileItemPanel pnl = createFileItemPanel(outputFile);

                pnl.showProgress(true);

                XferHolder<com.dropbox.core.DbxEntry, java.io.File> holder = new XferHolder<>();
                holder.xfer = new DropboxToLocalTransferAdapter(sourceCacheKey, inputFile, targetCacheKey, outputFile);
                holder.pnl = pnl;

                return holder;
            }

            default:
                return null;
        }
    }

}
