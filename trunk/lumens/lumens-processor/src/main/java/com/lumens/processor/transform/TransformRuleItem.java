/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.processor.transform;

import com.lumens.model.Format;

/**
 *
 * @author shaofeng wang
 */
public class TransformRuleItem
{
  private TransformRule rule;
  private TransformRuleItem parent;
  private Format src;
  private String valueScript;
  private String arrayLoop;

  public TransformRuleItem(TransformRule rule)
  {
    this.rule = rule;
  }

  public void setValue(String valueScript)
  {
    this.valueScript = valueScript;
  }

  public void setLoop(String arrayLoop)
  {
    this.arrayLoop = arrayLoop;
  }
}
