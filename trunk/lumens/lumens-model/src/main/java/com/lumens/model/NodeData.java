/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lumens.model;

import com.lumens.model.Format.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author shaofeng wang
 */
public class NodeData implements Data
{
  protected Map<String, Data> children = new HashMap<String, Data>();
  protected List<Data> childrenList = new ArrayList<Data>();
  protected List<Data> arrayItems;
  protected Format format;
  protected Object value;
  private Data parent;

  public NodeData(Format format)
  {
    this.format = format;
  }

  @Override
  public int index()
  {
    return -1;
  }

  @Override
  public void removeChild(Data data)
  {
    Data removed = children.remove(data.getFormat().getName());
    if (removed != null)
    {
      childrenList.remove(removed);
    }
  }

  @Override
  public Data addChild(Data data)
  {
    String name = data.getFormat().getName();
    if (children.containsKey(name))
    {
      throw new IllegalArgumentException("Duplicate child \"" + format.getName() + "\"");
    }
    data.setParent(this);
    children.put(name, data);
    childrenList.add(data);
    return data;
  }

  @Override
  public void setParent(Data parent)
  {
    this.parent = parent;
  }

  @Override
  public Data getParent()
  {
    return parent;
  }

  @Override
  public List<Data> getChildren()
  {
    return childrenList;
  }

  @Override
  public List<Data> getSlibling()
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
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void setValue(Object value)
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void setValue(short value)
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void setValue(int value)
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void setValue(long value)
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void setValue(float value)
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void setValue(double value)
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void setValue(byte[] value)
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void setValue(Date value)
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void setValue(String value)
  {
    if (format.getType() != Type.STRING)
    {
      throw new IllegalArgumentException("Error, data type is not string !");
    }
    this.value = value;
  }

  @Override
  public short getShort()
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public int getInt()
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public long getLong()
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public float getFloat()
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public double getDouble()
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public byte[] getBytes()
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public String getString()
  {
    if (format.getType() != Type.STRING)
    {
      throw new RuntimeException("The value type is not string");
    }
    return (String) value;
  }

  @Override
  public Date getDate()
  {
    throw new UnsupportedOperationException("Not supported yet.");
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
  public boolean isBytes()
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
}
