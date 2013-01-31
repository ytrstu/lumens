/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.service.config.datasource;

import com.lumens.client.rpc.beans.CComponentParameter;
import com.lumens.client.rpc.beans.CComponentTypeRegistry;
import java.util.List;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 *
 * @author shaofeng wang (shaofeng.cjpw@gmail.com)
 */
public class DatasourceHandlerImpl implements DatasourceHandler
{
    public static final boolean DEBUG = false;
    private List<CComponentTypeRegistry> datasourceConfig;
    private CComponentTypeRegistry currentComponent;

    public DatasourceHandlerImpl(List<CComponentTypeRegistry> datasourceConfig)
    {
        this.datasourceConfig = datasourceConfig;
    }

    @Override
    public void handle_id(final String data, final Attributes meta) throws SAXException
    {
        currentComponent.setId(data);
    }

    @Override
    public void handle_icon(final String data, final Attributes meta) throws SAXException
    {
        currentComponent.setIcon(data);
    }

    @Override
    public void start_parameter_list(final Attributes meta) throws SAXException
    {
    }

    @Override
    public void end_parameter_list() throws SAXException
    {
    }

    @Override
    public void handle_parameter(final Attributes meta) throws SAXException
    {
        CComponentParameter param = new CComponentParameter();
        Object value = meta.getValue("name");
        if (value != null)
            param.setId((String) value);
        value = meta.getValue("label");
        if (value != null)
            param.setName((String) value);
        value = meta.getValue("javatype");
        if (value != null)
            param.setJavatype((String) value);
        value = meta.getValue("encrypted");
        if (value != null)
            param.setEncrypted(Boolean.parseBoolean((String) value));
        this.currentComponent.addParameter(param);
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
    public void handle_name(final String data, final Attributes meta) throws SAXException
    {
        currentComponent.setName(data);
    }

    @Override
    public void handle_instance_icon(final String data, final Attributes meta) throws SAXException
    {
        currentComponent.setInstanceIcon(data);
    }

    @Override
    public void handle_class_name(final String data, final Attributes meta) throws SAXException
    {
        currentComponent.setClassName(data);
    }

    @Override
    public void start_datasource(final Attributes meta) throws SAXException
    {
        currentComponent = new CComponentTypeRegistry();
    }

    @Override
    public void end_datasource() throws SAXException
    {
        datasourceConfig.add(currentComponent);
        currentComponent = null;
    }
}
