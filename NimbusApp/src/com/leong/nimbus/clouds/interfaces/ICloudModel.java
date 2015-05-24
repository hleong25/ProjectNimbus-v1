/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leong.nimbus.clouds.interfaces;

import java.util.List;

/**
 *
 * @author henry
 */
public interface ICloudModel<T>
{
    T getRoot();
    T getItemById(String id);
    List<T> getChildrenItems(T parent);

    void transfer(ICloudTransfer<?,?> transfer);
}
