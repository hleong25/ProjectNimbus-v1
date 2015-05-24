/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leong.nimbus.gui.helpers;

import com.leong.nimbus.clouds.interfaces.ICloudTransfer;
import com.leong.nimbus.gui.components.FileItemPanel;

/**
 *
 * @author henry
 */
public class XferHolder<T, S>
{
    public ICloudTransfer<T, S> xfer;
    public FileItemPanel pnl;
}
