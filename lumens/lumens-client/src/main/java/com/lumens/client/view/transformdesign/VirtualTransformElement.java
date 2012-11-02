/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.client.view.transformdesign;

/**
 *
 * @author shaofeng wang (shaofeng.cjpw@gmail.com)
 */
public class VirtualTransformElement extends TransformElement
{
    private TransformElement tElement;

    public VirtualTransformElement(TransformElement tElement)
    {
        super();
        this.tElement = tElement;
    }

    public TransformElement getTransformElement()
    {
        return tElement;
    }
}
