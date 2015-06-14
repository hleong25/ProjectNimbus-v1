/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leong.nimbus.clouds.interfaces.transferadapters;

import com.leong.nimbus.utils.Logit;

/**
 *
 * @author henry
 */
public class DropboxToGDriveTransferAdapter
    extends CloudTransferAdapter<com.dropbox.core.DbxEntry, com.google.api.services.drive.model.File>
{
    private static final Logit Log = Logit.create(DropboxToGDriveTransferAdapter.class.getName());

    public DropboxToGDriveTransferAdapter(String sourceCacheKey,
                                          com.dropbox.core.DbxEntry source,
                                          String targetCacheKey,
                                          com.google.api.services.drive.model.File target)
    {
        super(sourceCacheKey, source, targetCacheKey, target);
    }

    @Override
    public long getFilesize()
    {
        return m_source.asFile().numBytes;
    }

}
