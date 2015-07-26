/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leong.nimbus.accountmanager;

import com.leong.nimbus.utils.Logit;
import com.leong.nimbus.utils.NimbusDatastore;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
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

        try
        {
            Document doc;
            Element root;

            doc = m_docbuilder.parse(accountsFile);
            root = doc.getDocumentElement();
            root.normalize();

            // TODO: do parse

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
        //try
        //{
            Document doc;
            Element root;

            doc = m_docbuilder.newDocument();
            root = doc.createElement("nimbus");
            doc.appendChild(root);

            for (AccountInfo info : m_accounts)
            {
                Element frag = info.serialize(doc);
                root.appendChild(frag);
            }

        //}

            StringWriter sw = new StringWriter();

        try
        {
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.transform(new DOMSource(doc), new StreamResult(sw));
        }
        catch (TransformerException ex)
        {
            Log.throwing("serialize", ex);
        }
            return sw.toString();
    }

}
