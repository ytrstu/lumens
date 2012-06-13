/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.processor.transform;

import com.lumens.model.Format;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author shaofeng wang
 */
public class TransformRuleItem
{
    private TransformRuleItem parent;
    private List<TransformRuleItem> children;
    private String value;
    private String arrayIterationPath;
    private Format format;
    
    TransformRuleItem(Format format)
    {
        this.format = format;
    }
    
    public void setValue(String value)
    {
        this.value = value;
    }
    
    public String getValue()
    {
        return value;
    }
    
    public void setArrayIterationPath(String arrayIterationPath)
    {
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
        TransformRuleItem item = new TransformRuleItem(child);
        item.parent = this;
        children.add(item);
        return item;
    }
    
    public Format getFormat()
    {
        return format;
    }
}
