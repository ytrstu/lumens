/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.model.serializer;

import com.lumens.model.Format;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author shaofeng wang
 */
public class DataFormatXmlSerializer implements XmlSerializer
{
    private final Format format;
    private final String charset;
    private final boolean useIndent;

    public DataFormatXmlSerializer(Format format, String charset, boolean indent)
    {
        this.format = format;
        this.charset = charset;
        this.useIndent = indent;
    }

    @Override
    public void read(InputStream in) throws Exception
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void write(OutputStream out) throws Exception
    {
        StringWriter dataOut = new StringWriter(new DataOutputStream(out));
        dataOut.println("<?xml version=\"1.0\" encoding=\"" + charset + "\"?>");
        StringBuilder indent = new StringBuilder("");
        writeFormatToXml(format, indent, dataOut);
    }

    private void writePropertiesToXml(Map<String, Object> properties, StringBuilder indent,
                                      StringWriter out) throws IOException
    {
        Set<Map.Entry<String, Object>> props = properties.entrySet();
        out.print(indent.toString()).println("<Properties>");
        for (Map.Entry<String, Object> en : props)
        {
            out.print(indent.toString() + "  ").print("<Property name=\"").print(en.getKey()).print(
                    "\">");
            out.print((String) en.getValue()).println("</Property>");
        }
        out.print(indent.toString()).println("</Properties>");
    }

    private void writeFormatToXml(Format format, StringBuilder indent, StringWriter out) throws Exception
    {
        boolean closeTag = false;
        out.print(indent.toString()).print("<Format name=\"").print(format.getName()).print("\" ").
                print(
                "form=\"").print(format.getForm().toString()).print("\" ").print("type=\"").
                print(format.getType().toString()).print("\" ");
        if (format.getProperties() != null)
        {
            closeTag = true;
            out.println(">");
            writePropertiesToXml(format.getProperties(), new StringBuilder(indent).append("  "), out);
        }
        List<Format> children = format.getChildren();
        if (children != null && children.size() > 0)
        {
            if (!closeTag)
                out.println(">");
            closeTag = true;
            for (Format child : children)
            {
                if (child.isArray())
                    writeFormatToXml(child, new StringBuilder(indent).append("  "), out);
                else if (child.isStruct())
                    writeFormatToXml(child, new StringBuilder(indent).append("  "), out);
                else if (child.isField())
                    writeFormatToXml(child, new StringBuilder(indent).append("  "), out);
            }
        }
        if (closeTag)
            out.print(indent.toString()).println("</Format>");
        else
            out.println("/>");
    }
}
