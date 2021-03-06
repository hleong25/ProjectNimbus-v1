/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leong.nimbus.gui.layout;

import com.leong.nimbus.gui.interfaces.ILayoutToCloudPanelProxy;
import com.leong.nimbus.utils.Logit;
import java.awt.Component;

/**
 *
 * @author henry
 */
public class WrapScrollPanel extends javax.swing.JPanel
{
    private static final Logit Log = Logit.create(WrapScrollPanel.class.getName());

    private ILayoutToCloudPanelProxy m_proxy;

    /**
     * Creates new form WrapScrollPanel
     */
    public WrapScrollPanel()
    {
        Log.entering("<init>");
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
        pnlComps = new javax.swing.JPanel();

        setLayout(new java.awt.BorderLayout());

        pnlScroll.setMinimumSize(new java.awt.Dimension(400, 300));
        pnlScroll.setPreferredSize(new java.awt.Dimension(400, 300));

        pnlComps.setBackground(new java.awt.Color(255, 255, 255));
        WrapLayout wraplayout = new WrapLayout(java.awt.FlowLayout.LEADING);
        wraplayout.setAlignOnBaseline(true);
        pnlComps.setLayout(wraplayout);
        pnlComps.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                pnlCompsMouseClicked(evt);
            }
        });
        pnlComps.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyReleased(java.awt.event.KeyEvent evt)
            {
                pnlCompsKeyReleased(evt);
            }
        });
        pnlScroll.setViewportView(pnlComps);

        add(pnlScroll, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void pnlCompsMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_pnlCompsMouseClicked
    {//GEN-HEADEREND:event_pnlCompsMouseClicked
        pnlComps.requestFocusInWindow();
    }//GEN-LAST:event_pnlCompsMouseClicked

    private void pnlCompsKeyReleased(java.awt.event.KeyEvent evt)//GEN-FIRST:event_pnlCompsKeyReleased
    {//GEN-HEADEREND:event_pnlCompsKeyReleased
        Log.entering("pnlCompsKeyReleased", evt);
        if (m_proxy != null)
        {
            m_proxy.proxyKeyReleased(evt);
        }
    }//GEN-LAST:event_pnlCompsKeyReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel pnlComps;
    private javax.swing.JScrollPane pnlScroll;
    // End of variables declaration//GEN-END:variables

    @Override
    public void removeAll()
    {
        pnlComps.removeAll();
    }

    @Override
    public Component add(Component comp)
    {
        //Log.entering("add", comp);
        return pnlComps.add(comp);
    }

}
