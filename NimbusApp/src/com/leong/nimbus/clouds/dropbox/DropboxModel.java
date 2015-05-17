/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leong.nimbus.clouds.dropbox;

import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxAuthFinish;
import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWebAuthNoRedirect;
import com.leong.nimbus.utils.Logit;
import com.leong.nimbus.utils.Tools;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author henry
 */
public class DropboxModel
{
    private static final Logit Log = Logit.create(DropboxModel.class.getName());

    private static final String APP_KEY = "954i1xyd8mu6o7m";
    private static final String APP_SECRET = "htc1ejxcr081hjg";

    private final DbxAppInfo m_appInfo = new DbxAppInfo(APP_KEY, APP_SECRET);
    private final DbxRequestConfig m_config = new DbxRequestConfig("Nimbus", Locale.getDefault().toString());
    private final DbxWebAuthNoRedirect m_webAuth = new DbxWebAuthNoRedirect(m_config, m_appInfo);

    private DbxClient m_client;

    public DropboxModel()
    {
        Log.entering("<init>");
    }

    public String getAuthUrl()
    {
        Log.entering("getAuthUrl");
        String url = m_webAuth.start();
        Log.info(url);
        return url;
    }

    public boolean login(String authCode)
    {
        Log.entering("login", new Object[]{authCode});

        m_client = null; // make sure the previous object is released

        if (Tools.isNullOrEmpty(authCode))
        {
            Log.severe("Auth code not valid");
            return false;
        }

        String accessToken;
        try
        {
            // This will fail if the user enters an invalid authorization code.
            Log.fine("Getting access token");
            DbxAuthFinish authFinish = m_webAuth.finish(authCode);
            accessToken = authFinish.accessToken;

            Log.info("Access token: "+ accessToken);
        }
        catch (DbxException ex)
        {
            Log.exception(ex);
            return false;
        }

        // geting the client
        m_client = new DbxClient(m_config, accessToken);

        try
        {
            Log.info("Linked account: "+m_client.getAccountInfo().displayName);
        }
        catch (DbxException ex)
        {
            Log.exception(ex);
            m_client = null;
            return false;
        }

        return true;
    }

}
