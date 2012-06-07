package com.lumens.model;

import com.lumens.model.Format.Form;
import java.util.Iterator;
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
    Format root = new DataFormat("root");
    Format person = root.addChild("Person", Form.STRUCT, Type.NONE);
    person.addChild("name", Form.FIELD, Type.STRING);
    Format asset = person.addChild("asset", Form.STRUCT, Type.NONE);
    asset.addChild("name", Form.FIELD, Type.STRING);
    asset.addChild("price", Form.FIELD, Type.FLOAT);
    assertEquals(Type.STRING, person.getChild("asset").getChild("name").getType());

    Element personData = new DataElement(person);
    Element nameData = personData.addChild("name");
    nameData.setValue("James wang");
    assertEquals("James wang", nameData.getString());
    Element assetData = personData.addChild("asset");
    assetData.addChild("name").setValue("Mac air book");
    assetData.addChild("price").setValue(12000.05f);
    assertEquals(12000.05f, personData.getChild("asset").getChild("price").getFloat());
  }

  public void testCollection()
  {
    // Create format
    Format root = new DataFormat("root");
    Format person = root.addChild("Person", Form.STRUCT);
    person.addChild("name", Form.FIELD, Type.STRING);
    Format asset = person.addChild("asset", Form.ARRAY);
    asset.addChild("name", Form.FIELD, Type.STRING);
    asset.addChild("price", Form.FIELD, Type.FLOAT);
    asset.addChild("vendor", Form.STRUCT).addChild("name", Form.FIELD, Type.STRING);

    // Fill data
    Element personData = new DataElement(person);
    Element nameData = personData.addChild("name");
    nameData.setValue("James wang");
    assertEquals("James wang", nameData.getString());
    Element assetData = personData.addChild("asset");
    Element assetDataItem = assetData.addArrayItem();
    assetDataItem.addChild("name").setValue("Mac air book");
    assetDataItem.addChild("price").setValue(12000.05f);
    assetDataItem.addChild("vendor").addChild("name").setValue("Apple");
    assertEquals(12000.05f, personData.getChild("asset").getArrayItems().get(0).getChild("price").getFloat());

    // TODO
    nameData = personData.getChildByPath("asset.vendor.name");
    assertNull(nameData);
    nameData = personData.getChildByPath("asset[0].vendor.name");
    assertNull(nameData);
  }

  public void testElementPath()
  {
    Path path = new ElementPath("asset.vendor.name");
    Iterator<String> it = path.iterator();
    assertEquals("asset", it.next());
    assertEquals("vendor", it.next());
    assertEquals("name", it.next());

    path = new ElementPath("asset.'vendor.info'.name");
    it = path.iterator();
    assertEquals("asset", it.next());
    assertEquals("vendor.info", it.next());
    assertEquals("name", it.next());

    Path removed = path.removeLeft(2);
    assertEquals("asset.'vendor.info'", removed.toString());
    path = new ElementPath("asset.'vendor.info'.name");
    removed = path.removeRight(2);
    assertEquals("'vendor.info'.name", removed.toString());
  }
  
  public void testScriptPath()
  {
    //var name = @asset.'vendor.info'[0].name
  }
}
