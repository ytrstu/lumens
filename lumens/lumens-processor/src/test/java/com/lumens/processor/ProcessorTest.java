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
import com.lumens.model.serializer.DataElementXmlSerializer;
import com.lumens.processor.script.JavaScriptBuilder;
import com.lumens.processor.transform.TransformInput;
import com.lumens.processor.transform.TransformProcessor;
import com.lumens.processor.transform.TransformRule;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.commons.io.IOUtils;

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

    private static InputStream getInputStream(String name) throws Exception
    {
        return ProcessorTest.class.getClassLoader().getResourceAsStream(name);
    }

    public void testTransform() throws Exception
    {
        // Create format
        Format person = new DataFormat("Person", Form.STRUCT);
        person.addChild("name", Format.Form.FIELD, Type.STRING);
        Format personAsset = person.addChild("asset", Format.Form.ARRAY);
        personAsset.addChild("name", Format.Form.FIELD, Type.STRING);
        personAsset.addChild("price", Format.Form.FIELD, Type.FLOAT);
        Format name = personAsset.addChild("vendor", Format.Form.STRUCT).addChild("name",
                                                                                  Format.Form.FIELD,
                                                                                  Type.STRING);
        assertEquals("asset.vendor.name", name.getFullPath().toString());

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
        //Format computer = asset.addChild("Computer", Form.STRUCT);
        computer.addChild("name", Form.FIELD, Type.STRING);
        Format cpu = computer.addChild("CPU", Form.ARRAY);
        cpu.addChild("name", Form.FIELD, Type.STRING);
        cpu.addChild("corenumber", Form.FIELD, Type.INT);
        cpu.addChild("speed", Form.FIELD, Type.STRING);

        TransformRule rule = new TransformRule(asset);
        rule.getRuleItem("Computer.name").setScript("@asset.name");
        rule.getRuleItem("Computer").setArrayIterationPath("asset");
        assertEquals("@asset.name", rule.getRuleItem("Computer.name").getScriptString());

        Processor transformProcessor = new TransformProcessor();
        Element result = (Element) transformProcessor.process(new TransformInput(personData, rule));
        assertEquals("Mac air book", result.getChildByPath("Computer.name").getString());
        assertEquals("HP computer", result.getChildByPath("Computer[1].name").getString());

        DataElementXmlSerializer serializer = new DataElementXmlSerializer(result, "UTF-8", true);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        serializer.write(baos);
        System.out.println(baos.toString());
    }

    public void testArrayToArray() throws Exception
    {
        // a.@b.c.@d.e.f --> a1.@a2.a3.@a4.a5 (@b-@a2, @d-@a4)
        // a.@b.c.@d.e.f --> a1.@a2.a3.@a4.a5 (@b-@a2)
        // a.@b.c.@d.e.f --> a1.@a2.a3.@a4.a5 (@d-@a4)
        // a.@b.c.@d.e.f --> a1.@a2.a3.@a4.a5 (@b-@a4) (wrong logic, what will happen ?)
        // a.@b.c.@d.e.f --> a1.@a2.a3.@a4.a5 (none)
        Format a = new DataFormat("a", Form.STRUCT);
        a.addChild("b", Form.ARRAY).addChild("c", Form.STRUCT).addChild("d", Form.ARRAY).addChild(
                "e", Form.STRUCT).addChild("f", Form.FIELD, Type.STRING);
        Format a1 = new DataFormat("a1", Form.STRUCT);
        Format a3 = a1.addChild("a2", Form.ARRAY).addChild("a3", Form.STRUCT);
        a3.addChild("a4", Form.ARRAY).addChild("a5", Form.FIELD, Type.STRING);
        a3.addChild("aa4", Form.ARRAY).addChild("aa5", Form.FIELD, Type.STRING);
        // Fill data into a
        Element a_data = new DataElement(a);
        // array b
        Element b_data = a_data.addChild("b");
        Element[] b_data_item = new Element[4];
        b_data_item[0] = b_data.addArrayItem();
        b_data_item[1] = b_data.addArrayItem();
        b_data_item[2] = b_data.addArrayItem();
        b_data_item[3] = b_data.addArrayItem();
        // array d
        for (int i = 0; i < b_data_item.length; ++i)
        {
            Element d_data = b_data_item[i].addChild("c").addChild("d");
            Element[] d_data_item = new Element[3];
            d_data_item[0] = d_data.addArrayItem();
            d_data_item[1] = d_data.addArrayItem();
            d_data_item[2] = d_data.addArrayItem();
            for (int j = 0; j < d_data_item.length; ++j)
            {
                d_data_item[j].addChild("e").addChild("f").
                        setValue("test-b[" + i + "]." + "d[" + j + ']');
            }
        }
        DataElementXmlSerializer serializer = new DataElementXmlSerializer(a_data, "UTF-8", true);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        serializer.write(baos);
        System.out.println(baos.toString());
        Processor transformProcessor = new TransformProcessor();
        //(@b-@a2, (@d-@a4, @d-@aa4))
        tryMultipleArrayToArrayTransform(transformProcessor, a1, a_data);
        //(@b-@a2)
        tryFirstArrayToFirstArray(transformProcessor, a1, a_data);
        //(@d-@a4)
        trySecondArrayToSecondArray(transformProcessor, a1, a_data);
        // check duplicate iteration path
        tryCheckingDuplicateArrayIterationPathConfiguration(a1);

        // Start ###################Test java script supporting

        // End ###################Test java script supporting
    }

    private void tryMultipleArrayToArrayTransform(Processor transformProcessor, Format dstFormat,
                                                  Element data) throws Exception
    {
        // build the transform rule
        TransformRule rule = new TransformRule(dstFormat);
        rule.getRuleItem("a2.a3.a4.a5").setScript("@b.c.d.e.f");
        rule.getRuleItem("a2.a3.aa4.aa5").setScript("@b.c.d.e.f");
        rule.getRuleItem("a2").setArrayIterationPath("b");
        rule.getRuleItem("a2.a3.a4").setArrayIterationPath("b.c.d");
        rule.getRuleItem("a2.a3.aa4").setArrayIterationPath("b.c.d");

        Element result = (Element) transformProcessor.process(new TransformInput(data, rule));

        DataElementXmlSerializer serializer = new DataElementXmlSerializer(result, "UTF-8", true);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        serializer.write(baos);
        System.out.println("tryMultipleArrayToArrayTransform####1:\n" + baos.toString());

        assertEquals("test-b[3].d[2]", result.getChildByPath("a2[3].a3.a4[2].a5").getString());
        assertEquals("test-b[3].d[2]", result.getChildByPath("a2[3].a3.aa4[2].aa5").getString());
    }

    private void tryFirstArrayToFirstArray(Processor transformProcessor, Format dstFormat,
                                           Element data) throws Exception
    {
        // build the transform rule
        TransformRule rule = new TransformRule(dstFormat);
        rule.getRuleItem("a2.a3.a4.a5").setScript("@b.c.d.e.f");
        rule.getRuleItem("a2.a3.aa4.aa5").setScript("@b.c.d.e.f");
        rule.getRuleItem("a2").setArrayIterationPath("b");

        Element result = (Element) transformProcessor.process(new TransformInput(data, rule));

        DataElementXmlSerializer serializer = new DataElementXmlSerializer(result, "UTF-8", true);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        serializer.write(baos);
        System.out.println("tryFirstArrayToFirstArray####\n" + baos.toString());

        assertEquals("test-b[3].d[0]", result.getChildByPath("a2[3].a3.a4[0].a5").getString());
    }

    private void trySecondArrayToSecondArray(Processor transformProcessor, Format dstFormat,
                                             Element data) throws Exception
    {
        // build the transform rule
        TransformRule rule = new TransformRule(dstFormat);
        rule.getRuleItem("a2.a3.a4.a5").setScript("@b.c.d.e.f");
        rule.getRuleItem("a2.a3.aa4.aa5").setScript("@b.c.d.e.f");
        rule.getRuleItem("a2.a3.a4").setArrayIterationPath("b.c.d");

        Element result = (Element) transformProcessor.process(new TransformInput(data, rule));

        DataElementXmlSerializer serializer = new DataElementXmlSerializer(result, "UTF-8", true);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        serializer.write(baos);
        System.out.println("trySecondArrayToSecondArray####\n" + baos.toString());

        assertEquals("test-b[1].d[0]", result.getChildByPath("a2[0].a3.a4[3].a5").getString());
    }

    private void tryCheckingDuplicateArrayIterationPathConfiguration(Format dstFormat) throws Exception
    {
        // build the transform rule
        TransformRule rule = new TransformRule(dstFormat);
        rule.getRuleItem("a2.a3.a4.a5").setScript("@b.c.d.e.f");
        rule.getRuleItem("a2.a3.aa4.aa5").setScript("@b.c.d.e.f");
        rule.getRuleItem("a2").setArrayIterationPath("b");
        rule.getRuleItem("a2.a3.a4").setArrayIterationPath("b.c.d");
        rule.getRuleItem("a2.a3.aa4").setArrayIterationPath("b.c.d");
        try
        {
            // It will be error, "b.c.d" is used in child element
            rule.getRuleItem("a2").setArrayIterationPath("b.c.d");
            fail();
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            rule.getRuleItem("a2.a3.a4").setArrayIterationPath(null);
            rule.getRuleItem("a2.a3.aa4").setArrayIterationPath(null);
            rule.getRuleItem("a2").setArrayIterationPath("b.c.d");
        }

        try
        {
            // It will be error, "b.c.d" is used in parent element
            rule.getRuleItem("a2.a3.a4").setArrayIterationPath("b.c.d");
            fail();
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    public void testJavaScriptBuilder() throws Exception
    {
        JavaScriptBuilder builder = new JavaScriptBuilder();
        String script = builder.build(IOUtils.toString(getInputStream(
                "test-script/transform-script-test.txt")));
        System.out.println(script);
    }
}
