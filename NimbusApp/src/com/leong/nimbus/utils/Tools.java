/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leong.nimbus.utils;

import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;


/**
 *
 * @author henry
 */
public class Tools
{
    private static final Logit Log = Logit.create(Tools.class.getName());

    public static boolean isNullOrEmpty(String str)
    {
        return (str == null) || str.isEmpty();
    }

    public static void wait(Object obj)
    {
        if (obj == null) return;

        try
        {
            synchronized(obj)
            {
                obj.wait();
            }
        }
        catch (InterruptedException ex)
        {
            Log.throwing("wait", ex);
        }
    }

    public static void wait(Object obj, long timeout)
    {
        if (obj == null) return;

        try
        {
            synchronized(obj)
            {
                obj.wait(timeout);
            }
        }
        catch (InterruptedException ex)
        {
            Log.throwing("wait", ex);
        }
    }

    public static void notify(Object obj)
    {
        if (obj == null) return;

        try
        {
            synchronized(obj)
            {
                obj.notify();
            }
        }
        catch (IllegalMonitorStateException ex)
        {
            Log.throwing("notify", ex);
        }
    }

    public static void notifyAll(Object obj)
    {
        if (obj == null) return;

        try
        {
            synchronized(obj)
            {
                obj.notifyAll();
            }
        }
        catch (IllegalMonitorStateException ex)
        {
            Log.throwing("notifyAll", ex);
        }
    }

    public static String formatTransferMsg(long elapsedNano, long totalBytes)
    {
        final DecimalFormat formatter = new DecimalFormat("#,###,###,###");

        final long elapsedSecs = TimeUnit.SECONDS.convert(elapsedNano, TimeUnit.NANOSECONDS);
        final double avgRate = (double)totalBytes / ((elapsedSecs > 0) ? elapsedSecs : 1);

        final String avgRateStr;// = String.format("%.0fkbps", avgRate/1000.0);
        if (avgRate > 10000000.0) // 10mbps
        {
            avgRateStr = String.format("%.03fmbps", avgRate/1000000.0);
        }
        else if (avgRate > 1000.0) // 1kbps
        {
            avgRateStr = String.format("%.0fkbps", avgRate/1000.0);
        }
        else // bps
        {
            avgRateStr = String.format("%.0fbps", avgRate);
        }

        final String msg = String.format("Transferred %s bytes in %s seconds (%s)",
                                         formatter.format(totalBytes),
                                         formatter.format(elapsedSecs),
                                         avgRateStr);
        return msg;
    }
}
