/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leong.nimbus.clouds.google.drive;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.googleapis.media.MediaHttpUploader;
import com.google.api.client.googleapis.media.MediaHttpUploaderProgressListener;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.About;
import com.google.api.services.drive.model.ChildList;
import com.google.api.services.drive.model.ChildReference;
import com.google.api.services.drive.model.File;
import com.leong.nimbus.clouds.interfaces.ICloudModel;
import com.leong.nimbus.clouds.interfaces.ICloudProgress;
import com.leong.nimbus.clouds.interfaces.ICloudTransfer;
import com.leong.nimbus.utils.Logit;
import com.leong.nimbus.utils.Tools;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author henry
 */
public class GDriveModel implements ICloudModel<com.google.api.services.drive.model.File>
{
    private static final Logit Log = Logit.create(GDriveModel.class.getName());

    private static final String CLIENT_ID = "377040850517-vc3hbqvqqct5svp9nrdagrhg2v06v0o2.apps.googleusercontent.com";
    private static final String CLIENT_SECRET = "-ezNN3hvssAwm6Ewgmrg69pI";

    private static final String REDIRECT_URI = "urn:ietf:wg:oauth:2.0:oob";

    private final GoogleAuthorizationCodeFlow m_flow;
    private Drive m_service;

    private File m_root;

    public GDriveModel()
    {
        Log.entering("<init>");
        //Tools.logit("GDriveModel.ctor()");

        HttpTransport httpTransport = new NetHttpTransport();
        JsonFactory jsonFactory = new JacksonFactory();

        //Tools.logit("new GoogleAuthorizationCodeFlow()");
        m_flow = new GoogleAuthorizationCodeFlow
                .Builder(httpTransport, jsonFactory, CLIENT_ID, CLIENT_SECRET, Arrays.asList(DriveScopes.DRIVE))
                .setAccessType("online")
                .setApprovalPrompt("auto").build();
    }

    public String getAuthUrl()
    {
        Log.entering("getAuthUrl");
        String url = m_flow.newAuthorizationUrl().setRedirectUri(REDIRECT_URI).build();
        Log.info(url);
        return url;
    }

    public boolean login(String authCode)
    {
        Log.entering("login", new Object[]{authCode});

        if (m_flow == null)
        {
            Log.severe("Google flow not initialized");
            return false;
        }

        HttpTransport httpTransport = m_flow.getTransport();
        JsonFactory jsonFactory = m_flow.getJsonFactory();

        m_service = null; // make sure the previous object is released

        if (Tools.isNullOrEmpty(authCode))
        {
            Log.severe("Auth code not valid");
            return false;
        }

        GoogleTokenResponse response = null;
        try
        {
            Log.fine("new token request");
            response = m_flow.newTokenRequest(authCode).setRedirectUri(REDIRECT_URI).execute();
        }
        catch (IOException ex)
        {
            Log.throwing("login", ex);
            return false;
        }

        if ((response == null) || response.isEmpty())
        {
            Log.severe("Response is null or empty");
            return false;
        }
        else
        {
            Log.fine("Response is " + response.toString());
        }

        Log.fine("new GoogleCredential()");
        GoogleCredential credential = new GoogleCredential.Builder()
                .setJsonFactory(jsonFactory)
                .setTransport(httpTransport)
                .setClientSecrets(CLIENT_ID, CLIENT_SECRET)
                .build()
                .setFromTokenResponse(response);

        //Create a new authorized API client
        Log.fine("new Drive.Builder()");
        m_service = new Drive.Builder(httpTransport, jsonFactory, credential)
                .setApplicationName("Nimbus")
                .build();

        return true;
    }

    @Override
    public File getRoot()
    {
        Log.entering("getRoot");

        if (m_root != null)
        {
            return m_root;
        }

        try
        {
            About about = m_service.about().get().execute();

            String rootID = about.getRootFolderId();

            Log.info("Root ID: "+rootID);

            m_root = getItemById(rootID);
        }
        catch (IOException ex)
        {
            Log.throwing("getRoot", ex);
        }

        return m_root;
    }

    @Override
    public File getItemById(String id)
    {
        Log.entering("getItemById", id);

        try
        {
            return m_service.files().get(id).execute();
        }
        catch (IOException ex)
        {
            Log.throwing("getItemById", ex);
        }
        return null;
    }

    @Override
    public List<File> getChildrenItems(File parent)
    {
        Log.entering("getChildrenItems", new Object[]{(parent != null ? parent.getId() : "(parent.null)")});

        final List<File> list = new ArrayList<>();

        try
        {
            Drive.Children.List request = m_service.children().list(parent.getId());

            do {
                try {
                    ChildList children = request.execute();

                    for (ChildReference child : children.getItems()) {

                        File file = m_service.files().get(child.getId()).execute();

                        if (file.getLabels().getTrashed()) continue;

                        list.add(file);

                    }
                    request.setPageToken(children.getNextPageToken());
                } catch (IOException ex) {
                    Log.throwing("getChildrenItems", ex);
                    request.setPageToken(null);
                }
            } while (request.getPageToken() != null &&
                    request.getPageToken().length() > 0);

        } catch (IOException ex)
        {
            Log.throwing("getChildrenItems", ex);
        }

        return list;
    }

    public void transfer(ICloudTransfer transfer)
    {
        // https://code.google.com/p/google-api-java-client/wiki/MediaUpload
        // http://stackoverflow.com/questions/25288849/resumable-uploads-google-drive-sdk-for-android-or-java

        Log.entering("transfer");

        try
        {
            final InputStream stream  = transfer.getInputStream();
            final File metadata = (File)transfer.getTargetObject();
            final ICloudProgress progressHandler = transfer.getProgressHandler();

            InputStreamContent mediaContent = new InputStreamContent(metadata.getMimeType(), stream);
            mediaContent.setLength(metadata.getFileSize());

            MediaHttpUploaderProgressListener progressListener = new MediaHttpUploaderProgressListener()
            {
                @Override
                public void progressChanged(MediaHttpUploader mhu) throws IOException
                {
                    switch (mhu.getUploadState()) {
                        case INITIATION_STARTED:
                            Log.fine("Initiation has started!");
                            progressHandler.initalize();
                            progressHandler.start(metadata.getFileSize());
                            break;
                        case INITIATION_COMPLETE:
                            Log.fine("Initiation is complete!");
                            break;
                        case MEDIA_IN_PROGRESS:
                            //Log.finer("BytesSent: "+mhu.getNumBytesUploaded()+" Progress: "+mhu.getProgress());
                            progressHandler.progress(mhu.getNumBytesUploaded());
                            break;
                        case MEDIA_COMPLETE:
                            Log.fine("Upload is complete!");
                            progressHandler.finish();
                            break;
                    }
                }
            };

            Drive.Files.Insert request = m_service.files().insert(metadata, mediaContent);
            request.getMediaHttpUploader()
                .setChunkSize(2*MediaHttpUploader.MINIMUM_CHUNK_SIZE)
                .setProgressListener(progressListener);

            Log.fine("Start uploading file");
            File xferredFile = request.execute();

            Log.fine("Uploaded file done");

            transfer.setTransferredObject(xferredFile);
        }
        catch (IOException ex)
        {
            Log.throwing("transfer", ex);
        }

        Tools.notifyAll(transfer);
    }
}
