/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.database.client.oracle;

/**
 *
 * @author shaofeng wang
 */
public interface OracleConstants
{
    public String ORACLE_CLASS = "oracle.jdbc.OracleDriver";
    public String TYPE = "type";
    public String DESCRIPTION = "description";
    public String TABLENAMES = "select t.table_name,t.comments, t.TABLE_TYPE from user_tab_comments t";
}
