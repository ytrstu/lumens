package com.lumens.connector;

import com.lumens.connector.database.DatabaseConnector;
import com.lumens.connector.webservice.WebServiceConnector;
import com.lumens.connector.webservice.soap.SOAPConstants;
import com.lumens.connector.webservice.soap.SOAPMessageBuilder;
import com.lumens.model.Element;
import com.lumens.model.Format;
import com.lumens.model.serializer.DataElementXmlSerializer;
import com.lumens.model.serializer.DataFormatXmlSerializer;
import com.lumens.processor.Processor;
import com.lumens.processor.transform.TransformProcessor;
import com.lumens.processor.transform.TransformRule;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.axiom.soap.SOAPEnvelope;

/**
 * Unit test for simple App.
 */
public class ConnectorTest extends TestCase implements SOAPConstants
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public ConnectorTest(String testName)
    {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite(ConnectorTest.class);
    }

    public void testOracleConnector() throws Exception
    {
        DatabaseConnector cntr = null;
        try
        {
            cntr = new DatabaseConnector();
            HashMap<String, Object> props = new HashMap<String, Object>();
            props.put(DatabaseConnector.OJDBC,
                      "file:///C:/oracle/product/11.1.0/db_1/jdbc/lib/ojdbc6.jar");
            props.put(DatabaseConnector.CONNECTION_URL, "jdbc:oracle:thin:@//127.0.0.1:1521/ORCL");
            props.put(DatabaseConnector.USER, "sh");
            props.put(DatabaseConnector.PASSWORD, "accit");
            props.put(DatabaseConnector.FULL_LOAD, true);
            cntr.configure(props);
            cntr.open();
            for (Format format : cntr.getFormatList(null).values())
            {
                DataFormatXmlSerializer xml = new DataFormatXmlSerializer(format, "UTF-8", true);
                xml.write(System.out);
            }
        }
        finally
        {
            cntr.close();
        }
    }

    public void testWebServiceConnector() throws Exception
    {
        WebServiceConnector connector = new WebServiceConnector();
        HashMap<String, Object> props = new HashMap<String, Object>();
        props.put(WebServiceConnector.WSDL,
                  getClass().getResource("/wsdl/IncidentManagement.wsdl").toString());
        connector.configure(props);
        connector.open();
        Map<String, Format> services = connector.getFormatList(Usage.CONSUME);
        Format retrieveIncident = services.get("RetrieveIncident");

        connector.getFormat(retrieveIncident, "RetrieveIncidentRequest.model.instance",
                            Usage.CONSUME);
        connector.getFormat(retrieveIncident, "RetrieveIncidentRequest.model.instance.attachments",
                            Usage.CONSUME);
        new DataFormatXmlSerializer(retrieveIncident, "UTF-8", true).write(System.out);
        assertNotNull(retrieveIncident.getChildByPath(
                "RetrieveIncidentRequest.model.instance.attachments.attachment"));

        TransformRule rule = new TransformRule(retrieveIncident);
        rule.getRuleItem("RetrieveIncidentRequest.attachmentData").setScript("true");
        rule.getRuleItem("RetrieveIncidentRequest.model.instance.AssigneeName").
                setScript("\'test\'");
        rule.getRuleItem("RetrieveIncidentRequest.model.instance.ClosedTime").setScript(
                "dateFormat(now(), \"yyyy-MM-dd HH:mm:ss\")");
        Processor transformProcessor = new TransformProcessor();
        List<Element> result = (List<Element>) transformProcessor.execute(rule);
        new DataElementXmlSerializer(result.get(0), "UTF-8", true).write(System.out);
        connector.close();
        SOAPMessageBuilder soapBuilder = new SOAPMessageBuilder();
        SOAPEnvelope envelope = soapBuilder.buildSOAPMessage(result.get(0));
        System.out.println(envelope);

        props.put(WebServiceConnector.WSDL,
                  getClass().getResource("/wsdl/ChinaOpenFundWS.asmx").toString());
        props.put(WebServiceConnector.PROXY_ADDR, "web-proxy.atl.hp.com");
        props.put(WebServiceConnector.PROXY_PORT, 8080);
        connector.configure(props);
        connector.open();
        services = connector.getFormatList(Usage.CONSUME);
        Format getOpenFundString = services.get("getOpenFundString");
        connector.getFormat(getOpenFundString, "getOpenFundString", Usage.CONSUME);
        //webserviceFormatExpand(connector, 2, getOpenFundString, Usage.CONSUME);
        rule = new TransformRule(getOpenFundString);
        rule.getRuleItem("getOpenFundString.userID").setScript("\"qqqqqqqqqqqqqqqqqqqqqqqqqqqqqq\"");
        transformProcessor = new TransformProcessor();
        result = (List<Element>) transformProcessor.execute(rule);
        new DataElementXmlSerializer(result.get(0), "UTF-8", true).write(System.out);

        services = connector.getFormatList(Usage.PRODUCE);
        getOpenFundString = services.get("getOpenFundString");
        connector.getFormat(getOpenFundString,
                            "getOpenFundStringResponse.getOpenFundStringResult.string.string",
                            Usage.PRODUCE);
        Operation op = connector.getOperation();
        List<Element> response = op.execute(result.get(0), getOpenFundString);
        new DataElementXmlSerializer(response.get(0), "UTF-8", true).write(System.out);

    }

    public void testPPMWS() throws Exception
    {
        String ppmWSDL = "http://16.173.232.74:16800/itg/ppmservices/DemandService?wsdl";
        WebServiceConnector connector = new WebServiceConnector();
        HashMap<String, Object> props = new HashMap<String, Object>();
        props.put(WebServiceConnector.WSDL, ppmWSDL);
        props.put(DatabaseConnector.USER, "admin");
        props.put(DatabaseConnector.PASSWORD, "admin");
        connector.configure(props);
        connector.open();
        Map<String, Format> services = connector.getFormatList(Usage.CONSUME);
        Format getRequests = services.get("getRequests");
        connector.getFormat(getRequests, "getRequests.requestIds.id", Usage.CONSUME);
        assertNotNull(getRequests.getChildByPath("getRequests.requestIds.id"));
        new DataFormatXmlSerializer(getRequests, "UTF-8", true).write(System.out);
        TransformRule rule = new TransformRule(getRequests);
        rule.getRuleItem("getRequests.requestIds.id").setScript("\"30392\"");
        TransformProcessor transformProcessor = new TransformProcessor();
        List<Element> result = (List<Element>) transformProcessor.execute(rule);
        new DataElementXmlSerializer(result.get(0), "UTF-8", true).write(System.out);
        Operation op = connector.getOperation();
        services = connector.getFormatList(Usage.PRODUCE);
        getRequests = services.get("getRequests");
        connector.getFormat(getRequests, "getRequestsResponse.return", Usage.PRODUCE);
        connector.getFormat(getRequests, "getRequestsResponse.return.fieldChangeNodes",
                            Usage.PRODUCE);
        connector.getFormat(getRequests, "getRequestsResponse.return.simpleFields", Usage.PRODUCE);
        connector.getFormat(getRequests, "getRequestsResponse.return.simpleFields.stringValue",
                            Usage.PRODUCE);
        new DataFormatXmlSerializer(getRequests, "UTF-8", true).write(System.out);
        assertNotNull(getRequests.getChildByPath("getRequestsResponse.return.simpleFields.stringValue"));
        List<Element> response = op.execute(result.get(0), getRequests);
        new DataElementXmlSerializer(response.get(0), "UTF-8", true).write(System.out);
    }
}
