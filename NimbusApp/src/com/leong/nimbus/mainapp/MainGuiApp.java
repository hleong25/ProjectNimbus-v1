/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leong.nimbus.mainapp;

import com.leong.nimbus.gui.PickCloudFrame;
import com.leong.nimbus.utils.Tools;

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
