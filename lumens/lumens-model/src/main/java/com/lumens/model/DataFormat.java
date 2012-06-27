/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author shaofeng wang
 */
public class DataFormat implements Format
{
    protected Map<String, Object> properties;
    protected Map<String, Format> children;
    protected List<Format> childrenList;
    private String name;
    private Type type = Type.NONE;
    private Form form = Form.NONE;
    private Format parent;

    public DataFormat()
    {
    }

    public DataFormat(String name)
    {
        this.name = name;
    }

    public DataFormat(String name, Form form)
    {
        this.name = name;
        this.form = form;
    }

    public DataFormat(String name, Form form, Type type)
    {
        this.name = name;
        this.form = form;
        this.type = type;
    }

    @Override
    public Type getType()
    {
        return type;
    }

    @Override
    public Form getForm()
    {
        return form;
    }

    @Override
    public void setType(Type type)
    {
        this.type = type;
    }

    @Override
    public void setForm(Form form)
    {
        this.form = form;
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public void setName(String name)
    {
        this.name = name;
    }

    @Override
    public void setProperty(String name, Object value)
    {
        if (properties == null)
            properties = new HashMap<String, Object>();
        properties.put(name, value);
    }

    @Override
    public Object getProperty(String name)
    {
        return properties == null ? null : properties.get(name);
    }

    @Override
    public Map<String, Object> getProperties()
    {
        return properties;
    }

    @Override
    public Format addChild(Format format)
    {
        if (children == null && childrenList == null)
        {
            children = new HashMap<String, Format>();
            childrenList = new ArrayList<Format>();
        }
        else if (children.containsKey(format.getName()))
            throw new IllegalArgumentException("Duplicate child \"" + format.getName() + "\"");
        format.setParent(this);

        children.put(format.getName(), format);
        childrenList.add(format);
        return format;
    }

    @Override
    public Format addChild(String name, Form form, Type type)
    {
        return addChild(new DataFormat(name, form, type));
    }

    @Override
    public Format addChild(String name, Form form)
    {
        return addChild(new DataFormat(name, form, Type.NONE));
    }

    @Override
    public Format getChild(String name)
    {
        return children.get(name);
    }

    @Override
    public List<Format> getChildren()
    {
        return childrenList;
    }

    @Override
    public Path getFullPath()
    {
        Path fullPath = new AccessPath((String) null);
        if (parent != null)
        {
            Format format = this;
            while (format.getParent() != null)
            {
                fullPath.addLeft(format.getName());
                format = format.getParent();
            }
        }
        return fullPath;
    }

    @Override
    public Format getParent()
    {
        return parent;
    }

    @Override
    public void setParent(Format parent)
    {
        this.parent = parent;
    }

    @Override
    public boolean isField()
    {
        return form == Form.FIELD;
    }

    @Override
    public boolean isStruct()
    {
        return form == Form.STRUCT;
    }

    @Override
    public boolean isArray()
    {
        return form == Form.ARRAY;
    }
}
