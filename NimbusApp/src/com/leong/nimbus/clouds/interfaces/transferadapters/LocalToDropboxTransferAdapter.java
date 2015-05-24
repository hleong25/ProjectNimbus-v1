/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leong.nimbus.clouds.interfaces.transferadapters;

import com.dropbox.core.DbxEntry;
import com.leong.nimbus.clouds.interfaces.ICloudProgress;
import com.leong.nimbus.clouds.interfaces.ICloudTransfer;
import com.leong.nimbus.utils.Logit;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author henry
 */
public class LocalToDropboxTransferAdapter
    implements ICloudTransfer<java.io.File, DbxEntry.File>
{
    private static final Logit Log = Logit.create(LocalToDropboxTransferAdapter.class.getName());

    protected final java.io.File m_source;
    protected final DbxEntry.File m_target;

    protected DbxEntry.File m_xferred;

    private ICloudProgress m_progress;

    public LocalToDropboxTransferAdapter(java.io.File source, DbxEntry.File target)
    {
        m_source = source;
        m_target = target;
    }

    @Override
    public long getFilesize()
    {
        return m_source.length();
    }

    @Override
    public File getSourceObject()
    {
        return m_source;
    }

    @Override
    public DbxEntry.File getTargetObject()
    {
        return m_target;
    }

    @Override
    public void setTransferredObject(Object obj)
    {
        m_xferred = (DbxEntry.File) obj;
    }

    @Override
    public DbxEntry.File getTransferredObject()
    {
        return m_xferred;
    }

    @Override
    public InputStream getInputStream()
    {
        // caller must close inputstream;

        FileInputStream fis = null;
        try
        {
            fis = new FileInputStream(getSourceObject());
            BufferedInputStream bis = new BufferedInputStream(fis);
            return bis;
        }
        catch (FileNotFoundException ex)
        {
            //Logger.getLogger(LocalPanel.class.getName()).log(Level.SEVERE, null, ex);
            Log.throwing("getInputStream", ex);

            try
            {
                if (fis != null)
                    fis.close();
            }
            catch (IOException ex1)
            {
                Log.throwing("getInputStream", ex1);
            }

            return null;
        }
    }

    @Override
    public OutputStream getOutputStream()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setProgressHandler(ICloudProgress progress)
    {
        m_progress = progress;
    }

    @Override
    public ICloudProgress getProgressHandler()
    {
        return m_progress;
    }

}
