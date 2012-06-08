/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.processor;

import com.lumens.model.DataElement;
import com.lumens.model.DataFormat;
import com.lumens.model.Element;
import com.lumens.model.Format;
import com.lumens.model.Format.Form;
import com.lumens.model.Type;
import com.lumens.processor.transform.TransformRule;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class ProcessorTest
        extends TestCase
{
  /**
   * Create the test case
   *
   * @param testName name of the test case
   */
  public ProcessorTest(String testName)
  {
    super(testName);
  }

  /**
   * @return the suite of tests being tested
   */
  public static Test suite()
  {
    return new TestSuite(ProcessorTest.class);
  }

  public void testTransformRule()
  {
    // Create format
    Format person = new DataFormat("Person", Form.STRUCT);
    person.addChild("name", Format.Form.FIELD, Type.STRING);
    Format personAsset = person.addChild("asset", Format.Form.ARRAY);
    personAsset.addChild("name", Format.Form.FIELD, Type.STRING);
    personAsset.addChild("price", Format.Form.FIELD, Type.FLOAT);
    Format name = personAsset.addChild("vendor", Format.Form.STRUCT).addChild("name", Format.Form.FIELD, Type.STRING);
    assertEquals("Person.asset.vendor.name", name.getFullPath().toString());

    // Fill data
    Element personData = new DataElement(person);
    Element nameData = personData.addChild("name");
    nameData.setValue("James wang");
    assertEquals("James wang", nameData.getString());
    Element assetData = personData.addChild("asset");
    // personAsset item 1
    Element assetDataItem = assetData.addArrayItem();
    assetDataItem.addChild("name").setValue("Mac air book");
    assetDataItem.addChild("price").setValue(12000.05f);
    assetDataItem.addChild("vendor").addChild("name").setValue("Apple");
    // personAsset item 2
    assetDataItem = assetData.addArrayItem();
    assetDataItem.addChild("name").setValue("HP computer");
    assetDataItem.addChild("price").setValue(15000.05f);
    assetDataItem.addChild("vendor").addChild("name").setValue("HP");

    Format asset = new DataFormat("Asset", Form.STRUCT);
    Format computer = asset.addChild("Computer", Form.ARRAY);
    computer.addChild("name", Form.FIELD, Type.STRING);
    Format cpu = computer.addChild("CPU", Form.ARRAY);
    cpu.addChild("name", Form.FIELD, Type.STRING);
    cpu.addChild("corenumber", Form.FIELD, Type.INT);
    cpu.addChild("speed", Form.FIELD, Type.STRING);

    TransformRule rule = new TransformRule(person, asset);
    rule.getRuleItem("asset.name").setValue("Computer.name");
    rule.getRuleItem("asset").setLoop("Computer");
  }
}
