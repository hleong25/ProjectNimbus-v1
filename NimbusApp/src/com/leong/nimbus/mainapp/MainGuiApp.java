/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leong.nimbus.mainapp;

import com.leong.nimbus.clouds.google.drive.GDriveDialog;
import com.leong.nimbus.gui.PickCloudFrame;
import com.leong.nimbus.utils.Tools;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author henry
 */
public class MainGuiApp
{
    public static void main(String[] args)
    {
        Tools.logit("MainGuiApp");

        //setupLookAndFeel();

        PickCloudFrame.showMe();

        if (true)
            return;

        try
        {
            Tools.logit("AbstractDialog.runAndWait() EventQueue.invokeAndWait(run)");
            //final PickCloudDialog picker = new PickCloudDialog();
            final PickCloudFrame picker = new PickCloudFrame();

            //picker.runAndWait();
            java.awt.EventQueue.invokeAndWait(new Runnable()
            {
                @Override
                public void run()
                {
                    picker.setVisible(true);
                }
            });

            //SwingUtilities.invokeAndWait(run);
        }
        catch (InterruptedException ex)
        {
            //Logger.getLogger(AbstractJDialog.class.getName()).log(Level.SEVERE, null, ex);
            Tools.logit("AbstractDialog.runAndWait() InterruptedException: "+ex.toString());
        }
        catch (InvocationTargetException ex)
        {
            //Logger.getLogger(AbstractJDialog.class.getName()).log(Level.SEVERE, null, ex);
            Tools.logit("AbstractDialog.runAndWait() InvocationTargetException: "+ex.toString());
        }
    }

    private static void setupLookAndFeel()
    {
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
            Tools.logit("setupLookAndFeel() ClassNotFoundException: "+ex);
        }
        catch (InstantiationException ex)
        {
            Tools.logit("setupLookAndFeel() InstantiationException: "+ex);
        }
        catch (IllegalAccessException ex)
        {
            Tools.logit("setupLookAndFeel() IllegalAccessException: "+ex);
        }
        catch (javax.swing.UnsupportedLookAndFeelException ex)
        {
            Tools.logit("setupLookAndFeel() UnsupportedLookAndFeelException: "+ex);
        }

    }

}
