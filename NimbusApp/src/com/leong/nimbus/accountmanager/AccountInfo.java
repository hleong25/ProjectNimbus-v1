/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leong.nimbus.accountmanager;

import com.leong.nimbus.clouds.CloudType;
import com.leong.nimbus.utils.Logit;
import com.leong.nimbus.utils.Tools;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author henry
 */
public class AccountInfo
{
    private static final Logit Log = Logit.create(AccountInfo.class.getName());

    public static final String ELEM_ROOT = "account";
    private static final String ELEM_TYPE = "type";
    private static final String ELEM_NAME = "name";
    private static final String ELEM_SECRET = "secret";
    private static final String ELEM_ITEM = "item";

    private static final String ATTR_VERSION = "version";
    private static final String ATTR_ID = "id";

    private int m_version = 1; // update as needed
    private final CloudType m_type;
    private String m_id;
    private String m_name;
    private List<String> m_secret;

    protected AccountInfo(CloudType type, String id)
    {
        m_type = type;
        m_id = id;
        m_name = "";
        m_secret = new ArrayList<>();
    }

    public static AccountInfo createInstance(CloudType type, String id)
    {
        AccountInfo info = new AccountInfo(type, id);
        return info;
    }

    public static AccountInfo createInstance(Element fragment)
    {
        String data;

        if (!fragment.getNodeName().equals(ELEM_ROOT))
        {
            Log.warning("Parsing element, not '"+ELEM_ROOT+"'");
            return null;
        }

        // get version
        int version = 0;
        {
            data = fragment.getAttribute(ATTR_VERSION);

            try
            {
                version = Integer.parseInt(data);
            }
            catch (NumberFormatException ex)
            {
                Log.warning("Failed to parse version");
                return null;
            }
        }

        // get account id
        String id = fragment.getAttribute(ATTR_ID);

        AccountInfo info = null;

        // parse each child element
        NodeList nodes = fragment.getChildNodes();

        for (int idx = 0, end = nodes.getLength(); idx < end; ++idx)
        {
            Node node = nodes.item(idx);
            if (node.getNodeType() != Node.ELEMENT_NODE)
            {
                continue;
            }

            Element elem = (Element) nodes.item(idx);
            String nodeName = elem.getNodeName();

            if (nodeName.equals(ELEM_TYPE))
            {
                data = elem.getTextContent();

                if (Tools.isNullOrEmpty(data))
                {
                    continue;
                }

                CloudType cloudType;
                if (data.equals(CloudType.GOOGLE_DRIVE.toString()))
                {
                    cloudType = CloudType.GOOGLE_DRIVE;
                }
                else if (data.equals(CloudType.DROPBOX.toString()))
                {
                    cloudType = CloudType.DROPBOX;
                }
                else
                {
                    Log.severe("Unknown cloud type: "+data);
                    return null;
                }

                info = AccountInfo.createInstance(cloudType, id);
            }
            else if (nodeName.equals(ELEM_NAME))
            {
                data = elem.getTextContent();
                info.setName(data);
            }
            else if (nodeName.equals(ELEM_SECRET))
            {
                NodeList secrets = elem.getElementsByTagName(ELEM_ITEM);

                for (int idx_secrets = 0, end_secrets = secrets.getLength();
                     idx_secrets < end_secrets;
                     ++idx_secrets)
                {
                    Node secret = secrets.item(idx_secrets);
                    if (secret.getNodeType() != Node.ELEMENT_NODE)
                    {
                        continue;
                    }

                    Element elem_secret = (Element) secret;
                    data = elem_secret.getTextContent();

                    if (Tools.isNullOrEmpty(data))
                    {
                        continue;
                    }

                    info.addSecret(data);
                }
            }
        }

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

    public String getId()
    {
        return m_id;
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

    public void addSecret(String secret)
    {
        m_secret.add(secret);
    }

    public String[] getSecret()
    {
        return m_secret.toArray(new String[0]);
    }

    public Element serialize(Document doc)
    {
        Element child;
        Element fragment = doc.createElement(ELEM_ROOT);

        fragment.setAttribute(ATTR_VERSION, String.valueOf(getVersion()));
        fragment.setAttribute(ATTR_ID, getId());

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
