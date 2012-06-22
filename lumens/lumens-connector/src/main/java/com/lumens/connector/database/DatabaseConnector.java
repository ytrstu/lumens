/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.database;

import com.lumens.connector.Client;
import com.lumens.connector.Configurable;
import com.lumens.connector.Connector;
import com.lumens.connector.Reader;
import com.lumens.connector.Writer;
import com.lumens.connector.database.oracle.OracleClient;
import com.lumens.model.Format;
import java.util.List;
import java.util.Map;

/**
 *
 * @author shaofeng wang
 */
public class DatabaseConnector implements Connector, Configurable
{
    private Client dbClient;
    private List<Format> tables;
    private String connUrl;
    private String user;
    private String password;

    @Override
    public void open()
    {
        // TODO only create oracle client now, select different db client later
        dbClient = new OracleClient(connUrl, user, password);
    }

    @Override
    public void close()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Reader createReader()
    {
        return new DatabaseReader(this);
    }

    @Override
    public Writer createWriter()
    {
        return new DatabaseWriter(this);
    }

    @Override
    public void setConfiguration(Map<String, Object> configuration)
    {
    }

    @Override
    public Map<String, Object> getConfiguration()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
