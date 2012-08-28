/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lumens.service;

import com.lumens.client.rpc.beans.ComponentRegistry;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author washaofe
 */
public class ComponentRegistryManager
{
    private Map<String, ComponentRegistry> datasourceCatalog = new HashMap<String, ComponentRegistry>();
    private Map<String, ComponentRegistry> processorCatalog = new HashMap<String, ComponentRegistry>();

    public void registerDataSource(String id,
                                   ComponentRegistry connectorRegistry)
    {
        datasourceCatalog.put(id, connectorRegistry);
    }

    public void registerProcessor(String id, ComponentRegistry connectorRegistry)
    {
        processorCatalog.put(id, connectorRegistry);
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
}
