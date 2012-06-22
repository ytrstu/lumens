/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector;

import com.lumens.model.Element;
import java.util.Iterator;

/**
 *
 * @author shaofeng wang
 */
public interface Reader
{
    public void setCondition(Condition cond);

    public int read() throws RuntimeException;

    public Iterator<Element> iterator();

    public void cleanup();
}
