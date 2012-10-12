package com.lumens.client.demo;

import com.smartgwt.client.widgets.tree.TreeNode;

/**
 *
 * @author Shaofeng Wang
 */
public class AgentTreeNode extends TreeNode
{
    public AgentTreeNode(int id, String name, boolean isOpen, String icon, int scncount,
                         AgentTreeNode... children)
    {
        setAttribute("id", id);
        setAttribute("name", name);
        setAttribute("isOpen", isOpen);
        if (icon != null)
        {
            setIcon(icon);
        }
        setAttribute("scncount", scncount);
        setAttribute("children", children);
    }

    public AgentTreeNode(int id, String name, boolean isOpen, String icon)
    {
        setAttribute("id", id);
        setAttribute("name", name);
        setIcon(icon);
        setAttribute("isOpen", isOpen);
    }
}
