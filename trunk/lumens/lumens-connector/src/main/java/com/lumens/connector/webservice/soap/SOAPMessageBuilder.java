/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.webservice.soap;

import com.lumens.model.Element;
import com.lumens.model.Format;
import com.lumens.model.Type;
import java.util.List;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.soap.SOAPBody;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axiom.soap.SOAPFactory;

/**
 *
 * @author shaofeng wang
 */
public class SOAPMessageBuilder implements SOAPConstants
{
    private SOAPFactory soapFactory;
    private int nsCount;

    public SOAPMessageBuilder()
    {
        this(SOAP11);
    }

    public SOAPMessageBuilder(int soapVersion)
    {
        if (soapVersion == SOAP11)
            soapFactory = OMAbstractFactory.getSOAP11Factory();
    }

    public SOAPEnvelope buildSOAPMessage(Element element)
    {
        // TODO not handle binary and attachment
        if (element == null || element.getChildren() == null)
            return null;
        List<Element> children = element.getChildren();
        for (Element message : children)
        {
            Format messageFmt = message.getFormat();
            Integer isMessage = (Integer) messageFmt.getProperty(SOAPMESSAGE);
            if (isMessage != null && isMessage.intValue() == SOAPMESSAGE_IN)
            {
                String targetNamespace = (String) messageFmt.getProperty(TARGETNAMESPACE);
                OMNamespace omNs = soapFactory.createOMNamespace(targetNamespace,
                                                                 NAMESPACEPREFIX + nsCount);
                ++nsCount;
                OMElement soapBody = soapFactory.createOMElement(messageFmt.getName(), omNs);
                if (messageFmt.getType() != Type.NONE)
                {
                    String value = message.getString();
                    if (value != null)
                        soapBody.setText(value);
                }
                if (!message.isField() && message.getChildren() != null)
                {
                    for (Element e : message.getChildren())
                    {
                        buildSOAPElement(soapBody, e, omNs);
                    }
                }

                SOAPEnvelope envelope = soapFactory.createSOAPEnvelope();
                SOAPBody body = soapFactory.createSOAPBody(envelope);
                body.addChild(soapBody);
                return envelope;
            }
        }

        return null;
    }

    private void buildSOAPElement(OMElement parent, Element element, OMNamespace omNs)
    {
        Format elemFmt = element.getFormat();
        String targetNamespace = (String) elemFmt.getProperty(TARGETNAMESPACE);
        if (omNs.getNamespaceURI().equals(targetNamespace))
        {
            OMElement soapElem = soapFactory.createOMElement(elemFmt.getName(), omNs, parent);
            if (elemFmt.getType() != Type.NONE)
            {
                String value = element.getString();
                if (value != null)
                    soapElem.setText(value);
            }
            if (!elemFmt.isField() && element.getChildren() != null)
            {
                for (Element e : element.getChildren())
                {
                    buildSOAPElement(soapElem, e, omNs);
                }
            }
        }
    }
}
