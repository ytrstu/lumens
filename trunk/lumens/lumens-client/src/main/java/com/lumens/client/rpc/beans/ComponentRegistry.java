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
    private transient Class<?> clazz;

    public ComponentRegistry()
    {
    }

    public ComponentRegistry(String id, String name, String icon,
                             String componentIcon, Class<?> clazz)
    {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.componentIcon = componentIcon;
        this.clazz = clazz;
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

    public Class<?> getComponentClass()
    {
        return clazz;
    }
}
