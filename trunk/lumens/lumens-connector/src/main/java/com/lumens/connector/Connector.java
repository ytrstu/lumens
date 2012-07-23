/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector;

import com.lumens.model.Format;

/**
 *
 * @author shaofeng wang
 */
public interface Connector extends Configurable
{
    public void open();

    public void close();

    public Operation getOperation();

    public Format getFormats(Param param);

    public Format getFormat(Format format, Param param);
}
