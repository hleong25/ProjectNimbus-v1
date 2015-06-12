/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leong.nimbus.clouds.interfaces.transferadapters;

import com.dropbox.core.DbxEntry;
import com.leong.nimbus.utils.Logit;
import java.io.OutputStream;

/**
 *
 * @author henry
 */
public class DropboxToDropboxTransferAdapter
    extends CloudTransferAdapter<DbxEntry, DbxEntry>
{
    private static final Logit Log = Logit.create(DropboxToDropboxTransferAdapter.class.getName());

    public DropboxToDropboxTransferAdapter(String sourceCacheKey,
                                           DbxEntry source,
                                           String targetCacheKey,
                                           DbxEntry target)
    {
        super(sourceCacheKey, source, targetCacheKey, target);
    }

    @Override
    public long getFilesize()
    {
        return m_source.asFile().numBytes;
    }

}
