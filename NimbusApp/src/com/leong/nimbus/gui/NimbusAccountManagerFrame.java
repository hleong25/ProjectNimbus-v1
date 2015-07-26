/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leong.nimbus.gui;

import com.leong.nimbus.accountmanager.AccountInfo;
import com.leong.nimbus.accountmanager.AccountManagerModel;
import com.leong.nimbus.clouds.CloudType;
import com.leong.nimbus.clouds.dropbox.DropboxController;
import com.leong.nimbus.clouds.google.drive.GDriveController;
import com.leong.nimbus.clouds.interfaces.ICloudController;
import com.leong.nimbus.utils.Logit;
import javax.swing.JOptionPane;

/**
 *
 * @author henry
 */
public class NimbusAccountManagerFrame extends javax.swing.JFrame
{
    private static final Logit Log = Logit.create(NimbusAccountManagerFrame.class.getName());

    /**
     * Creates new form NimbusAccountManagerFrame
     */
    public NimbusAccountManagerFrame()
    {
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

        pnlAccounts = new javax.swing.JSplitPane();
        pnlExistingAccounts = new javax.swing.JPanel();
        pnlAccountDetails = new javax.swing.JPanel();
        jMenuBar1 = new javax.swing.JMenuBar();
        mnuAddAccount = new javax.swing.JMenu();
        mnuAddGoogleDrive = new javax.swing.JMenuItem();
        mnuAddDropbox = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Nimbus");
        setMinimumSize(new java.awt.Dimension(800, 600));
        setPreferredSize(new java.awt.Dimension(800, 600));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        pnlExistingAccounts.setMinimumSize(new java.awt.Dimension(200, 10));
        pnlExistingAccounts.setPreferredSize(new java.awt.Dimension(200, 100));
        pnlAccounts.setLeftComponent(pnlExistingAccounts);
        pnlAccounts.setRightComponent(pnlAccountDetails);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(pnlAccounts, gridBagConstraints);

        mnuAddAccount.setText("Add Account");

        mnuAddGoogleDrive.setText("Google Drive");
        mnuAddGoogleDrive.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                mnuAddGoogleDriveActionPerformed(evt);
            }
        });
        mnuAddAccount.add(mnuAddGoogleDrive);

        mnuAddDropbox.setText("Dropbox");
        mnuAddDropbox.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                mnuAddDropboxActionPerformed(evt);
            }
        });
        mnuAddAccount.add(mnuAddDropbox);

        jMenuBar1.add(mnuAddAccount);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void mnuAddGoogleDriveActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_mnuAddGoogleDriveActionPerformed
    {//GEN-HEADEREND:event_mnuAddGoogleDriveActionPerformed
        addAccount(CloudType.GOOGLE_DRIVE);
    }//GEN-LAST:event_mnuAddGoogleDriveActionPerformed

    private void mnuAddDropboxActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_mnuAddDropboxActionPerformed
    {//GEN-HEADEREND:event_mnuAddDropboxActionPerformed
        addAccount(CloudType.DROPBOX);
    }//GEN-LAST:event_mnuAddDropboxActionPerformed

    public static void showMe()
    {
        java.awt.EventQueue.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                NimbusAccountManagerFrame frame = new NimbusAccountManagerFrame();
                frame.setVisible(true);
            }
        });



        {
            AccountManagerModel acctmgr = AccountManagerModel.getInstance();
            if (acctmgr != null)
            {
                AccountInfo info;

                info = AccountInfo.createInstance(CloudType.GOOGLE_DRIVE);
                info.setName("hleong25@gmail.com");
                info.setSecret(new String[]{"token", "refresh"});
                acctmgr.addAccountInfo(info);

                info = AccountInfo.createInstance(CloudType.DROPBOX);
                info.setName("hleong25+dropbox@gmail.com");
                info.setSecret(new String[]{"token"});
                acctmgr.addAccountInfo(info);

                Log.info(acctmgr.toString());
            }
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[])
    {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try
        {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels())
            {
                if ("Nimbus".equals(info.getName()))
                {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        }
        catch (ClassNotFoundException ex)
        {
            java.util.logging.Logger.getLogger(NimbusAccountManagerFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (InstantiationException ex)
        {
            java.util.logging.Logger.getLogger(NimbusAccountManagerFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (IllegalAccessException ex)
        {
            java.util.logging.Logger.getLogger(NimbusAccountManagerFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (javax.swing.UnsupportedLookAndFeelException ex)
        {
            java.util.logging.Logger.getLogger(NimbusAccountManagerFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                new NimbusAccountManagerFrame().setVisible(true);
            }
        });
    }

    protected void addAccount(CloudType cloudType)
    {
        ICloudController<?> controller = null;

        switch (cloudType)
        {
            case GOOGLE_DRIVE:
                controller = new GDriveController();
                break;
            case DROPBOX:
                controller = new DropboxController();
                break;

            default:
                Log.severe("Failed to add '"+cloudType.toString()+"' acount.");
                return;
        }

        boolean isLogin = controller.login(this, null);

        if (isLogin)
        {
            // kill me since we're good now
            dispose();
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenu mnuAddAccount;
    private javax.swing.JMenuItem mnuAddDropbox;
    private javax.swing.JMenuItem mnuAddGoogleDrive;
    private javax.swing.JPanel pnlAccountDetails;
    private javax.swing.JSplitPane pnlAccounts;
    private javax.swing.JPanel pnlExistingAccounts;
    // End of variables declaration//GEN-END:variables
}
