/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leong.nimbus.utils;

import com.leong.nimbus.utils.Logger.Level;

/**
 *
 * @author henry
 */
public class Tools
{
    public static void logit(String msg)
    {
        Logger.msg(Level.INFO, msg);
    }

    public static void logit(StringBuffer msg)
    {
        Tools.logit(msg.toString());
    }

    public static boolean isNullOrEmpty(String str)
    {
        return (str == null) || str.isEmpty();
    }

}
