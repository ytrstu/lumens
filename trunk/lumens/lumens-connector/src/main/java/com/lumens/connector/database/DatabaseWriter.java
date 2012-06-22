/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.database;

import com.lumens.connector.Cacheable;
import com.lumens.connector.Writer;
import com.lumens.model.Element;

/**
 *
 * @author shaofeng wang
 */
public class DatabaseWriter implements Writer, Cacheable
{
    private DatabaseConnector db;

    DatabaseWriter(DatabaseConnector db)
    {
        this.db = db;
    }

    @Override
    public int write(Operate o, Element e) throws RuntimeException
    {
        return 0;
    }

    @Override
    public void cleanup()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setCacheSize(int size)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getCacheSize()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
