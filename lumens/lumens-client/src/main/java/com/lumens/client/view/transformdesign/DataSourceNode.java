package com.lumens.client.view.transformdesign;

import com.lumens.client.rpc.beans.ComponentRegistry;
import com.lumens.client.view.ViewConstants;
import com.smartgwt.client.widgets.tree.TreeNode;

/**
 *
 * @author shaofeng wang
 */
public class DataSourceNode extends TreeNode implements ViewConstants
{
    private String dataSourceID;
    private String dataSourceName;

    public DataSourceNode(ComponentRegistry component)
    {
        this.dataSourceID = component.getID();
        this.dataSourceName = component.getName();
        super.setIcon(component.getIcon());
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
