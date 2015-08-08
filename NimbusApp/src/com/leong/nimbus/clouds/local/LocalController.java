/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leong.nimbus.clouds.local;

import com.leong.nimbus.clouds.CloudType;
import com.leong.nimbus.clouds.interfaces.CloudControllerAdapter;
import com.leong.nimbus.utils.Logit;
import java.io.File;

/**
 *
 * @author henry
 */
public class LocalController
    extends CloudControllerAdapter<java.io.File>
{
    private static final Logit Log = Logit.create(LocalController.class.getName());

    public LocalController()
    {
        super(LocalController.class.getName(), new LocalModel());

        this.
        Log.entering("<init>");

        //m_rootFolder = m_model.getIdFromItem(getRoot());
    }

    @Override
    public CloudType getCloudType()
    {
        return CloudType.LOCAL_FILE_SYSTEM;
    }

    @Override
    public File getParent(File item)
    {
        return item.getParentFile();
    }
}
