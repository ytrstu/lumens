/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.processor.transform;

import com.lumens.model.DataElement;
import com.lumens.model.Element;
import com.lumens.model.Format;
import com.lumens.processor.Input;
import com.lumens.processor.Processor;
import com.lumens.processor.ProcessorUtils;
import java.util.Iterator;

/**
 *
 * @author shaofeng wang
 */
public class TransformProcessor implements Processor
{
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
        if (value != null)
        {
            // TODO need to process value
            if (ProcessorUtils.isScript(value))
                value = data.getChildByPath(ProcessorUtils.getPath(value)).getString();
            else
                value = data.getChildByPath(value).getString();
            result.setValue(value);
        }

        Format format = rule.getFormat();
        if (format.isArray())
        {
            String arrayIterationPath = rule.getArrayIterationPath();
            if (arrayIterationPath != null)
            {
            }
        }
        else
        {
            Iterator<TransformRuleItem> it = rule.iterator();
            if (it != null)
            {
                while (it.hasNext())
                {
                    TransformRuleItem ruleItem = it.next();
                    Element child = new DataElement(ruleItem.getFormat());
                    processRuleItem(ctx, data, child, ruleItem);
                    result.addChild(child);
                }
            }
        }
    }
}
