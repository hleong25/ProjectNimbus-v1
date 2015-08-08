/*
w
yellowa13dsadyfdsfsDFSFfdsfsdfdsfsdyel
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leong.nimbus.accountmanager;

import com.leong.nimbus.utils.Logit;
import com.leong.nimbus.utils.NimbusDatastore;
import com.leong.nimbus.utils.Tools;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author henry
 */
public class AccountManagerModel
{
    private static final Logit Log = Logit.create(AccountManagerModel.class.getName());
    private static AccountManagerModel m_singleton = null;
    private static boolean m_createdSingleton = false;

    private final String FILE_ACCOUNTS = "accounts";

    private final String ELEM_ROOT = "nimbus";

    private final DocumentBuilder m_docbuilder;

    private final List<AccountInfo> m_accounts;

    protected AccountManagerModel() throws ParserConfigurationException
    {
        m_accounts = new ArrayList<>();

        DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
        m_docbuilder = dbfactory.newDocumentBuilder();

        parseCredsFile();
    }

    static public AccountManagerModel getInstance()
    {
        if (!m_createdSingleton && (m_singleton == null))
        {
            // create the singleton only once
            m_createdSingleton = true;
            try
            {
                m_singleton = new AccountManagerModel();
            }
            catch (ParserConfigurationException ex)
            {
                Log.throwing("getInstance", ex);
                return null;
            }
        }
        return m_singleton;
    }

    protected boolean parseCredsFile()
    {
        File accountsFile = NimbusDatastore.getFile("creds", FILE_ACCOUNTS);
        if (!accountsFile.canRead())
        {
            Log.warning("Failed to read creds file");
            return false;
        }

        Log.fine("Reading credentials: " + accountsFile.getAbsolutePath());

        try
        {
            Document doc;
            Element root;

            doc = m_docbuilder.parse(accountsFile);
            root = doc.getDocumentElement();
            root.normalize();

            if (!root.getNodeName().equals(ELEM_ROOT))
            {
                Log.severe("Failed to parse accounts file because root element not '"+ELEM_ROOT+"'.");
                return false;
            }

            NodeList nodes = root.getElementsByTagName(AccountInfo.ELEM_ROOT);

            for (int idx = 0, end = nodes.getLength(); idx < end; ++idx)
            {
                Node node = nodes.item(idx);

                switch (node.getNodeType())
                {
                    case Node.ELEMENT_NODE:
                        {
                            Element elem = (Element) node;
                            AccountInfo info = AccountInfo.createInstance(elem);

                            if (info != null)
                            {
                                m_accounts.add(info);
                            }
                            else
                            {
                                Log.warning("Failed to parse #"+idx+" account info");
                            }

                        }
                        break;
                }
            }

            return true;
        }
        catch (SAXException | IOException ex)
        {
            Log.throwing("parseCredsFile", ex);
        }
        return false;
    }

    public boolean addAccountInfo(AccountInfo account)
    {
        m_accounts.add(account);
        return true;
    }

    @Override
    public String toString()
    {
        return serialize();
    }

    public String serialize()
    {
        Document doc;
        Element root;

        doc = m_docbuilder.newDocument();
        root = doc.createElement(ELEM_ROOT);
        doc.appendChild(root);

        for (AccountInfo info : m_accounts)
        {
            Element frag = info.serialize(doc);
            root.appendChild(frag);
        }

        return Tools.xmlToString(doc);
    }

    public boolean exportAsFile()
    {
        BufferedWriter bw = NimbusDatastore.getWriterNoThrow("creds", FILE_ACCOUNTS);

        if (bw == null)
        {
            return false;
        }

        PrintWriter writer = new PrintWriter(bw);

        writer.print(toString());
        writer.flush();
        writer.close();

        return true;
    }
}
