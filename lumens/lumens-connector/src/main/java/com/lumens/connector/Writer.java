/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector;

import com.lumens.model.Element;

/**
 *
 * @author shaofeng wang
 */
public interface Writer
{
    public enum Operate
    {
        CREATE,
        UPDATE,
        DELETE
    }

    public int write(Operate o, Element e) throws RuntimeException;

    public void cleanup();
}
