/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.webservice;

import com.lumens.connector.Connector;
import com.lumens.connector.FormatBuilder;
import com.lumens.connector.Operation;
import com.lumens.connector.Usage;
import com.lumens.connector.webservice.soap.SOAPClient;
import com.lumens.model.Format;
import java.util.Map;

/**
 * TODO need to enhance to handle complex xsd element
 *
 * @author shaofeng wang
 */
public class WebServiceConnector implements Connector
{
    public static final String WSDL = "WSDL";
    public static final String USER = "User";
    public static final String PASSWORD = "Password";
    public static final String PROXY_ADDR = "ProxyAddress";
    public static final String PROXY_PORT = "ProxyPort";
    public static final String PROXY_USER = "ProxyUser";
    public static final String PROXY_PASSWORD = "ProxyPassword";
    private SOAPClient soapClient;
    private FormatBuilder formatBuilder;
    private String wsdlURL;
    private String user;
    private String password;
    private String proxyAddr;
    private int proxyPort = 80;
    private String proxyUser;
    private String proxyPassword;

    @Override
    public void open()
    {
        soapClient = new SOAPClient(this);
        soapClient.open();
        formatBuilder = soapClient.getFormatBuilder();
    }

    @Override
    public void close()
    {
        if (soapClient != null)
            soapClient.close();
        soapClient = null;
    }

    @Override
    public Map<String, Format> getFormatList(Usage use)
    {
        return formatBuilder.getFormatList(use);
    }

    @Override
    public Format getFormat(Format format, String path, Usage use)
    {
        return formatBuilder.getFormat(format, path, use);
    }

    @Override
    public void setParameters(Map<String, Object> parameters)
    {
        if (parameters.containsKey(WSDL))
            wsdlURL = (String) parameters.get(WSDL);
        if (parameters.containsKey(USER))
            user = (String) parameters.get(USER);
        if (parameters.containsKey(PASSWORD))
            password = (String) parameters.get(PASSWORD);
        if (parameters.containsKey(PROXY_ADDR))
            proxyAddr = (String) parameters.get(PROXY_ADDR);
        if (parameters.containsKey(PROXY_PORT))
            proxyPort = (Integer) parameters.get(PROXY_PORT);
        if (parameters.containsKey(PROXY_USER))
            proxyUser = (String) parameters.get(PROXY_USER);
        if (parameters.containsKey(PROXY_PASSWORD))
            proxyPassword = (String) parameters.get(PROXY_PASSWORD);
    }

    @Override
    public Operation getOperation()
    {
        return new WebServiceOperation(getClient());
    }

    protected SOAPClient getClient()
    {
        return soapClient;
    }

    public String getProxyAddr()
    {
        return proxyAddr;
    }

    public int getProxyPort()
    {
        return proxyPort;
    }

    public String getProxyUser()
    {
        return proxyUser;
    }

    public String getProxyPassword()
    {
        return proxyPassword;
    }

    /**
     * @return the wsdlURL
     */
    public String getWsdlURL()
    {
        return wsdlURL;
    }

    /**
     * @return the user
     */
    public String getUser()
    {
        return user;
    }

    /**
     * @return the password
     */
    public String getPassword()
    {
        return password;
    }
}
