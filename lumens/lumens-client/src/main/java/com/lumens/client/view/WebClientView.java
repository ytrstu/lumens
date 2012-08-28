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
public class WebClientView extends VLayout implements ClickHandler
{
    private Label title;
    private Layout header;
    private ToolStrip toolbar;
    private DataTransformManageView managementView;
    private DataTransformDesignerView dataTransformationBuilderView;
    private ToolStripButton buttonManagement;
    private ToolStripButton buttonDataTransformationDesigner;
    private ActiveView currentView;

    private WebClientView()
    {
        super();
    }

    public static WebClientView build()
    {
        WebClientView view = new WebClientView();
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
        toolbar.setWidth100();
        toolbar.setHeight(32);
        addMember(toolbar);
        buttonManagement = new ToolStripButton();
        buttonManagement.setActionType(SelectionType.RADIO);
        buttonManagement.setRadioGroup("Console");
        buttonManagement.setTitle("Management");
        buttonManagement.addClickHandler(this);
        toolbar.addButton(buttonManagement);
        toolbar.addSpacer(5);
        buttonDataTransformationDesigner = new ToolStripButton();
        buttonDataTransformationDesigner.setActionType(SelectionType.RADIO);
        buttonDataTransformationDesigner.setRadioGroup("Console");
        buttonDataTransformationDesigner.setTitle("Transform Designer");
        buttonDataTransformationDesigner.addClickHandler(this);
        toolbar.addButton(buttonDataTransformationDesigner);
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
        } else if (buttonDataTransformationDesigner == event.getSource())
        {
            if (currentView != dataTransformationBuilderView)
            {
                currentView.setActive(false);
                dataTransformationBuilderView.
                        setActive(buttonDataTransformationDesigner.
                        isSelected());
                currentView = dataTransformationBuilderView;
            }
        }
    }

    private void buildManagementView()
    {
        managementView = DataTransformManageView.build();
        managementView.setLayoutContainer(this);
    }

    private void buildScenarioBuilderView()
    {
        dataTransformationBuilderView = DataTransformDesignerView.build();
        dataTransformationBuilderView.setLayoutContainer(this);
        dataTransformationBuilderView.setActive(true);
        buttonDataTransformationDesigner.setSelected(true);
        currentView = dataTransformationBuilderView;
    }
}
