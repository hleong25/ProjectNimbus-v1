/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leong.nimbus.utils;

import java.text.MessageFormat;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;

/**
 *
 * @author henry
 */
public class LogitFormatter extends SimpleFormatter
{
    public LogitFormatter()
    {
    }

    @Override
    public synchronized String format(LogRecord record)
    {
        //return super.format(record);

        StringBuilder str = new StringBuilder();
        str.append("["+record.getLevel()+"] ");
        str.append(record.getSourceClassName());
        str.append(".");
        str.append(record.getSourceMethodName());
        str.append("() ");
        if (record.getParameters() == null)
        {
            str.append(record.getMessage());
        }
        else
        {
            str.append(MessageFormat.format(record.getMessage(), record.getParameters()));
        }
        str.append("\n");

        return str.toString();
    }
}
