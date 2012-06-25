/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.database.client.oracle;

import com.lumens.connector.database.client.AbstractClient;
import com.lumens.connector.database.DbUtils;
import com.lumens.model.DataFormat;
import com.lumens.model.Format;
import com.lumens.model.Format.Form;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author shaofeng wang
 */
public class OracleClient extends AbstractClient implements OracleConstants
{


    public OracleClient(String ojdbcURL, String connURL, String user, String password)
    {
        super(ojdbcURL, ORACLE_CLASS, connURL, user, password);
    }

    @Override
    public void open()
    {
        conn = DbUtils.getConnection(driver, connURL, user, password);
    }

    @Override
    public void close()
    {
        DbUtils.releaseConnection(conn);
    }

    @Override
    public Format describeFormats()
    {
        Format tables = new DataFormat("Tables", Form.STRUCT);
        Statement stat = null;
        ResultSet ret = null;
        try
        {
            stat = conn.createStatement();
            ret = stat.executeQuery(TABLENAMES);
            if (!ret.isClosed())
            {
                while (ret.next())
                {
                    String tableName = ret.getString(1);
                    String description = ret.getString(2);
                    String type = ret.getString(3);
                    Format table = tables.addChild(tableName, Form.STRUCT);
                    table.setProperty(DESCRIPTION, description);
                    table.setProperty(TYPE, type);
                }
            }
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
        finally
        {
            DbUtils.releaseResultSet(ret);
            DbUtils.releaseStatement(stat);
        }
        return tables;
    }
}
