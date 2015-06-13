/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leong.nimbus.clouds.local;

import com.leong.nimbus.clouds.CloudType;
import com.leong.nimbus.clouds.interfaces.CloudControllerAdapter;
import com.leong.nimbus.clouds.interfaces.ICloudController;
import com.leong.nimbus.clouds.interfaces.ICloudTransfer;
import com.leong.nimbus.utils.GlobalCache;
import com.leong.nimbus.utils.Logit;
import com.leong.nimbus.utils.Tools;
import java.awt.Component;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        super(new LocalModel());

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
