/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.service;

import com.lumens.client.rpc.beans.CComponentTypeRegistry;
import com.lumens.service.config.datasource.DatasourceHelper;
import java.util.List;
import junit.framework.TestCase;

/**
 *
 * @author shaofeng wang (shaofeng.cjpw@gmail.com)
 */
public class LumensServiceTest extends TestCase
{
    public LumensServiceTest(String testName)
    {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception
    {
        super.tearDown();
    }

    public void testLoadConfig() throws Exception
    {
        List<CComponentTypeRegistry> compList = DatasourceHelper.loadDatasouceConfiguration();
        assertEquals(2, compList.size());
    }
}
