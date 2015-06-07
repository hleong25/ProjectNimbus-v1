/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leong.nimbus.clouds.interfaces.transferadapters;

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

/**
 *
 * @author henry
 */
public class LocalToLocalTransferAdapter
    extends CloudTransferAdapter<File, File>
{
    private static final Logit Log = Logit.create(LocalToLocalTransferAdapter.class.getName());

    public LocalToLocalTransferAdapter(File source, File target)
    {
        super(source, target);
    }

    @Override
    public long getFilesize()
    {
        return m_source.length();
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
        // caller must close inputstream;

        FileOutputStream fos = null;
        try
        {
            fos = new FileOutputStream(getTargetObject());
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            return bos;
        }
        catch (FileNotFoundException ex)
        {
            Log.throwing("getOutputStream", ex);

            try
            {
                if (fos != null)
                    fos.close();
            }
            catch (IOException ex1)
            {
                Log.throwing("getOutputStream", ex1);
            }

            return null;
        }
    }
}
