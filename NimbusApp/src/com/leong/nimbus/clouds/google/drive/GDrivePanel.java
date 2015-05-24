/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leong.nimbus.clouds.google.drive;

import com.google.api.services.drive.model.File;
import com.leong.nimbus.clouds.google.drive.gui.GDriveFileItem;
import com.leong.nimbus.clouds.google.drive.gui.GDriveFileItemPanelMouseAdapter;
import com.leong.nimbus.clouds.interfaces.CloudPanelAdapter;
import com.leong.nimbus.clouds.interfaces.ICloudProgress;
import com.leong.nimbus.clouds.interfaces.ICloudTransfer;
import com.leong.nimbus.clouds.interfaces.transferadapters.LocalToGDriveTransferAdapter;
import com.leong.nimbus.gui.components.FileItemPanel;
import com.leong.nimbus.gui.helpers.BusyTaskCursor;
import com.leong.nimbus.gui.helpers.DefaultDropTargetAdapter;
import com.leong.nimbus.gui.helpers.ResponsiveTaskUI;
import com.leong.nimbus.gui.helpers.XferHolder;
import com.leong.nimbus.gui.layout.AllCardsPanel;
import com.leong.nimbus.utils.Logit;
import java.awt.Color;
import java.awt.dnd.DropTarget;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

/**
 *
 * @author henry
 */
public class GDrivePanel
    extends CloudPanelAdapter<File, GDriveController>
{
    private static final Logit Log = Logit.create(GDrivePanel.class.getName());

    private final List<File> m_trailPaths = new ArrayList<>();

    /**
     * Creates new form GDrivePanel
     */
    public GDrivePanel()
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
        btnConnect = new javax.swing.JButton();
        txtPath = new javax.swing.JTextField();
        pnlFiles = new com.leong.nimbus.gui.layout.AllCardsPanel();

        setLayout(new java.awt.BorderLayout());

        pnlTop.setLayout(new java.awt.GridBagLayout());

        btnConnect.setText("Connect to Google Drive!");
        btnConnect.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnConnectActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        pnlTop.add(btnConnect, gridBagConstraints);

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

    private void btnConnectActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnConnectActionPerformed
    {//GEN-HEADEREND:event_btnConnectActionPerformed
        BusyTaskCursor.doTask(this, new BusyTaskCursor.IBusyTask()
            {
                @Override
                public void run()
                {
                    if (m_controller.login(GDrivePanel.this))
                    {
                        new DropTarget(pnlFiles, new DefaultDropTargetAdapter()
                            {
                                @Override
                                public boolean onAction_drop(List list)
                                {
                                    return GDrivePanel.this.onAction_drop(list);
                                }
                            });

                            showFiles(m_controller.getRoot(), false);
                        }
                    }
                });
    }//GEN-LAST:event_btnConnectActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnConnect;
    private com.leong.nimbus.gui.layout.AllCardsPanel pnlFiles;
    private javax.swing.JPanel pnlTop;
    private javax.swing.JTextField txtPath;
    // End of variables declaration//GEN-END:variables

    @Override
    public void initPanel()
    {
        Log.entering("initPanel");
        m_controller = new GDriveController();

        pnlFiles.setProxy(this);

        txtPath.setText("");
    }

    @Override
    public String getAbsolutePath(File item)
    {
        Log.entering("getAbsolutePath", new Object[]{item});
        return item.getId();
    }

    @Override
    public void setCurrentPath(File path)
    {
        Log.entering("setCurrentPath", new Object[]{path.getId()});

        super.setCurrentPath(path);

        if (m_trailPaths.contains(path))
        {
            // the list of trails has the path,
            // remove everything after it

            int idx = m_trailPaths.indexOf(path);

            ListIterator<File> itr = m_trailPaths.listIterator(idx + 1);
            while (itr.hasNext())
            {
                m_trailPaths.remove(itr.next());
            }
        }
        else
        {
            m_trailPaths.add(path);
        }

        String pathStr = "";
        for (File trail : m_trailPaths)
        {
            pathStr += "/" + trail.getTitle();
        }

        txtPath.setText(pathStr);
    }

    @Override
    public FileItemPanel createFileItemPanel(File file)
    {
        FileItemPanel pnl = new FileItemPanel(new GDriveFileItem(file));

        pnl.setBackground(Color.WHITE);

        pnl.addMouseListener(new GDriveFileItemPanelMouseAdapter(file)
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
    public XferHolder createXferHolder(java.io.File file)
    {
        final java.io.File inputFile = file;
        final com.google.api.services.drive.model.File outputFile = m_controller.generateMetadata(m_currentPath, inputFile);
        final FileItemPanel pnl = createFileItemPanel(outputFile);

        pnl.showProgress(true);

        XferHolder<java.io.File, com.google.api.services.drive.model.File> holder = new XferHolder<>();
        holder.xfer = new LocalToGDriveTransferAdapter(inputFile, outputFile);
        holder.pnl = pnl;

        return holder;
    }
}
