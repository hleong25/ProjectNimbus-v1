/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leong.nimbus.clouds.local;

import com.leong.nimbus.clouds.interfaces.ICloudModel;
import com.leong.nimbus.clouds.interfaces.ICloudProgress;
import com.leong.nimbus.clouds.interfaces.ICloudTransfer;
import com.leong.nimbus.utils.GlobalCache;
import com.leong.nimbus.utils.Logit;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
    public String getGlobalCacheKey()
    {
        return GlobalCache.getInstance().getKey(this);
    }

    @Override
    public boolean login(String userid)
    {
        return true;
    }

    @Override
    public boolean login(String userid, String authCode)
    {
        return true;
    }

    @Override
    public String getAuthUrl()
    {
        return "";
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
    public String getIdByItem(File item)
    {
        return item.getAbsolutePath();
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
    public void transfer(final ICloudTransfer<?,? super java.io.File> transfer)
    {
        Log.entering("transfering", new Object[]{transfer});

        InputStream is = transfer.getInputStream();
        OutputStream os = transfer.getOutputStream();

        try
        {
            final int BUFFER_SIZE = 256*1024;
            byte[] buffer = new byte[BUFFER_SIZE];

            long totalSent = 0;
            int readSize = 0;

            ICloudProgress progress = transfer.getProgressHandler();

            progress.initalize();
            progress.start(transfer.getFilesize());

            while (transfer.getCanTransfer() && ((readSize = is.read(buffer)) > 0))
            {
                totalSent += readSize;

                os.write(buffer, 0, readSize);

                progress.progress(totalSent);
            }

            if (transfer.getCanTransfer())
            {
                Log.fine("Transfer finished");

                progress.finish();

                File outputFile = (File) transfer.getTargetObject();
                transfer.setTransferredObject(new File(outputFile.getAbsolutePath()));
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
                Log.fine("Closing input stream");
                is.close();
            }
            catch (IOException ex)
            {
                Log.throwing("transfer", ex);
            }

            try
            {
                Log.fine("Closing output stream");
                os.flush();
                os.close();
            }
            catch (IOException ex)
            {
                Log.throwing("transfer", ex);
            }
        }
    }

    @Override
    public InputStream getDownloadStream(File downloadFile)
    {
        // TODO: error checking
        // caller must close stream
        try
        {
            final int BUFFER_SIZE = 256*1024;
            InputStream is = new BufferedInputStream(new FileInputStream(downloadFile), BUFFER_SIZE);
            return is;
        }
        catch (FileNotFoundException ex)
        {
            Log.throwing("getDownloadStream", ex);
        }

        return null;
    }

    @Override
    public OutputStream getUploadStream(File uploadFile)
    {
        // TODO: error checking
        // caller must close stream
        try
        {
            final int BUFFER_SIZE = 256*1024;
            OutputStream os = new BufferedOutputStream(new FileOutputStream(uploadFile), BUFFER_SIZE);
            return os;
        }
        catch (FileNotFoundException ex)
        {
            Log.throwing("getUploadStream", ex);
        }

        return null;
    }

    @Override
    public boolean isFolder(File item)
    {
        return item.isDirectory();
    }

    @Override
    public String getName(File item)
    {
        return item.getName();
    }

    @Override
    public String getAbsolutePath(File item)
    {
        return item.getAbsolutePath();
    }
}
