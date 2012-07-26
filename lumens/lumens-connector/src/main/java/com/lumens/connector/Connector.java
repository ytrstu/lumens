/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector;

import com.lumens.model.Format;
import java.util.Map;

/**
 *
 * @author shaofeng wang
 */
public interface Connector extends Configurable
{
    public void open();

    public void close();

    public Operation getOperation();

    public Map<String, Format> getFormatList(Usage param);

    public Format getFormat(Format format, String path, Usage use);
}
