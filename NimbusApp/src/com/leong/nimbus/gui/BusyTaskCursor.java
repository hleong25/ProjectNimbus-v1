/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leong.nimbus.gui;

import java.awt.Component;
import java.awt.Cursor;

/**
 *
 * @author henry
 */
public final class BusyTaskCursor
{
    protected final static Cursor CURSOR_BUSY = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);
    protected final static Cursor CURSOR_DEFAULT = Cursor.getDefaultCursor();

    public interface IBusyTask
    {
        void run();
    }

    private BusyTaskCursor()
    {
        // empty
    }

    public static void doTask(final Component component, IBusyTask taskIface)
    {
        try
        {
            //Tools.logit("BusyTaskCursor.doTask() cursor busy");
            component.setCursor(CURSOR_BUSY);

            //Tools.logit("BusyTaskCursor.doTask() taskIface.run()");
            taskIface.run();
        }
        finally
        {
            //Tools.logit("BusyTaskCursor.doTask() cursor default");
            component.setCursor(CURSOR_DEFAULT);
        }
    }

}
