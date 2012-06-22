/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.database;

import com.lumens.connector.Cacheable;
import com.lumens.connector.Condition;
import com.lumens.connector.Reader;
import com.lumens.model.Element;
import java.util.Iterator;

/**
 *
 * @author shaofeng wang
 */
public class DatabaseReader implements Reader, Cacheable
{
    private DatabaseConnector db;

    DatabaseReader(DatabaseConnector db)
    {
        this.db = db;
    }

    @Override
    public void setCondition(Condition cond)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int read() throws RuntimeException
    {
        return 0;
    }

    @Override
    public Iterator<Element> iterator()
    {
        return null;
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
