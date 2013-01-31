package com.lumens.client.service;

import com.lumens.client.rpc.beans.CComponentTypeRegistry;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author shaofeng wang (shaofeng.cjpw@gmail.com)
 */
public class CComponentTypeRegistryManager
{
    private Map<String, CComponentTypeRegistry> datasourceCatalog = new HashMap<String, CComponentTypeRegistry>();
    private Map<String, CComponentTypeRegistry> processorCatalog = new HashMap<String, CComponentTypeRegistry>();

    public void registerDataSource(CComponentTypeRegistry connectorRegistry)
    {
        datasourceCatalog.put(connectorRegistry.getId(), connectorRegistry);
    }

    public void registerProcessor(CComponentTypeRegistry connectorRegistry)
    {
        processorCatalog.put(connectorRegistry.getId(), connectorRegistry);
    }

    public int getDataSourceCount()
    {
        return datasourceCatalog.size();
    }

    public int getProcessorCount()
    {
        return processorCatalog.size();
    }

    public CComponentTypeRegistry[] getDataSourceList()
    {
        CComponentTypeRegistry[] registryArray = new CComponentTypeRegistry[datasourceCatalog.
                size()];
        datasourceCatalog.values().toArray(registryArray);
        return registryArray;
    }

    public CComponentTypeRegistry[] getProcessorCatalog()
    {
        CComponentTypeRegistry[] registryArray = new CComponentTypeRegistry[processorCatalog.
                size()];
        processorCatalog.values().toArray(registryArray);
        return registryArray;
    }

    public CComponentTypeRegistry lookupDataSource(String id)
    {
        return datasourceCatalog.get(id);
    }

    public CComponentTypeRegistry lookupProcessor(String id)
    {
        return processorCatalog.get(id);
    }
}
