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
public class GDriveToGDriveTransferAdapter
    extends CloudTransferAdapter<com.google.api.services.drive.model.File, com.google.api.services.drive.model.File>
{
    private static final Logit Log = Logit.create(GDriveToGDriveTransferAdapter.class.getName());

    public GDriveToGDriveTransferAdapter(String sourceCacheKey,
                                         com.google.api.services.drive.model.File source,
                                         String targetCacheKey,
                                         com.google.api.services.drive.model.File target)
    {
        super(sourceCacheKey, source, targetCacheKey, target);
    }

    @Override
    public long getFilesize()
    {
        return m_source.getFileSize();
    }

}
