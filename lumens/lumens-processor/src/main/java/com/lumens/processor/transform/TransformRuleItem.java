/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.processor.transform;

import com.lumens.model.Format;
import com.lumens.processor.ProcessorUtils;
import com.lumens.processor.Script;
import com.lumens.processor.script.AccessPathScript;
import com.lumens.processor.script.JavaScript;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 *
 * @author shaofeng wang
 */
public class TransformRuleItem
{
    private Set<String> arrayIterationCache;
    private TransformRuleItem parent;
    private List<TransformRuleItem> children;
    private Script script;
    private String orignalScript;
    private String arrayIterationPath;
    private Format format;

    TransformRuleItem(Format format, Set<String> arrayIterationCache)
    {
        this.format = format;
        this.arrayIterationCache = arrayIterationCache;
    }

    public void setScript(String script) throws Exception
    {
        orignalScript = script;
        if (ProcessorUtils.isPathFormat(script))
            this.script = new AccessPathScript(ProcessorUtils.getAccessPath(script));
        else
            this.script = new JavaScript(script);
    }

    public Script getScript()
    {
        return script;
    }

    public String getScriptString()
    {
        return orignalScript;
    }

    public void setArrayIterationPath(String arrayIterationPath)
    {
        if (!arrayIterationCache.contains(arrayIterationPath))
            this.arrayIterationPath = arrayIterationPath;
    }

    public String getArrayIterationPath()
    {
        return arrayIterationPath;
    }

    public TransformRuleItem getParent()
    {
        return parent;
    }

    public Iterator<TransformRuleItem> iterator()
    {
        return children != null ? children.iterator() : null;
    }

    public TransformRuleItem getChild(String name)
    {
        if (children != null)
        {
            for (TransformRuleItem item : children)
                if (item.format.getName().equalsIgnoreCase(name))
                    return item;
        }
        return null;
    }

    public TransformRuleItem addChild(String name)
    {
        if (children == null)
            children = new ArrayList<TransformRuleItem>();
        Format child = format.getChild(name);
        if (child == null)
            throw new IllegalArgumentException("The child format \"" + name + "\" does not exist");
        TransformRuleItem item = new TransformRuleItem(child, arrayIterationCache);
        item.parent = this;
        children.add(item);
        return item;
    }

    public Format getFormat()
    {
        return format;
    }

    List<TransformRuleItem> getChildren()
    {
        return children;
    }
}
