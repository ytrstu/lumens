/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.service.config.datasource;

import com.lumens.client.rpc.beans.CComponentTypeRegistry;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author shaofeng wang (shaofeng.cjpw@gmail.com)
 */
public class DatasourceHelper
{
    public static List<CComponentTypeRegistry> loadDatasouceConfiguration() throws Exception
    {
        List<CComponentTypeRegistry> datasourceConfig = new ArrayList<CComponentTypeRegistry>();
        DatasourceParser.parse(DatasourceHelper.class.getResource("datasource.xml"),
                               new DatasourceHandlerImpl(datasourceConfig));
        return datasourceConfig;
    }
}
