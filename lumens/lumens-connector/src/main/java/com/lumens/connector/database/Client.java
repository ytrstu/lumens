/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.database;

import com.lumens.model.Format;

/**
 *
 * @author shaofeng wang
 */
public interface Client
{
    public void open();

    public void close();

    public Format getFormats(boolean fullLoad);

    public Format getFormat(Format format);
}
