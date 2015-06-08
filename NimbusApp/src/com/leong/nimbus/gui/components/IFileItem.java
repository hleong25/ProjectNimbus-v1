/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leong.nimbus.gui.components;

import com.leong.nimbus.clouds.CloudType;
import com.leong.nimbus.clouds.interfaces.ICloudController;
import javax.swing.ImageIcon;

/**
 *
 * @author henry
 */
public interface IFileItem<T>
{
    ICloudController<T> getCloudController();

    T getCloudObject();

    ImageIcon getIcon();
    String getLabel();
}
