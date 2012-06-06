/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lumens.model;

import com.lumens.model.Format.Type;
import com.sun.org.apache.xalan.internal.xsltc.compiler.Constants;
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
    children.put(name, data);
    childrenList.add(data);
    return data;
  }

  @Override
  public Data getParent()
  {
    throw new UnsupportedOperationException("Not supported yet.");
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
    if (format.getType() == Type.STRING)
    {
      this.value = value;
    }
    throw new IllegalArgumentException("Error, data type is not string !");
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
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public Date getDate()
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }
}
