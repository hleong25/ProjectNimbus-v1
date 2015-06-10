/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leong.nimbus.utils;

import java.util.HashMap;
import java.util.Map;
import org.mortbay.log.Log;

/**
 *
 * @author henry
 */
public final class GlobalCache
    extends HashMap<String, Object>
{
    //private static final Logit Log = Logit.create(GlobalCache.class.getName());

    private static GlobalCache m_global = null;

    private GlobalCache()
    {
        // do nothing
    }

    public static GlobalCache getInstance()
    {
        if (m_global == null)
        {
            m_global = new GlobalCache();
        }

        return m_global;
    }

    @Override
    public Object put(String key, Object value)
    {
        if (!Tools.isNullOrEmpty(key) && (value != null))
        {
            if (containsKey(key) && (get(key) != value))
            {
                Log.info("Overriding cache: "+key);
            }

            return super.put(key, value);
        }
        Log.warn("Adding cache failed.");
        return null;
    }

    public String getKey(Object needle)
    {
        for (Map.Entry<String, Object> entry : entrySet())
        {
            if (entry.getValue() == needle)
            {
                return entry.getKey();
            }
        }
        return null;
    }
}
