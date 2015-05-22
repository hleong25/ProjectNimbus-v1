/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leong.nimbus.clouds.google.drive;

import com.google.api.client.googleapis.media.MediaHttpUploader;
import com.google.api.client.googleapis.media.MediaHttpUploaderProgressListener;
import com.google.api.services.drive.model.File;
import com.leong.nimbus.clouds.google.drive.gui.GDriveFileItem;
import com.leong.nimbus.clouds.google.drive.gui.GDriveFileItemPanelMouseAdapter;
import com.leong.nimbus.clouds.interfaces.CloudPanelAdapter;
import com.leong.nimbus.gui.components.FileItemPanel;
import com.leong.nimbus.gui.helpers.BusyTaskCursor;
import com.leong.nimbus.gui.helpers.ResponsiveTaskUI;
import com.leong.nimbus.gui.layout.AllCardsPanel;
import com.leong.nimbus.utils.Logit;
import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author henry
 */
public class GDrivePanel
    extends CloudPanelAdapter<File, GDriveController>
{
    private static final Logit Log = Logit.create(GDrivePanel.class.getName());

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

        btnConnect = new javax.swing.JButton();
        pnlFiles = new com.leong.nimbus.gui.layout.AllCardsPanel();

        setLayout(new java.awt.BorderLayout());

        btnConnect.setText("Connect to Google Drive!");
        btnConnect.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnConnectActionPerformed(evt);
            }
        });
        add(btnConnect, java.awt.BorderLayout.PAGE_START);
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
                    // setup drag and drop once logged in
                    //new DropTarget(pnlFiles, m_dropTarget);

                    //new DropTarget(pnlFiles, new DefaultDropTargetAdapter()
                    //{
                    //    @Override
                    //    public boolean onAction_drop(List list)
                    //    {
                    //        return GDrivePanel.this.onAction_drop(list);
                    //    }
                    //});

                    showFiles(m_controller.getRoot(), false);
                }
            }
        });
    }//GEN-LAST:event_btnConnectActionPerformed

    protected boolean onAction_drop(List list)
    {
        Log.entering("onAction_drop", new Object[]{list});

        class FileHolder
        {
            public java.io.File content;
            public File metadata;
            public FileItemPanel pnl;
        }

        List<FileHolder> uploadFiles = new ArrayList<>();

        for (Object obj : list)
        {
            final java.io.File content = (java.io.File) obj;
            final File metadata = m_controller.generateMetadata(m_currentPath, content);
            final FileItemPanel pnl = createFileItemPanel(metadata);

            pnl.showProgress(true);

            FileHolder holder = new FileHolder();
            holder.content = content;
            holder.metadata = metadata;
            holder.pnl = pnl;

            uploadFiles.add(holder);

            // show the new item being added
            pnlFiles.add(pnl);
            pnlFiles.revalidate();

            ResponsiveTaskUI.yield();
        }

        // Loop them through
        for (FileHolder holder : uploadFiles)
        {
            // Print out the file path
            Log.fine("File path: "+holder.content.getPath());

            final FileItemPanel pnl = holder.pnl;

            m_controller.uploadLocalFile(holder.metadata, holder.content, new MediaHttpUploaderProgressListener()
            {
                @Override
                public void progressChanged(MediaHttpUploader mhu) throws IOException
                {
                    switch (mhu.getUploadState()) {
                        case INITIATION_STARTED:
                            Log.fine("Initiation has started!");
                            break;
                        case INITIATION_COMPLETE:
                            Log.fine("Initiation is complete!");
                            break;
                        case MEDIA_IN_PROGRESS:
                            Log.finer("BytesSent: "+mhu.getNumBytesUploaded()+" Progress: "+mhu.getProgress());
                            pnl.setProgress((int)(mhu.getProgress()*100.0));
                            ResponsiveTaskUI.yield();
                            break;
                        case MEDIA_COMPLETE:
                            Log.fine("Upload is complete!");
                            pnl.setProgress(100);
                            ResponsiveTaskUI.yield();
                            break;
                    }
                }
            });
        }

        showFiles(m_currentPath, false);

        return true;
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnConnect;
    private com.leong.nimbus.gui.layout.AllCardsPanel pnlFiles;
    // End of variables declaration//GEN-END:variables

    @Override
    public void initPanel()
    {
        Log.entering("initPanel");
        m_controller = new GDriveController();

        pnlFiles.setProxy(this);
    }

    @Override
    public String getAbsolutePath(File item)
    {
        Log.entering("getAbsolutePath", new Object[]{item});
        return item.getId();
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
}
