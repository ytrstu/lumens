/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.webservice.soap;

import com.lumens.model.DataElement;
import com.lumens.model.Element;
import com.lumens.model.Format;
import com.lumens.model.Type;
import java.util.Iterator;
import java.util.List;
import javax.xml.namespace.QName;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.soap.SOAPBody;
import org.apache.axiom.soap.SOAPEnvelope;

/**
 *
 * @author shaofeng wang
 */
public class ElementFromSOAPBuilder implements SOAPConstants
{
    public Element buildElement(Format format, SOAPEnvelope envelope)
    {
        // TODO need to handle binary attachment
        if (format == null || format.getChildren() == null)
            return null;
        Element element = new DataElement(format);
        List<Format> children = format.getChildren();
        for (Format message : children)
        {
            Integer isMessage = (Integer) message.getProperty(SOAPMESSAGE);
            if (isMessage != null && isMessage.intValue() == SOAPMESSAGE_OUT)
            {
                Element messageElement = element.addChild(message.getName());
                String namespace = (String) message.getProperty(TARGETNAMESPACE);
                SOAPBody body = envelope.getBody();
                OMElement omElem = body.getFirstElement();
                if (omElem != null)
                {
                    QName qName = omElem.getQName();
                    if (qName.getLocalPart().equals(message.getName())
                        && qName.getNamespaceURI().equals(namespace))
                        buildElementFromOMElement(messageElement, omElem);
                }
                break;
            }
        }
        return element;
    }

    private void buildElementFromOMElement(Element element, OMElement omElem)
    {
        Format format = element.getFormat();
        List<Format> children = format.getChildren();
        if (children != null)
        {
            for (Format child : children)
            {
                String namespace = (String) child.getProperty(TARGETNAMESPACE);
                Iterator<OMElement> it = omElem.getChildrenWithName(new QName(namespace, child.getName()));
                if (it != null && it.hasNext())
                {
                    Element childElement = element.addChild(child.getName());
                    while (it.hasNext())
                    {
                        OMElement omChild = it.next();
                        if (child.isArray())
                        {
                            Element arrayItem = childElement.addArrayItem();
                            if (child.getType() != Type.NONE && arrayItem.isArrayItem())
                                arrayItem.setValue(omChild.getText());
                            buildElementFromOMElement(arrayItem, omChild);
                        }
                        else
                        {
                            if (child.getType() != Type.NONE)
                                childElement.setValue(omChild.getText());
                            buildElementFromOMElement(childElement, omChild);
                        }
                    }
                }
            }
        }
    }
}
