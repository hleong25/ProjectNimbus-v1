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
public final class FileUtils
{
    private static final Logit Log = Logit.create(FileUtils.class.getName());

    public static BufferedWriter getWriter(String filePath)
    {
        File outFile = new File(System.getProperty("user.home"), filePath);

        try
        {
            if (!outFile.exists())
            {
                String abspath = outFile.getParent();
                if (!FileUtils.mkdir(abspath))
                {
                    Log.warning("Failed to create path: "+abspath);
                    return null;
                }

                if (outFile.createNewFile())
                {
                    Log.fine("Creating file: "+outFile.getAbsolutePath());
                }
                else
                {
                    Log.warning("Fail to create file: "+outFile.getAbsolutePath());
                    return null;
                }
            }

            BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));

            return writer;
        }
        catch (IOException ex)
        {
            Log.throwing("getWriter", ex);
        }

        return null;
    }

    public static BufferedReader getReader(String filePath)
    {
        File inFile = new File(System.getProperty("user.home"), filePath);

        if (!inFile.exists())
        {
            Log.warning("File not found: "+inFile.getAbsolutePath());
            return null;
        }

        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(inFile));

            return reader;
        }
        catch (IOException ex)
        {
            Log.throwing("getReader", ex);
        }

        return null;
    }

    public static boolean mkdir(String abspath)
    {
        File path = new File(abspath);
        if (path.exists())
        {
            return true;
        }
        return path.mkdirs();
    }
}
