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
  protected Map<String, Element> children = new HashMap<String, Element>();
  protected List<Element> childrenList = new ArrayList<Element>();
  protected List<Element> arrayItems;
  protected Format format;
  protected Object value;
  private Element parent;
  private int index = -1;
  private boolean isArrayItem;

  public DataElement(Format format)
  {
    this.format = format;
  }

  @Override
  public int index()
  {
    return index;
  }

  @Override
  public void removeChild(Element data)
  {
    Element removed = children.remove(data.getFormat().getName());
    if (removed != null)
      childrenList.remove(removed);
  }

  @Override
  public Element addChild(String name)
  {
    Format child = format.getChild(name);
    return addChild(new DataElement(child));
  }

  @Override
  public Element addChild(Element data)
  {
    if (isArray())
      throw new RuntimeException("Error, the data node is an array, it is not an array item");
    String name = data.getFormat().getName();
    if (children.containsKey(name))
      throw new IllegalArgumentException("Duplicate child \"" + format.getName() + "\"");
    data.setParent(this);
    children.put(name, data);
    childrenList.add(data);
    return data;
  }

  @Override
  public void setParent(Element parent)
  {
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
    return getChildByPath(new ElementPath(path));
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
      if (token.isIndexed() || child.isArray())
      {
        items = child.getArrayItems();
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
    return childrenList;
  }

  @Override
  public List<Element> getSlibling()
  {
    throw new UnsupportedOperationException("Not supported yet.");
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
    this.value = value;
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
  public Element addArrayItem()
  {
    if (!format.isArray())
      throw new RuntimeException("Error, the node type is not an array");
    DataElement data = new DataElement(format);
    if (arrayItems == null)
      arrayItems = new ArrayList<Element>();
    data.setParent(this);
    data.index = arrayItems.size();
    data.isArrayItem = true;
    this.arrayItems.add(data);
    return data;
  }

  @Override
  public List<Element> getArrayItems()
  {
    return arrayItems;
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
