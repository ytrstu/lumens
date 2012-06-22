/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector;

import java.util.Map;

/**
 *
 * @author shaofeng wang
 */
public interface Configurable
{
    public void setConfiguration(Map<String, Object> configuration);

    public Map<String, Object> getConfiguration();
}
