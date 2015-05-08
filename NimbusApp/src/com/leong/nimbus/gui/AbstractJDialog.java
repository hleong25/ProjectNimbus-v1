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
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import javax.swing.JDialog;

/**
 *
 * @author henry
 */
public abstract class AbstractJDialog
    extends JDialog
    implements DropTargetListener
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

        //myInit();
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

        try
        {
            Tools.logit("AbstractJDialog.run() EventQueue.invokeAndWait(run)");
            java.awt.EventQueue.invokeAndWait(run);
            //SwingUtilities.invokeAndWait(run);
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

    protected void do_run()
    {
        //pack();
        setModal(true);
        setVisible(true);
    }

    protected void action_windowOnClosing()
    {
        Tools.logit("AbstractJDialog.action_windowOnClosing()");
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //
    // Implements DropTargetListener
    //
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void dragEnter(DropTargetDragEvent dtde)
    {

    }

    @Override
    public void dragOver(DropTargetDragEvent dtde)
    {
    }

    @Override
    public void dropActionChanged(DropTargetDragEvent dtde)
    {
    }

    @Override
    public void dragExit(DropTargetEvent dte)
    {
    }

    @Override
    public void drop(DropTargetDropEvent dtde)
    {
        Tools.logit("AbstractJDialog.drop()");

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
                    List files = (List) transferable.getTransferData(flavor);

                    onDropAction(files);

                    //// Loop them through
                    //for (Object obj : files)
                    //{
                    //    File file = (File) obj;

                    //    // Print out the file path
                    //    System.out.println("File path is '" + file.getPath() + "'.");

                    //}
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

    protected abstract boolean onDropAction(List objs);
}
