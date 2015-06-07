/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leong.nimbus.gui.helpers;

import com.leong.nimbus.gui.datatransfer.TransferableAdapter;
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

    public abstract boolean onAction_drop(List list);

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
                final List files;

                // If the drop items are files
                if (flavor.isFlavorJavaFileListType())
                {
                    Log.fine("Drag&Drop from system");
                    Log.fine("Flavor mime: "+flavor.getMimeType());
                    // Get all of the dropped files
                    files = (List) transferable.getTransferData(flavor);
                }
                else if (flavor == TransferableAdapter.LocalFileFlavor)
                {
                    Log.fine("Drag&Drop from local file");
                    Log.fine("Flavor mime: "+flavor.getMimeType());
                    files = (List)transferable.getTransferData(flavor);
                }
                else if (flavor == TransferableAdapter.GDriveFileFlavor)
                {
                    Log.fine("Drag&Drop from gdrive file");
                    Log.fine("Flavor mime: "+flavor.getMimeType());
                    files = (List)transferable.getTransferData(flavor);
                }
                else if (flavor == TransferableAdapter.DropboxFileFlavor)
                {
                    Log.fine("Drag&Drop from dropbox file");
                    Log.fine("Flavor mime: "+flavor.getMimeType());
                    files = (List)transferable.getTransferData(flavor);
                }
                else
                {
                    files = null;
                }

                ResponsiveTaskUI.doTask(new ResponsiveTaskUI.IResponsiveTask()
                {
                    @Override
                    public void run()
                    {
                        if (files != null)
                        {
                            onAction_drop(files);
                        }
                    }
                });
            }
            catch (Exception ex)
            {
                Log.throwing("drop", ex);
            }
        }

        // Inform that the drop is complete
        dtde.dropComplete(true);
    }
}
