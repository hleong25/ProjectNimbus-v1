/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leong.nimbus.clouds.interfaces.transferadapters;

import com.leong.nimbus.clouds.interfaces.ICloudProgress;
import com.leong.nimbus.clouds.interfaces.ICloudTransfer;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * @author henry
 * @param <S> = Source object
 * @param <T> = Target object
 */
public abstract class CloudTransferAdapter<S, T>
    implements ICloudTransfer<S, T>
{
    protected final S m_source;
    protected final T m_target;

    // the returned object after it was transfered
    protected T m_xferred;

    protected ICloudProgress m_progress;

    protected AtomicBoolean m_canTransfer;

    public CloudTransferAdapter(S source, T target)
    {
        m_source = source;
        m_target = target;
    }

    @Override
    public S getSourceObject()
    {
        return m_source;
    }

    @Override
    public T getTargetObject()
    {
        return m_target;
    }

    @Override
    public void setTransferredObject(T obj)
    {
        m_xferred = obj;
    }

    @Override
    public T getTransferredObject()
    {
        return m_xferred;
    }

    @Override
    public void setProgressHandler(ICloudProgress progress)
    {
        m_progress = progress;
    }

    @Override
    public ICloudProgress getProgressHandler()
    {
        return m_progress;
    }

    @Override
    public void setCanTransfer(AtomicBoolean canTransfer)
    {
        m_canTransfer = canTransfer;
    }

    @Override
    public boolean getCanTransfer()
    {
        return m_canTransfer.get();
    }
}
