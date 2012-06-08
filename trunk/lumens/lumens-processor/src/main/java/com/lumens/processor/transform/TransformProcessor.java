/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.processor.transform;

import com.lumens.model.Element;
import com.lumens.model.Format;
import com.lumens.processor.Input;
import com.lumens.processor.Processor;
import com.lumens.processor.Result;
import java.util.Iterator;

/**
 *
 * @author shaofeng wang
 */
public class TransformProcessor implements Processor
{
    @Override
    public Result process(Input input)
    {
        if (input instanceof TransformInput)
        {
            TransformInput transInput = (TransformInput) input;
            Element data = transInput.getData();
            TransformRuleItem ruleEntry = transInput.getRule().getRuleEntry();
            Format formatEntry = ruleEntry.getFormat();
            processRuleItem(data, formatEntry, ruleEntry);
        }
        return null;
    }

    private void processRuleItem(Element data, Format formatEntry, TransformRuleItem ruleEntry)
    {
        Iterator<TransformRuleItem> item = ruleEntry.iterator();
    }
}
