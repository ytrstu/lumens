/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.model;

/**
 *
 * @author shaofeng wang
 */
public enum Type
{
    NONE("None"),
    SHORT("Short"),
    INT("Integer"),
    LONG("Long"),
    FLOAT("Float"),
    DOUBLE("Double"),
    STRING("String"),
    BINARY("Binary"),
    DATE("Date");
    private String name;

    private Type(String name)
    {
        this.name = name;
    }

    @Override
    public String toString()
    {
        return name;
    }
}
