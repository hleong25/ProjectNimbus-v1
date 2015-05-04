/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leong.nimbus.mainapp;

import com.leong.nimbus.clouds.google.drive.GDriveController;

/**
 *
 * @author henry
 */
public class MainGuiApp
{
    public static void main(String[] args)
    {
        GDriveController gdrive = new GDriveController();
        gdrive.login();
    }
}
