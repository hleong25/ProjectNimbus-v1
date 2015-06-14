/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leong.nimbus.clouds.interfaces.transferadapters;

import com.leong.nimbus.clouds.interfaces.ICloudController;
import com.leong.nimbus.clouds.interfaces.ICloudProgress;
import com.leong.nimbus.clouds.interfaces.ICloudTransfer;
import com.leong.nimbus.utils.GlobalCache;
import com.leong.nimbus.utils.Logit;
import java.io.InputStream;
import java.io.OutputStream;
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
    private static final Logit Log = Logit.create(CloudTransferAdapter.class.getName());

    protected final String m_sourceCacheKey;
    protected final S m_source;

    protected final String m_targetCacheKey;
    protected final T m_target;

    // the returned object after it was transfered
    protected T m_xferred;

    protected ICloudProgress m_progress;

    protected AtomicBoolean m_canTransfer;

    public CloudTransferAdapter(String sourceCacheKey,
                                S source,
                                String targetCacheKey,
                                T target)
    {
        Log.entering("<init>", new Object[]{sourceCacheKey, source, targetCacheKey, target});

        m_sourceCacheKey = sourceCacheKey;
        m_source = source;

        m_targetCacheKey = targetCacheKey;
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
    public InputStream getInputStream()
    {
        Log.entering("getInputStream");
        // caller must close inputstream;
        ICloudController controller = (ICloudController)GlobalCache.getInstance().get(m_sourceCacheKey);
        Log.fine("SourceCacheKey:"+m_sourceCacheKey+" Controller:"+controller);
        return controller.getDownloadStream(getSourceObject());
    }

    @Override
    public OutputStream getOutputStream()
    {
        Log.entering("getOutputStream");
        // caller must close outputstream;
        ICloudController controller = (ICloudController)GlobalCache.getInstance().get(m_targetCacheKey);
        Log.fine("TargetCacheKey:"+m_targetCacheKey+" Controller:"+controller);
        return controller.getUploadStream(getTargetObject());
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
