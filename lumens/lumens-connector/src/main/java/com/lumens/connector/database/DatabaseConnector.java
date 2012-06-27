/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.database;

import com.lumens.connector.Configurable;
import com.lumens.connector.Connector;
import com.lumens.connector.Reader;
import com.lumens.connector.Writer;
import com.lumens.connector.database.client.oracle.OracleClient;
import com.lumens.model.Format;
import java.util.Map;

/**
 *
 * @author shaofeng wang
 */
public class DatabaseConnector implements Connector, Configurable
{
    public static final String OJDBC = "OJDBC";
    public static final String CONNECTION_URL = "ConnectionURL";
    public static final String USER = "User";
    public static final String PASSWORD = "Password";
    public static final String FULL_LOAD = "FullLoad";
    private Client dbClient;
    private Format tables;
    private String ojdbcURL;
    private String connURL;
    private String user;
    private String password;
    private boolean fullLoad;

    @Override
    public void open()
    {
        // TODO only create oracle client now, select different db client later
        dbClient = new OracleClient(ojdbcURL, connURL, user, password);
        dbClient.open();
        tables = dbClient.describeFormats(fullLoad);
    }

    @Override
    public void close()
    {
        if (dbClient != null)
            dbClient.close();
        tables = null;
    }

    @Override
    public Format getFormats()
    {
        return tables;
    }

    @Override
    public void describeFormat(Format format)
    {
        dbClient.describeFormat(format);
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
        if (configuration.containsKey(OJDBC))
            ojdbcURL = (String) configuration.get(OJDBC);
        if (configuration.containsKey(CONNECTION_URL))
            connURL = (String) configuration.get(CONNECTION_URL);
        if (configuration.containsKey(USER))
            user = (String) configuration.get(USER);
        if (configuration.containsKey(PASSWORD))
            password = (String) configuration.get(PASSWORD);
        if (configuration.containsKey(FULL_LOAD))
            fullLoad = (Boolean) configuration.get(FULL_LOAD);
    }

    @Override
    public Map<String, Object> getConfiguration()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
