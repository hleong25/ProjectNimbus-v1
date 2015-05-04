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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.SwingUtilities;

/**
 *
 * @author henry
 */
public abstract class AbstractJDialog extends JDialog
{
    public AbstractJDialog()
    {
        this(null);

        //Tools.logit("AbstractJDialog.ctor(null)");
    }

    public AbstractJDialog(Window window)
    {
        super(window);

        Tools.logit("AbstractJDialog.ctor(window)");

        myInit();
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
        Tools.logit("AbstractJDialog.run()");

        Runnable run = new Runnable()
        {
            @Override
            public void run()
            {
                do_run();
            }
        };

        if (false)
        {
            Tools.logit("AbstractJDialog.run() EventQueue.invokeLater(run)");
            SwingUtilities.invokeLater(run);
        }
        else
        {
            try
            {
        Tools.logit("AbstractJDialog.run() EventQueue.invokeAndWait(run)");
                //java.awt.EventQueue.invokeAndWait(run);
                SwingUtilities.invokeAndWait(run);
            }
            catch (InterruptedException ex)
            {
                //Logger.getLogger(AbstractJDialog.class.getName()).log(Level.SEVERE, null, ex);
                Tools.logit("AbstractJDialog.run() InterruptedException: "+ex.toString());
            }
            catch (InvocationTargetException ex)
            {
                //Logger.getLogger(AbstractJDialog.class.getName()).log(Level.SEVERE, null, ex);
                Tools.logit("AbstractJDialog.run() InvocationTargetException: "+ex.toString());
            }
        }
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
        Tools.logit("AbstractJDialog.initFrame()");

        setModal(true);

        //setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        //addWindowListener(new WindowAdapter()
        //{
        //    public void windowClosing(WindowEvent e)
        //    {
        //        Tools.logit("AbstractJDialog.windowClosing()");
        //        action_windowOnClosing();
        //        dispose();
        //    }
        //});

        return true;
    }

    protected void action_windowOnClosing()
    {
        Tools.logit("AbstractJDialog.action_windowOnClosing()");
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //
    // Interfaces
    //
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // to initialize the member variables
    public abstract boolean initVars();
}
