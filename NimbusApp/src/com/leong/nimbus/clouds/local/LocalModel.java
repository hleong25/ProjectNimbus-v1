/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leong.nimbus.clouds.local;

import com.leong.nimbus.clouds.interfaces.ICloudModel;
import com.leong.nimbus.clouds.interfaces.ICloudProgress;
import com.leong.nimbus.clouds.interfaces.ICloudTransfer;
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

    public void transfer(ICloudTransfer transfer)
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

            while (is.available() > 0)
            {
                int readSize = is.read(buffer);
                totalSent += readSize;

                os.write(buffer, 0, readSize);

                progress.progress(totalSent);
            }

            progress.finish();
        }
        catch (IOException ex)
        {
            //Logger.getLogger(LocalModel.class.getName()).log(Level.SEVERE, null, ex);
            // todo log
        }
        finally
        {
            try
            {
                is.close();
            }
            catch (IOException ex)
            {
                // todo log
            }
            try
            {
                os.close();
            }
            catch (IOException ex)
            {
                // todo log
            }
        }

    }
}
