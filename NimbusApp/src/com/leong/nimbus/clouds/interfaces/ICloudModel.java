/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leong.nimbus.clouds.interfaces;

import com.leong.nimbus.utils.GlobalCacheKey;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 *
 * @author henry
 */
public interface ICloudModel<T>
{
    GlobalCacheKey getGlobalCacheKey();

    boolean login(String userid);
    boolean login(String userid, String authCode);
    String getAuthUrl();

    T getRoot();
    T getItemById(String id);
    String getIdByItem(T item);
    List<T> getChildrenItems(T parent);

    boolean isFolder(T item);
    String getName(T item);
    String getAbsolutePath(T item);

    // target must be of type T
    void transfer(final ICloudTransfer</*source*/?, /*target*/? super T> transfer);
    InputStream getDownloadStream(T downloadFile);
    OutputStream getUploadStream(T uploadFile);
}
