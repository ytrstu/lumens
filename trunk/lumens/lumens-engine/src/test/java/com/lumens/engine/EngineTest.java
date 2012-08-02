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

    private static int counter = 0;

    public EngineTest(String testName)
    {
        super(testName);
    }

    public String generateName()
    {
        return "Component-" + counter;
    }

    public void testEngine() throws Exception
    {
        int nameCounter = 1;

        // Create start component
        Transformation callGetOpenFundString = new Transformation();
        callGetOpenFundString.setName(generateName());
        callGetOpenFundString.initialize();

        // Create ws connector to read data
        HashMap<String, Object> props = new HashMap<String, Object>();
        props.put(WebServiceConnector.WSDL,
                  getClass().getResource("/wsdl/ChinaOpenFundWS.asmx").toString());
        props.put(WebServiceConnector.PROXY_ADDR, "web-proxy.atl.hp.com");
        props.put(WebServiceConnector.PROXY_PORT, 8080);
        DataSource src = new DataSource();
        src.setName(generateName());
        src.configure(props);
        src.initialize();

        // Expand format tree
        Connector connector = src.getConnector();
        Map<String, Format> consumes = connector.getFormatList(Usage.CONSUME);
        Format getOpenFundStringRequest = connector.getFormat(
                consumes.get("getOpenFundString"),
                "getOpenFundString.userID",
                Usage.CONSUME);
        Map<String, Format> produces = connector.getFormatList(Usage.PRODUCE);
        Format getOpenFundStringResponse = connector.getFormat(produces.get("getOpenFundString"),
                                                               "getOpenFundStringResponse.getOpenFundStringResult.string",
                                                               Usage.PRODUCE);
        String requestName = getOpenFundStringRequest.getName() + (nameCounter++);
        getOpenFundStringRequest = getOpenFundStringRequest.deepClone();
        src.registerConsumeFormat(requestName, getOpenFundStringRequest);
        src.registerProduceFormat(requestName, getOpenFundStringResponse.deepClone());

        // Create transformation to a data source
        Transformation getOpenFundStr2getOpenFundStr = new Transformation();
        getOpenFundStr2getOpenFundStr.setName(generateName());
        getOpenFundStr2getOpenFundStr.initialize();

        // Link transform call to datasource webservice getOpenFundString
        callGetOpenFundString.to(src);
        src.to(getOpenFundStr2getOpenFundStr);
        getOpenFundStr2getOpenFundStr.to(src);

        TransformRule rule4openfundstr = new TransformRule(getOpenFundStringRequest);
        rule4openfundstr.getRuleItem("getOpenFundString.userID").setScript("\"123\"");
        callGetOpenFundString.registerRule(requestName, rule4openfundstr);

        TransformRule rule4openfundstrAgain = new TransformRule(getOpenFundStringRequest);
        rule4openfundstrAgain.getRuleItem("getOpenFundString.userID").setScript(
                "@getOpenFundStringResponse.getOpenFundStringResult.string");
        getOpenFundStr2getOpenFundStr.registerRule(requestName, rule4openfundstrAgain);

        // Execute all start rules to drive the ws connector
        SingleThreadExecuteStack exeStack = new SingleThreadExecuteStack();
        exeStack.push(new ExecuteContextImpl(callGetOpenFundString, null, requestName));
        while (!exeStack.isEmpty())
        {
            ExecuteContext context = exeStack.pop();
            Component exe = context.getNextComponent();
            List<ExecuteContext> nextActions = exe.execute(context);
            exeStack.push(nextActions);
        }

        //src.hasNext();
        //src.next();

        Router router = new Router();
    }
}
