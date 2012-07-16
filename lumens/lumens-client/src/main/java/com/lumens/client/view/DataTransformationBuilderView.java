/*
 * (C) Copyright Hewlett-Packard Development Company, L.P. All Rights Reserved.
 */
package com.lumens.client.view;

import com.lumens.client.view.element.ElementLink;
import com.lumens.client.view.element.TransformationElement;
import com.smartgwt.client.types.Cursor;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.Layout;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripMenuButton;

public class DataTransformationBuilderView extends HLayout implements WebClientView
{
    private SectionStack sectionStack;
    private ToolStrip toolBar;
    private VLayout dtBuilderLayout;
    private boolean active;
    private Layout layoutContainer;
    private DataTransformationBuilderPane DataTransformationBuilderPane;

    private DataTransformationBuilderView()
    {
        super();
    }

    public static DataTransformationBuilderView build()
    {
        DataTransformationBuilderView view = new DataTransformationBuilderView();
        view.setWidth100();
        view.setHeight100();

        // Build connector navigation
        view.sectionStack = new SectionStack();
        view.sectionStack.setVisibilityMode(VisibilityMode.MULTIPLE);
        view.sectionStack.setHeight100();
        view.sectionStack.setWidth(260);

        SectionStackSection cntrsSection = new SectionStackSection("Connectors");
        view.sectionStack.addSection(cntrsSection);
        SectionStackSection controlSection = new SectionStackSection("Processors");
        view.sectionStack.addSection(controlSection);
        view.sectionStack.setShowResizeBar(true);

        // Build layout for toolbar and scenario area
        view.dtBuilderLayout = new VLayout();
        view.dtBuilderLayout.setWidth100();
        view.dtBuilderLayout.setHeight100();
        view.dtBuilderLayout.setBackgroundColor(ViewConstants.BACKGROUD_COLOR);

        // Build toolbar
        view.buildToolBar();
        view.buildScenario();
        // Add to parent panel
        view.addMember(view.sectionStack);
        view.addMember(view.dtBuilderLayout);

        return view;
    }

    @Override
    public boolean isActive()
    {
        return active;
    }

    @Override
    public void setActive(boolean active)
    {
        this.active = active;
        if (active && !layoutContainer.hasMember(this))
        {
            DataTransformationBuilderPane.buildDrawItems();
            layoutContainer.addMember(this);
        }
        else if (!active && layoutContainer.hasMember(this))
        {
            layoutContainer.removeMember(this);
        }
    }

    @Override
    public void setLayoutContainer(Layout layout)
    {
        layoutContainer = layout;
    }

    private void buildToolBar()
    {
        toolBar = new ToolStrip();
        toolBar.setWidth100();
        toolBar.setHeight(30);
        Menu menu = new Menu();
        menu.setShowShadow(true);
        menu.setShadowDepth(1);
        MenuItem openItem = new MenuItem("Open");
        menu.setItems(openItem);
        ToolStripMenuButton projButton = new ToolStripMenuButton("Project", menu);
        projButton.setWidth(80);
        toolBar.addSpacer(15);
        toolBar.addMenuButton(projButton);
        ToolStripMenuButton toolButton = new ToolStripMenuButton("Tools", menu);
        toolButton.setWidth(80);
        toolBar.addSpacer(15);
        toolBar.addMenuButton(toolButton);
        ToolStripMenuButton monButton = new ToolStripMenuButton("Monitor", menu);
        monButton.setWidth(80);
        toolBar.addSpacer(15);
        toolBar.addMenuButton(monButton);
        ToolStripMenuButton opsButton = new ToolStripMenuButton("Options", menu);
        opsButton.setWidth(80);
        toolBar.addSpacer(15);
        toolBar.addMenuButton(opsButton);
        dtBuilderLayout.addMember(toolBar);
    }

    private void buildScenario()
    {
        dtBuilderLayout.addMember(buildScenarioBuilerPane());
        // Build workers
        TransformationElement worker1 = buildMockConnector("xml-file.png", "XML File");
        worker1.setLeft(100);
        worker1.setTop(100);
        TransformationElement worker2 = buildMockConnector("transform.png", "Transform");
        worker2.setLeft(300);
        worker2.setTop(200);
        DataTransformationBuilderPane.addElement(worker1);
        DataTransformationBuilderPane.addElement(worker2);

        ElementLink link = buildLink();
        link.set(worker1, worker2);
        DataTransformationBuilderPane.addElement(link);

        /*TransformationElement worker3 = buildMockConnector("http.png", "WebService");
         worker3.setLeft(500);
         worker3.setTop(300);
         ElementLink link2 = buildLink();
         link2.set(worker2, worker3);
         DataTransformationBuilderPane.addElement(worker3);
         DataTransformationBuilderPane.addElement(link2);*/
    }

    private DataTransformationBuilderPane buildScenarioBuilerPane()
    {
        DataTransformationBuilderPane = new DataTransformationBuilderPane();
        DataTransformationBuilderPane.setHeight100();
        DataTransformationBuilderPane.setWidth100();
        DataTransformationBuilderPane.setBackgroundColor(ViewConstants.BACKGROUD_COLOR);
        DataTransformationBuilderPane.setOverflow(Overflow.HIDDEN);
        DataTransformationBuilderPane.setCursor(Cursor.AUTO);
        return DataTransformationBuilderPane;
    }

    private ElementLink buildLink()
    {
        return new ElementLink();
    }

    private TransformationElement buildMockConnector(String ico, String label)
    {
        TransformationElement e = new TransformationElement(ico);
        e.setLabel(label);
        return e;
    }
}
