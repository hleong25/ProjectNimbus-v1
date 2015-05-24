/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leong.nimbus.clouds.local;

import com.leong.nimbus.clouds.interfaces.CloudPanelAdapter;
import com.leong.nimbus.clouds.interfaces.ICloudProgress;
import com.leong.nimbus.clouds.interfaces.ICloudTransfer;
import com.leong.nimbus.clouds.interfaces.transferadapters.LocalToLocalTransferAdapter;
import com.leong.nimbus.clouds.local.gui.LocalFileItem;
import com.leong.nimbus.clouds.local.gui.LocalFileItemPanelMouseAdapter;
import com.leong.nimbus.gui.components.FileItemPanel;
import com.leong.nimbus.gui.helpers.DefaultDropTargetAdapter;
import com.leong.nimbus.gui.helpers.ResponsiveTaskUI;
import com.leong.nimbus.gui.helpers.XferHolder;
import com.leong.nimbus.gui.layout.AllCardsPanel;
import com.leong.nimbus.utils.Logit;
import java.awt.Color;
import java.awt.dnd.DropTarget;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
    public void initPanel()
    {
        Log.entering("initPanel");
        m_controller = new LocalController();

        pnlFiles.setProxy(this);
        pnlFiles.setView(AllCardsPanel.ViewType.LARGE_ICONS);

        Log.fine("Showing root path");
        File root = m_controller.getRoot();
        responsiveShowFiles(root, false);
        //showFiles(root, false);


        new DropTarget(pnlFiles, new DefaultDropTargetAdapter()
        {
            @Override
            public boolean onAction_drop(List list)
            {
                return LocalPanel.this.onAction_drop(list);
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
        FileItemPanel pnl = new FileItemPanel(new LocalFileItem(file));

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
    public XferHolder createXferHolder(File file)
    {
        final File inputFile = file;
        final File outputFile = new File(m_currentPath, inputFile.getName());
        final FileItemPanel pnl = createFileItemPanel(inputFile);

        pnl.showProgress(true);

        XferHolder<File, File> holder = new XferHolder<>();
        holder.xfer = new LocalToLocalTransferAdapter(inputFile, outputFile);
        holder.pnl = pnl;

        return holder;
    }

}
