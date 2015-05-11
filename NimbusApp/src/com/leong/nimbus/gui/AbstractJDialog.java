/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leong.nimbus.gui;

import com.leong.nimbus.utils.Tools;
import java.awt.Window;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import javax.swing.JDialog;

/**
 *
 * @author henry
 */
public abstract class AbstractJDialog
    extends JDialog
{

    protected DropTargetAdapter m_dropTarget;

    public AbstractJDialog()
    {
        this(null);

        //Tools.logit("AbstractJDialog.ctor(null)");
    }

    public AbstractJDialog(Window window)
    {
        super(window);

        Tools.logit("AbstractJDialog.ctor(window)");

        m_dropTarget = new DropTargetAdapter()
        {
            @Override
            public void drop(DropTargetDropEvent dtde)
            {
                action_drop(dtde);
            }
        };
    }

    public void setTitle(String title)
    {
        final String APP_NAME = " - Nimbus";

        if (title.endsWith(APP_NAME))
            super.setTitle(title);
        else
            super.setTitle(title + APP_NAME);
    }

    public void runLater()
    {
        Tools.logit("AbstractJDialog.runLater()");

        Runnable run = new Runnable()
        {
            @Override
            public void run()
            {
                do_run();
            }
        };

        java.awt.EventQueue.invokeLater(run);
    }

    public void runAndWait()
    {
        Tools.logit("AbstractJDialog.runAndWait()");

        Runnable run = new Runnable()
        {
            @Override
            public void run()
            {
                do_run();
            }
        };

        try
        {
            Tools.logit("AbstractJDialog.runAndWait() EventQueue.invokeAndWait(run)");
            java.awt.EventQueue.invokeAndWait(run);
            //SwingUtilities.invokeAndWait(run);
        }
        catch (InterruptedException ex)
        {
            //Logger.getLogger(AbstractJDialog.class.getName()).log(Level.SEVERE, null, ex);
            Tools.logit("AbstractJDialog.runAndWait() InterruptedException: "+ex.toString());
        }
        catch (InvocationTargetException ex)
        {
            //Logger.getLogger(AbstractJDialog.class.getName()).log(Level.SEVERE, null, ex);
            Tools.logit("AbstractJDialog.runAndWait() InvocationTargetException: "+ex.toString());
        }
    }

    protected void do_run()
    {
        //pack();
        //setModal(true);
        setVisible(true);
    }

    protected void action_windowOnClosing()
    {
        Tools.logit("AbstractJDialog.action_windowOnClosing()");
    }

    protected void responsiveTaskUI()
    {
        // this call should be from another thread so the UI can be responsive
        // refer to class ResponsiveTaskUI
        try
        {
            // suspend this thread via sleep() and yeild control to other threads
            Thread.sleep(10);
        }
        catch (InterruptedException ex)
        {
            //Logger.getLogger(ResponsiveTaskUI.class.getName()).log(Level.SEVERE, null, ex);
            Tools.logit("AbstractJDialog.responsiveTaskUI() Thread.sleep() error: "+ex);
        }
    }

    protected void action_drop(DropTargetDropEvent dtde)
    {
        Tools.logit("AbstractJDialog.action_drop()");

        // Accept copy drops
        dtde.acceptDrop(DnDConstants.ACTION_COPY);

        // Get the transfer which can provide the dropped item data
        Transferable transferable = dtde.getTransferable();

        // Get the data formats of the dropped item
        DataFlavor[] flavors = transferable.getTransferDataFlavors();

        // Loop through the flavors
        for (DataFlavor flavor : flavors)
        {
            try
            {
                // If the drop items are files
                if (flavor.isFlavorJavaFileListType())
                {
                    // Get all of the dropped files
                    final List files = (List) transferable.getTransferData(flavor);

                    ResponsiveTaskUI.doTask(new ResponsiveTaskUI.IResponsiveTask()
                    {
                        @Override
                        public void run()
                        {
                            onAction_drop(files);
                        }
                    });
                }
            }
            catch (Exception e)
            {
                // Print out the error stack
                e.printStackTrace();

            }
        }

        // Inform that the drop is complete
        dtde.dropComplete(true);
    }

    protected boolean onAction_drop(List objs)
    {
        return false;
    }
}
