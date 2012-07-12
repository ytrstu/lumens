/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector;

import com.lumens.model.Format;

/**
 *
 * @author shaofeng wang
 */
public interface Connector
{
    public void open();

    public void close();

    public Reader createReader();

    public Writer createWriter();

    public Format getFormats(Usage usage);

    public Format getFormat(Format format, Usage usage);
}
