/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.client.rpc.beans;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.util.ArrayList;

/**
 *
 * @author shaofeng wang
 */
public class CFormat implements IsSerializable
{
    private String name;
    private ArrayList<CFormat> children;
    private ArrayList<Entry<String, String>> properties;

    public String getName()
    {
        return name;
    }

    public void addChild(CFormat format)
    {
        if (children == null)
            children = new ArrayList<CFormat>();
        children.add(format);
    }

    public boolean hasChildren()
    {
        return children != null && !children.isEmpty();
    }

    public ArrayList<CFormat> getChildren()
    {
        return children;
    }

    public String getProperty(String name)
    {
        for (Entry<String, String> entry : properties)
        {
            if (entry.getKey().equals(name))
            {
                return entry.getValue();
            }
        }
        return null;
    }

    public void setProperty(String name, String value)
    {
        if (properties == null)
            properties = new ArrayList<Entry<String, String>>();
        for (Entry<String, String> entry : properties)
        {
            if (entry.getKey().equals(name))
            {
                entry.setValue(value);
                return;
            }
        }
        properties.add(new Entry<String, String>(name, value));
    }
}
