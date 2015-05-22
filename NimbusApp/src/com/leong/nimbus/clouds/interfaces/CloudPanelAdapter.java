/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leong.nimbus.clouds.interfaces;

import com.leong.nimbus.gui.components.FileItemPanel;
import com.leong.nimbus.gui.helpers.BusyTaskCursor;
import com.leong.nimbus.gui.helpers.FileItemPanelGroup;
import com.leong.nimbus.gui.layout.AllCardsPanel;
import com.leong.nimbus.utils.Logit;
import java.awt.Component;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JPanel;

/**
 *
 * @author henry
 */
public abstract class CloudPanelAdapter<T, CONTROLLER extends ICloudController<T>>
    extends javax.swing.JPanel
    implements ICloudPanel<T>
{
    private static final Logit Log = Logit.create(CloudPanelAdapter.class.getName());

    protected final Map<T, List<Component>> m_cachedComponents = new HashMap<>();

    protected CONTROLLER m_controller;

    protected T m_currentPath;

    protected CloudPanelAdapter()
    {
        Log.entering("<init>");
    }

    @Override
    public void setCurrentPath(T path)
    {
        m_currentPath = path;
    }

    @Override
    public void responsiveShowFiles(final T path, final boolean useCache)
    {
        Log.entering("responsiveShowFiles", new Object[]{getAbsolutePath(path), useCache});

        BusyTaskCursor.doTask(this, new BusyTaskCursor.IBusyTask()
        {
            @Override
            public void run()
            {
                showFiles(path, useCache);
            }
        });
    }

    @Override
    public List<Component> getFiles(final T parent, final boolean useCache)
    {
        Log.entering("getFiles", new Object[]{getAbsolutePath(parent), useCache});

        List<Component> list;

        if (useCache && m_cachedComponents.containsKey(parent))
        {
            Log.fine(String.format("Cache hit '%s'", getAbsolutePath(parent)));
            list = m_cachedComponents.get(parent);
        }
        else
        {
            list = new ArrayList<>();

            FileItemPanelGroup group = new FileItemPanelGroup();

            // show parent link
            {
                T grandParentFile = m_controller.getParent(parent);

                if (grandParentFile != null)
                {
                    FileItemPanel pnl = createFileItemPanel(grandParentFile);

                    pnl.setLabel("..");

                    group.add(pnl);
                    list.add(pnl);
                }
            }

            // get all files in this folder
            final List<T> files = m_controller.getChildrenItems(parent, useCache);

            Log.fine("Total files: "+files.size());

            for (T file : files)
            {
                FileItemPanel pnl = createFileItemPanel(file);
                group.add(pnl);
                list.add(pnl);
            }

            m_cachedComponents.put(parent, list);
        }

        return list;
    }

    @Override
    public void showFiles(final T parent, final boolean useCache)
    {
        Log.entering("showFiles", new Object[]{getAbsolutePath(parent), useCache});

        //txtPath.setText(parent.getAbsolutePath());
        setCurrentPath(parent);

        List<Component> list = getFiles(parent, useCache);

        if (!list.isEmpty())
        {
            // must reset the highlights
            FileItemPanel pnl = (FileItemPanel) list.get(0);
            if (pnl.getGroup() != null)
            {
                pnl.getGroup().reset();
            }
        }

        final JPanel pnlFiles = getFilesPanel();

        // remove all items first
        pnlFiles.removeAll();

        // add the components to the panel
        for (Component pnl : list)
        {
            pnlFiles.add(pnl);
        }

        // make sure repaint happens
        pnlFiles.revalidate();
        pnlFiles.repaint();

        // for keyreleased to work properly
        pnlFiles.requestFocusInWindow();
    }

    @Override
    public void setPanelView(AllCardsPanel.ViewType type)
    {
        AllCardsPanel pnl = getFilesPanel();
        pnl.setView(type);
    }

}
