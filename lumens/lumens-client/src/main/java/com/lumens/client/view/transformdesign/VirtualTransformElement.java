/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.client.view.transformdesign;

import com.lumens.client.rpc.beans.ClientTransformElement;

/**
 *
 * @author shaofeng wang (shaofeng.cjpw@gmail.com)
 */
public class VirtualTransformElement extends ClientTransformElement
{
    private ClientTransformElement tElement;

    public VirtualTransformElement(ClientTransformElement tElement)
    {
        super();
        this.tElement = tElement;
    }

    public ClientTransformElement getTransformElement()
    {
        return tElement;
    }
}
