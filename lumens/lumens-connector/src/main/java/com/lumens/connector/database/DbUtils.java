/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.dbcp.PoolingDataSource;

/**
 *
 * @author shaofeng wang
 */
public class DbUtils
{
    public static void releaseConnection(Connection conn)
    {
        if (conn != null)
        {
            try
            {
                conn.close();
            }
            catch (SQLException ex)
            {
            }
        }
    }

    public static Connection getConnection(PoolingDataSource dataSource, String user,
                                           String password)
    {
        try
        {
            return dataSource.getConnection(user, password);
        }
        catch (SQLException ex)
        {
            throw new RuntimeException(ex.getMessage());
        }
    }
}
