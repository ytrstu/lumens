/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.processor.transform;

import com.lumens.model.Format;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author shaofeng wang
 */
public class TransformRuleItem
{
  private TransformRuleItem parent;
  private Map<String, TransformRuleItem> children;
  private String value;
  private String arrayLoop;
  private Format format;

  TransformRuleItem(Format format)
  {
    this.format = format;
  }

  public void setValue(String value)
  {
    this.value = value;
  }

  public void setLoop(String arrayLoop)
  {
    this.arrayLoop = arrayLoop;
  }

  public String getValue()
  {
    return value;
  }

  public String getLoop()
  {
    return arrayLoop;
  }

  public TransformRuleItem getChild(String name)
  {
    if (children == null)
      return null;
    return children.get(name);
  }

  public TransformRuleItem addChild(String name)
  {
    if (children == null)
      children = new HashMap<String, TransformRuleItem>();
    Format child = format.getChild(name);
    if (child == null)
      throw new IllegalArgumentException("The child format \"" + name + "\" does not exist");
    TransformRuleItem item = new TransformRuleItem(child);
    children.put(name, item);
    return item;
  }
}
