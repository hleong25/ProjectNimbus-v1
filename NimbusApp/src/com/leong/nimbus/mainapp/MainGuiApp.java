/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leong.nimbus.mainapp;

import com.leong.nimbus.clouds.google.drive.GDriveController;
import com.leong.nimbus.clouds.google.drive.GDriveFrame;
import com.leong.nimbus.utils.Tools;

/**
 *
 * @author henry
 */
public class MainGuiApp
{
    public static void main(String[] args)
    {
        //GDriveController gdrive = new GDriveController();
        //gdrive.login();

        Tools.logit("MainGuiApp");

        {
            GDriveFrame gdriveFrame = new GDriveFrame();
            Tools.logit("GDrive.run()");
            gdriveFrame.run();
            Tools.logit("GDrive.run()");
        }

        Tools.logit("exit");
    }

}
