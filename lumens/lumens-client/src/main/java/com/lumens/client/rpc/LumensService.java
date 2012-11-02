package com.lumens.client.rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.lumens.client.rpc.beans.ComponentRegistry;

/**
 *
 * @author shaofeng wang
 */
@RemoteServiceRelativePath("service")
public interface LumensService extends RemoteService
{
    public ComponentRegistry[] getDataSourceCatalog();
    public ComponentRegistry[] getProcessorCatalog();
    public void saveDataSource();
}