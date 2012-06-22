/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.database;

import com.lumens.connector.Client;
import com.lumens.model.Format;
import java.sql.Connection;
import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.impl.GenericObjectPool;

/**
 *
 * @author shaofeng wang
 */
public abstract class AbstractClient implements Client
{
    private PoolingDataSource dataSource;
    private String user;
    private String password;

    public AbstractClient(String connUrl, String user, String password)
    {
        ObjectPool connectionPool = new GenericObjectPool();
        ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(connUrl, null);
        new PoolableConnectionFactory(connectionFactory, connectionPool, null, null, false, true);
        dataSource = new PoolingDataSource(connectionPool);
        this.user = user;
        this.password = password;
    }

    @Override
    public Format describeFormats()
    {
        Connection conn = null;
        try
        {
            conn = DbUtils.getConnection(dataSource, user, password);
        }
        finally
        {
            DbUtils.releaseConnection(conn);
        }
        return null;
    }
}
