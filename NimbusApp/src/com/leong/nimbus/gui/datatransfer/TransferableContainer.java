/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leong.nimbus.gui.datatransfer;

import com.leong.nimbus.clouds.interfaces.ICloudController;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author henry
 */
public class TransferableContainer<T>
    implements Serializable
{
    protected ICloudController m_controller;
    protected List<T> m_list;

    public TransferableContainer(ICloudController controller, List<T> list)
    {
        m_controller = controller;
        m_list = list;
    }

    public ICloudController getController()
    {
        return m_controller;
    }

    public List<T> getList()
    {
        return m_list;
    }

    public String toString()
    {
        return "[TransferableContainer] type:"+m_controller.getCloudType()+" count:"+m_list.size();
    }

}
