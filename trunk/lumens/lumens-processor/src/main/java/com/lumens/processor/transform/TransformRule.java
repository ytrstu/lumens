/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.processor.transform;

import com.lumens.model.AccessPath;
import com.lumens.model.Format;
import com.lumens.model.Path;
import com.lumens.model.PathToken;
import com.lumens.processor.Rule;
import java.util.Iterator;

/**
 *
 * @author shaofeng wang
 */
public class TransformRule implements Rule
{
    private Format dstFmt;
    private TransformRuleItem root;
    private String name;

    public TransformRule(String name, Format dest)
    {
        this.name = name;
        this.dstFmt = dest;
    }

    public TransformRule(Format dest)
    {
        name = dest.getName() + System.currentTimeMillis();
        dstFmt = dest;
    }

    public String getName()
    {
        return name;
    }

    public TransformRuleItem getRuleEntry()
    {
        return root;
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
