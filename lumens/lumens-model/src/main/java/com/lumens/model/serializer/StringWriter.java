/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.model.serializer;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 *
 * @author shaofeng wang
 */
class StringWriter
{
    private DataOutputStream out;

    public StringWriter(DataOutputStream out)
    {
        this.out = out;
    }

    public StringWriter print(String str) throws IOException
    {
        if (str != null)
            out.writeBytes(str);
        return this;
    }

    public StringWriter println(String str) throws IOException
    {
        print(str);
        print("\n");
        return this;
    }
}
