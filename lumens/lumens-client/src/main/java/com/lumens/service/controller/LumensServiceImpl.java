package com.lumens.service.controller;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.lumens.client.rpc.DataSourceService;
import com.lumens.client.rpc.beans.ComponentRegistry;
import com.lumens.client.service.ComponentRegistryManager;
import com.lumens.client.view.ViewConstants;
import com.lumens.connector.database.DatabaseConnector;
import com.lumens.connector.webservice.WebServiceConnector;
import com.lumens.processor.transform.TransformProcessor;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

/**
 *
 * @author shaofeng wang
 */
public class LumensServiceImpl extends RemoteServiceServlet
        implements DataSourceService, ViewConstants
{
    private ComponentRegistryManager componentRegistryManager = new ComponentRegistryManager();

    @Override
    public void init(ServletConfig config) throws ServletException
    {
        super.init(config);
        componentRegistryManager.registerDataSource(new ComponentRegistry(
                webserviceID, "WebService(SOAP)", "soap.png",
                WebServiceConnector.class));
        componentRegistryManager.registerDataSource(
                new ComponentRegistry(
                databaseID, "Database", "database.png",
                DatabaseConnector.class));
        componentRegistryManager.registerProcessor(
                new ComponentRegistry(
                databaseID, "Transform", "transform.png",
                TransformProcessor.class));
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
    public void saveDataSource()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
