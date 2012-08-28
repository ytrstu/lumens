/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lumens.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.lumens.client.rpc.beans.ComponentRegistry;

/**
 *
 * @author washaofe
 */
public interface DataSourceServiceAsync
{
    public void getDataSourceCatalog(AsyncCallback<ComponentRegistry[]> callback);

    public void getProcessorCatalog(AsyncCallback<ComponentRegistry[]> callback);

    public void saveDataSource(AsyncCallback<String> callback);
}
