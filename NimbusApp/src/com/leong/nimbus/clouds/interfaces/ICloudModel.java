/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leong.nimbus.clouds.interfaces;

import java.io.InputStream;
import java.util.List;

/**
 *
 * @author henry
 */
public interface ICloudModel<T>
    extends java.io.Serializable
{
    boolean login(String userid);
    boolean login(String userid, String authCode);
    String getAuthUrl();

    T getRoot();
    T getItemById(String id);
    List<T> getChildrenItems(T parent);

    // target must be of type T
    void transfer(ICloudTransfer</*source*/?, /*target*/? super T> transfer);
    InputStream getDownloadStream(T downloadFile);
}
