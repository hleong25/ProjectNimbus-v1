/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leong.nimbus.clouds.local;

import com.leong.nimbus.clouds.interfaces.ICloudModel;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.filechooser.FileSystemView;

/**
 *
 * @author henry
 */
public class LocalModel implements ICloudModel
{
    public LocalModel()
    {
    }

    public File getHomeFile()
    {
        File homeFile = FileSystemView.getFileSystemView().getHomeDirectory();
        return homeFile;
    }

    public File getFile(String path)
    {
        File file = new File(path);
        return file;
    }

    public List<File> getFiles(String path)
    {
        List<File> list = new ArrayList<>();

        File currPath = getFile(path);

        if (!currPath.isDirectory())
        {
            // nothing to do
            return list;
        }

        list.addAll(Arrays.asList(currPath.listFiles()));

        return list;
    }
}
