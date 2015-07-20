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
import com.dropbox.core.DbxWriteMode;
import com.dropbox.core.util.Collector;
import com.leong.nimbus.clouds.interfaces.ICloudModel;
import com.leong.nimbus.clouds.interfaces.ICloudProgress;
import com.leong.nimbus.clouds.interfaces.ICloudTransfer;
import com.leong.nimbus.utils.GlobalCache;
import com.leong.nimbus.utils.GlobalCacheKey;
import com.leong.nimbus.utils.Logit;
import com.leong.nimbus.utils.NimbusDatastore;
import com.leong.nimbus.utils.Tools;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

    @Override
    public GlobalCacheKey getGlobalCacheKey()
    {
        return GlobalCache.getInstance().getKey(this);
    }

    @Override
    public String getAuthUrl()
    {
        Log.entering("getAuthUrl");
        String url = m_webAuth.start();
        Log.info(url);
        return url;
    }

    @Override
    public boolean login(String userid)
    {
        Log.entering("login", new Object[]{userid});

        String accessToken = null;

        if (!Tools.isNullOrEmpty(userid))
        {
            try
            {
                BufferedReader reader = NimbusDatastore.getReader("creds", "dropbox_"+userid);

                accessToken = reader.readLine();
            }
            catch (IOException ex)
            {
                Log.throwing("login", ex);
            }
        }

        if (Tools.isNullOrEmpty(accessToken))
        {
            Log.warning("Access token is emtpy");
            return false;
        }

        Log.fine("Using stored credentials");

        // getting the client
        m_client = new DbxClient(m_config, accessToken);

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

        getRoot();

        return true;
    }

    @Override
    public boolean login(String userid, String authCode)
    {
        Log.entering("login", new Object[]{userid, authCode});

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

        // save the access token for reuse
        try
        {
            BufferedWriter writer = NimbusDatastore.getWriter("creds", "dropbox_"+userid);

            writer.write(accessToken, 0, accessToken.length());
            writer.newLine();
            writer.flush();
            writer.close();
        }
        catch (IOException ex)
        {
            Log.throwing("login", ex);
            return false;
        }

        return login(userid);
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
    public String getIdByItem(DbxEntry item)
    {
        return item.path;
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

    @Override
    public boolean isFolder(DbxEntry item)
    {
        return item.isFolder();
    }

    @Override
    public String getName(DbxEntry item)
    {
        return item.name;
    }

    @Override
    public String getAbsolutePath(DbxEntry item)
    {
        return item.path;
    }

    @Override
    public void transfer(final ICloudTransfer<?, DbxEntry> transfer)
    {
        Log.entering("transfering", new Object[]{transfer});

        final InputStream is = transfer.getInputStream();
        OutputStream os = null;

        try
        {
            final int BUFFER_SIZE = 256*1024;
            final byte[] buffer = new byte[BUFFER_SIZE];

            final DbxEntry uploadFile = transfer.getTargetObject();
            // TODO: check if uploadFile is a file

            final DbxClient.Uploader uploader = m_client
                .startUploadFileChunked(
                    BUFFER_SIZE,
                    uploadFile.path,
                    DbxWriteMode.add(),
                    uploadFile.asFile().numBytes);

            // get the output stream
            os = uploader.getBody();

            long totalSent = 0;
            int readSize = 0;

            ICloudProgress progress = transfer.getProgressHandler();

            progress.initalize();
            progress.start(transfer.getFilesize());

            final long startTime = System.nanoTime();
            while (transfer.getCanTransfer() && ((readSize = is.read(buffer)) > 0))
            {
                totalSent += readSize;

                os.write(buffer, 0, readSize);

                progress.progress(totalSent);
            }

            if (transfer.getCanTransfer())
            {
                final long elapsedNano = System.nanoTime() - startTime;

                DbxEntry outputFile = uploader.finish();

                Log.fine(Tools.formatTransferMsg(elapsedNano, outputFile.asFile().numBytes));

                transfer.setTransferredObject(outputFile);

                progress.finish();
            }
            else
            {
                Log.warning("Transferred aborted");
            }
        }
        catch (IOException | DbxException ex)
        {
            Log.throwing("transfer", ex);
        }
        finally
        {
            try
            {
                Log.fine("Closing input stream");
                if (is != null) is.close();
            }
            catch (IOException ex)
            {
                Log.throwing("transfer", ex);
            }

            try
            {
                Log.fine("Closing output stream");
                if (is != null)
                {
                    os.flush();
                    os.close();
                }
            }
            catch (IOException ex)
            {
                Log.throwing("transfer", ex);
            }
        }
    }

    @Override
    public InputStream getDownloadStream(DbxEntry downloadFile)
    {
        Log.entering("getDownloadStream", new Object[]{downloadFile});

        try
        {
            DbxClient.Downloader downloader = m_client.startGetFile(downloadFile.path, downloadFile.asFile().rev);
            return downloader.body;
        }
        catch (DbxException ex)
        {
            Log.throwing("getDownloadStream", ex);
        }

        return null;
    }

}
