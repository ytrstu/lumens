package com.lumens.engine;

import com.lumens.model.Element;
import com.lumens.processor.Processor;
import com.lumens.processor.transform.TransformProcessor;
import com.lumens.processor.transform.TransformRule;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author shaofeng wang
 */
public class Transformation implements TransformComponent
{
    private String name;
    private Processor processor;
    private TransformComponent to;
    private List<TransformRule> ruleList = new ArrayList<TransformRule>();
    private Map<String, List<TransformRule>> ruleFindList = new HashMap<String, List<TransformRule>>();

    @Override
    public void to(TransformComponent to)
    {
        this.to = to;
    }

    public void registerRule(TransformRule rule)
    {
        for (TransformRule r : ruleList)
            if (r.getName().equals(rule.getName()))
                return;
        ruleList.add(rule);
        List<TransformRule> rules = ruleFindList.get(rule.getSourceName());
        if (rules == null)
        {
            rules = new ArrayList<TransformRule>();
            ruleFindList.put(rule.getSourceName(), rules);
        }
        rules.add(rule);
    }

    public TransformRule removeRule(String ruleName)
    {
        Iterator<TransformRule> it = ruleList.iterator();
        while (it.hasNext())
        {
            TransformRule rule = it.next();
            if (rule.getName().equals(ruleName))
            {
                it.remove();
                List<TransformRule> rules = ruleFindList.get(rule.
                        getSourceName());
                if (rules == null)
                    throw new RuntimeException("Not found");
                rules.remove(rule);
                return rule;
            }
        }
        return null;
    }

    @Override
    public List<ExecuteContext> execute(ExecuteContext context)
    {
        List<Element> results = new ArrayList<Element>();
        String targetFormatName = context.getTargetFormatName();
        List<TransformRule> rules = ruleFindList.get(targetFormatName);
        List<ExecuteContext> exList = new ArrayList<ExecuteContext>();
        Object input = context.getInput();
        for (TransformRule rule : rules)
        {
            if (input != null && input instanceof List)
            {
                List list = (List) input;
                if (!list.isEmpty() && list.get(0) instanceof Element)
                {
                    List<Element> inputs = (List<Element>) input;
                    for (Element data : inputs)
                    {
                        List<Element> result = (List<Element>) processor.
                                execute(rule, data);
                        if (!result.isEmpty())
                            results.addAll(result);
                    }
                }
            } else if (input == null || input instanceof Element)
            {
                Element data = input == null ? null : (Element) input;
                List<Element> result = (List<Element>) processor.execute(
                        rule, data);
                if (!result.isEmpty())
                    results.addAll(result);
            }
            exList.
                    add(
                    new TransformExecuteContext(results, rule.getDesinationName()));
        }
        return exList;
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
    public boolean accept(ExecuteContext ctx)
    {
        return ruleFindList.containsKey(ctx.getTargetFormatName());
    }

    @Override
    public Map<String, TransformComponent> getToList()
    {
        Map<String, TransformComponent> toList = new HashMap<String, TransformComponent>();
        toList.put(to.getName(), to);
        return toList;
    }

    @Override
    public boolean hasTo()
    {
        return to != null;
    }

    @Override
    public boolean isSingleTo()
    {
        return true;
    }
}
