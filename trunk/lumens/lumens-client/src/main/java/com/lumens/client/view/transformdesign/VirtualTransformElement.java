/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.client.view.transformdesign;

import com.lumens.client.rpc.beans.CComponent;

/**
 *
 * @author shaofeng wang (shaofeng.cjpw@gmail.com)
 */
public class VirtualTransformElement extends CComponent
{
    private CComponent tElement;

    public VirtualTransformElement(CComponent tElement)
    {
        super();
        this.tElement = tElement;
    }

    public CComponent getTransformElement()
    {
        return tElement;
    }
}
