package com.lumens.client.view;

import com.google.gwt.core.client.GWT;
import com.lumens.client.constant.ViewConstants;
import com.lumens.client.rpc.ComponentServiceAsyncCallback;
import com.lumens.client.rpc.LumensService;
import com.lumens.client.rpc.LumensServiceAsync;
import com.lumens.client.rpc.beans.ComponentRegistry;
import com.lumens.client.view.transformdesign.ComponentCatalogNodeClickHandler;
import com.lumens.client.view.transformdesign.DataTransformDesignerPane;
import com.smartgwt.client.types.Cursor;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.TreeModelType;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.widgets.grid.CellFormatter;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.Layout;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.layout.events.SectionHeaderClickHandler;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripMenuButton;
import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeGrid;
import com.smartgwt.client.widgets.tree.TreeGridField;
import com.smartgwt.client.widgets.tree.TreeNode;

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
        SectionHeaderClickHandler click = new ComponentCatalogNodeClickHandler(
                view.sectionStack);
        view.sectionStack.addSectionHeaderClickHandler(click);

        SectionStackSection dataSourceSection = new SectionStackSection(messages.
                datasource_section());
        dataSourceSection.setID(DATASOURCE_SECTION_ID);
        dataSourceSection.setShowHeader(true);
        view.sectionStack.addSection(dataSourceSection);
        TreeGrid dsTree = initComponentCatalog("datasource/");
        getDataSourceCatalogGroup(dsTree);
        dataSourceSection.addItem(dsTree);

        SectionStackSection processorSection = new SectionStackSection(
                messages.processor_section());
        processorSection.setID(PROCESSOR_SECTION_ID);
        view.sectionStack.addSection(processorSection);
        view.sectionStack.setShowResizeBar(true);
        processorSection.addItem(initComponentCatalog("processor/"));

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
            dataTransformationBuilderPane.paint();
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
        dtBuilderLayout.addMember(buildScenarioBuilerPane());
    }

    private DataTransformDesignerPane buildScenarioBuilerPane()
    {
        dataTransformationBuilderPane = new DataTransformDesignerPane();
        dataTransformationBuilderPane.setHeight100();
        dataTransformationBuilderPane.setWidth100();
        dataTransformationBuilderPane.setBackgroundColor(BACKGROUD_COLOR);
        dataTransformationBuilderPane.setOverflow(Overflow.HIDDEN);
        dataTransformationBuilderPane.setCursor(Cursor.AUTO);
        return dataTransformationBuilderPane;
    }

    private static TreeGrid initComponentCatalog(String iconFolder)
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

    private static void getDataSourceCatalogGroup(TreeGrid treeGrid)
    {
        ComponentNode[] ds = new ComponentNode[1];
        ds[0] = new ComponentNode(new ComponentRegistry("", "", "../loading.gif",
                                                        "", null));
        TreeNode root = new TreeNode("ComponentRoot", ds);

        Tree dataSourceTree = new Tree();
        dataSourceTree.setModelType(TreeModelType.CHILDREN);
        dataSourceTree.setNameProperty(COMPONENT_NAME);
        dataSourceTree.setRoot(root);
        // group tree grid
        TreeGridField connectorsField = new TreeGridField(
                "ComponentField");
        connectorsField.setShowHover(false);
        connectorsField.setCellFormatter(new CellFormatter()
        {
            @Override
            public String format(Object value, ListGridRecord record,
                                 int rowNum,
                                 int colNum)
            {
                return "Loading ...";
            }
        });
        treeGrid.setData(dataSourceTree);
        treeGrid.setFields(connectorsField);

        LumensServiceAsync dsService = (LumensServiceAsync) GWT.create(
                LumensService.class);
        dsService.
                getDataSourceCatalog(
                new ComponentServiceAsyncCallback(
                DATASOURCE_SECTION_ID, treeGrid));
    }
}
