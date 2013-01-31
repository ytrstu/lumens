/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.service.controller;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.lumens.client.constant.ViewConstants;
import com.lumens.client.rpc.LumensService;
import com.lumens.client.rpc.beans.CComponentTypeRegistry;
import com.lumens.client.rpc.beans.CProject;
import com.lumens.client.rpc.beans.ProjectItem;
import com.lumens.client.service.CComponentTypeRegistryManager;
import com.lumens.engine.TransformProject;
import com.lumens.processor.transform.TransformProcessor;
import com.lumens.service.config.datasource.DatasourceHelper;
import com.lumens.service.config.project.ProjectManager;
import com.lumens.service.controller.utils.ServiceUtils;
import java.util.List;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

/**
 *
 * @author shaofeng wang (shaofeng.cjpw@gmail.com)
 */
public class LumensServiceImpl extends RemoteServiceServlet
        implements LumensService, ViewConstants
{
    private CComponentTypeRegistryManager componentRegistryManager = new CComponentTypeRegistryManager();
    private TransformProject currentProject;
    private ProjectManager projectManager = new ProjectManager();

    @Override
    public void init(ServletConfig config) throws ServletException
    {
        super.init(config);
        try
        {
            List<CComponentTypeRegistry> datasources = DatasourceHelper.
                    loadDatasouceConfiguration();
            for (CComponentTypeRegistry registry : datasources)
                componentRegistryManager.registerDataSource(registry);

            // TODO
            componentRegistryManager.registerProcessor(
                    new CComponentTypeRegistry(
                    transformPrID, "Transform", "transform.png",
                    "processor/32/transform.png",
                    TransformProcessor.class.getName()));
        } catch (Exception e)
        {
            throw new ServletException(e);
        }
    }

    @Override
    public CComponentTypeRegistry[] getDataSourceCatalog()
    {
        return componentRegistryManager.getDataSourceList();
    }

    @Override
    public CComponentTypeRegistry[] getProcessorCatalog()
    {
        return componentRegistryManager.getProcessorCatalog();
    }

    @Override
    public void saveTransformProject(CProject project)
    {
        currentProject = ServiceUtils.convertAsServerProject(project);
    }

    @Override
    public CProject openTransformProject(String projectName)
    {
        return ServiceUtils.convertAsClientProject(projectManager.openProject(projectName));
    }

    @Override
    public ProjectItem[] getProjectList()
    {
        return (ProjectItem[]) projectManager.getProjectList().toArray();
    }
}
