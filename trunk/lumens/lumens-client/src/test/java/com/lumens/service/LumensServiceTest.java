/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lumens.service;

import com.lumens.client.rpc.beans.ComponentRegistry;
import com.lumens.service.config.DatasourceHelper;
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
       List<ComponentRegistry> compList = DatasourceHelper.
                loadDatasouceConfiguration();
        assertEquals(2, compList.size());
    }
}
