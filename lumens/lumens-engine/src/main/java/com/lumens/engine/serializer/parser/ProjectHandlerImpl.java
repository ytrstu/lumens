/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.engine.serializer.parser;

import com.lumens.connector.Direction;
import com.lumens.engine.TransformComponent;
import com.lumens.engine.TransformProject;
import com.lumens.engine.component.DataSource;
import com.lumens.engine.component.DataTransformation;
import com.lumens.engine.component.FormatEntry;
import com.lumens.engine.component.RegisterFormatComponent;
import com.lumens.engine.component.RuleComponent;
import com.lumens.engine.component.TransformRuleEntry;
import com.lumens.model.Format;
import com.lumens.model.Type;
import com.lumens.model.Value;
import com.lumens.model.serializer.parser.FormatHandler;
import com.lumens.model.serializer.parser.FormatHandlerImpl;
import com.lumens.processor.transform.TransformRule;
import com.lumens.processor.transform.serializer.parser.TransformRuleHandler;
import com.lumens.processor.transform.serializer.parser.TransformRuleHandlerImpl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 *
 * @author shaofeng wang (shaofeng.cjpw@gmail.com)
 */
public class ProjectHandlerImpl implements ProjectHandler
{
    public enum ReadStatus
    {
        NONE,
        PROJECT,
        DATASRC,
        DATAPSR,
        FORMAT
    }
    // Project
    private ReadStatus status = ReadStatus.NONE;
    private TransformProject project;
    private Map<String, TransformComponent> tComponentCache = new HashMap<String, TransformComponent>();
    private Map<String, List<String>> linkInfoList = new HashMap<String, List<String>>();
    private TransformComponent tc;
    // Format
    private FormatHandler formatHandler;
    private Map<String, Value> propList;
    private Map<String, FormatEntry> registeredFormatList;
    private FormatEntry formatEntry;
    private List<Format> formatList;
    // Rule
    private TransformRuleHandler transformRuleHandler;
    private TransformRuleEntry curRuleEntry;
    private String curComponentTargetName;
    private List<TransformRule> ruleList;

    public ProjectHandlerImpl(TransformProject project)
    {
        this.project = project;
    }

    @Override
    public void start_project(final Attributes meta) throws SAXException
    {
        status = ReadStatus.PROJECT;
        String name = meta.getValue("name");
        if (name == null)
            throw new SAXException("Project name can not be empty !");
        project.setName(name);
    }

    @Override
    public void end_project() throws SAXException
    {
        Iterator<Entry<String, List<String>>> it = linkInfoList.entrySet().iterator();
        while (it.hasNext())
        {
            Entry<String, List<String>> entry = it.next();
            TransformComponent s = tComponentCache.get(entry.getKey());
            for (String targetName : entry.getValue())
            {
                TransformComponent t = tComponentCache.get(targetName);
                if (t != null)
                    s.targetTo(t);
            }
        }
        status = ReadStatus.NONE;
    }

    @Override
    public void handle_description(final String data,
                                   final Attributes meta) throws SAXException
    {
        if (status == ReadStatus.PROJECT && data != null)
            project.setDescription(data.trim());
        else if (data != null)
            tc.setDescription(data.trim());
    }

    @Override
    public void handle_position(final Attributes meta) throws SAXException
    {
        if (tc != null)
        {
            tc.setX(Integer.parseInt(meta.getValue("x")));
            tc.setY(Integer.parseInt(meta.getValue("y")));
        }
    }

    @Override
    public void start_datasource_list(final Attributes meta) throws SAXException
    {
    }

    @Override
    public void end_datasource_list() throws SAXException
    {
    }

    @Override
    public void start_datasource(final Attributes meta) throws SAXException
    {
        if (status == ReadStatus.PROJECT)
        {
            status = ReadStatus.DATASRC;
            String className = meta.getValue("class-name");
            if (className == null || className.isEmpty())
                throw new SAXException("Class name can not be empty !");
            tc = new DataSource(className);
            String name = meta.getValue("name");
            if (name == null || name.isEmpty())
                throw new SAXException("Data source name can not be empty !");
            tc.setName(name);
            tComponentCache.put(tc.getName(), tc);
        } else
            throw new SAXException("Error status \'" + status + "\' to read datasource list!");
    }

    @Override
    public void end_datasource() throws SAXException
    {
        if (status == ReadStatus.DATASRC)
            project.getDatasourceList().add((DataSource) tc);
        else
            throw new SAXException("Error status \'" + status + "\' to read datasource list!");
        tc = null;
        status = ReadStatus.PROJECT;
    }

    @Override
    public void start_format_list(final Attributes meta) throws SAXException
    {
        if (status == ReadStatus.DATASRC)
        {
            registeredFormatList = ((DataSource) tc).getRegisteredFormatList(
                    Direction.valueOf(meta.getValue("direction")));
        }
    }

    @Override
    public void end_format_list() throws SAXException
    {
        registeredFormatList = null;
    }

    @Override
    public void start_format_entry(final Attributes meta) throws SAXException
    {
        if (status == ReadStatus.DATASRC && registeredFormatList != null)
        {
            formatEntry = new FormatEntry(meta.getValue("name"), null,
                                          Direction.valueOf(meta.getValue("direction")));
            registeredFormatList.put(formatEntry.getName(), formatEntry);
            if (formatList == null)
            {
                formatList = new ArrayList<Format>();
                formatHandler = new FormatHandlerImpl(formatList);
            }
            status = ReadStatus.FORMAT;
        }
    }

    @Override
    public void end_format_entry() throws SAXException
    {
        if (status == ReadStatus.FORMAT && formatEntry != null
            && formatList != null && !formatList.isEmpty())
        {
            formatEntry.setFormat(formatList.get(0));
        }

        formatEntry = null;
        formatList.clear();
        status = ReadStatus.DATASRC;
    }

    @Override
    public void start_format(final Attributes meta) throws SAXException
    {
        if (status == ReadStatus.FORMAT && formatHandler != null)
            formatHandler.start_format(meta);
    }

    @Override
    public void end_format() throws SAXException
    {
        if (status == ReadStatus.FORMAT && formatHandler != null)
            formatHandler.end_format();
    }

    @Override
    public void start_property_list(final Attributes meta) throws SAXException
    {
        if (status == ReadStatus.DATASRC)
        {
            propList = new HashMap<String, Value>();
        }
    }

    @Override
    public void end_property_list() throws SAXException
    {
        if (tc != null && status == ReadStatus.DATASRC)
        {
            DataSource ds = (DataSource) tc;
            ds.setPropertyList(propList);
        }
        propList = null;
    }

    @Override
    public void handle_property(final String data,
                                final Attributes meta) throws SAXException
    {
        if (status == ReadStatus.DATASRC && propList != null)
        {
            propList.put(meta.getValue("name"),
                         new Value(Type.parseString(meta.getValue("type")), data));
        } else if (status == ReadStatus.FORMAT && formatHandler != null)
        {
            formatHandler.handle_property(data, meta);
        }
    }

    @Override
    public void start_target_list(final Attributes meta) throws SAXException
    {
    }

    @Override
    public void end_target_list() throws SAXException
    {
    }

    @Override
    public void handle_target(final Attributes meta) throws SAXException
    {
        if (tc != null)
        {
            String srcName = tc.getName();
            List<String> targetList = linkInfoList.get(srcName);
            if (targetList == null)
            {
                targetList = new ArrayList<String>();
                linkInfoList.put(srcName, targetList);
            }
            curComponentTargetName = meta.getValue("name");
            targetList.add(curComponentTargetName);
        }
    }

    @Override
    public void start_processor_list(final Attributes meta) throws SAXException
    {
    }

    @Override
    public void end_processor_list() throws SAXException
    {
    }

    @Override
    public void start_processor(final Attributes meta) throws SAXException
    {
        if (status == ReadStatus.PROJECT)
        {
            status = ReadStatus.DATAPSR;
            String className = meta.getValue("class-name");
            if (className == null || className.isEmpty())
                throw new SAXException("Error, the property 'class-name' is empty !");
            try
            {
                tc = (TransformComponent) (Class.forName(className).newInstance());
                tc.setName(meta.getValue("name"));
                tComponentCache.put(tc.getName(), tc);
            } catch (Exception e)
            {
                throw new SAXException(e);
            }
        } else
            throw new SAXException("Error status \'" + status + "\' to read processor list!");
    }

    @Override
    public void end_processor() throws SAXException
    {
        if (status == ReadStatus.DATAPSR)
            project.getDataTransformationList().add((DataTransformation) tc);
        status = ReadStatus.PROJECT;
    }

    @Override
    public void start_transform_rule_list(final Attributes meta) throws SAXException
    {
    }

    @Override
    public void end_transform_rule_list() throws SAXException
    {
    }

    @Override
    public void start_transform_rule_entry(Attributes meta) throws SAXException
    {
        if (status == ReadStatus.DATAPSR)
        {
            curRuleEntry = new TransformRuleEntry(meta.getValue("name"),
                                                  meta.getValue("source-name"),
                                                  meta.getValue("target-name"));
        }
    }

    @Override
    public void end_transform_rule_entry() throws SAXException
    {
        if (status == ReadStatus.DATAPSR)
        {
            if (tc instanceof RuleComponent && ruleList != null && !ruleList.isEmpty())
            {
                curRuleEntry.setRule(ruleList.get(0));
                ((RuleComponent) tc).registerRule(curRuleEntry);
            }
        }
    }

    @Override
    public void start_transform_rule(final Attributes meta) throws SAXException
    {
        if (status == ReadStatus.DATAPSR)
        {
            if (tc.isSingleTarget() && tc instanceof RuleComponent)
            {
                TransformComponent tComp = tComponentCache.get(curComponentTargetName);
                if (tComp instanceof RegisterFormatComponent)
                {
                    RegisterFormatComponent rfComp = (RegisterFormatComponent) tComp;
                    FormatEntry fEntry = rfComp.getRegisteredFormatList(Direction.IN).
                            get(curRuleEntry.getTargetName());
                    Format format = fEntry.getFormat();
                    ruleList = new ArrayList<TransformRule>();
                    transformRuleHandler = new TransformRuleHandlerImpl(format, ruleList);
                    transformRuleHandler.start_transform_rule(meta);
                }
            } else
                throw new SAXException("Error, the current component is not a rule component !");
        }
    }

    @Override
    public void end_transform_rule() throws SAXException
    {
        if (status == ReadStatus.DATAPSR)
        {
            if (transformRuleHandler != null)
                transformRuleHandler.end_transform_rule();
        }
    }

    @Override
    public void start_transform_rule_item(final Attributes meta) throws SAXException
    {
        if (status == ReadStatus.DATAPSR)
        {
            if (transformRuleHandler != null)
                transformRuleHandler.start_transform_rule_item(meta);
        }
    }

    @Override
    public void end_transform_rule_item() throws SAXException
    {
        if (status == ReadStatus.DATAPSR)
        {
            if (transformRuleHandler != null)
                transformRuleHandler.end_transform_rule_item();
        }
    }

    @Override
    public void handle_script(final String data,
                              final Attributes meta) throws SAXException
    {
        if (status == ReadStatus.DATAPSR)
        {
            if (transformRuleHandler != null)
                transformRuleHandler.handle_script(data, meta);
        }
    }
}
