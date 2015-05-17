/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leong.nimbus.clouds.dropbox;

import com.leong.nimbus.utils.Logit;
import com.leong.nimbus.utils.Tools;
import edu.stanford.ejalbert.BrowserLauncher;
import edu.stanford.ejalbert.exception.BrowserLaunchingInitializingException;
import edu.stanford.ejalbert.exception.UnsupportedOperatingSystemException;
import java.awt.Component;
import java.util.Comparator;
import javax.swing.JOptionPane;

/**
 *
 * @author henry
 */
public class DropboxController
{
    private static final Logit Log = Logit.create(DropboxController.class.getName());

    private final DropboxModel m_model = new DropboxModel();

    //private final Comparator<File> m_comparatorFiles;
    //private final Map<File, List<File>> m_cachedListFiles = new HashMap<>();
    //private final Map<String, File> m_cachedFiles = new HashMap<>();

    public DropboxController()
    {
        Log.entering("<init>");

        //m_comparatorFiles = new Comparator<File>()
        //{
        //    @Override
        //    public int compare(File f1, File f2)
        //    {
        //        boolean f1_isdir = f1.getMimeType().equals(GDriveConstants.MIME_TYPE_FOLDER);
        //        boolean f2_isdir = f2.getMimeType().equals(GDriveConstants.MIME_TYPE_FOLDER);

        //        if (f1_isdir ^ f2_isdir)
        //        {
        //            return f1_isdir ? -1 : 1;
        //        }

        //        return f1.getTitle().compareTo(f2.getTitle());
        //    }
        //};
    }

    public boolean login(Component parentComponent)
    {
        Log.entering("login", parentComponent);

        String authUrl = m_model.getAuthUrl();

        try
        {
            // TODO: does not work with OSX -- i think...
            BrowserLauncher launcher = new BrowserLauncher();
            launcher.setNewWindowPolicy(true);

            Log.fine("Opening new browser to "+authUrl);
            launcher.openURLinBrowser(authUrl);
        }
        catch (BrowserLaunchingInitializingException | UnsupportedOperatingSystemException ex)
        {
            Log.exception(ex);
        }

        String authCode = JOptionPane.showInputDialog(parentComponent, "Input the authentication code here");

        if (Tools.isNullOrEmpty(authCode))
        {
            Log.severe("Auth code not valid.");
            return false;
        }

        authCode = authCode.trim();
        Log.info("Auth code: "+authCode);

        return m_model.login(authCode);
    }

}
