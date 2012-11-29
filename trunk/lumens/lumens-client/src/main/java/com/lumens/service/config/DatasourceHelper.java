/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.service.config;

import com.lumens.client.rpc.beans.ComponentRegistry;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author shaofeng wang (shaofeng.cjpw@gmail.com)
 */
public class DatasourceHelper
{
    public static List<ComponentRegistry> loadDatasouceConfiguration() throws Exception
    {
        List<ComponentRegistry> datasourceConfig = new ArrayList<ComponentRegistry>();
        DatasourceParser.parse(DatasourceParser.class.getResource(
                "datasource.xml"), new DatasourceHandlerImpl(datasourceConfig));
        return datasourceConfig;
    }
}
