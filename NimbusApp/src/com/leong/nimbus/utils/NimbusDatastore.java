/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leong.nimbus.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author henry
 */
public final class NimbusDatastore
{
    private static final Logit Log = Logit.create(NimbusDatastore.class.getName());

    private static final String ROOT_PATH = System.getProperty("user.home") + "/.nimbus";

    private NimbusDatastore()
    {
        // empty
    }

    public static File getFile(String pkg, String key)
    {
        return new File(ROOT_PATH+"/"+pkg, key);
    }

    public static BufferedWriter getWriterNoThrow(String pkg, String key)
    {
        try
        {
            return getWriter(pkg, key);
        }
        catch (IOException ex)
        {
            Log.throwing("getWriterNoThrow", ex);
            return null;
        }
    }

    public static BufferedWriter getWriter(String pkg, String key) throws IOException
    {
        File file = getFile(pkg, key);

        if (file.exists())
        {
            if (!file.canWrite())
            {
                String err = "File not writeable: "+file.getAbsolutePath();
                Log.severe(err);
                throw new IOException(err);
            }

            Log.info("File overwrite: "+file.getAbsolutePath());
        }
        else
        {
            String abspath = file.getParent();
            if (!FileUtils.mkdir(abspath))
            {
                String err = "Failed to create path: "+abspath;
                Log.severe(err);
                throw new IOException(err);
            }

            if (file.createNewFile())
            {
                Log.fine("Creating file: "+file.getAbsolutePath());
            }
            else
            {
                String err = "Fail to create file: "+file.getAbsolutePath();
                Log.severe(err);
                throw new IOException(err);
            }
        }

        BufferedWriter writer = new BufferedWriter(new FileWriter(file));

        return writer;
    }

    public static BufferedReader getReaderNoThrow(String pkg, String key)
    {
        try
        {
            return getReader(pkg, key);
        }
        catch (IOException ex)
        {
            Log.throwing("readNoThrow", ex);
            return null;
        }
    }

    public static BufferedReader getReader(String pkg, String key) throws IOException
    {
        File file = getFile(pkg, key);

        if (!file.exists())
        {
            String err = "File not found: "+file.getAbsolutePath();
            Log.severe(err);
            throw new IOException(err);
        }

        BufferedReader reader = new BufferedReader(new FileReader(file));

        return reader;
    }
}
