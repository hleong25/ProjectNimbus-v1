/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leong.nimbus.mainapp;

import com.leong.nimbus.gui.PickCloudFrame;
import com.leong.nimbus.utils.Logit;

/**
 *
 * @author henry
 */
public class MainGuiApp
{
    private static final Logit Log = Logit.create(MainGuiApp.class.getName());

    public static void main(String[] args)
    {
        Logit.init();
        Log.entering("main");

        //setupLookAndFeel();

        PickCloudFrame.showMe();
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
        catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex)
        {
            Log.severe(ex.toString());
        }

    }

}
