/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leong.nimbus.gui.datatransfer;

import com.leong.nimbus.clouds.CloudType;
import com.leong.nimbus.gui.components.FileItemPanel;
import com.leong.nimbus.utils.Logit;
import java.awt.datatransfer.DataFlavor;
import java.io.File;
import java.util.List;

/**
 *
 * @author henry
 */
public class ListLocalTransferable
    extends TransferableAdapter<File>
{
    private static final Logit Log = Logit.create(ListLocalTransferable.class.getName());

    private static final DataFlavor[] LocalFlavors = new DataFlavor[]{
        LocalFileFlavor
    };

    public ListLocalTransferable()
    {
        // empty
    }

    @Override
    public DataFlavor[] getTransferDataFlavors()
    {
        return LocalFlavors;
    }

    public static ListLocalTransferable createInstance(List<FileItemPanel> pnls)
    {
        ListLocalTransferable list = new ListLocalTransferable();

        for (FileItemPanel pnl : pnls)
        {
            if (pnl.getFileItem().getCloudType() != CloudType.LOCAL_FILE_SYSTEM)
            {
                continue;
            }

            list.add((File) pnl.getFileItem().getCloudObject());
        }

        return list;
    }
}
