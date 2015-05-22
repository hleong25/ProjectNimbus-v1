/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leong.nimbus.gui.layout;

import com.leong.nimbus.utils.Logit;
import java.awt.CardLayout;
import java.awt.Component;
import java.util.EnumMap;
import java.util.Map;

/**
 *
 * @author henry
 */
public class AllCardsPanel extends javax.swing.JPanel
{
    private static final Logit Log = Logit.create(AllCardsPanel.class.getName());

    public static enum ViewType
    {
        LARGE_ICONS,
        DETAILED_MODE,
    }

    private static final Map<ViewType, String> ViewTypeStrings;
    static
    {
        ViewTypeStrings = new EnumMap<>(ViewType.class);
        ViewTypeStrings.put(ViewType.LARGE_ICONS, "Large Icons");
        ViewTypeStrings.put(ViewType.DETAILED_MODE, "Detailed Mode");
    };

    private final CardLayout m_cards;

    /**
     * Creates new form AllCardsPanel
     */
    public AllCardsPanel()
    {
        initComponents();

        m_cards = (CardLayout)pnlAllCards.getLayout();
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

        pnlAllCards = new javax.swing.JPanel();
        pnlWrap = new com.leong.nimbus.gui.layout.WrapScrollPanel();
        pnlTable = new com.leong.nimbus.gui.layout.TablePanel();

        setLayout(new java.awt.BorderLayout());

        pnlAllCards.setLayout(new java.awt.CardLayout());
        pnlAllCards.add(pnlWrap, "Large Icons");
        pnlAllCards.add(pnlTable, "Detailed Mode");

        add(pnlAllCards, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel pnlAllCards;
    private com.leong.nimbus.gui.layout.TablePanel pnlTable;
    private com.leong.nimbus.gui.layout.WrapScrollPanel pnlWrap;
    // End of variables declaration//GEN-END:variables

    public void showView(ViewType type)
    {
        Log.entering("showView", type);

        String cardName = ViewTypeStrings.get(type);

        m_cards.show(pnlAllCards, cardName);
    }

    @Override
    public Component add(Component comp)
    {
        pnlWrap.add(comp);
        pnlTable.add(comp);

        return comp;
    }


}