/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leong.nimbus.clouds.dropbox;

import com.dropbox.core.DbxEntry;
import com.leong.nimbus.clouds.dropbox.gui.DropboxFileItem;
import com.leong.nimbus.clouds.dropbox.gui.DropboxFileItemPanelMouseAdapter;
import com.leong.nimbus.clouds.interfaces.CloudPanelAdapter;
import com.leong.nimbus.clouds.interfaces.ICloudController;
import com.leong.nimbus.clouds.interfaces.transferadapters.CloudFileUtils;
import com.leong.nimbus.clouds.interfaces.transferadapters.DropboxToDropboxTransferAdapter;
import com.leong.nimbus.clouds.interfaces.transferadapters.GDriveToDropboxTransferAdapter;
import com.leong.nimbus.clouds.interfaces.transferadapters.LocalToDropboxTransferAdapter;
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

/**
 *
 * @author henry
 */
public class DropboxPanel
    extends CloudPanelAdapter<DbxEntry, DropboxController>
{
    private static final Logit Log = Logit.create(DropboxPanel.class.getName());

    /**
     * Creates new form DropboxPanel
     */
    public DropboxPanel()
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
        java.awt.GridBagConstraints gridBagConstraints;

        pnlTop = new javax.swing.JPanel();
        txtPath = new javax.swing.JTextField();
        pnlFiles = new com.leong.nimbus.gui.layout.AllCardsPanel();

        setLayout(new java.awt.BorderLayout(0, 2));

        pnlTop.setLayout(new java.awt.GridBagLayout());

        txtPath.setText("Path");
        txtPath.setMargin(new java.awt.Insets(2, 2, 2, 2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        pnlTop.add(txtPath, gridBagConstraints);

        add(pnlTop, java.awt.BorderLayout.NORTH);
        add(pnlFiles, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.leong.nimbus.gui.layout.AllCardsPanel pnlFiles;
    private javax.swing.JPanel pnlTop;
    private javax.swing.JTextField txtPath;
    // End of variables declaration//GEN-END:variables

    @Override
    public void initPanel()
    {
        Log.entering("initPanel");
        m_controller = new DropboxController();

        pnlFiles.setProxy(this);

        txtPath.setText("");
    }

    @Override
    public boolean login(String userid)
    {
        Log.entering("login", new Object[]{userid});

        if (m_controller.login(DropboxPanel.this, userid))
        {
            new DropTarget(pnlFiles, new DefaultDropTargetAdapter()
            {
                @Override
                public boolean onAction_drop(TransferableContainer tc)
                {
                    return DropboxPanel.this.onAction_drop(tc);
                }
            });

            showFiles(m_controller.getRoot(), false);
            return true;
        }

        return false;
    }

    @Override
    public String getAbsolutePath(DbxEntry item)
    {
        //Log.entering("getAbsolutePath", new Object[]{item.toStringMultiline()});
        return item.path;
    }

    @Override
    public void setCurrentPath(DbxEntry path)
    {
        Log.entering("setCurrentPath", new Object[]{path.path});

        super.setCurrentPath(path);
        txtPath.setText(getAbsolutePath(path));
    }

    @Override
    public FileItemPanel createFileItemPanel(DbxEntry file)
    {
        FileItemPanel pnl = new FileItemPanel(new DropboxFileItem(m_controller, file));

        pnl.setBackground(Color.WHITE);

        pnl.addMouseListener(new DropboxFileItemPanelMouseAdapter(file)
        {
            @Override
            public void onOpenFolder(final DbxEntry item)
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
    public XferHolder<?, DbxEntry> createXferHolder(GlobalCacheKey sourceCacheKey, Object input)
    {
        Log.entering("createXferHolder", new Object[]{sourceCacheKey, input});
        final GlobalCacheKey targetCacheKey = GlobalCache.getInstance().getKey(m_controller);
        final ICloudController genericInputController = (ICloudController) GlobalCache.getInstance().get(sourceCacheKey);
        switch (genericInputController.getCloudType())
        {
            case LOCAL_FILE_SYSTEM:
            {
                final java.io.File inputFile = (java.io.File)input;
                final DbxEntry outputFile = CloudFileUtils.convertToDropbox(m_currentPath, inputFile);
                final FileItemPanel pnl = createFileItemPanel(outputFile);

                pnl.showProgress(true);

                XferHolder<java.io.File, DbxEntry> holder = new XferHolder<>();
                holder.xfer = new LocalToDropboxTransferAdapter(sourceCacheKey, inputFile, targetCacheKey, outputFile);
                holder.pnl = pnl;

                return holder;
            }

            case GOOGLE_DRIVE:
            {
                final com.google.api.services.drive.model.File inputFile = (com.google.api.services.drive.model.File)input;
                final DbxEntry outputFile = CloudFileUtils.convertToDropbox(m_currentPath, inputFile);
                final FileItemPanel pnl = createFileItemPanel(outputFile);

                pnl.showProgress(true);

                XferHolder<com.google.api.services.drive.model.File, DbxEntry> holder = new XferHolder<>();
                holder.xfer = new GDriveToDropboxTransferAdapter(sourceCacheKey, inputFile, targetCacheKey, outputFile);
                holder.pnl = pnl;

                return holder;
            }

            case DROPBOX:
            {
                final DbxEntry inputFile = (DbxEntry)input;
                final DbxEntry outputFile = CloudFileUtils.convertToDropbox(m_currentPath, inputFile);
                final FileItemPanel pnl = createFileItemPanel(outputFile);

                pnl.showProgress(true);

                XferHolder<DbxEntry, DbxEntry> holder = new XferHolder<>();
                holder.xfer = new DropboxToDropboxTransferAdapter(sourceCacheKey, inputFile, targetCacheKey, outputFile);
                holder.pnl = pnl;

                return holder;
            }

            default:
                return null;
        }
    }

}
