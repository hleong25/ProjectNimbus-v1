/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leong.nimbus.clouds.google.drive;

import com.leong.nimbus.clouds.interfaces.ICloudController;
import com.leong.nimbus.utils.Tools;
import edu.stanford.ejalbert.BrowserLauncher;
import edu.stanford.ejalbert.exception.BrowserLaunchingInitializingException;
import edu.stanford.ejalbert.exception.UnsupportedOperatingSystemException;
import javax.swing.JOptionPane;

/**
 *
 * @author henry
 */
public class GDriveController implements ICloudController
{
    private GDriveModel m_model = null;

    public GDriveController()
    {
        Tools.logit("GDriveController.ctor()");

        m_model = new GDriveModel();
    }

    public boolean login()
    {
        Tools.logit("GDriveController.login()");

        String authUrl = m_model.getAuthUrl();

        try
        {
            // TODO: does not work with OSX -- i think...
            BrowserLauncher launcher = new BrowserLauncher();
            launcher.setNewWindowPolicy(true);

            Tools.logit("Opening new browser to "+authUrl);
            launcher.openURLinBrowser(authUrl);
        }
        catch (BrowserLaunchingInitializingException e)
        {
            e.printStackTrace();
        }
        catch (UnsupportedOperatingSystemException e)
        {
            e.printStackTrace();
        }

        String authCode = JOptionPane.showInputDialog("Input the authentication code here");

        if (Tools.isNullOrEmpty(authCode))
        {
            return false;
        }

        authCode = authCode.trim();
        Tools.logit("Auth code is "+authCode);

        return m_model.login(authCode);
    }
}
