/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leong.nimbus.gui;

import com.leong.nimbus.utils.Tools;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.InvocationTargetException;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 *
 * @author henry
 */
public abstract class AbstractJFrame extends JFrame
{
    public AbstractJFrame()
    {
        Tools.logit("AbstractJFrame.ctor(null)");

        myInit();
    }

    public AbstractJFrame(String title)
    {
        Tools.logit("AbstractJFrame.ctor(title='"+title+"')");

        myInit();

        setTitle(title);
    }

    public void setTitle(String title)
    {
        final String APP_NAME = " - Nimbus";

        if (title.endsWith(APP_NAME))
            super.setTitle(title);
        else
            super.setTitle(title + APP_NAME);
    }

    public void run()
    {
        Tools.logit("AbstractJFrame.run()");

        Tools.logit("AbstractJFrame.run() EventQueue.invokeLater(run)");
        java.awt.EventQueue.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                do_run();
            }
        });
    }

    protected void do_run()
    {
        //pack();
        setVisible(true);
    }

    protected boolean myInit()
    {
        initFrame();
        initVars();

        return true;
    }

    // to initialize the GUI form
    public boolean initFrame()
    {
        Tools.logit("AbstractJFrame.initFrame()");

        addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                Tools.logit("AbstractJFrame.windowClosing()");
                action_windowOnClosing();
                dispose();
            }
        });

        return true;
    }

    protected void action_windowOnClosing()
    {
        Tools.logit("AbstractJFrame.action_windowOnClosing()");
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //
    // Interfaces
    //
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // to initialize the member variables
    public abstract boolean initVars();
}
