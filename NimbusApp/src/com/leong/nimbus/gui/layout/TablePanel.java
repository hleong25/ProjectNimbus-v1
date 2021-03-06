/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leong.nimbus.gui.layout;

import com.leong.nimbus.gui.interfaces.ILayoutToCloudPanelProxy;
import java.awt.Component;

/**
 *
 * @author henry
 */
public class TablePanel extends javax.swing.JPanel
{
    private ILayoutToCloudPanelProxy m_proxy;

    /**
     * Creates new form TablePanel
     */
    public TablePanel()
    {
        initComponents();
    }

    public void setProxy(ILayoutToCloudPanelProxy proxy)
    {
        m_proxy = proxy;
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

        pnlScroll = new javax.swing.JScrollPane();
        pnlTable = new javax.swing.JTable();

        setLayout(new java.awt.BorderLayout());

        pnlScroll.setMinimumSize(new java.awt.Dimension(400, 300));
        pnlScroll.setPreferredSize(new java.awt.Dimension(400, 300));

        pnlTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String []
            {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        pnlScroll.setViewportView(pnlTable);

        add(pnlScroll, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane pnlScroll;
    private javax.swing.JTable pnlTable;
    // End of variables declaration//GEN-END:variables

    @Override
    public void removeAll()
    {
        pnlTable.removeAll();
    }

    @Override
    public Component add(Component comp)
    {
        //Log.entering("add", comp);
        //return pnlTable.add(comp);
        return comp;
    }
}
