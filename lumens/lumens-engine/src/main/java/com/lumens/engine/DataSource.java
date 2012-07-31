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
public class DataSource implements Component
{
    private Map<String, Component> toList;
    private String name;
    private Connector connector;
    private Map<String, Format> produceFormats = new HashMap<String, Format>();
    private Map<String, Format> consumeFormats = new HashMap<String, Format>();

    public void configure(Map<String, Object> config)
    {
        connector = new WebServiceConnector();
        connector.configure(config);
    }

    @Override
    public void initialize()
    {
        connector.open();
    }

    @Override
    public void cleanup()
    {
        connector.close();
        produceFormats.clear();
        consumeFormats.clear();
    }

    @Override
    public void to(Component to)
    {
        if (toList == null)
            toList = new HashMap<String, Component>();
        toList.put(to.getName(), to);
    }

    @Override
    public List<ExecuteContext> execute(ExecuteContext context)
    {
        try
        {
            Operation operation = connector.getOperation();
            Format resultFormat = produceFormats.get(context.getResultFormatName());
            List<Element> result = new ArrayList<Element>();
            Object input = context.getInput();
            if (input instanceof List)
            {
                List list = (List) input;
                if (!list.isEmpty() && list.get(0) instanceof Element)
                {
                    List<Element> inputDataList = (List<Element>) list;
                    for (Element data : inputDataList)
                        result.addAll(operation.execute(data, resultFormat));
                }
            }
            else if (input instanceof Element)
                result.addAll(operation.execute((Element) input, resultFormat));
            // TODO need to check which target need this result
            List<ExecuteContext> execList = new ArrayList<ExecuteContext>();
            if (!result.isEmpty())
            {
                for (Component comp : toList.values())
                    execList.add(new ExecuteContextImpl(comp, result, context.
                            getResultFormatName()));
            }
            return execList;
        }
        catch (Exception ex)
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
            consumeFormats.put(formatName, format);
        else
            produceFormats.put(formatName, format);
    }

    public Format removeFormat(String formatName, Usage use)
    {
        if (use == Usage.CONSUME)
            return consumeFormats.remove(formatName);
        else
            return produceFormats.remove(formatName);
    }

    public Map<String, Format> getRegisteredFormatList(Usage use)
    {
        if (use == Usage.CONSUME)
            return consumeFormats;
        else
            return produceFormats;
    }

    void registerConsumeFormat(String consumeName, Format format)
    {
        consumeFormats.put(consumeName, format);
    }

    void registerProduceFormat(String produceName, Format format)
    {
        produceFormats.put(produceName, format);
    }
}
