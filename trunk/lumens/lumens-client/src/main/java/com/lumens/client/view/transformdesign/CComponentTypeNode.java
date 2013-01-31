/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.client.view.transformdesign;

import com.lumens.client.constant.ViewConstants;
import com.lumens.client.rpc.beans.CComponentTypeRegistry;
import com.smartgwt.client.widgets.tree.TreeNode;

/**
 *
 * @author shaofeng wang (shaofeng.cjpw@gmail.com)
 */
public class CComponentTypeNode extends TreeNode implements ViewConstants
{
    private String componentTypeID;
    private String componentName;

    public CComponentTypeNode(CComponentTypeRegistry component)
    {
        this.componentTypeID = component.getId();
        this.componentName = component.getName();
        super.setIcon(component.getIcon());
        setAttribute(COMPONENT_NAME, component.getName());
    }

    public String getComponentID()
    {
        return componentTypeID;
    }

    public String getComponentName()
    {
        return componentName;
    }
}
