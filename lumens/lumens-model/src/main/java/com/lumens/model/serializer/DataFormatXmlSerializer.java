/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.model.serializer;

import com.lumens.model.Format;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Map;

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
        DataOutputStream dataOut = new DataOutputStream(out);
        dataOut.writeBytes("<?xml version=\"1.0\" encoding=\"" + charset + "\"?>\n");
        StringBuilder indent = new StringBuilder();
        writeToXml(format, dataOut, indent);
    }

    private void writeToXml(Format format, DataOutputStream out, StringBuilder indent) throws Exception
    {
        out.writeBytes("<");
        out.writeBytes(format.getName());
        Map<String, Object> props = format.getProperties();
        if (props != null)
        {
            out.writeBytes(" ");
            Iterator<Map.Entry<String, Object>> it = props.entrySet().iterator();
            while (it.hasNext())
            {
                Map.Entry<String, Object> e = it.next();
                out.writeBytes(e.getKey() + "=" + "\"" + e.getValue() + "\" ");
            }
        }
        if (format.isField())
            out.writeBytes("/>\n");
        else
            out.writeBytes(">\n");
        if (!format.isField() && format.getChildren() != null)
        {
            for (Format child : format.getChildren())
                writeToXml(child, out, indent);

            out.writeBytes("</");
            out.writeBytes(format.getName());
            out.writeBytes(">\n");
        }

    }
}
