/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.client.rpc.beans;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 *
 * @author shaofeng wang (shaofeng.cjpw@gmail.com)
 */
public class Entry<K, V> implements IsSerializable
{
    private K key;
    private V value;

    public Entry(K key, V value)
    {
        this.key = key;
        this.value = value;
    }

    public K getKey()
    {
        return key;
    }

    public V getValue()
    {
        return value;
    }

    public void setValue(V value)
    {
        this.value = value;
    }

    @Override
    public boolean equals(Object o)
    {
        if (o != null && o instanceof Entry)
        {
            Entry entry = (Entry) o;
            return key.equals(entry.getKey());
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return key.hashCode();
    }
}
