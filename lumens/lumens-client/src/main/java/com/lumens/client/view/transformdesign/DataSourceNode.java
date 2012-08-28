/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lumens.client.view.transformdesign;

import com.lumens.client.view.ViewConstants;
import com.smartgwt.client.widgets.tree.TreeNode;

/**
 *
 * @author washaofe
 */
public class DataSourceNode extends TreeNode implements ViewConstants
{
    private String dataSourceID;
    private String dataSourceName;

    public DataSourceNode(String dataSourceID, String dataSourceName,
                          String icon)
    {
        this.dataSourceID = dataSourceID;
        this.dataSourceName = dataSourceName;
        super.setIcon(icon);
        setAttribute(DATASOURCE_NAME, dataSourceName);
    }

    public String getDataSourceID()
    {
        return dataSourceID;
    }

    public String getDataSourceName()
    {
        return dataSourceName;
    }
}
