/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leong.nimbus.gui.datatransfer;

import com.google.api.services.drive.model.File;
import com.leong.nimbus.clouds.CloudType;
import com.leong.nimbus.clouds.interfaces.ICloudController;
import com.leong.nimbus.gui.components.FileItemPanel;
import com.leong.nimbus.utils.Logit;
import java.awt.datatransfer.DataFlavor;
import java.util.List;

/**
 *
 * @author henry
 */
public class ListGDriveTransferable
    extends TransferableAdapter<File>
{
    private static final Logit Log = Logit.create(ListGDriveTransferable.class.getName());

    private static final DataFlavor[] GDriveFlavors = new DataFlavor[]{
        GDriveFileFlavor
    };

    public ListGDriveTransferable(ICloudController controller)
    {
        super(controller);
    }

    @Override
    public DataFlavor[] getTransferDataFlavors()
    {
        return GDriveFlavors;
    }

    public static ListGDriveTransferable createInstance(ICloudController controller, List<FileItemPanel> pnls)
    {
        ListGDriveTransferable list = new ListGDriveTransferable(controller);

        for (FileItemPanel pnl : pnls)
        {
            if (pnl.getFileItem().getCloudController().getCloudType() != CloudType.GOOGLE_DRIVE)
            {
                continue;
            }

            list.add((File) pnl.getFileItem().getCloudObject());
        }

        return list;
    }
}
