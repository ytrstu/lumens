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
  private int index = -1;
  private boolean isArrayItem;

  public NodeData(Format format)
  {
    this.format = format;
  }

  @Override
  public int index()
  {
    return index;
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
  public Data addChild(String name)
  {
    Format child = format.getChild(name);
    return addChild(new NodeData(child));
  }

  @Override
  public Data addChild(Data data)
  {
    if (isArray())
    {
      throw new RuntimeException("Error, the data node is an array, it is not an array item");
    }
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
  public Data getChild(String name)
  {
    return children.get(name);
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
    {
      throw new IllegalArgumentException("Error, data type is not short !");
    }
    this.value = value;
  }

  @Override
  public void setValue(int value)
  {
    if (!isInt())
    {
      throw new IllegalArgumentException("Error, data type is not int !");
    }
    this.value = value;
  }

  @Override
  public void setValue(long value)
  {
    if (!isLong())
    {
      throw new IllegalArgumentException("Error, data type is not long !");
    }
    this.value = value;
  }

  @Override
  public void setValue(float value)
  {
    if (!isFloat())
    {
      throw new IllegalArgumentException("Error, data type is not float !");
    }
    this.value = value;
  }

  @Override
  public void setValue(double value)
  {
    if (!isDouble())
    {
      throw new IllegalArgumentException("Error, data type is not double !");
    }
    this.value = value;
  }

  @Override
  public void setValue(byte[] value)
  {
    if (!isBinary())
    {
      throw new IllegalArgumentException("Error, data type is not binary !");
    }
    this.value = value;
  }

  @Override
  public void setValue(Date value)
  {
    if (!isDate())
    {
      throw new IllegalArgumentException("Error, data type is not date time !");
    }
    this.value = value;
  }

  @Override
  public void setValue(String value)
  {
    if (!isString())
    {
      throw new IllegalArgumentException("Error, data type is not string !");
    }
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
  public Data addArrayItem()
  {
    if (!format.isArray())
    {
      throw new RuntimeException("Error, the node type is not an array");
    }
    NodeData data = new NodeData(format);
    if (arrayItems == null)
    {
      arrayItems = new ArrayList<Data>();
    }
    data.setParent(this);
    data.index = arrayItems.size();
    data.isArrayItem = true;
    this.arrayItems.add(data);
    return data;
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
