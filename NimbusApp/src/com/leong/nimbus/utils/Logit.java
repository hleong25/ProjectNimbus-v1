/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leong.nimbus.utils;

import java.text.MessageFormat;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 *
 * @author henry
 */
public final class Logit extends Logger
{
    protected Logit(Logger logger)
    {
        super(logger.getName(), logger.getResourceBundleName());
    }

    public static void init()
    {
        // this should be called once in main()

        // remove the default handler
        Logger rootLogger = Logger.getLogger("");
        Handler[] handlers = rootLogger.getHandlers();

        if (handlers[0] instanceof ConsoleHandler)
        {
            rootLogger.removeHandler(handlers[0]);
        }
    }

    public static Logit create(String name)
    {
        return Logit.create(name, Level.ALL);
    }

    public static Logit create(String name, Level level)
    {
        Logit logger = new Logit(Logger.getLogger(name));

        ConsoleHandler console = new ConsoleHandler();
        //console.setFormatter(new SimpleFormatter());
        console.setFormatter(new Formatter()
        {
            @Override
            public String format(LogRecord record)
            {
                StringBuilder str = new StringBuilder();
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
        });

        console.setLevel(level);
        logger.setLevel(level);

        logger.addHandler(console);

        return logger;
    }

    public void entering(String sourceMethod)
    {
        super.entering(this.getName(), sourceMethod);
    }

    public void entering(String sourceMethod, String param1)
    {
        //super.entering(this.getName(), sourceMethod, param1);
        this.entering(sourceMethod, new Object[]{param1});
    }

    public void entering(String sourceMethod, Object param1)
    {
        //super.entering(this.getName(), sourceMethod, param1);
        this.entering(sourceMethod, new Object[]{param1});
    }

    public void entering(String sourceMethod, Object... params)
    {
        super.entering(this.getName(), sourceMethod, params);
    }


}
