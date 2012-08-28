/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lumens.client.rpc.beans;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

/**
 *
 * @author washaofe
 */
public class ComponentRegistry implements Serializable, IsSerializable
{
    private String id;
    private String name;
    private String icon;
    private transient Class<?> clazz;

    public ComponentRegistry()
    {
    }

    public ComponentRegistry(String id, String name, String icon, Class<?> clazz)
    {
        this.id = id;
        this.name = name;
        this.icon = icon;
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

    public Class<?> getConnectorClass()
    {
        return clazz;
    }
}
