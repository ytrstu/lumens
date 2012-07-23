/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.xml;

import com.lumens.connector.Connector;
import com.lumens.connector.Operation;
import com.lumens.connector.Param;
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
    public Format getFormats(Param param)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Format getFormat(Format format, Param param)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Operation getOperation()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
