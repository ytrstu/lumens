package com.lumens.connector;

import com.lumens.connector.Writer.Operate;
import com.lumens.connector.database.DatabaseConnector;
import com.lumens.connector.webservice.WebServiceConnector;
import com.lumens.connector.webservice.soap.SOAPClient;
import com.lumens.connector.webservice.soap.SOAPConstants;
import com.lumens.connector.webservice.soap.SOAPMessageBuilder;
import com.lumens.model.DataElement;
import com.lumens.model.DataFormat;
import com.lumens.model.Element;
import com.lumens.model.Format;
import com.lumens.model.Format.Form;
import com.lumens.model.Type;
import com.lumens.model.serializer.DataElementXmlSerializer;
import com.lumens.model.serializer.DataFormatXmlSerializer;
import com.lumens.processor.Processor;
import com.lumens.processor.transform.TransformProcessor;
import com.lumens.processor.transform.TransformRule;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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

    public void testConnector()
    {
        Connector connector = new DatabaseConnector();
        Reader reader = connector.createReader();
        int recordCount = reader.read();
        Iterator<Element> it = reader.iterator();
        Writer writer = connector.createWriter();
        Element e = new DataElement(new DataFormat("test", Form.FIELD, Type.STRING));
        e.setValue("Hello Lumens");
        writer.write(Operate.CREATE, e);
    }

    public static void testOracleConnector() throws Exception
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
            cntr.setConfiguration(props);
            cntr.open();
            DataFormatXmlSerializer xml = new DataFormatXmlSerializer(cntr.getFormats(null), "UTF-8",
                                                                      true);
            xml.write(System.out);
        }
        finally
        {
            cntr.close();
        }
    }

    public void TtestWebServiceConnector() throws Exception
    {
        WebServiceConnector connector = new WebServiceConnector();
        HashMap<String, Object> props = new HashMap<String, Object>();
        props.put(WebServiceConnector.WSDL,
                  getClass().getResource("/wsdl/IncidentManagement.wsdl").toString());
        connector.setConfiguration(props);
        connector.open();
        Format services = connector.getFormats(Usage.INPUT);
        DataFormatXmlSerializer xml = new DataFormatXmlSerializer(services, "UTF-8",
                                                                  true);
        xml.write(System.out);
        for (Format service : services.getChildren())
        {
            for (Format message : service.getChildren())
            {
                connector.getFormat(message, null);
            }
        }
        xml.write(System.out);
        Format RetrieveIncident = services.getChild("RetrieveIncident");
        TransformRule rule = new TransformRule(RetrieveIncident);
        rule.getRuleItem("RetrieveIncidentRequest.attachmentData").setScript("true");
        rule.getRuleItem("RetrieveIncidentRequest.model.instance.AssigneeName").
                setScript("\'test\'");
        rule.getRuleItem("RetrieveIncidentRequest.model.instance.ClosedTime").setScript(
                "dateFormat(now(), \"yyyy-MM-dd HH:mm:ss\")");
        Processor transformProcessor = new TransformProcessor(rule);
        List<Element> result = (List<Element>) transformProcessor.execute(null);
        DataElementXmlSerializer serializer = new DataElementXmlSerializer(result.get(0), "UTF-8",
                                                                           true);
        serializer.write(System.out);
        connector.close();
        SOAPMessageBuilder soapBuilder = new SOAPMessageBuilder();
        SOAPEnvelope envelope = soapBuilder.buildSOAPMessage(result.get(0));
        System.out.println(envelope);

        props.put(WebServiceConnector.WSDL,
                  getClass().getResource("/wsdl/ChinaOpenFundWS.asmx").toString());
        connector.setConfiguration(props);
        connector.open();
        services = connector.getFormats(Usage.BOTH);
        xml = new DataFormatXmlSerializer(services, "UTF-8", true);
        xml.write(System.out);
        for (Format service : services.getChildren())
        {
            for (Format message : service.getChildren())
            {
                connector.getFormat(message, null);
            }
        }
        xml.write(System.out);
        Format getOpenFundString = services.getChild("getOpenFundString");
        rule = new TransformRule(getOpenFundString);
        rule.getRuleItem("getOpenFundString.userID").setScript("\"13482718164\"");
        transformProcessor = new TransformProcessor(rule);
        result = (List<Element>) transformProcessor.execute(null);
        serializer = new DataElementXmlSerializer(result.get(0), "UTF-8", true);
        serializer.write(System.out);

        SOAPClient client = connector.getClient();
        Element responseElement = client.execute(result.get(0), getOpenFundString);
        serializer = new DataElementXmlSerializer(responseElement, "UTF-8", true);
        serializer.write(System.out);
    }

    public void testPPMWS() throws Exception
    {
        String ppmWSDL = "http://16.173.232.74:16800/itg/ppmservices/DemandService?wsdl";
        WebServiceConnector connector = new WebServiceConnector();
        HashMap<String, Object> props = new HashMap<String, Object>();
        props.put(WebServiceConnector.WSDL, ppmWSDL);
        props.put(DatabaseConnector.USER, "admin");
        props.put(DatabaseConnector.PASSWORD, "admin");
        connector.setConfiguration(props);
        connector.open();
        Format services = connector.getFormats(Usage.BOTH);
        for (Format service : services.getChildren())
        {
            for (Format message : service.getChildren())
            {
                connector.getFormat(message, null);
            }
        }
        Format getRequests = services.getChild("getRequests");
        DataFormatXmlSerializer xml = new DataFormatXmlSerializer(getRequests, "UTF-8", true);
        xml.setCareProperties(true);
        xml.write(System.out);
        TransformRule rule = new TransformRule(getRequests);
        rule.getRuleItem("getRequests.requestIds.id").setScript("\"30392\"");
        TransformProcessor transformProcessor = new TransformProcessor(rule);
        List<Element> result = (List<Element>) transformProcessor.execute(null);
        DataElementXmlSerializer elemXml = new DataElementXmlSerializer(result.get(0), "UTF-8", true);
        elemXml.write(System.out);
        SOAPMessageBuilder builder = new SOAPMessageBuilder();

        System.out.println(builder.buildSOAPMessage(result.get(0)));

        SOAPClient client = connector.getClient();
        Element responseElement = client.execute(result.get(0), getRequests);
        elemXml = new DataElementXmlSerializer(responseElement, "UTF-8", true);
        elemXml.write(System.out);
    }
}
