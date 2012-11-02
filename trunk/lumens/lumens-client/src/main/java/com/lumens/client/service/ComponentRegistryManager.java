package com.lumens.client.service;

import com.lumens.client.rpc.beans.ComponentRegistry;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author shaofeng wang
 */
public class ComponentRegistryManager
{
    private Map<String, ComponentRegistry> datasourceCatalog = new HashMap<String, ComponentRegistry>();
    private Map<String, ComponentRegistry> processorCatalog = new HashMap<String, ComponentRegistry>();

    public void registerDataSource(ComponentRegistry connectorRegistry)
    {
        datasourceCatalog.put(connectorRegistry.getID(), connectorRegistry);
    }

    public void registerProcessor(ComponentRegistry connectorRegistry)
    {
        processorCatalog.put(connectorRegistry.getID(), connectorRegistry);
    }

    public int getDataSourceCount()
    {
        return datasourceCatalog.size();
    }

    public int getProcessorCount()
    {
        return processorCatalog.size();
    }

    public ComponentRegistry[] getDataSourceList()
    {
        ComponentRegistry[] registryArray = new ComponentRegistry[datasourceCatalog.
                size()];
        datasourceCatalog.values().toArray(registryArray);
        return registryArray;
    }

    public ComponentRegistry[] getProcessorCatalog()
    {
        ComponentRegistry[] registryArray = new ComponentRegistry[processorCatalog.
                size()];
        processorCatalog.values().toArray(registryArray);
        return registryArray;
    }

    public ComponentRegistry lookupDataSource(String id)
    {
        return datasourceCatalog.get(id);
    }

    public ComponentRegistry lookupProcessor(String id)
    {
        return processorCatalog.get(id);
    }
}
