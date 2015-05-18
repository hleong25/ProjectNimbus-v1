/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leong.nimbus.clouds.local;

import com.leong.nimbus.clouds.interfaces.CloudPanelAdapter;
import com.leong.nimbus.clouds.local.gui.LocalFileItem;
import com.leong.nimbus.clouds.local.gui.LocalFileItemPanelMouseAdapter;
import com.leong.nimbus.gui.components.FileItemPanel;
import com.leong.nimbus.gui.helpers.WrapLayout;
import com.leong.nimbus.utils.Logit;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.KeyEvent;
import java.io.File;
import javax.swing.JPanel;

/**
 *
 * @author henry
 */
public class LocalPanel
    extends CloudPanelAdapter<File, LocalController>
    //extends javax.swing.JPanel
    //implements ICloudPanel
{
    private static final Logit Log = Logit.create(LocalPanel.class.getName());

    /**
     * Creates new form LocalPanel
     */
    public LocalPanel()
    {
        super(new LocalController());

        Log.entering("<init>");
        initComponents();

        Log.fine("Showing root path");
        File root = m_controller.getRoot();
        showFiles(root, true);
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
        pnlScroll = new javax.swing.JScrollPane();
        pnlFiles = new javax.swing.JPanel();

        setLayout(new java.awt.BorderLayout(0, 2));

        txtPath.setText("Path");
        txtPath.setMargin(new java.awt.Insets(2, 2, 2, 2));
        add(txtPath, java.awt.BorderLayout.PAGE_START);

        pnlScroll.setMinimumSize(new java.awt.Dimension(400, 300));
        pnlScroll.setPreferredSize(new java.awt.Dimension(400, 300));

        pnlFiles.setBackground(new java.awt.Color(255, 255, 255));
        WrapLayout wraplayout = new WrapLayout(FlowLayout.LEADING);
        wraplayout.setAlignOnBaseline(true);
        pnlFiles.setLayout(wraplayout);
        pnlFiles.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                pnlFilesMouseClicked(evt);
            }
        });
        pnlFiles.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyReleased(java.awt.event.KeyEvent evt)
            {
                pnlFilesKeyReleased(evt);
            }
        });
        pnlScroll.setViewportView(pnlFiles);

        add(pnlScroll, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void pnlFilesMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_pnlFilesMouseClicked
    {//GEN-HEADEREND:event_pnlFilesMouseClicked
        pnlFiles.requestFocusInWindow();
    }//GEN-LAST:event_pnlFilesMouseClicked

    private void pnlFilesKeyReleased(java.awt.event.KeyEvent evt)//GEN-FIRST:event_pnlFilesKeyReleased
    {//GEN-HEADEREND:event_pnlFilesKeyReleased
        Log.entering("pnlFilesKeyReleased", evt);
        if (evt.getKeyCode() == KeyEvent.VK_F5)
        {
            Log.fine("KeyEvent.VK_F5");
            showFiles(m_currentPath, false);
        }
    }//GEN-LAST:event_pnlFilesKeyReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel pnlFiles;
    private javax.swing.JScrollPane pnlScroll;
    private javax.swing.JTextField txtPath;
    // End of variables declaration//GEN-END:variables

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
    public JPanel getFilesPanel()
    {
        return pnlFiles;
    }
}
