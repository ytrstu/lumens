package com.lumens.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.lumens.client.rpc.beans.ComponentRegistry;

/**
 *
 * @author shaofeng wang
 */
public interface DataSourceServiceAsync
{
    public void getDataSourceCatalog(AsyncCallback<ComponentRegistry[]> callback);

    public void getProcessorCatalog(AsyncCallback<ComponentRegistry[]> callback);

    public void saveDataSource(AsyncCallback<String> callback);
}
