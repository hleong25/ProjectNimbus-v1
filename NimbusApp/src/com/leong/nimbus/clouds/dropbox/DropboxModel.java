/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leong.nimbus.clouds.dropbox;

import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxAuthFinish;
import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxEntry;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWebAuthNoRedirect;
import com.dropbox.core.util.Collector;
import com.leong.nimbus.clouds.interfaces.ICloudModel;
import com.leong.nimbus.utils.Logit;
import com.leong.nimbus.utils.Tools;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 *
 * @author henry
 */
public class DropboxModel implements ICloudModel<DbxEntry>
{
    private static final Logit Log = Logit.create(DropboxModel.class.getName());

    private static final String APP_KEY = "954i1xyd8mu6o7m";
    private static final String APP_SECRET = "htc1ejxcr081hjg";

    private final DbxAppInfo m_appInfo = new DbxAppInfo(APP_KEY, APP_SECRET);
    private final DbxRequestConfig m_config = new DbxRequestConfig("Nimbus", Locale.getDefault().toString());
    private final DbxWebAuthNoRedirect m_webAuth = new DbxWebAuthNoRedirect(m_config, m_appInfo);

    private DbxClient m_client;

    private DbxEntry m_root;

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
            Log.throwing("login", ex);
            return false;
        }

        // geting the client
        m_client = new DbxClient(m_config, accessToken);

        getRoot();

        try
        {
            Log.info("Linked account: "+m_client.getAccountInfo().displayName);
        }
        catch (DbxException ex)
        {
            Log.throwing("login", ex);
            m_client = null;
            return false;
        }

        return true;
    }

    @Override
    public DbxEntry getRoot()
    {
        Log.entering("getRoot");

        if (m_root != null)
        {
            return m_root;
        }

        m_root = getItemById(DropboxConstants.FOLDER_ROOT);
        return m_root;
    }

    @Override
    public DbxEntry getItemById(String id)
    {
        Log.entering("getItemById", id);

        try
        {
            DbxEntry entry = m_client.getMetadata(id);
            //Log.fine(entry.toStringMultiline());
            return entry;
        }
        catch (DbxException ex)
        {
            Log.throwing("getItemById", ex);
        }

        return null;
    }

    @Override
    public List<DbxEntry> getChildrenItems(DbxEntry parent)
    {
        Log.entering("getChildrenItems", parent);

        if (!parent.isFolder())
        {
            Log.warning("Entry '"+parent.path+"' is not a folder");
            return null;
        }

        try
        {
            DbxEntry.WithChildrenC<ArrayList<DbxEntry>> items = m_client.getMetadataWithChildrenC(parent.path, new Collector.ArrayListCollector<DbxEntry>());

            final List<DbxEntry> list = items.children;

            if (false)
            {
                for (DbxEntry entry : list)
                {
                    Log.finer(entry.toStringMultiline());
                }
            }

            return list;
        }
        catch (DbxException ex)
        {
            Log.throwing("getChildrenItems", ex);
        }

        return null;
    }

    //public DbxEntry.File uploadLocalFile(InputStream stream)
    //{
    //    DbxEntry.File aaa;
    //    m_client.uploadFileChunked(chunkSize, APP_KEY, null, numBytes, null);

    //    final int BUFFSIZE = 256*1024;
    //    byte outbuff[BUFFSIZE] = new byte[];
    //    BufferedInputStream bis = new BufferedInputStream(stream, BUFFSIZE);

    //    bis.read(outbuff);
    //
    //    DbxStreamWriter<RuntimeException> bbb = new DbxStreamWriter<RuntimeException>()
    //    {

    //        @Override
    //        public void write(NoThrowOutputStream out) throws RuntimeException
    //        {
    //        }
    //    }

    //    return null;
    //}
}
