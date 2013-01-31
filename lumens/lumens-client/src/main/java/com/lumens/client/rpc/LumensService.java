package com.lumens.client.rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.lumens.client.rpc.beans.CProject;
import com.lumens.client.rpc.beans.CComponentTypeRegistry;
import com.lumens.client.rpc.beans.ProjectItem;

/**
 *
 * @author shaofeng wang
 */
@RemoteServiceRelativePath("service")
public interface LumensService extends RemoteService
{
    public CComponentTypeRegistry[] getDataSourceCatalog();

    public CComponentTypeRegistry[] getProcessorCatalog();

    public void saveTransformProject(CProject project);

    public ProjectItem[] getProjectList();

    public CProject openTransformProject(String projectName);
}