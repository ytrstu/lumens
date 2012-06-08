/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.processor.transform;

import com.lumens.model.AccessPath;
import com.lumens.model.Format;
import com.lumens.model.Path;
import com.lumens.model.PathToken;
import java.util.Iterator;

/**
 *
 * @author shaofeng wang
 */
public class TransformRule
{
  private Format dstFmt;
  private TransformRuleItem root;

  public TransformRule(Format dest)
  {
    dstFmt = dest;
  }

  public TransformRuleItem getRuleItem(String path)
  {
    Path fmtPath = new AccessPath(path);
    if (root == null)
      root = new TransformRuleItem(dstFmt);
    if (!fmtPath.isEmpty())
    {
      PathToken token = null;
      TransformRuleItem parent = root;
      TransformRuleItem child = null;
      Iterator<PathToken> it = fmtPath.iterator();
      while (it.hasNext())
      {
        token = it.next();
        child = parent.getChild(token.toString());
        if (child == null)
          child = parent.addChild(token.toString());
        parent = child;
      }
      return child;
    }

    return null;
  }
}
