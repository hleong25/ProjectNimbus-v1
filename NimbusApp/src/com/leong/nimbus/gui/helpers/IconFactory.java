/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leong.nimbus.gui.helpers;

import com.leong.nimbus.clouds.CloudType;
import javax.swing.ImageIcon;

/**
 *
 * @author henry
 */
public class IconFactory
{
    public enum IconSize
    {
        SMALL,
        MEDIUM,
        LARGE,
    }

    public static ImageIcon get(CloudType type, IconSize size)
    {
        String path;

        switch (type)
        {
            case LOCAL_FILE_SYSTEM:
                path = "resources/icons/local/local-%s.png";
                break;
            case GOOGLE_DRIVE:
                path = "resources/icons/google/drive-%s.png";
                break;
            case DROPBOX:
                path = "resources/icons/dropbox/dropbox-%s.png";
                break;
            default:
                return null;
        }

        int size_px = 0;
        switch (size)
        {
            case SMALL:
                size_px = 16;
                break;
            case MEDIUM:
                size_px = 32;
                break;
            case LARGE:
                size_px = 64;
                break;
            default:
                return null;
        }

        String res_path = String.format(path, size_px);

        ImageIcon icon = new ImageIcon(IconFactory.class.getClassLoader().getResource(res_path));
        return icon;
    }

}
