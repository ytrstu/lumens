/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.webservice.soap;

import com.lumens.connector.FormatBuilder;
import com.lumens.connector.Usage;
import com.lumens.connector.webservice.WebServiceConnector;
import com.lumens.model.Element;
import com.lumens.model.Format;
import java.util.ArrayList;
import java.util.List;
import javax.xml.namespace.QName;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.OperationClient;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.axis2.transport.http.HttpTransportProperties;
import org.apache.axis2.transport.http.HttpTransportProperties.Authenticator;
import org.apache.axis2.transport.http.HttpTransportProperties.ProxyProperties;
import org.apache.axis2.wsdl.WSDLConstants;

/**
 *
 * @author shaofeng wang
 */
public class SOAPClient implements SOAPConstants
{
    private FormatFromWSDLBuilder formatBuilder;
    private SOAPMessageBuilder soapBuilder;
    private ElementFromSOAPBuilder elementBuilder;
    private Authenticator basicAuth;
    private ServiceClient client;
    private WebServiceConnector connector;

    public SOAPClient(WebServiceConnector connector)
    {
        this.connector = connector;
    }

    public void open()
    {
        formatBuilder = new FormatFromWSDLBuilder(connector.getWsdlURL());
        formatBuilder.loadWSDL();
        soapBuilder = new SOAPMessageBuilder();
        elementBuilder = new ElementFromSOAPBuilder();
        try
        {
            client = new ServiceClient();
            Options options = client.getOptions();
            String user = connector.getUser();
            if (user != null)
            {
                basicAuth = new HttpTransportProperties.Authenticator();
                List auth = new ArrayList();
                auth.add(Authenticator.BASIC);
                basicAuth.setAuthSchemes(auth);
                basicAuth.setUsername(user);
                if (connector.getPassword() != null)
                    basicAuth.setPassword(connector.getPassword());
                basicAuth.setPreemptiveAuthentication(true);
                options.setProperty(HTTPConstants.AUTHENTICATE, basicAuth);
            }
            options.setProperty(HTTPConstants.REUSE_HTTP_CLIENT, true);
            ProxyProperties pp = new ProxyProperties();
            String proxyAddr = connector.getProxyAddr();
            if (proxyAddr != null && !proxyAddr.isEmpty())
            {
                pp.setProxyName(proxyAddr);
                pp.setProxyPort(connector.getProxyPort());
                if (connector.getProxyUser() != null)
                {
                    pp.setUserName(connector.getProxyUser());
                    if (connector.getProxyPassword() != null)
                        pp.setPassWord(connector.getProxyPassword());
                }
                options.setProperty(HTTPConstants.PROXY, pp);
            }
        }
        catch (AxisFault ex)
        {
            throw new RuntimeException(ex);
        }
    }

    public void close()
    {
        formatBuilder = null;
        if (client != null)
        {
            try
            {
                client.cleanup();
            }
            catch (AxisFault ex)
            {//Ignore exception when close
            }
        }
    }

    public FormatBuilder getFormatBuilder()
    {
        return formatBuilder;
    }

    public Element execute(Element requestElement, Format responseFormat) throws Exception
    {
        Format format = requestElement.getFormat();
        SOAPEnvelope reqEnvelop = soapBuilder.buildSOAPMessage(requestElement);
        Options options = client.getOptions();
        options.setAction((String) format.getProperty(SOAPACTION));
        String strEndPoint = (String) format.getProperty(SOAPENDPOINT);
        options.setTo(new EndpointReference(strEndPoint));

        Object bindingIn = format.getProperty(BINDINGINPUT);
        Object bindingOut = format.getProperty(BINDINGOUTPUT);
        QName operationQName = null;
        if (bindingIn != null && bindingOut != null)
            operationQName = ServiceClient.ANON_OUT_IN_OP;
        else if (bindingIn != null)
            operationQName = ServiceClient.ANON_OUT_ONLY_OP;

        MessageContext msgctx = new MessageContext();
        msgctx.setEnvelope(reqEnvelop);
        OperationClient opClient = client.createClient(operationQName);
        opClient.addMessageContext(msgctx);
        // Do executing
        opClient.execute(true);

        MessageContext responseMsgCtx = opClient.getMessageContext(
                WSDLConstants.MESSAGE_LABEL_IN_VALUE);
        SOAPEnvelope responseEnvelope = responseMsgCtx.getEnvelope();
        return elementBuilder.buildElement(responseFormat, responseEnvelope);
    }
}
