/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.model.serializer;

import com.lumens.model.Element;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 *
 * @author shaofeng wang
 */
public class DataElementXmlSerializer implements XmlSerializer
{
    private Element element;
    private String charset;
    private boolean useIndent;

    public DataElementXmlSerializer(Element element, String charset, boolean indent)
    {
        this.charset = charset;
        this.element = element;
        this.useIndent = indent;
    }

    @Override
    public void read(InputStream in) throws Exception
    {
    }

    @Override
    public void write(OutputStream out) throws Exception
    {
        DataOutputStream dataOut = new DataOutputStream(out);
        dataOut.writeBytes("<?xml version=\"1.0\" encoding=\"" + charset + "\"?>\n");
        StringBuilder indent = new StringBuilder();
        writeToXml(element, dataOut, indent);
    }

    private void writeToXml(Element elem, DataOutputStream out, StringBuilder indent) throws IOException
    {
        if (elem.isArray() || elem.isStruct())
        {
            if (elem.isArrayItem() || elem.isStruct())
            {
                out.writeBytes("<");
                out.writeBytes(elem.getFormat().getName());
                out.writeBytes(">\n");
            }
            List<Element> children = elem.getChildren();
            if (children != null && !children.isEmpty())
            {
                for (Element child : children)
                    writeToXml(child, out, indent);
            }
            if (elem.isArrayItem() || elem.isStruct())
            {
                out.writeBytes("</");
                out.writeBytes(elem.getFormat().getName());
                out.writeBytes(">\n");
            }
        }
        else if (elem.isField())
        {
            out.writeBytes("<");
            out.writeBytes(elem.getFormat().getName());
            out.writeBytes(">");
            out.writeBytes(elem.getValue().toString());
            out.writeBytes("</");
            out.writeBytes(elem.getFormat().getName());
            out.writeBytes(">\n");
        }
    }
}
