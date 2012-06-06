/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lumens.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 *
 * @author shaofeng wang
 */
public interface Format
{
  public enum Type
  {
    NONE,
    SHORT,
    INT,
    LONG,
    FLOAT,
    DOUBLE,
    STRING,
    BINARY,
    DATE
  }

  public enum Form
  {
    NONE,
    FIELD,
    STRUCT,
    ARRAY
  }
  public static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
  public static final DateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");
  public static final DateFormat DATETIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

  public Type getType();

  public Form getForm();

  public void setType(Type type);

  public void setForm(Form form);

  public String getName();

  public void setName(String name);

  public Format getParent();

  public void setParent(Format format);

  public Format addChild(Format format);

  public Format addChild(String name, Form form, Type type);

  public Format getChild(String name);

  public List<Format> getChildren();

  public boolean isField();

  public boolean isStructure();

  public boolean isArray();
}