/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leong.nimbus.gui.datatransfer;

import com.dropbox.core.DbxEntry;
import com.leong.nimbus.clouds.CloudType;
import com.leong.nimbus.gui.components.FileItemPanel;
import com.leong.nimbus.utils.Logit;
import java.awt.datatransfer.DataFlavor;
import java.util.List;

/**
 *
 * @author henry
 */
public class ListDropboxTransferable
    extends TransferableAdapter<DbxEntry>
{
    private static final Logit Log = Logit.create(ListDropboxTransferable.class.getName());

    private static final DataFlavor[] DropboxFlavors = new DataFlavor[]{
        DropboxFileFlavor
    };

    public ListDropboxTransferable()
    {
        // empty
    }

    @Override
    public DataFlavor[] getTransferDataFlavors()
    {
        return DropboxFlavors;
    }

    public static ListDropboxTransferable createInstance(List<FileItemPanel> pnls)
    {
        ListDropboxTransferable list = new ListDropboxTransferable();

        for (FileItemPanel pnl : pnls)
        {
            if (pnl.getFileItem().getCloudType() != CloudType.DROPBOX)
            {
                continue;
            }

            list.add((DbxEntry) pnl.getFileItem().getCloudObject());
        }

        return list;
    }
}
