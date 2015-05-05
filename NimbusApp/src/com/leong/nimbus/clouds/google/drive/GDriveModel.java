/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leong.nimbus.clouds.google.drive;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.ChildList;
import com.google.api.services.drive.model.ChildReference;
import com.google.api.services.drive.model.File;
import com.leong.nimbus.clouds.interfaces.ICloudModel;
import com.leong.nimbus.utils.Tools;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author henry
 */
public class GDriveModel implements ICloudModel
{
    private static String CLIENT_ID = "377040850517-vc3hbqvqqct5svp9nrdagrhg2v06v0o2.apps.googleusercontent.com";
    private static String CLIENT_SECRET = "-ezNN3hvssAwm6Ewgmrg69pI";

    private static String REDIRECT_URI = "urn:ietf:wg:oauth:2.0:oob";

    private GoogleAuthorizationCodeFlow m_flow = null;
    private Drive m_service = null;

    public GDriveModel()
    {
        Tools.logit("GDriveModel.ctor()");

        HttpTransport httpTransport = new NetHttpTransport();
        JsonFactory jsonFactory = new JacksonFactory();

        Tools.logit("new GoogleAuthorizationCodeFlow()");
        m_flow = new GoogleAuthorizationCodeFlow
                .Builder(httpTransport, jsonFactory, CLIENT_ID, CLIENT_SECRET, Arrays.asList(DriveScopes.DRIVE))
                .setAccessType("online")
                .setApprovalPrompt("auto").build();
    }

    public String getAuthUrl()
    {
        Tools.logit("GDriveModel.getAuthUrl()");
        Tools.logit("getting auth url");
        String url = m_flow.newAuthorizationUrl().setRedirectUri(REDIRECT_URI).build();
        Tools.logit(url);
        return url;
    }

    public boolean login(String authCode)
    {
        Tools.isNullOrEmpty("GDrive.Model.login()");
        if (m_flow == null)
        {
            Tools.logit("Google flow not initialized");
            return false;
        }

        HttpTransport httpTransport = m_flow.getTransport();
        JsonFactory jsonFactory = m_flow.getJsonFactory();

        m_service = null; // make sure the previous object is released

        if (Tools.isNullOrEmpty(authCode))
        {
            Tools.logit("GDriveModel.login() Auth code not valid");
            return false;
        }

        GoogleTokenResponse response = null;
        try
        {
            Tools.logit("newTokenRequest()");
            response = m_flow.newTokenRequest(authCode).setRedirectUri(REDIRECT_URI).execute();
        }
        catch (IOException e)
        {
            e.printStackTrace();

            return false;
        }

        if ((response == null) || response.isEmpty())
        {
            Tools.logit("Response is null or empty");
            return false;
        }
        else
        {
            Tools.logit("Response is " + response.toString());
        }

        Tools.logit("new GoogleCredential()");
        GoogleCredential credential = new GoogleCredential.Builder()
                .setJsonFactory(jsonFactory)
                .setTransport(httpTransport)
                .setClientSecrets(CLIENT_ID, CLIENT_SECRET)
                .build()
                .setFromTokenResponse(response);

        //Create a new authorized API client
        Tools.logit("new Drive.Builder()");
        m_service = new Drive.Builder(httpTransport, jsonFactory, credential)
                .setApplicationName("Nimbus")
                .build();

        return true;
    }

    public List<File> getFiles(String path)
    {
        List<File> list = new LinkedList<File>();

        try
        {
            Drive.Children.List request = m_service.children().list(path);

            do {
                try {
                    ChildList children = request.execute();

                    for (ChildReference child : children.getItems()) {

                        File file = m_service.files().get(child.getId()).execute();

                        if (file.getLabels().getTrashed()) continue;

                        list.add(file);

                    }
                    request.setPageToken(children.getNextPageToken());
                } catch (IOException e) {
                    System.out.println("An error occurred: " + e);
                    request.setPageToken(null);
                }
            } while (request.getPageToken() != null &&
                    request.getPageToken().length() > 0);


        } catch (IOException e)
        {
            e.printStackTrace();
        }

        return list;
    }

}
