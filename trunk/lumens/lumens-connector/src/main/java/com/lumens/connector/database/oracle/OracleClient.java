/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.database.oracle;

import com.lumens.connector.database.AbstractClient;

/**
 *
 * @author shaofeng wang
 */
public class OracleClient extends AbstractClient
{
    public OracleClient(String connUrl, String user, String password)
    {
        super(connUrl, user, password);
    }
}
