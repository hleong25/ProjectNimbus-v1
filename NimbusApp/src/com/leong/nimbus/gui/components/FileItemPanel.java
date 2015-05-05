/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leong.nimbus.gui.components;

import java.awt.Component;

/**
 *
 * @author henry
 */
public class FileItemPanel extends javax.swing.JPanel
{

    /**
     * Creates new form FileItemPanel
     */
    public FileItemPanel(IFileItem item)
    {
        initComponents();

        m_item = item;

        lblIcon.setIcon(m_item.getIcon());
        lblIcon.setText(null);

        lblLabel.setText(getHtmlLabel(m_item.getLabel()));
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

        lblIcon = new javax.swing.JLabel();
        lblLabel = new javax.swing.JLabel();

        setBorder(javax.swing.BorderFactory.createEmptyBorder(2, 2, 2, 2));
        setLayout(new java.awt.BorderLayout());

        lblIcon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblIcon.setText("icon");
        lblIcon.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 2, 0));
        lblIcon.setMaximumSize(new java.awt.Dimension(96, 32));
        lblIcon.setMinimumSize(new java.awt.Dimension(96, 32));
        lblIcon.setPreferredSize(new java.awt.Dimension(96, 32));
        add(lblIcon, java.awt.BorderLayout.CENTER);

        lblLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblLabel.setText("label");
        lblLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        add(lblLabel, java.awt.BorderLayout.SOUTH);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lblIcon;
    private javax.swing.JLabel lblLabel;
    // End of variables declaration//GEN-END:variables

    protected IFileItem m_item;

    public IFileItem getFileItem()
    {
        return m_item;
    }

    protected String getHtmlLabel(String str)
    {
        final int max_width = getSize().width;

        return "<html><body style='text-align:center; width:"+Integer.toString(max_width)+"px'><p>"+str+"</p></body></html>";
    }

    @Override
    public Component.BaselineResizeBehavior getBaselineResizeBehavior() {
        return Component.BaselineResizeBehavior.CONSTANT_ASCENT;
    }

    @Override
    public int getBaseline(int width, int height) {
        return 0;
    }
}
