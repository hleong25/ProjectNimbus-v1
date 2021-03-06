/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leong.nimbus.clouds.interfaces.transferadapters;

import com.leong.nimbus.utils.GlobalCacheKey;
import com.leong.nimbus.utils.Logit;

/**
 *
 * @author henry
 */
public class DropboxToLocalTransferAdapter
    extends CloudTransferAdapter<com.dropbox.core.DbxEntry, java.io.File>
{
    private static final Logit Log = Logit.create(DropboxToLocalTransferAdapter.class.getName());

    public DropboxToLocalTransferAdapter(GlobalCacheKey sourceCacheKey,
                                         com.dropbox.core.DbxEntry source,
                                         GlobalCacheKey targetCacheKey,
                                         java.io.File target)
    {
        super(sourceCacheKey, source, targetCacheKey, target);
    }

    @Override
    public long getFilesize()
    {
        return m_source.asFile().numBytes;
    }

}
