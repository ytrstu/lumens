/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.database;

import com.lumens.connector.Configurable;
import com.lumens.connector.Connector;
import com.lumens.connector.Reader;
import com.lumens.connector.Writer;
import com.lumens.connector.database.oracle.OracleClient;
import com.lumens.model.Format;
import java.util.Map;

/**
 *
 * @author shaofeng wang
 */
public class DatabaseConnector implements Connector, Configurable
{
    private Client dbClient;
    private Format tables;
    private String ojdbcURL;
    private String connURL;
    private String user;
    private String password;

    @Override
    public void open()
    {
        // TODO only create oracle client now, select different db client later
        dbClient = new OracleClient(ojdbcURL, connURL, user, password);
        dbClient.open();
        tables = dbClient.describeFormats();
    }

    @Override
    public void close()
    {
        dbClient.close();
        tables = null;
    }

    @Override
    public Reader createReader()
    {
        return new DatabaseReader(dbClient);
    }

    @Override
    public Writer createWriter()
    {
        return new DatabaseWriter(dbClient);
    }

    @Override
    public void setConfiguration(Map<String, Object> configuration)
    {
        if (configuration.containsKey("OJDBC"))
            ojdbcURL = (String) configuration.get("OJDBC");
        if (configuration.containsKey("ConnectionURL"))
            connURL = (String) configuration.get("ConnectionURL");
        if (configuration.containsKey("User"))
            user = (String) configuration.get("User");
        if (configuration.containsKey("Password"))
            password = (String) configuration.get("Password");
    }

    @Override
    public Map<String, Object> getConfiguration()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
