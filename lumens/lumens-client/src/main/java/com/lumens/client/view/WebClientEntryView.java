/*
 * (C) Copyright Hewlett-Packard Development Company, L.P. All Rights Reserved.
 */
package com.lumens.client.view;

import com.smartgwt.client.types.SelectionType;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.Layout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

/**
 *
 * TODO Build the root panel Web view
 */
public class WebClientEntryView extends VLayout implements ClickHandler
{
    private Label title;
    private Layout header;
    private ToolStrip toolbar;
    private WebClientManagementView managementView;
    private DataTransformationBuilderView dataTransformationBuilderView;
    private ToolStripButton buttonManagement;
    private ToolStripButton buttonDataTransformationDesigner;
    private WebClientView currentView;

    private WebClientEntryView()
    {
        super();
    }

    public static WebClientEntryView build()
    {
        WebClientEntryView view = new WebClientEntryView();
        view.setHeight100();
        view.setWidth100();
        view.buildHeader();
        view.buildToolBar();
        // Default management view
        view.buildManagementView();
        view.buildScenarioBuilderView();
        return view;
    }

    private void buildHeader()
    {
        header = new Layout();
        header.setStyleName("lumens_headerbar");

        Img logo = new Img("lumens.png", 74, 45);
        header.setLayoutLeftMargin(10);
        header.addMember(logo);
        title = new Label("Data Transform System");
        title.setStyleName("lumens_title");
        title.setWidth(300);
        header.addMember(title);
        header.setWidth100();
        header.setHeight(45);
        addMember(header);
    }

    private void buildToolBar()
    {
        toolbar = new ToolStrip();
        buttonManagement = new ToolStripButton();
        buttonManagement.setActionType(SelectionType.RADIO);
        buttonManagement.setRadioGroup("Console");
        buttonManagement.setTitle("Management");
        buttonManagement.addClickHandler(this);
        toolbar.addButton(buttonManagement);
        buttonDataTransformationDesigner = new ToolStripButton();
        buttonDataTransformationDesigner.setTitle("Transform Designer");
        buttonDataTransformationDesigner.setActionType(SelectionType.RADIO);
        buttonDataTransformationDesigner.setRadioGroup("Console");
        buttonDataTransformationDesigner.addClickHandler(this);
        toolbar.addButton(buttonDataTransformationDesigner);
        toolbar.setWidth100();
        toolbar.setHeight(40);
        addMember(toolbar);
    }

    @Override
    public void onClick(ClickEvent event)
    {
        if (buttonManagement == event.getSource())
        {
            if (currentView != managementView)
            {
                currentView.setActive(false);
                managementView.setActive(buttonManagement.isSelected());
                currentView = managementView;
            }
        }
        else if (buttonDataTransformationDesigner == event.getSource())
        {
            if (currentView != dataTransformationBuilderView)
            {
                currentView.setActive(false);
                dataTransformationBuilderView.setActive(buttonDataTransformationDesigner.
                        isSelected());
                currentView = dataTransformationBuilderView;
            }
        }
    }

    private void buildManagementView()
    {
        managementView = WebClientManagementView.build();
        managementView.setLayoutContainer(this);
        managementView.setActive(true);
        buttonManagement.setSelected(true);
        currentView = managementView;
    }

    private void buildScenarioBuilderView()
    {
        dataTransformationBuilderView = DataTransformationBuilderView.build();
        dataTransformationBuilderView.setLayoutContainer(this);
        //dataTransformationBuilderView.setActive(true);
        //buttonDataTransformationDesigner.setSelected(true);
        //currentView = dataTransformationBuilderView;
    }
}
