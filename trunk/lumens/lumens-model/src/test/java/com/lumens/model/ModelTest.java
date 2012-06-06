package com.lumens.model;

import com.lumens.model.Format.Form;
import com.lumens.model.Format.Type;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class ModelTest
        extends TestCase
{
  /**
   * Create the test case
   *
   * @param testName name of the test case
   */
  public ModelTest(String testName)
  {
    super(testName);
  }

  /**
   * @return the suite of tests being tested
   */
  public static Test suite()
  {
    return new TestSuite(ModelTest.class);
  }

  public void testFormatAndData()
  {
    // Create a simple structure, collection and attribute meta data model
    Format root = new NodeFormat("root");
    Format person = root.addChild("Person", Form.STRUCTRUE, Type.NONE);
    person.addChild("name", Form.ATTRIBUTE, Type.STRING);
    Format asset = person.addChild("asset", Form.COLLECTION, Type.NONE);
    asset.addChild("name", Form.ATTRIBUTE, Type.STRING);
    asset.addChild("price", Form.ATTRIBUTE, Type.FLOAT);
    assertEquals(Type.STRING, person.getChild("asset").getChild("name").getType());

    Data personData = new NodeData(person);
    Data nameData = personData.addChild("name");
    nameData.setValue("James wang");
    assertEquals("James wang", nameData.getString());
  }
}
