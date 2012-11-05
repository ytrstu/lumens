/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.database;

import com.lumens.connector.Connector;
import com.lumens.connector.Operation;
import com.lumens.connector.Usage;
import com.lumens.connector.database.client.oracle.OracleClient;
import com.lumens.model.Format;
import java.util.Map;

/**
 *
 * @author shaofeng wang
 */
public class DatabaseConnector implements Connector
{
    public static final String OJDBC = "OJDBC";
    public static final String CONNECTION_URL = "ConnectionURL";
    public static final String USER = "User";
    public static final String PASSWORD = "Password";
    public static final String FULL_LOAD = "FullLoad";
    private Client dbClient;
    private Map<String, Format> tables;
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
        tables = dbClient.getFormatList(fullLoad);
    }

    @Override
    public void close()
    {
        if (dbClient != null)
            dbClient.close();
        tables = null;
    }

    @Override
    public Map<String, Format> getFormatList(Usage use)
    {
        return tables;
    }

    @Override
    public Format getFormat(Format format, String path, Usage use)
    {
        return dbClient.getFormat(format);
    }

    @Override
    public Operation getOperation()
    {
        return new DatabaseOperation(dbClient);
    }

    @Override
    public void setParameters(Map<String, Object> parameters)
    {
        if (parameters.containsKey(OJDBC))
            ojdbcURL = (String) parameters.get(OJDBC);
        if (parameters.containsKey(CONNECTION_URL))
            connURL = (String) parameters.get(CONNECTION_URL);
        if (parameters.containsKey(USER))
            user = (String) parameters.get(USER);
        if (parameters.containsKey(PASSWORD))
            password = (String) parameters.get(PASSWORD);
        if (parameters.containsKey(FULL_LOAD))
            fullLoad = (Boolean) parameters.get(FULL_LOAD);
    }
}
