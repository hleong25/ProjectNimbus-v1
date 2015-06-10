/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leong.nimbus.utils;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author henry
 */
public final class GlobalCache
    extends HashMap<String, Object>
{
    private static final Logit Log = Logit.create(GlobalCache.class.getName());

    public interface IProperties
    {
        String getPackageName();
    }

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
        throw new NoSuchMethodError("Use put(IProperties, String, Object)");
    }

    public Object put(final IProperties props, String key, Object value)
    {
        String pkgkey = ((props != null) ? props.getPackageName()+"/" : "")+key;

        if (!Tools.isNullOrEmpty(pkgkey) && (value != null))
        {
            if (containsKey(pkgkey) && (get(pkgkey) != value))
            {
                Log.info("Overriding cache: "+pkgkey);
            }

            return super.put(pkgkey, value);
        }
        Log.warning("Adding cache failed.");
        return null;
    }

    public String getKey(final Object needle)
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
