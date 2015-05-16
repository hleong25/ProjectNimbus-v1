/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leong.nimbus.gui.helpers;

import com.leong.nimbus.utils.Logit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.util.List;

/**
 *
 * @author henry
 */
public abstract class DefaultDropTargetAdapter extends DropTargetAdapter
{
    private static final Logit Log = Logit.create(DefaultDropTargetAdapter.class.getName());

    public DefaultDropTargetAdapter()
    {
        // do nothing
    }

    @Override
    public void drop(DropTargetDropEvent dtde)
    {
        Log.entering("drop");

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
            catch (Exception ex)
            {
                // Print out the error stack
                Log.severe(ex.toString());
            }
        }

        // Inform that the drop is complete
        dtde.dropComplete(true);
    }

    public abstract boolean onAction_drop(List list);
}
