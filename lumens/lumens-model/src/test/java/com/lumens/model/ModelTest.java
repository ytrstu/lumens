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
    Format person = root.addChild("Person", Form.STRUCT, Type.NONE);
    person.addChild("name", Form.FIELD, Type.STRING);
    Format asset = person.addChild("asset", Form.STRUCT, Type.NONE);
    asset.addChild("name", Form.FIELD, Type.STRING);
    asset.addChild("price", Form.FIELD, Type.FLOAT);
    assertEquals(Type.STRING, person.getChild("asset").getChild("name").getType());

    Data personData = new NodeData(person);
    Data nameData = personData.addChild("name");
    nameData.setValue("James wang");
    assertEquals("James wang", nameData.getString());
    Data assetData = personData.addChild("asset");
    assetData.addChild("name").setValue("Mac air book");
    assetData.addChild("price").setValue(12000.05f);
    assertEquals(12000.05f, personData.getChild("asset").getChild("price").getFloat());
  }

  public void testCollection()
  {
    // Create format
    Format root = new NodeFormat("root");
    Format person = root.addChild("Person", Form.STRUCT, Type.NONE);
    person.addChild("name", Form.FIELD, Type.STRING);
    Format asset = person.addChild("asset", Form.ARRAY, Type.NONE);
    asset.addChild("name", Form.FIELD, Type.STRING);
    asset.addChild("price", Form.FIELD, Type.FLOAT);

    // Fill data
    Data personData = new NodeData(person);
    Data nameData = personData.addChild("name");
    nameData.setValue("James wang");
    assertEquals("James wang", nameData.getString());
    Data assetData = personData.addChild("asset");
    Data assetDataItem = assetData.addArrayItem();
    assetDataItem.addChild("name").setValue("Mac air book");
    assetDataItem.addChild("price").setValue(12000.05f);
  }
}
