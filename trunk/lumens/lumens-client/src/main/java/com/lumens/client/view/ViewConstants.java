/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lumens.client.view;

import com.google.gwt.core.client.GWT;
import com.lumens.client.i18n.Clienti18nConstants;

/**
 *
 * @author washaofe
 */
public interface ViewConstants
{
    public Clienti18nConstants messages = GWT.create(Clienti18nConstants.class);
    public String BACKGROUD_COLOR = "#fbfbfb";
    public String DATASOURCE_SECTION_ID = "Datasource_section_ID";
    public String PROCESSOR_SECTION_ID = "Processor_section_ID";
    public String DATASOURCE_NAME = "DataSourceName";
    //Temp constants
    public String webserviceID = "WebserviceSOAP-Datasource";
    public String databaseID = "Database-Datasource";
    public String transformPrID = "Transform-Processor";
}
