/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.webservice.soap;

import com.lumens.model.Element;
import com.lumens.model.Format;
import com.lumens.model.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private Map<String, OMNamespace> namespaceCache;
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
        try
        {
            namespaceCache = new HashMap<String, OMNamespace>();
            if (element == null || element.getChildren() == null)
                return null;
            List<Element> children = element.getChildren();
            for (Element message : children)
            {
                Format messageFmt = message.getFormat();
                Integer isMessage = messageFmt.getProperty(SOAPMESSAGE).getInt();
                if (isMessage != null && isMessage.intValue() == SOAPMESSAGE_IN)
                {
                    String targetNamespace = messageFmt.getProperty(
                            TARGETNAMESPACE).getString();
                    OMNamespace omNs = soapFactory.createOMNamespace(
                            targetNamespace,
                            NAMESPACEPREFIX + nsCount);
                    namespaceCache.put(targetNamespace, omNs);
                    ++nsCount;
                    OMElement soapBody = soapFactory.createOMElement(messageFmt.
                            getName(), omNs);
                    if (messageFmt.getType() != Type.NONE)
                    {
                        if (!message.isNull())
                            soapBody.setText(message.getValue().getString());
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
        } finally
        {
            namespaceCache = null;
        }

        return null;
    }

    private void buildSOAPElement(OMElement parent, Element element,
                                  OMNamespace omNs)
    {
        Format elemFmt = element.getFormat();
        boolean isXmlAttribute = elemFmt.getProperty(SOAPATTRIBUTE) != null;
        if (!element.isArray())
        {
            String targetNamespace = elemFmt.getProperty(TARGETNAMESPACE).
                    getString();
            if (!omNs.getNamespaceURI().equals(targetNamespace))
            {
                omNs = namespaceCache.get(targetNamespace);
                if (omNs == null && targetNamespace != null)
                {
                    omNs = soapFactory.createOMNamespace(targetNamespace,
                                                         NAMESPACEPREFIX + nsCount);
                    namespaceCache.put(targetNamespace, omNs);
                }
            }
            if (isXmlAttribute)
            {
                if (elemFmt.getType() != Type.NONE && !element.isNull())
                    parent.addAttribute(elemFmt.getName(), element.getValue().
                            getString(), omNs);
                return;
            }

            OMElement soapElem = soapFactory.createOMElement(elemFmt.getName(),
                                                             omNs, parent);

            if (elemFmt.getType() != Type.NONE)
            {
                if (!element.isNull())
                    soapElem.setText(element.getValue().getString());
            }
            if (!elemFmt.isField() && element.getChildren() != null)
            {
                for (Element e : element.getChildren())
                    buildSOAPElement(soapElem, e, omNs);
            }
        } else if (element.isArray() && element.getChildren() != null)
        {
            for (Element e : element.getChildren())
                buildSOAPElement(parent, e, omNs);
        }
    }
}
