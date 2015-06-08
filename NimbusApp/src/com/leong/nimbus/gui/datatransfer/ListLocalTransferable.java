/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leong.nimbus.gui.datatransfer;

import com.leong.nimbus.clouds.CloudType;
import com.leong.nimbus.clouds.interfaces.ICloudController;
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

    public ListLocalTransferable(ICloudController controller)
    {
        super(controller);
    }

    @Override
    public DataFlavor[] getTransferDataFlavors()
    {
        return LocalFlavors;
    }

    public static ListLocalTransferable createInstance(ICloudController controller, List<FileItemPanel> pnls)
    {
        Log.entering("createInstance", new Object[] {controller, pnls});

        ListLocalTransferable list = new ListLocalTransferable(controller);

        for (FileItemPanel pnl : pnls)
        {
            Log.fine(pnl.getFileItem().getCloudController().getCloudType().toString());
            if (pnl.getFileItem().getCloudController().getCloudType() != CloudType.LOCAL_FILE_SYSTEM)
            {
                continue;
            }

            list.add((File) pnl.getFileItem().getCloudObject());
        }

        return list;
    }
}
