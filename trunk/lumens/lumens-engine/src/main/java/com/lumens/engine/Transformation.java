/*
 * To change this template, choose Tools | Templates
 * and initialize the template in the editor.
 */
package com.lumens.engine;

import com.lumens.model.Element;
import com.lumens.processor.Processor;
import com.lumens.processor.transform.TransformProcessor;
import com.lumens.processor.transform.TransformRule;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author shaofeng wang
 */
public class Transformation implements Component
{
    private String name;
    private Processor processor;
    private Component to;
    private Map<String, TransformRule> rules = new HashMap<String, TransformRule>();

    @Override
    public void to(Component to)
    {
        this.to = to;
    }

    public void registerRule(String ruleName, TransformRule rule)
    {
        rules.put(ruleName, rule);
    }

    public TransformRule removeRule(String ruleName)
    {
        return rules.remove(ruleName);
    }

    @Override
    public List<ExecuteContext> execute(ExecuteContext context)
    {
        List<ExecuteContext> execList = new ArrayList<ExecuteContext>();
        TransformRule rule = rules.get(context.getResultFormatName());
        Object result = processor.execute(rule, (Element) context.getInput());
        execList.add(new ExecuteContextImpl(to, result, context.getResultFormatName()));
        return execList;
    }

    @Override
    public void initialize()
    {
        processor = new TransformProcessor();
    }

    @Override
    public void cleanup()
    {
        processor = null;
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public void setName(String name)
    {
        this.name = name;
    }
}
