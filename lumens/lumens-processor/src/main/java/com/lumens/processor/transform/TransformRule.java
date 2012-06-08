/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.processor.transform;

import com.lumens.model.Format;

/**
 *
 * @author shaofeng wang
 */
public class TransformRule
{
  private Format srcFmt;
  private Format dstFmt;
  private TransformRuleItem root;

  public TransformRule(Format source, Format dest)
  {
    srcFmt = source;
    dstFmt = dest;
  }

  public TransformRuleItem getRuleItem(String path)
  {
    return null;
  }
}
