package com.lumens.client.rpc.beans;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author shaofeng wang
 */
public class ComponentRegistry implements IsSerializable
{
    private String id;
    private String name;
    private String icon;
    private String componentIcon;
    private List<ComponentParameter> parameters = new ArrayList<ComponentParameter>();
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

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getIcon()
    {
        return icon;
    }

    public void setIcon(String icon)
    {
        this.icon = icon;
    }

    public String getComponentIcon()
    {
        return componentIcon;
    }

    public void setComponentIcon(String componentIcon)
    {
        this.componentIcon = componentIcon;
    }

    public String getClassName()
    {
        return className;
    }

    public void setClassName(String className)
    {
        this.className = className;
    }

    public void addParameter(ComponentParameter param)
    {
        parameters.add(param);
    }

    public List<ComponentParameter> getParameters()
    {
        return this.parameters;
    }
}
