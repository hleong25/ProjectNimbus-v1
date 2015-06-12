/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leong.nimbus.clouds.interfaces.transferadapters;

import com.leong.nimbus.utils.Logit;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 *
 * @author henry
 */
public class LocalToLocalTransferAdapter
    extends CloudTransferAdapter<File, File>
{
    private static final Logit Log = Logit.create(LocalToLocalTransferAdapter.class.getName());

    public LocalToLocalTransferAdapter(String sourceCacheKey,
                                       File source,
                                       String targetCacheKey,
                                       File target)
    {
        super(sourceCacheKey, source, targetCacheKey, target);
    }

    @Override
    public long getFilesize()
    {
        return m_source.length();
    }

}
