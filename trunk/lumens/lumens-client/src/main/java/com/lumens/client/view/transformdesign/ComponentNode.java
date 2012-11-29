package com.lumens.client.view.transformdesign;

import com.lumens.client.constant.ViewConstants;
import com.lumens.client.rpc.beans.ComponentRegistry;
import com.smartgwt.client.widgets.tree.TreeNode;

/**
 *
 * @author shaofeng wang
 */
public class ComponentNode extends TreeNode implements ViewConstants
{
    private String componentID;
    private String componentName;

    public ComponentNode(ComponentRegistry component)
    {
        this.componentID = component.getId();
        this.componentName = component.getName();
        super.setIcon(component.getIcon());
        setAttribute(COMPONENT_NAME, component.getName());
    }

    public String getComponentID()
    {
        return componentID;
    }

    public String getComponentName()
    {
        return componentName;
    }
}
