/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lumens.model;

import java.util.Date;
import java.util.List;

/**
 *
 * @author shaofeng wang
 */
public interface Data
{
  /*Methods to get and set properties of data node*/
  public int index();

  public void removeChild(Data data);

  public Data addChild(String name);

  public Data addChild(Data data);

  public Data addArrayItem();

  public Data getParent();

  public Data getChild(String name);

  public List<Data> getChildren();
  
  public List<Data> getArrayItems();

  public List<Data> getSlibling();

  public Format getFormat();

  public Object getValue();

  public void setParent(Data data);

  public void setValue(Object value);

  public void setValue(short value);

  public void setValue(int value);

  public void setValue(long value);

  public void setValue(float value);

  public void setValue(double value);

  public void setValue(byte[] value);

  public void setValue(Date value);

  public void setValue(String value);

  public short getShort();

  public int getInt();

  public long getLong();

  public float getFloat();

  public double getDouble();

  public byte[] getBinary();

  public String getString();

  public Date getDate();

  public boolean isArray();

  public boolean isArrayItem();

  public boolean isShort();

  public boolean isInt();

  public boolean isLong();

  public boolean isFloat();

  public boolean isDouble();

  public boolean isBinary();

  public boolean isDate();

  public boolean isString();
}
