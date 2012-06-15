/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author shaofeng wang
 */
public class DataElement implements Element
{
    protected Map<String, Element> children;
    protected List<Element> childrenList;
    protected List<Element> arrayItems;
    protected Format format;
    protected Object value;
    private Element parent;
    private int level = 0;
    private boolean isArrayItem;

    public DataElement(Format format)
    {
        this.format = format;
    }

    @Override
    public int getLevel()
    {
        return level;
    }

    @Override
    public void removeChild(Element child)
    {
        if (isField())
            throw new RuntimeException("Error, the data node is field");

        if (isStruct())
        {
            Element removed = children.remove(child.getFormat().getName());
            if (removed != null)
                childrenList.remove(removed);
        }
        else
            arrayItems.remove(child);
    }

    @Override
    public Element newChild(Format child)
    {
        Element elem = new DataElement(child);
        elem.setParent(this);
        return elem;
    }

    @Override
    public Element addChild(String name)
    {
        Format child = format.getChild(name);
        return addChild(new DataElement(child));
    }

    @Override
    public Element addChild(Element child)
    {
        if (isArray())
            throw new RuntimeException("Error, the data node is an array, it is not an array item");
        if (children == null)
        {
            children = new HashMap<String, Element>();
            childrenList = new ArrayList<Element>();
        }
        String name = child.getFormat().getName();
        if (children.containsKey(name))
            throw new IllegalArgumentException("Duplicate child \"" + format.getName() + "\"");
        child.setParent(this);
        children.put(name, child);
        childrenList.add(child);
        return child;
    }

    @Override
    public Element addArrayItem()
    {
        return addArrayItem(newArrayItem());
    }

    @Override
    public Element addArrayItem(Element item)
    {
        if (!isArray())
            throw new RuntimeException("Error, the data node is not an array");

        if (arrayItems == null)
            arrayItems = new ArrayList<Element>();
        item.setParent(this);
        arrayItems.add(item);
        return item;
    }

    @Override
    public Element newArrayItem()
    {
        if (!isArray())
            throw new RuntimeException("Error, the node type is not an array");
        DataElement data = new DataElement(format);
        data.isArrayItem = true;
        return data;
    }

    private List<Element> getArrayItems()
    {
        return arrayItems;
    }

    @Override
    public void setParent(Element parent)
    {
        this.level = parent.isArray() ? parent.getLevel() : parent.getLevel() + 1;
        this.parent = parent;
    }

    @Override
    public Element getParent()
    {
        return parent;
    }

    @Override
    public Element getChild(String name)
    {
        return children.get(name);
    }

    @Override
    public Element getChildByPath(String path)
    {
        return getChildByPath(new AccessPath(path));
    }

    @Override
    public Element getChildByPath(Path path)
    {
        Element child = this;
        PathToken token = null;
        List<Element> items = null;
        Iterator<PathToken> it = path.iterator();
        while (it.hasNext())
        {
            token = it.next();
            child = child.getChild(token.toString());
            if (child != null && (token.isIndexed() || child.isArray() && it.hasNext()))
            {
                items = child.getChildren();
                if (items == null)
                    throw new IllegalArgumentException("Error path \"" + path.toString() + "\"");
                child = items.get(token.isIndexed() ? token.index() : 0);
            }
        }

        return child;
    }

    @Override
    public List<Element> getChildren()
    {
        if (isArray())
            return getArrayItems();
        return childrenList;
    }

    @Override
    public Format getFormat()
    {
        return format;
    }

    @Override
    public Object getValue()
    {
        return value;
    }

    @Override
    public void setValue(Object value)
    {
        if ((isShort() && value instanceof Short)
            || (isInt() && value instanceof Integer)
            || (isLong() && value instanceof Long)
            || (isFloat() && value instanceof Float)
            || (isDouble() && value instanceof Double)
            || (isDate() && value instanceof Date)
            || (isBinary() && value instanceof byte[])
            || (isString() && value instanceof String))
            this.value = value;
        else
            throw new IllegalArgumentException("Error, date type is \"" + format.getType().name() + "\"," + " value type is not correct !");
    }

    @Override
    public void setValue(short value)
    {
        if (!isShort())
            throw new IllegalArgumentException("Error, data type is not short !");
        this.value = value;
    }

    @Override
    public void setValue(int value)
    {
        if (!isInt())
            throw new IllegalArgumentException("Error, data type is not int !");
        this.value = value;
    }

    @Override
    public void setValue(long value)
    {
        if (!isLong())
            throw new IllegalArgumentException("Error, data type is not long !");
        this.value = value;
    }

    @Override
    public void setValue(float value)
    {
        if (!isFloat())
            throw new IllegalArgumentException("Error, data type is not float !");
        this.value = value;
    }

    @Override
    public void setValue(double value)
    {
        if (!isDouble())
            throw new IllegalArgumentException("Error, data type is not double !");
        this.value = value;
    }

    @Override
    public void setValue(byte[] value)
    {
        if (!isBinary())
            throw new IllegalArgumentException("Error, data type is not binary !");
        this.value = value;
    }

    @Override
    public void setValue(Date value)
    {
        if (!isDate())
            throw new IllegalArgumentException("Error, data type is not date time !");
        this.value = value;
    }

    @Override
    public void setValue(String value)
    {
        if (!isString())
            throw new IllegalArgumentException("Error, data type is not string !");
        this.value = value;
    }

    @Override
    public short getShort()
    {
        return (Short) value;
    }

    @Override
    public int getInt()
    {
        return (Integer) value;
    }

    @Override
    public long getLong()
    {
        return (Long) value;
    }

    @Override
    public float getFloat()
    {
        return (Float) value;
    }

    @Override
    public double getDouble()
    {
        return (Double) value;
    }

    @Override
    public byte[] getBinary()
    {
        return (byte[]) value;
    }

    @Override
    public String getString()
    {
        return (String) value;
    }

    @Override
    public Date getDate()
    {
        return (Date) value;
    }

    @Override
    public boolean isShort()
    {
        return format.getType() == Type.SHORT;
    }

    @Override
    public boolean isInt()
    {
        return format.getType() == Type.INT;
    }

    @Override
    public boolean isLong()
    {
        return format.getType() == Type.LONG;
    }

    @Override
    public boolean isFloat()
    {
        return format.getType() == Type.FLOAT;
    }

    @Override
    public boolean isDouble()
    {
        return format.getType() == Type.DOUBLE;
    }

    @Override
    public boolean isBinary()
    {
        return format.getType() == Type.BINARY;
    }

    @Override
    public boolean isDate()
    {
        return format.getType() == Type.DATE;
    }

    @Override
    public boolean isString()
    {
        return format.getType() == Type.STRING;
    }

    @Override
    public boolean isField()
    {
        return format.isField();
    }

    @Override
    public boolean isStruct()
    {
        return format.isStruct() || isArrayItem();
    }

    @Override
    public boolean isArray()
    {
        return format.isArray() && !isArrayItem();
    }

    @Override
    public boolean isArrayItem()
    {
        return isArrayItem;
    }
}