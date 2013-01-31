/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.service.config.datasource;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 *
 * @author shaofeng wang (shaofeng.cjpw@gmail.com)
 */
public interface DatasourceHandler
{
    /**
     *
     * A data element event handling method.
     *
     * @param data value or null
     * @param meta attributes
     */
    public void handle_id(final String data, final Attributes meta) throws SAXException;

    /**
     *
     * A data element event handling method.
     *
     * @param data value or null
     * @param meta attributes
     */
    public void handle_icon(final String data, final Attributes meta) throws SAXException;

    /**
     *
     * A container element start event handling method.
     *
     * @param meta attributes
     */
    public void start_parameter_list(final Attributes meta) throws SAXException;

    /**
     *
     * A container element end event handling method.
     */
    public void end_parameter_list() throws SAXException;

    /**
     *
     * An empty element event handling method.
     *
     * @param data value or null
     */
    public void handle_parameter(final Attributes meta) throws SAXException;

    /**
     *
     * A container element start event handling method.
     *
     * @param meta attributes
     */
    public void start_datasource_list(final Attributes meta) throws SAXException;

    /**
     *
     * A container element end event handling method.
     */
    public void end_datasource_list() throws SAXException;

    /**
     *
     * A data element event handling method.
     *
     * @param data value or null
     * @param meta attributes
     */
    public void handle_name(final String data, final Attributes meta) throws SAXException;

    /**
     *
     * A data element event handling method.
     *
     * @param data value or null
     * @param meta attributes
     */
    public void handle_instance_icon(final String data, final Attributes meta) throws SAXException;

    /**
     *
     * A data element event handling method.
     *
     * @param data value or null
     * @param meta attributes
     */
    public void handle_class_name(final String data, final Attributes meta) throws SAXException;

    /**
     *
     * A container element start event handling method.
     *
     * @param meta attributes
     */
    public void start_datasource(final Attributes meta) throws SAXException;

    /**
     *
     * A container element end event handling method.
     */
    public void end_datasource() throws SAXException;
}
