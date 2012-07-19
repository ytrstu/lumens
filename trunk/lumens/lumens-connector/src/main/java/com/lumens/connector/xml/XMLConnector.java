/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.xml;

import com.lumens.connector.Connector;
import com.lumens.connector.Reader;
import com.lumens.connector.Usage;
import com.lumens.connector.Writer;
import com.lumens.model.Format;

/**
 *
 * @author shaofeng wang
 */
public class XMLConnector implements Connector
{

    @Override
    public void open()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void close()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Reader createReader()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Writer createWriter()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Format getFormats(Usage usage)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Format getFormat(Format format, Usage usage)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
