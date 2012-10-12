package com.lumens.client.rpc.beans;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author shaofeng wang
 */
public class DataFormat implements Serializable, IsSerializable
{
    private String name;
    private ArrayList<DataFormat> children;
    private ArrayList<Entry<String, Object>> properties;

    public String getName()
    {
        return name;
    }

    public void addChild(DataFormat format)
    {
        if (children == null)
            children = new ArrayList<DataFormat>();
        children.add(format);
    }

    public boolean hasChildren()
    {
        return children != null && !children.isEmpty();
    }

    public ArrayList<DataFormat> getChildren()
    {
        return children;
    }

    public Object getProperty(String name)
    {
        for (Entry<String, Object> entry : properties)
        {
            if (entry.getKey().equals(name))
            {
                return entry.getValue();
            }
        }
        return null;
    }

    public void setProperty(String name, Object value)
    {
        if (properties == null)
            properties = new ArrayList<Entry<String, Object>>();
        for (Entry<String, Object> entry : properties)
        {
            if (entry.getKey().equals(name))
            {
                entry.setValue(value);
                return;
            }
        }
        properties.add(new Entry<String, Object>(name, value));
    }
}
