/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leong.nimbus.gui;

import com.leong.nimbus.utils.Tools;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author henry
 */
public class ResponsiveTaskUI
{
    public interface IResponsiveTask
    {
        void run();
    }

    private ResponsiveTaskUI()
    {
        // do nothing
    }

    public static void doTask(final IResponsiveTask taskIFace)
    {
        Thread thread = new Thread() {
            @Override
            public void run()
            {
                taskIFace.run();

                //try
                //{
                //    // suspend this thread via sleep() and yeild control to other threads
                //    Thread.sleep(5000);
                //}
                //catch (InterruptedException ex)
                //{
                //    //Logger.getLogger(ResponsiveTaskUI.class.getName()).log(Level.SEVERE, null, ex);
                //    Tools.logit("ResponsiveTaskUI.doTask() Thread.sleep() error: "+ex);
                //}
            }
        };

        thread.start();
    }
}
