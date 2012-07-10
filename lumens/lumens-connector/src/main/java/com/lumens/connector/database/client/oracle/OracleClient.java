/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.database.client.oracle;

import com.lumens.connector.database.DbUtils;
import com.lumens.connector.database.client.AbstractClient;
import com.lumens.model.DataFormat;
import com.lumens.model.Format;
import com.lumens.model.Format.Form;
import com.lumens.model.Type;
import java.sql.PreparedStatement;
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
    public Format getFormats(boolean fullLoad)
    {
        Format tables = new DataFormat("Tables", Form.STRUCT);
        Statement stat = null;
        PreparedStatement preparedStat = null;
        ResultSet ret = null;
        ResultSet preparedRet = null;
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
                if (fullLoad)
                {
                    preparedStat = conn.prepareStatement(TABLECOLUMNS + '?');
                    for (Format format : tables.getChildren())
                    {
                        preparedStat.setString(1, format.getName());
                        preparedRet = preparedStat.executeQuery();
                        if (!preparedRet.isClosed())
                        {
                            while (preparedRet.next())
                            {
                                String columnName = preparedRet.getString(1);
                                String dataType = preparedRet.getString(2);
                                String dataLength = preparedRet.getString(3);
                                Format table = format.addChild(columnName, Form.FIELD, toType(
                                        dataType));
                                table.setProperty(DATA_TYPE, dataType);
                                table.setProperty(DATA_LENGTH, dataLength);
                            }
                        }
                    }
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
            DbUtils.releaseResultSet(preparedRet);
            DbUtils.releaseStatement(preparedStat);
        }
        return tables;
    }

    @Override
    public Format getFormat(Format format)
    {
        Statement stat = null;
        ResultSet ret = null;
        try
        {
            stat = conn.createStatement();
            ret = stat.executeQuery(TABLECOLUMNS + '\'' + format.getName() + '\'');
            if (!ret.isClosed())
            {
                while (ret.next())
                {
                    String columnName = ret.getString(1);
                    String dataType = ret.getString(2);
                    String dataLength = ret.getString(3);
                    Format table = format.addChild(columnName, Form.FIELD, toType(dataType));
                    table.setProperty(DATA_TYPE, dataType);
                    table.setProperty(DATA_LENGTH, dataLength);
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

        return format;
    }

    private Type toType(String dataType)
    {
        if (dataType.equalsIgnoreCase(CHAR)
            || dataType.startsWith(VARCHAR2) || dataType.startsWith(NVARCHAR2) || dataType.
                equalsIgnoreCase(CLOB))
            return Type.STRING;
        else if (dataType.startsWith(NUMBER))
            return Type.INT;
        else if (dataType.equalsIgnoreCase(DATE))
            return Type.DATE;
        else if (dataType.startsWith(NUMBERIC))
            return Type.DOUBLE;
        else if (dataType.equalsIgnoreCase(BLOB))
            return Type.BINARY;
        return Type.NONE;
    }
}
