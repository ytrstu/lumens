/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.webservice.soap;

import com.lumens.connector.Usage;
import com.lumens.model.Element;
import com.lumens.model.Format;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.OperationClient;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.context.MessageContext;
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
    private ServiceClient client;
    private String wsdlURL;
    private String user;
    private String password;

    public SOAPClient(String wsdlURL, String user, String password)
    {
        this.wsdlURL = wsdlURL;
        this.user = user;
        this.password = password;
    }

    public void open()
    {
        formatBuilder = new FormatFromWSDLBuilder(wsdlURL);
        formatBuilder.loadWSDL();
        soapBuilder = new SOAPMessageBuilder();
        elementBuilder = new ElementFromSOAPBuilder();
        try
        {
            client = new ServiceClient();
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
            {
            }
        }
    }

    public Format buildServiceFormats(Usage usage)
    {
        return formatBuilder.buildServiceFormats(usage);
    }

    public Format getFormat(Format format)
    {
        return formatBuilder.buildMessageFormat(format);
    }

    public Element execute(Element requestElement, Format responseFormat) throws Exception
    {
        Format format = requestElement.getFormat();
        SOAPEnvelope reqEnvelop = soapBuilder.buildSOAPMessage(requestElement);
        Options options = new Options();
        options.setAction((String) format.getProperty(SOAPACTION));
        client.setOptions(options);
        options.setTo(new EndpointReference((String) format.getProperty(SOAPENDPOINT)));
        MessageContext msgctx = new MessageContext();
        msgctx.setEnvelope(reqEnvelop);
        OperationClient opClient = client.createClient(ServiceClient.ANON_OUT_IN_OP);
        opClient.addMessageContext(msgctx);
        opClient.execute(true);
        MessageContext responseMsgCtx = opClient.getMessageContext(
                WSDLConstants.MESSAGE_LABEL_IN_VALUE);
        SOAPEnvelope responseEnvelope = responseMsgCtx.getEnvelope();
        return elementBuilder.buildElement(responseFormat, responseEnvelope);
    }
}
