package com.lumens.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.lumens.client.rpc.beans.ClientTransformProject;
import com.lumens.client.rpc.beans.ComponentRegistry;

/**
 *
 * @author shaofeng wang
 */
public interface LumensServiceAsync
{
    public void getDataSourceCatalog(AsyncCallback<ComponentRegistry[]> callback);

    public void getProcessorCatalog(AsyncCallback<ComponentRegistry[]> callback);

    public void saveTransformProject(ClientTransformProject project,
                                     AsyncCallback<Void> callback);

    public void openTransformProject(
            AsyncCallback<ClientTransformProject> callback);
}
