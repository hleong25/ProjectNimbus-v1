/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leong.nimbus.clouds.dropbox;

import com.dropbox.core.DbxEntry;
import com.leong.nimbus.clouds.CloudType;
import com.leong.nimbus.clouds.interfaces.CloudControllerAdapter;
import com.leong.nimbus.utils.Logit;
import com.leong.nimbus.utils.Tools;

/**
 *
 * @author henry
 */
public class DropboxController
    extends CloudControllerAdapter<DbxEntry>
{
    private static final Logit Log = Logit.create(DropboxController.class.getName());

    public DropboxController()
    {
        super(DropboxController.class.getName(), new DropboxModel());

        Log.entering("<init>");

        m_rootFolder = DropboxConstants.FOLDER_ROOT;
    }

    @Override
    public CloudType getCloudType()
    {
        return CloudType.DROPBOX;
    }

    @Override
    public DbxEntry getParent(DbxEntry item)
    {
        Log.entering("getParent", (item != null ? item.path : "null"));

        if (item == null)
        {
            Log.warning("item is null");
            return null;
        }

        String path = item.path;

        if (path.equals(DropboxConstants.FOLDER_ROOT))
        {
            return null;
        }

        int idxOffset = path.lastIndexOf('/');
        String parentPath = path.substring(0, idxOffset);

        if (Tools.isNullOrEmpty(parentPath))
        {
            parentPath = DropboxConstants.FOLDER_ROOT;
        }

        Log.fine("Parent: "+parentPath);
        return getItemById(parentPath, true);
    }

}
