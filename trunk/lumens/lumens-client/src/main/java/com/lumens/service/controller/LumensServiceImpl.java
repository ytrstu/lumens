package com.lumens.service.controller;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.lumens.client.constant.ViewConstants;
import com.lumens.client.rpc.LumensService;
import com.lumens.client.rpc.beans.ComponentRegistry;
import com.lumens.client.service.ComponentRegistryManager;
import com.lumens.connector.database.DatabaseConnector;
import com.lumens.connector.webservice.WebServiceConnector;
import com.lumens.processor.transform.TransformProcessor;
import com.lumens.service.config.DatasourceHelper;
import java.util.List;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

/**
 *
 * @author shaofeng wang
 */
public class LumensServiceImpl extends RemoteServiceServlet
        implements LumensService, ViewConstants
{
    private ComponentRegistryManager componentRegistryManager = new ComponentRegistryManager();

    @Override
    public void init(ServletConfig config) throws ServletException
    {
        super.init(config);
        try
        {
            List<ComponentRegistry> datasources = DatasourceHelper.
                    loadDatasouceConfiguration();
            for (ComponentRegistry registry : datasources)
                componentRegistryManager.registerDataSource(registry);

            // TODO
            componentRegistryManager.registerProcessor(
                    new ComponentRegistry(
                    transformPrID, "Transform", "transform.png",
                    "processor/32/transform.png",
                    TransformProcessor.class.getName()));
        } catch (Exception e)
        {
            throw new ServletException(e);
        }
    }

    @Override
    public ComponentRegistry[] getDataSourceCatalog()
    {
        return componentRegistryManager.getDataSourceList();
    }

    @Override
    public ComponentRegistry[] getProcessorCatalog()
    {
        return componentRegistryManager.getProcessorCatalog();
    }

    @Override
    public void createDataSource()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void createProcessor()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
