/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.processor.transform;

import com.lumens.model.Element;
import com.lumens.processor.Input;

/**
 *
 * @author shaofeng wang
 */
public class TransformInput implements Input
{
    private Element element;
    private TransformRule rule;

    public TransformInput(Element element, TransformRule rule)
    {
        this.element = element;
        this.rule = rule;
    }

    public Element getData()
    {
        return element;
    }

    public TransformRule getRule()
    {
        return rule;
    }
}
