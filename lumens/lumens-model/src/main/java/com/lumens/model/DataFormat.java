/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
  protected Map<String, Format> children = new HashMap<String, Format>();
  protected List<Format> childrenList = new ArrayList<Format>();
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
  public Format addChild(Format format)
  {
    if (children.containsKey(format.getName()))
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
  public boolean isStructure()
  {
    return form == Form.STRUCT;
  }

  @Override
  public boolean isArray()
  {
    return form == Form.ARRAY;
  }
}
