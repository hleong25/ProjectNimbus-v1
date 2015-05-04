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
public final class Logger
{
    public enum Level
    {
        WARNING,
        INFO,
        STD,
        DEBUG,
        VERBOSE;
    }

    private Logger()
    {
        // empty
    }

    public static void msg(Level level, String msg)
    {
        String newmsg = String.format("[%s] %s", level.toString(), msg);
        System.out.println(newmsg);
    }

    public static void msg(Level level, StringBuffer msg)
    {
        Logger.msg(level, msg.toString());
    }
}
