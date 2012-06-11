/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.processor.transform;

import com.lumens.model.DataElement;
import com.lumens.model.Element;
import com.lumens.model.Format;
import com.lumens.model.Format.Form;
import com.lumens.model.Type;
import com.lumens.processor.Input;
import com.lumens.processor.Processor;
import com.lumens.processor.ProcessorUtils;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author shaofeng wang
 */
public class TransformProcessor implements Processor
{
    private boolean generateNullElement = Boolean.getBoolean("transform.gen.null");

    @Override
    public Object process(Input input)
    {
        if (input instanceof TransformInput)
        {
            TransformInput transInput = (TransformInput) input;
            Element data = transInput.getData();
            TransformRuleItem ruleEntry = transInput.getRule().getRuleEntry();
            TransformContext ctx = new TransformContext();
            Format formatEntry = ruleEntry.getFormat();
            Element result = new DataElement(formatEntry);
            processRuleItem(ctx, data, result, ruleEntry);
            return result;
        }
        return null;
    }

    private void processRuleItem(TransformContext ctx, Element data, Element result,
                                 TransformRuleItem rule)
    {
        String value = rule.getValue();
        Format format = result.getFormat();
        if (result.isArray())
        {
            String arrayIterationPath = rule.getArrayIterationPath();
            if (arrayIterationPath != null)
            {
                Element array = data.getChildByPath(arrayIterationPath);
                if (!array.isArray())
                    throw new IllegalArgumentException("Wrong array iteration path \"" + arrayIterationPath + "\"");
                List<Element> items = array.getArrayItems();
                if (items == null)
                    return;
                for (int index = 0; index < items.size(); ++index)
                {
                    Element item = result.newArrayItem();
                    processRuleItem(ctx, data, item, rule);
                    if (generateNullElement || item.getChildren() != null)
                        result.addArrayItem(item);
                }
            }
        }
        else
        {
            if (value != null)
            {
                // TODO need to process value
                if (ProcessorUtils.isScript(value))
                    value = data.getChildByPath(ProcessorUtils.getPath(value)).getString();
                else
                    value = data.getChildByPath(value).getString();
                if (format.getType() != Type.NONE)
                    result.setValue(value);
            }
            Iterator<TransformRuleItem> it = rule.iterator();
            if (it != null)
            {
                while (it.hasNext())
                {
                    TransformRuleItem ruleItem = it.next();
                    Element child = new DataElement(ruleItem.getFormat());
                    processRuleItem(ctx, data, child, ruleItem);
                    if (generateNullElement
                        || child.getFormat().getForm() == Form.FIELD
                        || child.getArrayItems() != null
                        || child.getChildren() != null)
                        result.addChild(child);
                }
            }
        }
    }
}
