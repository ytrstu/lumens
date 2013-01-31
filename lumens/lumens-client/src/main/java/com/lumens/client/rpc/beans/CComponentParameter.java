/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.client.rpc.beans;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.lumens.client.utils.CConstants;

/**
 *
 * @author shaofeng wang (shaofeng.cjpw@gmail.com)
 */
public class CComponentParameter implements IsSerializable
{
    private String id;
    private String name;
    private String javatype;
    private boolean encrypted;
    private String value = CConstants.g_strEmpty;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public boolean isEncrypted()
    {
        return encrypted;
    }

    public void setEncrypted(boolean encrypted)
    {
        this.encrypted = encrypted;
    }

    public String getJavatype()
    {
        return javatype;
    }

    public void setJavatype(String javatype)
    {
        this.javatype = javatype;
    }

    public void setValue(String value)
    {
        this.value = value;
    }

    public String getValue()
    {
        return value;
    }

    public CComponentParameter copy()
    {
        CComponentParameter copyed = new CComponentParameter();
        copyed.id = this.id;
        copyed.name = this.name;
        copyed.value = this.value;
        copyed.javatype = this.javatype;
        copyed.encrypted = this.encrypted;
        return copyed;
    }
}
