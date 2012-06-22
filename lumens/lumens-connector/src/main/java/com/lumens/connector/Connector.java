/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector;

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
}
