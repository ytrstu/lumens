/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.xml;

import com.lumens.connector.Connector;
import com.lumens.connector.Operation;
import com.lumens.connector.Usage;
import com.lumens.model.Format;
import java.util.Map;

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
    public Map<String, Format> getFormatList(Usage param)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Format getFormat(Format format, String path, Usage param)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Operation getOperation()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setParameters(Map<String, Object> parameters)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
