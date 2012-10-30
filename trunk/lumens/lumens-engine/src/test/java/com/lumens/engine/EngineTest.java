/*
 * To change this template, choose Tools | Templates
 * and initialize the template in the editor.
 */
package com.lumens.engine;

import com.lumens.connector.Connector;
import com.lumens.connector.Usage;
import com.lumens.connector.webservice.WebServiceConnector;
import com.lumens.model.Format;
import com.lumens.processor.transform.TransformRule;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import junit.framework.TestCase;

/**
 *
 * @author washaofe
 */
public class EngineTest extends TestCase
{
    public EngineTest(String testName)
    {
        super(testName);
    }

    public void testEngine() throws Exception
    {
        int nameCounter = 1;
        // Create ws connector to read data
        HashMap<String, Object> props = new HashMap<String, Object>();
        props.put(WebServiceConnector.WSDL,
                  getClass().getResource("/wsdl/ChinaOpenFundWS.asmx").
                toString());
        props.put(WebServiceConnector.PROXY_ADDR, "web-proxy.atl.hp.com");
        props.put(WebServiceConnector.PROXY_PORT, 8080);
        DataSource datasource = new DataSource();
        datasource.setName("ChinaMobile-WebService-SOAP");
        datasource.setParameters(props);
        datasource.initialize();

        // Expand format tree
        //*******************************************************************************************
        Map<String, Format> consumes = datasource.getFormatList(Usage.CONSUME);
        Connector connector = datasource.getConnector();
        Format getOpenFundStringRequest = connector.getFormat(
                consumes.get("getOpenFundString"),
                "getOpenFundString.userID",
                Usage.CONSUME);
        Map<String, Format> produces = datasource.getFormatList(Usage.PRODUCE);
        Format getOpenFundStringResponse = connector.getFormat(produces.get(
                "getOpenFundString"),
                                                               "getOpenFundStringResponse.getOpenFundStringResult.string",
                                                               Usage.PRODUCE);
        String targetFormatName = getOpenFundStringRequest.getName() + (nameCounter++);
        getOpenFundStringRequest = getOpenFundStringRequest.deepClone();
        getOpenFundStringResponse = getOpenFundStringResponse.deepClone();
        datasource.registerConsumeFormat(targetFormatName,
                                         getOpenFundStringRequest);
        datasource.registerProduceFormat(targetFormatName,
                                         getOpenFundStringResponse);

        String targetFormatName2 = getOpenFundStringRequest.getName() + (nameCounter++);
        datasource.registerConsumeFormat(targetFormatName2,
                                         getOpenFundStringRequest);
        datasource.registerProduceFormat(targetFormatName2,
                                         getOpenFundStringResponse);

        //******************************************************************************************

        // Create transformation to a data source
        Transformation callGetOpenFundString = new Transformation();
        callGetOpenFundString.setName("GetOpenFundString-WS-Transform");
        callGetOpenFundString.initialize();

        Transformation callGetOpenFundString2 = new Transformation();
        callGetOpenFundString2.setName("GetOpenFundString2-WS-Transform");
        callGetOpenFundString2.initialize();

        // Link transform call to datasource webservice getOpenFundString
        callGetOpenFundString.to(datasource);
        datasource.to(callGetOpenFundString2);
        callGetOpenFundString2.to(datasource);

        // Create start point transformation
        String startPoint = "DataDriven";
        TransformRule rule1 = new TransformRule(startPoint, targetFormatName,
                                                getOpenFundStringRequest);
        rule1.getRuleItem("getOpenFundString.userID").setScript(
                "\"123\"");
        callGetOpenFundString.registerRule(rule1);

        // Create the loop transformation datasource->transformation->datasource
        TransformRule rule2 = new TransformRule(targetFormatName,
                                                targetFormatName2,
                                                getOpenFundStringRequest);
        rule2.getRuleItem("getOpenFundString.userID").setScript(
                "@getOpenFundStringResponse.getOpenFundStringResult.string");
        callGetOpenFundString2.registerRule(rule2);

        //**********************************************************************
        // Execute all start rules to drive the ws connector
        SingleThreadExecuteStack executorStack = new SingleThreadExecuteStack();
        executorStack.push(
                new TransformExecutor(callGetOpenFundString,
                                      new TransformExecuteContext(null,
                                                                  startPoint)));
        while (!executorStack.isEmpty())
        {
            Executor executor = executorStack.pop();
            List<Executor> tExList = executor.execute();
            executorStack.push(tExList);
        }
    }
}
