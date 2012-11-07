package com.lumens.client.rpc.beans;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

/**
 *
 * @author shaofeng wang
 */
public class ComponentRegistry implements Serializable, IsSerializable
{
    private String id;
    private String name;
    private String icon;
    private String componentIcon;
    private transient String className;

    public ComponentRegistry()
    {
    }

    public ComponentRegistry(String id, String name, String icon,
                             String componentIcon, String className)
    {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.componentIcon = componentIcon;
        this.className = className;
    }

    public String getID()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public String getIcon()
    {
        return icon;
    }

    public String getComponentIcon()
    {
        return componentIcon;
    }

    public String getComponentClass()
    {
        return className;
    }
}
