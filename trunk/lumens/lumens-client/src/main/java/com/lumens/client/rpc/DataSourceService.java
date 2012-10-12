package com.lumens.client.rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.lumens.client.rpc.beans.ComponentRegistry;

/**
 *
 * @author shaofeng wang
 */
@RemoteServiceRelativePath("datasource")
public interface DataSourceService extends RemoteService
{
    public ComponentRegistry[] getDataSourceCatalog();
    public ComponentRegistry[] getProcessorCatalog();
    public void saveDataSource();
}