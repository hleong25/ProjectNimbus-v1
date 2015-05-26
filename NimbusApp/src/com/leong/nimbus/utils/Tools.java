/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leong.nimbus.utils;


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
}
