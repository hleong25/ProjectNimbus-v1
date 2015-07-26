/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leong.nimbus.accountmanager;

import com.leong.nimbus.clouds.CloudType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author henry
 */
public class AccountInfo
{
    private static final String ELEM_TYPE = "type";
    private static final String ELEM_NAME = "name";
    private static final String ELEM_SECRET = "secret";
    private static final String ELEM_ITEM = "item";

    private static final String ATTR_VERSION = "version";

    private int m_version = 1; // update as needed
    private final CloudType m_type;
    private String m_name;
    private List<String> m_secret;

    public AccountInfo(CloudType type)
    {
        m_type = type;
        m_name = "";
        m_secret = new ArrayList<>();
    }

    public static AccountInfo createInstance(CloudType type)
    {
        AccountInfo info = new AccountInfo(type);
        return info;
    }

    public static AccountInfo createInstance(Element fragment)
    {
        AccountInfo info = null;
        NodeList nodes;
        Element elem;
        String tmpString;

        // TODO: version is in root attribute, not in type elem

        // get type and version
        nodes = fragment.getElementsByTagName(ELEM_TYPE);

        if (nodes.getLength() != 1)
        {
            return info;
        }

        {
            elem = (Element) nodes.item(0);
            tmpString = elem.getNodeValue();

            CloudType cloudType;
            if (tmpString.equals(CloudType.GOOGLE_DRIVE.toString()))
            {
                cloudType = CloudType.GOOGLE_DRIVE;
            }
            else if (tmpString.equals(CloudType.DROPBOX.toString()))
            {
                cloudType = CloudType.DROPBOX;
            }
            else
            {
                // null
                return info;
            }

            info = new AccountInfo(cloudType);

            tmpString = elem.getAttribute(ATTR_VERSION);
            int version = 0;
            
            version = Integer.parseInt(tmpString);

            info.setVersion(version);
        }

        // parse name
        {
            nodes = fragment.getElementsByTagName(ELEM_NAME);

            if (nodes.getLength() > 0)
            {
                elem = (Element) nodes.item(0);
                tmpString = elem.getNodeValue();

                info.setName(tmpString);
            }
        }

        // parse secrets

        return info;
    }

    private void setVersion(int version)
    {
        m_version = version;
    }

    public int getVersion()
    {
        return m_version;
    }

    public CloudType getType()
    {
        return m_type;
    }

    public void setName(String name)
    {
        m_name = name;
    }

    public String getName()
    {
        return m_name;
    }

    public void setSecret(String[] secret)
    {
        m_secret = Arrays.asList(secret);
    }

    public String[] getSecret()
    {
        return (String[])m_secret.toArray();
    }

    public Element serialize(Document doc)
    {
        Element child;
        Element fragment = doc.createElement("account");

        //doc.appendChild(fragment);

        fragment.setAttribute(ATTR_VERSION, String.valueOf(getVersion()));

        {
            child = doc.createElement(ELEM_TYPE);
            fragment.appendChild(child);
            child.setTextContent(getType().toString());
        }

        {
            child = doc.createElement(ELEM_NAME);
            fragment.appendChild(child);
            child.setTextContent(getName());
        }

        {
            child = doc.createElement(ELEM_SECRET);
            fragment.appendChild(child);

            int idx = 0;
            for (String value : getSecret())
            {
                Element item = doc.createElement(ELEM_ITEM);
                child.appendChild(item);

                item.setAttribute("index", String.valueOf(idx));
                item.setTextContent(value);

                idx++;
            }
        }

        return fragment;
    }
}
