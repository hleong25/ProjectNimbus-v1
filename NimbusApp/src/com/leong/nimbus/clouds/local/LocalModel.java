/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leong.nimbus.clouds.local;

import com.leong.nimbus.clouds.interfaces.ICloudModel;
import com.leong.nimbus.clouds.interfaces.ICloudProgress;
import com.leong.nimbus.clouds.interfaces.ICloudTransfer;
import com.leong.nimbus.utils.Logit;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.filechooser.FileSystemView;

/**
 *
 * @author henry
 */
public class LocalModel implements ICloudModel<java.io.File>
{
    private static final Logit Log = Logit.create(LocalModel.class.getName());

    public LocalModel()
    {
    }

    @Override
    public File getRoot()
    {
        File root = FileSystemView.getFileSystemView().getHomeDirectory();
        return root;
    }

    @Override
    public File getItemById(String id)
    {
        File file = new File(id);
        return file;
    }

    @Override
    public List<File> getChildrenItems(File parent)
    {
        List<File> list = new ArrayList<>();

        if (!parent.isDirectory())
        {
            // nothing to do
            return list;
        }

        list.addAll(Arrays.asList(parent.listFiles()));

        return list;
    }

    @Override
    public void transfer(ICloudTransfer<?,?> transfer)
    {
        InputStream is = transfer.getInputStream();
        OutputStream os = transfer.getOutputStream();

        try
        {
            final int BUFFER_SIZE = 256*1024;
            byte[] buffer = new byte[BUFFER_SIZE];

            long totalSent = 0;

            ICloudProgress progress = transfer.getProgressHandler();

            progress.initalize();
            progress.start(transfer.getFilesize());

            while (transfer.getCanTransfer() && is.available() > 0)
            {
                int readSize = is.read(buffer);
                totalSent += readSize;

                os.write(buffer, 0, readSize);

                progress.progress(totalSent);
            }

            if (transfer.getCanTransfer())
            {
                progress.finish();

                {
                    File outputFile = (File) transfer.getTargetObject();
                    transfer.setTransferredObject(new File(outputFile.getAbsolutePath()));
                }
            }
            else
            {
                Log.warning("Transferred aborted");
            }
        }
        catch (IOException ex)
        {
            Log.throwing("transfer", ex);
        }
        finally
        {
            try
            {
                is.close();
            }
            catch (IOException ex)
            {
                Log.throwing("transfer", ex);
            }

            try
            {
                os.close();
            }
            catch (IOException ex)
            {
                Log.throwing("transfer", ex);
            }
        }

    }
}
