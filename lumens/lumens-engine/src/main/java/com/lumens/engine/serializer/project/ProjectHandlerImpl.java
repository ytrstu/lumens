/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.engine.serializer.project;

import com.lumens.connector.Direction;
import com.lumens.engine.TransformComponent;
import com.lumens.engine.TransformProject;
import com.lumens.engine.component.DataSource;
import com.lumens.engine.component.FormatEntry;
import com.lumens.model.Format;
import com.lumens.model.Type;
import com.lumens.model.Value;
import com.lumens.model.serializer.parser.FormatHandler;
import com.lumens.model.serializer.parser.FormatHandlerImpl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        DATATRS,
        FORMAT
    }
    private TransformProject project;
    private TransformComponent tc;
    private Map<String, Value> propList;
    private Map<String, FormatEntry> registeredFormatList;
    private FormatEntry formatEntry;
    private List<Format> formatList;
    private FormatHandler formatHandler;
    private ReadStatus status = ReadStatus.NONE;

    public ProjectHandlerImpl(TransformProject project)
    {
        this.project = project;
    }

    @Override
    public void start_transform_rule_item(final Attributes meta) throws SAXException
    {
    }

    @Override
    public void end_transform_rule_item() throws SAXException
    {
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
    public void start_target_list(final Attributes meta) throws SAXException
    {
    }

    @Override
    public void end_target_list() throws SAXException
    {
    }

    @Override
    public void start_transform_rule(final Attributes meta) throws SAXException
    {
    }

    @Override
    public void end_transform_rule() throws SAXException
    {
    }

    @Override
    public void handle_script(final String data, final Attributes meta) throws SAXException
    {
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
    public void start_property_list(final Attributes meta) throws SAXException
    {
        if (status == ReadStatus.DATASRC)
        {
            propList = new HashMap<String, Value>();
        }
    }

    @Override
    public void handle_property(final String data, final Attributes meta) throws SAXException
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
    public void start_transform_rule_list(final Attributes meta) throws SAXException
    {
    }

    @Override
    public void end_transform_rule_list() throws SAXException
    {
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
        status = ReadStatus.NONE;
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
    public void handle_description(final String data, final Attributes meta) throws SAXException
    {
        if (status == ReadStatus.PROJECT)
            project.setDescription(data);
        else
            tc.setDescription(data);
    }

    @Override
    public void handle_target(final Attributes meta) throws SAXException
    {
    }

    @Override
    public void start_processor(final Attributes meta) throws SAXException
    {
    }

    @Override
    public void end_processor() throws SAXException
    {
    }

    @Override
    public void start_datasource(final Attributes meta) throws SAXException
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
    }

    @Override
    public void end_datasource() throws SAXException
    {
        if (status == ReadStatus.DATASRC)
            project.getDatasourceList().add((DataSource) tc);
        else
            throw new SAXException(
                    "Wrong xml configuration, the current component is not a datasource!");
        tc = null;
        status = ReadStatus.PROJECT;
    }
}
