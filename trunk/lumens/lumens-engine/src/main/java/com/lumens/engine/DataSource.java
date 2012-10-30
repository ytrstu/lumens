/*
 * To change this template, choose Tools | Templates
 * and initialize the template in the editor.
 */
package com.lumens.engine;

import com.lumens.connector.Connector;
import com.lumens.connector.Operation;
import com.lumens.connector.Usage;
import com.lumens.connector.webservice.WebServiceConnector;
import com.lumens.model.Element;
import com.lumens.model.Format;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author shaofeng wang
 */
public class DataSource implements TransformComponent
{
    private Map<String, TransformComponent> toList;
    private String name;
    private Connector connector;
    private Map<String, Format> registerProduceFormatList = new HashMap<String, Format>();
    private Map<String, Format> registerConsumeFormatList = new HashMap<String, Format>();
    private Map<String, Format> consumeFormatList;
    private Map<String, Format> produceFormatList;

    public void setParameters(Map<String, Object> parameters)
    {
        connector = new WebServiceConnector();
        connector.setParameters(parameters);
    }

    @Override
    public void initialize()
    {
        connector.open();
        consumeFormatList = connector.getFormatList(Usage.CONSUME);
        produceFormatList = connector.getFormatList(Usage.PRODUCE);
    }

    public Map<String, Format> getFormatList(Usage usage)
    {
        return usage == Usage.CONSUME ? consumeFormatList : produceFormatList;
    }

    @Override
    public void cleanup()
    {
        connector.close();
        registerProduceFormatList.clear();
        registerConsumeFormatList.clear();
    }

    @Override
    public void to(TransformComponent to)
    {
        if (toList == null)
            toList = new HashMap<String, TransformComponent>();
        toList.put(to.getName(), to);
    }

    @Override
    public Map<String, TransformComponent> getToList()
    {
        return toList;
    }

    @Override
    public List<ExecuteContext> execute(ExecuteContext context)
    {
        try
        {
            String targetFormatName = context.getTargetFormatName();
            Format targetFormat = registerProduceFormatList.
                    get(targetFormatName);
            List<Element> result = new ArrayList<Element>();
            Object input = context.getInput();
            Operation operation = connector.getOperation();
            if (input instanceof List)
            {
                List list = (List) input;
                if (!list.isEmpty() && list.get(0) instanceof Element)
                {
                    List<Element> inputDataList = (List<Element>) list;
                    for (Element data : inputDataList)
                        result.addAll(operation.execute(data, targetFormat));
                }
            } else if (input instanceof Element)
            {
                result.addAll(operation.execute((Element) input, targetFormat));
            }
            List<ExecuteContext> exList = new ArrayList<ExecuteContext>();
            exList.add(new TransformExecuteContext(result, targetFormatName));
            return exList;
        } catch (Exception ex)
        {
            throw new RuntimeException(ex);
        }
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

    public Connector getConnector()
    {
        return connector;
    }

    public void registerFormat(String formatName, Format format, Usage use)
    {
        if (use == Usage.CONSUME)
            registerConsumeFormatList.put(formatName, format);
        else
            registerProduceFormatList.put(formatName, format);
    }

    public Format removeFormat(String formatName, Usage use)
    {
        if (use == Usage.CONSUME)
            return registerConsumeFormatList.remove(formatName);
        else
            return registerProduceFormatList.remove(formatName);
    }

    public Map<String, Format> getRegisteredFormatList(Usage use)
    {
        if (use == Usage.CONSUME)
            return registerConsumeFormatList;
        else
            return registerProduceFormatList;
    }

    public void registerConsumeFormat(String consumeName, Format format)
    {
        registerConsumeFormatList.put(consumeName, format);
    }

    public void registerProduceFormat(String produceName, Format format)
    {
        registerProduceFormatList.put(produceName, format);
    }

    @Override
    public boolean accept(ExecuteContext ctx)
    {
        return registerConsumeFormatList.containsKey(ctx.getTargetFormatName());
    }

    @Override
    public boolean hasTo()
    {
        return !toList.isEmpty();
    }

    @Override
    public boolean isSingleTo()
    {
        return false;
    }
}
