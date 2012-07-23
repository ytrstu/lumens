/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.webservice;

import com.lumens.connector.Connector;
import com.lumens.connector.Operation;
import com.lumens.connector.Param;
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
    private SOAPClient soapClient;
    private String wsdlURL;
    private String user;
    private String password;

    @Override
    public void open()
    {
        soapClient = new SOAPClient(wsdlURL, user, password);
        soapClient.open();
    }

    @Override
    public void close()
    {
        if (soapClient != null)
            soapClient.close();
        soapClient = null;
    }

    @Override
    public Format getFormats(Param param)
    {
        return soapClient.buildServiceFormats(param);
    }

    @Override
    public Format getFormat(Format format, Param param)
    {
        return soapClient.getFormat(format);
    }

    @Override
    public void configure(Map<String, Object> configuration)
    {
        if (configuration.containsKey(WSDL))
            wsdlURL = (String) configuration.get(WSDL);
        if (configuration.containsKey(USER))
            user = (String) configuration.get(USER);
        if (configuration.containsKey(PASSWORD))
            password = (String) configuration.get(PASSWORD);
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
}
