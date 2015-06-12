/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leong.nimbus.clouds.interfaces.transferadapters;

import com.google.api.services.drive.model.File;
import com.leong.nimbus.clouds.interfaces.ICloudController;
import com.leong.nimbus.utils.GlobalCache;
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
public class GDriveToLocalTransferAdapter
    extends CloudTransferAdapter<com.google.api.services.drive.model.File, java.io.File>
{
    private static final Logit Log = Logit.create(GDriveToLocalTransferAdapter.class.getName());

    public GDriveToLocalTransferAdapter(String sourceCacheKey,
                                        File source,
                                        String targetCacheKey,
                                        java.io.File target)
    {
        super(sourceCacheKey, source, targetCacheKey, target);
    }

    @Override
    public long getFilesize()
    {
        return m_source.getFileSize();
    }

}
