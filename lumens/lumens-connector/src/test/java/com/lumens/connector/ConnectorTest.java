package com.lumens.connector;

import com.lumens.connector.Writer.Operate;
import com.lumens.connector.database.DatabaseConnector;
import com.lumens.model.DataElement;
import com.lumens.model.DataFormat;
import com.lumens.model.Element;
import com.lumens.model.Format.Form;
import com.lumens.model.Type;
import java.util.Iterator;
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
}
