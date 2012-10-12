package com.lumens.client.view;

import com.lumens.client.view.transformdesign.DataSourceServiceClickHandler;
import com.lumens.client.view.transformdesign.DataTransformDesignerPane;
import com.lumens.client.view.transformdesign.ElementLink;
import com.lumens.client.view.transformdesign.TransformationElement;
import com.smartgwt.client.types.Cursor;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.Layout;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.layout.events.OnSectionHeaderClickHandler;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripMenuButton;
import com.smartgwt.client.widgets.tree.TreeGrid;

/**
 *
 * @author shaofeng wang
 */
public class DataTransformDesignerView extends HLayout implements
        ActiveView, ViewConstants
{
    private SectionStack sectionStack;
    private ToolStrip toolBar;
    private VLayout dtBuilderLayout;
    private boolean active;
    private Layout layoutContainer;
    private DataTransformDesignerPane dataTransformationBuilderPane;

    private DataTransformDesignerView()
    {
        super();
    }

    public static DataTransformDesignerView build()
    {
        DataTransformDesignerView view = new DataTransformDesignerView();
        view.setWidth100();
        view.setHeight100();

        // Build connector navigation
        view.sectionStack = new SectionStack();
        view.sectionStack.setVisibilityMode(VisibilityMode.MULTIPLE);
        view.sectionStack.setOverflow(Overflow.HIDDEN);
        view.sectionStack.setHeight100();
        view.sectionStack.setWidth(260);
        OnSectionHeaderClickHandler click = new DataSourceServiceClickHandler(
                view.sectionStack);
        view.sectionStack.addOnSectionHeaderClickHandler(click);

        SectionStackSection dataSourceSection = new SectionStackSection(messages.datasource_section());
        dataSourceSection.setID(DATASOURCE_SECTION_ID);
        dataSourceSection.setShowHeader(true);
        view.sectionStack.addSection(dataSourceSection);
        dataSourceSection.addItem(initDataSourceCatalog("datasource/"));

        SectionStackSection processorSection = new SectionStackSection(
                messages.processor_section());
        processorSection.setID(PROCESSOR_SECTION_ID);
        view.sectionStack.addSection(processorSection);
        view.sectionStack.setShowResizeBar(true);
        processorSection.addItem(initDataSourceCatalog("processor/"));

        // Build layout for toolbar and scenario area
        view.dtBuilderLayout = new VLayout();
        view.dtBuilderLayout.setWidth100();
        view.dtBuilderLayout.setHeight100();
        view.dtBuilderLayout.setBackgroundColor(ViewConstants.BACKGROUD_COLOR);

        // Build toolbar
        view.buildToolBar();
        view.buildTransformWorker();
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
            dataTransformationBuilderPane.buildDrawItems();
            layoutContainer.addMember(this);
        } else if (!active && layoutContainer.hasMember(this))
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

    private void buildTransformWorker()
    {
        dtBuilderLayout.addMember(buildScenarioBuilerPane());/*
        // Build workers
        TransformationElement worker1 = buildMockConnector("datasource/32/database.png",
                                                           "Database");
        worker1.setLeft(100);
        worker1.setTop(100);
        TransformationElement worker2 = buildMockConnector("processor/32/transform.png",
                                                           "Transform");
        worker2.setLeft(300);
        worker2.setTop(200);
        dataTransformationBuilderPane.addElement(worker1);
        dataTransformationBuilderPane.addElement(worker2);
        ElementLink link = buildLink();
        link.set(worker1, worker2);
        dataTransformationBuilderPane.addElement(link);

        TransformationElement worker3 = buildMockConnector("datasource/32/soap.png",
                                                           "WebService");
        worker3.setLeft(500);
        worker3.setTop(300);
        ElementLink link2 = buildLink();
        link2.set(worker2, worker3);
        dataTransformationBuilderPane.addElement(worker3);
        dataTransformationBuilderPane.addElement(link2);//*/
    }

    private DataTransformDesignerPane buildScenarioBuilerPane()
    {
        dataTransformationBuilderPane = new DataTransformDesignerPane();
        dataTransformationBuilderPane.setHeight100();
        dataTransformationBuilderPane.setWidth100();
        dataTransformationBuilderPane.setBackgroundColor(
                ViewConstants.BACKGROUD_COLOR);
        dataTransformationBuilderPane.setOverflow(Overflow.HIDDEN);
        dataTransformationBuilderPane.setCursor(Cursor.AUTO);
        dataTransformationBuilderPane.setCanAcceptDrop(true);
        dataTransformationBuilderPane.addDropHandler(dataTransformationBuilderPane);
        return dataTransformationBuilderPane;
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

    private static Canvas initDataSourceCatalog(String iconFolder)
    {
        TreeGrid treeGrid = new TreeGrid();
        treeGrid.setWidth100();
        treeGrid.setHeight100();
        treeGrid.setShowHeader(false);
        treeGrid.setShowResizeBar(false);
        treeGrid.setAppImgDir(iconFolder);
        treeGrid.setCanDragRecordsOut(true);  
        treeGrid.setShowOpenIcons(false);
        treeGrid.setOpenIconSuffix("");
        treeGrid.setDropIconSuffix("");
        treeGrid.setClosedIconSuffix("");
        return treeGrid;
    }
}
