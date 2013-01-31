/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.client.rpc.beans;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author shaofeng wang (shaofeng.cjpw@gmail.com)
 */
public class CComponentTypeRegistry implements IsSerializable
{
    private String id;
    private String name;
    private String icon;
    private String instanceIcon;
    private List<CComponentParameter> parameters = new ArrayList<CComponentParameter>();
    private transient String className;

    public CComponentTypeRegistry()
    {
    }

    public CComponentTypeRegistry(CComponentTypeRegistry copy)
    {
        this.id = copy.getId();
        this.name = copy.getName();
        this.icon = copy.getIcon();
        this.instanceIcon = copy.getInstanceIcon();
        this.className = copy.getClassName();
        for (CComponentParameter p : copy.getParameters())
            this.parameters.add(p.copy());
    }

    public CComponentTypeRegistry(String id, String name, String icon,
                                  String instanceIcon, String className)
    {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.instanceIcon = instanceIcon;
        this.className = className;
    }

    public CComponentTypeRegistry copy()
    {
        CComponentTypeRegistry copyed = new CComponentTypeRegistry(this);
        return copyed;
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

    public String getInstanceIcon()
    {
        return instanceIcon;
    }

    public void setInstanceIcon(String instanceIcon)
    {
        this.instanceIcon = instanceIcon;
    }

    public String getClassName()
    {
        return className;
    }

    public void setClassName(String className)
    {
        this.className = className;
    }

    public void addParameter(CComponentParameter param)
    {
        parameters.add(param);
    }

    public List<CComponentParameter> getParameters()
    {
        return this.parameters;
    }
}
