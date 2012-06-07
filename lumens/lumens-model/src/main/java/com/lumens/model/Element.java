/*
 * Lumens developer shaofeng.cjpw@gmail.com
 */
package com.lumens.model;

import java.util.Date;
import java.util.List;

/**
 *
 * @author shaofeng wang
 */
public interface Element
{
  /*Methods to get and set properties of data node*/
  public int index();

  public void removeChild(Element data);

  public Element addChild(String name);

  public Element addChild(Element data);

  public Element addArrayItem();

  public Element getParent();

  public Element getChild(String name);

  public Element getChildByPath(String path);

  public List<Element> getChildren();

  public List<Element> getArrayItems();

  public List<Element> getSlibling();

  public Format getFormat();

  public Object getValue();

  public void setParent(Element data);

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
