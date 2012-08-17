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
        List<Element> results = new ArrayList<Element>();
        String target = context.getTarget();
        TransformRule rule = rules.get(target);
        target = rule.getName();
        Object input = context.getInput();
        if (to.accept(target))
        {
            if (input instanceof List)
            {
                List list = (List) input;
                if (!list.isEmpty() && list.get(0) instanceof Element)
                {
                    List<Element> inputs = (List<Element>) input;
                    for (Element data : inputs)
                    {
                        List<Element> result = (List<Element>) processor.execute(rule, data);
                        if (!result.isEmpty())
                            results.addAll(result);
                    }
                }
            }
            else if (input instanceof Element || input == null)
            {
                Element data = input == null ? null : (Element) input;
                List<Element> result = (List<Element>) processor.execute(rule, data);
                if (!result.isEmpty())
                    results.addAll(result);
            }

            if (!results.isEmpty())
                execList.add(new ExecuteContextImpl(to, results, target));
        }
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

    @Override
    public boolean accept(String name)
    {
        return rules.containsKey(name);
    }
}
