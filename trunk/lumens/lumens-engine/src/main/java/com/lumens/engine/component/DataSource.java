/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.engine.component;

import com.lumens.connector.Connector;
import com.lumens.connector.Direction;
import com.lumens.connector.Operation;
import com.lumens.connector.OperationResult;
import com.lumens.engine.TransformExecuteContext;
import com.lumens.engine.run.ExecuteContext;
import com.lumens.model.Element;
import com.lumens.model.Format;
import com.lumens.model.Value;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author shaofeng wang (shaofeng.cjpw@gmail.com)
 */
public class DataSource extends AbstractTransformComponent
{
    private String name;
    private String className;
    private String description;
    private Connector connector;
    private Map<String, FormatEntry> registerProduceFormatList = new HashMap<String, FormatEntry>();
    private Map<String, FormatEntry> registerConsumeFormatList = new HashMap<String, FormatEntry>();
    private Map<String, Format> consumeFormatList;
    private Map<String, Format> produceFormatList;
    private Map<String, Value> propertyList;

    public DataSource(String className)
    {
        this.className = className;
    }

    @Override
    public String getClassName()
    {
        return className;
    }

    public void setPropertyList(Map<String, Value> propertyList)
    {
        this.propertyList = propertyList;
    }

    public Map<String, Value> getPropertyList()
    {
        return propertyList;
    }

    @Override
    public void open() throws Exception
    {
        connector = (Connector) Class.forName(className).newInstance();
        connector.setPropertyList(propertyList);
        connector.open();
        consumeFormatList = connector.getFormatList(Direction.IN);
        produceFormatList = connector.getFormatList(Direction.OUT);
    }

    public Map<String, Format> getFormatList(Direction direction)
    {
        return direction == Direction.IN ? consumeFormatList : produceFormatList;
    }

    @Override
    public void close()
    {
        connector.close();
        registerProduceFormatList.clear();
        registerConsumeFormatList.clear();
    }

    @Override
    public List<ExecuteContext> execute(ExecuteContext context)
    {
        try
        {
            String targetName = context.getTargetName();
            FormatEntry entry = registerProduceFormatList.get(targetName);
            Format targetFormat = entry.getFormat();
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
                    {
                        OperationResult opRet = operation.execute(data);
                        result.addAll(opRet.getResult(targetFormat));
                    }
                }
            } else if (input instanceof Element)
            {
                OperationResult opRet = operation.execute((Element) input);
                result.addAll(opRet.getResult(targetFormat));
            }
            List<ExecuteContext> exList = new ArrayList<ExecuteContext>();
            exList.add(new TransformExecuteContext(result, entry.getName()));
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

    public void registerFormat(String formatId, Format format, Direction direction)
    {
        if (direction == Direction.IN)
            registerConsumeFormatList.put(formatId, new FormatEntry(formatId, format, Direction.IN));
        else
            registerProduceFormatList.
                    put(formatId, new FormatEntry(formatId, format, Direction.OUT));
    }

    public FormatEntry removeFormat(String formatId, Direction direction)
    {
        if (direction == Direction.IN)
        {
            return registerConsumeFormatList.remove(formatId);
        } else
            return registerProduceFormatList.remove(formatId);
    }

    public Map<String, FormatEntry> getRegisteredFormatList(Direction direction)
    {
        if (direction == Direction.IN)
            return registerConsumeFormatList;
        else
            return registerProduceFormatList;
    }

    @Override
    public boolean accept(ExecuteContext ctx)
    {
        return registerConsumeFormatList.containsKey(ctx.getTargetName());
    }

    @Override
    public boolean isSingleTarget()
    {
        return false;
    }
}
