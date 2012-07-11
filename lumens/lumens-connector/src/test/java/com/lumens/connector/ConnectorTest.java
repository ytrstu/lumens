package com.lumens.connector;

import com.lumens.connector.Writer.Operate;
import com.lumens.connector.database.DatabaseConnector;
import com.lumens.connector.webservice.WebServiceConnector;
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

/**
 * Unit test for simple App.
 */
public class ConnectorTest
        extends TestCase
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

    public static void TtestOracleConnector() throws Exception
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
            DataFormatXmlSerializer xml = new DataFormatXmlSerializer(cntr.getFormats(), "UTF-8",
                                                                      true);
            xml.write(System.out);
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
                  getClass().getResource("/sm-wsdl/IncidentManagement.wsdl").toString());
        //props.put(WebServiceConnector.WSDL, "http://citvm25:13930/SM/7/IncidentManagement.wsdl");
        connector.setConfiguration(props);
        connector.open();
        Format services = connector.getFormats();
        DataFormatXmlSerializer xml = new DataFormatXmlSerializer(services, "UTF-8",
                                                                  true);
        xml.write(System.out);
        for (Format service : services.getChildren())
        {
            for (Format message : service.getChildren())
                connector.getFormat(message);
            break;
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
        List<Element> result = (List<Element>) transformProcessor.process(null);
        DataElementXmlSerializer serializer = new DataElementXmlSerializer(result.get(0), "UTF-8",
                                                                           true);
        serializer.write(System.out);
    }
}
