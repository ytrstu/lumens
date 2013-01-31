package com.lumens.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.lumens.client.rpc.beans.CProject;
import com.lumens.client.rpc.beans.CComponentTypeRegistry;
import com.lumens.client.rpc.beans.ProjectItem;

/**
 *
 * @author shaofeng wang
 */
public interface LumensServiceAsync
{
    public void getDataSourceCatalog(AsyncCallback<CComponentTypeRegistry[]> callback);

    public void getProcessorCatalog(AsyncCallback<CComponentTypeRegistry[]> callback);

    public void saveTransformProject(CProject project, AsyncCallback<Void> callback);

    public void getProjectList(AsyncCallback<ProjectItem[]> callback);

    public void openTransformProject(String projectName,
                                     AsyncCallback<CProject> callback);
}
