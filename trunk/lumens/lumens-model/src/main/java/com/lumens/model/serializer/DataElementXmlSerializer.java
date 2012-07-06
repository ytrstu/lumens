/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.model.serializer;

import com.lumens.model.Element;
import com.lumens.model.Format;
import com.lumens.model.Format.Form;
import com.lumens.model.Type;
import java.io.DataOutputStream;
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
        StringWriter dataOut = new StringWriter(new DataOutputStream(out));
        dataOut.println("<?xml version=\"1.0\" encoding=\"" + charset + "\"?>");
        StringBuilder indent = new StringBuilder("");
        writeElementToXml(element, indent, dataOut);
    }

    private void writeElementToXml(Element element, StringBuilder indent, StringWriter out) throws Exception
    {
        boolean closeTag = false;
        Format format = element.getFormat();
        out.print(indent.toString()).print("<Element name=\"").print(format.getName()).print("\" ").
                print("form=\"");
        if (element.isArrayItem())
            out.print(Form.STRUCT.toString());
        else
            out.print(format.getForm().toString());
        out.print("\" ").print("type=\"").print(format.getType().toString()).print("\"");

        if (format.getType() != Type.NONE)
        {
            if (!closeTag)
                out.print(">");
            closeTag = true;
            if (element.isField())
                out.print(element.getString());
            else
                out.println(element.getString());
        }

        List<Element> children = element.getChildren();
        if (children != null && children.size() > 0)
        {
            if (!closeTag)
                out.println(">");
            closeTag = true;
            for (Element child : children)
            {
                if (child.isArray())
                    writeElementToXml(child, new StringBuilder(indent).append("  "), out);
                else if (child.isStruct())
                    writeElementToXml(child, new StringBuilder(indent).append("  "), out);
                else if (child.isField())
                    writeElementToXml(child, new StringBuilder(indent).append("  "), out);
            }
        }
        if (closeTag)
        {
            if (element.isField())
                out.println("</Element>");
            else
                out.print(indent.toString()).println("</Element>");
        }
        else
            out.println("/>");
    }
}
