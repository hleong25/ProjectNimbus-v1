/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leong.nimbus.clouds.interfaces.transferadapters;

import com.leong.nimbus.utils.Logit;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author henry
 */
public class DropboxToLocalTransferAdapter
    extends CloudTransferAdapter<com.dropbox.core.DbxEntry, java.io.File>
{
    private static final Logit Log = Logit.create(DropboxToLocalTransferAdapter.class.getName());

    public DropboxToLocalTransferAdapter(String sourceCacheKey, com.dropbox.core.DbxEntry source, java.io.File target)
    {
        super(sourceCacheKey, source, target);
    }

    @Override
    public long getFilesize()
    {
        return m_source.asFile().numBytes;
    }

    @Override
    public OutputStream getOutputStream()
    {
        try
        {
            OutputStream os = new BufferedOutputStream(new FileOutputStream(m_target));
            return os;
        }
        catch (FileNotFoundException ex)
        {
            Log.throwing("getOutputStream", ex);
        }
        return null;
    }

}
