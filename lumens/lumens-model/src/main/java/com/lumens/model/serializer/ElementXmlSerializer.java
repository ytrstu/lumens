/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.model.serializer;

import com.lumens.io.StringWriter;
import com.lumens.io.XmlSerializer;
import com.lumens.model.Element;
import com.lumens.model.Format;
import com.lumens.model.Type;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 *
 * @author shaofeng wang
 */
public class ElementXmlSerializer implements XmlSerializer
{
    private Element element;
    private boolean useIndent;
    private String INDENT = "  ";

    public ElementXmlSerializer(Element element, boolean indent)
    {
        this.element = element;
        this.useIndent = indent;
    }

    public void initIndent(String indent)
    {
        this.INDENT = indent;
    }

    @Override
    public void read(InputStream in) throws Exception
    {
    }

    @Override
    public void write(OutputStream out) throws Exception
    {
        StringWriter dataOut = new StringWriter(out);
        writeElementToXml(element, "", dataOut);
    }

    private void writeElementToXml(Element element, String indent,
                                   StringWriter out) throws Exception
    {
        boolean closeTag = false;
        Format format = element.getFormat();
        out.print(indent).print("<element name=\"").print(format.
                getName()).print("\" ").
                print("form=\"");
        out.print(format.getForm().toString());
        out.print("\" ").print("type=\"").print(format.getType().toString()).
                print("\"");

        if (!element.isArray() && format.getType() != Type.NONE && !element.
                isNull())
        {
            if (!closeTag)
                out.print(">");
            closeTag = true;
            if (element.isField() || element.getChildren() == null)
                out.print(element.getValue().getString());
            else
                out.println(element.getValue().getString());
        }

        List<Element> children = element.getChildren();
        if (children != null && children.size() > 0)
        {
            if (!closeTag)
                out.println(">");
            closeTag = true;
            for (Element child : children)
                writeElementToXml(child, indent + INDENT, out);
        }
        if (closeTag)
        {
            if (element.isField() || element.getChildren() == null)
                out.println("</element>");
            else
                out.print(indent.toString()).println("</element>");
        } else
            out.println("/>");
    }
}
