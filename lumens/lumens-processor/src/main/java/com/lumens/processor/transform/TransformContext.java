/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.processor.transform;

import com.lumens.model.Element;

/**
 *
 * @author shaofeng wang
 */
public class TransformContext
{
    private Element input;
    private Element result;

    public TransformContext(Element input, Element result)
    {
        this.input = input;
        this.result = result;
    }

    Element getData()
    {
        return input;
    }

    Element getResult()
    {
        return result;
    }
}
