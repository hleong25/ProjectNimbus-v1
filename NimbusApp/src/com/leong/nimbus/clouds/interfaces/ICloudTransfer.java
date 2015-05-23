/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leong.nimbus.clouds.interfaces;

import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author henry
 * @param <S> = Source object
 * @param <T> = Target object
 */
public interface ICloudTransfer<S, T>
{
    long getFilesize();

    S getSourceObject();
    T getTargetObject();

    void setTransferredObject(T obj);
    T getTransferredObject();

    InputStream getInputStream();
    OutputStream getOutputStream();

    void setProgressHandler(ICloudProgress progress);
    ICloudProgress getProgressHandler();
}