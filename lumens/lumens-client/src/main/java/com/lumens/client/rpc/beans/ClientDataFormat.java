package com.lumens.client.rpc.beans;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.util.ArrayList;

/**
 *
 * @author shaofeng wang
 */
public class ClientDataFormat implements IsSerializable
{
    private String name;
    private ArrayList<ClientDataFormat> children;
    private ArrayList<Entry<String, String>> properties;

    public String getName()
    {
        return name;
    }

    public void addChild(ClientDataFormat format)
    {
        if (children == null)
            children = new ArrayList<ClientDataFormat>();
        children.add(format);
    }

    public boolean hasChildren()
    {
        return children != null && !children.isEmpty();
    }

    public ArrayList<ClientDataFormat> getChildren()
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
