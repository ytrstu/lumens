package com.lumens.client.view;

import com.google.gwt.core.client.GWT;
import com.lumens.client.constant.ViewConstants;
import com.lumens.client.rpc.ElementServiceAsyncCallback;
import com.lumens.client.rpc.LumensService;
import com.lumens.client.rpc.LumensServiceAsync;
import com.lumens.client.rpc.beans.CComponentTypeRegistry;
import com.lumens.client.view.transformdesign.ComponentTypeCatalogNodeClickHandler;
import com.lumens.client.view.transformdesign.CComponentTypeNode;
import com.lumens.client.view.transformdesign.ComponentSettingsApplyHandler;
import com.lumens.client.view.transformdesign.ComponentSettingsListGrid;
import com.lumens.client.view.transformdesign.DataTransformDesignPane;
import com.lumens.client.view.transformdesign.TransformProjectStorageActionHandler;
import com.smartgwt.client.types.Cursor;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.TreeModelType;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.widgets.grid.CellFormatter;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.Layout;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.layout.events.SectionHeaderClickHandler;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;
import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeGrid;
import com.smartgwt.client.widgets.tree.TreeGridField;
import com.smartgwt.client.widgets.tree.TreeNode;

/**
 *
 * @author shaofeng wang (shaofeng.cjpw@gmail.com)
 */
public class DataTransformDesignView extends HLayout implements
        ActiveView, ViewConstants
{
    private SectionStack tComponentBoxStack;
    private SectionStack paramInfoStack;
    private ToolStrip toolBar;
    private boolean active;
    private VLayout tBuilderLayout;
    private DataTransformDesignPane dataTransformationBuilderPane;
    private TabSet transformConfigPane;
    private Layout layoutContainer;
    private HLayout builderPaneLayout;
    private VLayout tElementLayout;

    private DataTransformDesignView()
    {
        super();
    }

    public static DataTransformDesignView build()
    {
        DataTransformDesignView view = new DataTransformDesignView();
        view.setWidth100();
        view.setHeight100();

        // Build connector navigation
        view.tComponentBoxStack = new SectionStack();
        view.tComponentBoxStack.setVisibilityMode(VisibilityMode.MULTIPLE);
        view.tComponentBoxStack.setOverflow(Overflow.HIDDEN);
        view.tComponentBoxStack.setShowResizeBar(true);
        view.tComponentBoxStack.setResizeBarSize(1);
        view.tComponentBoxStack.setHeight100();
        view.tComponentBoxStack.setWidth(260);
        SectionHeaderClickHandler click = new ComponentTypeCatalogNodeClickHandler(
                view.tComponentBoxStack);
        view.tComponentBoxStack.addSectionHeaderClickHandler(click);

        SectionStackSection dataSourceSection = new SectionStackSection(messages.
                datasource_section());
        dataSourceSection.setID(DATASOURCE_SECTION_ID);
        view.tComponentBoxStack.addSection(dataSourceSection);
        TreeGrid dsTree = buildComponentCatalog("datasource/");
        getDataSourceCatalogGroup(dsTree);
        dataSourceSection.addItem(dsTree);

        SectionStackSection processorSection = new SectionStackSection(
                messages.processor_section());
        processorSection.setID(PROCESSOR_SECTION_ID);
        processorSection.addItem(buildComponentCatalog("processor/"));
        view.tComponentBoxStack.addSection(processorSection);

        // Build layout for toolbar and scenario area

        view.tElementLayout = new VLayout();
        view.tElementLayout.setWidth100();
        view.tElementLayout.setHeight("*");
        view.tElementLayout.setBackgroundColor(ViewConstants.BACKGROUD_COLOR);
        view.tElementLayout.setShowResizeBar(true);
        view.tElementLayout.setResizeBarTarget("next");
        // Build toolbar
        view.buildToolBar();
        // Build logic pane and parameter pane
        view.buildTransformDesignPane();

        view.tBuilderLayout = new VLayout();
        view.tBuilderLayout.setHeight100();
        view.tBuilderLayout.setBackgroundColor(ViewConstants.BACKGROUD_COLOR);
        view.tBuilderLayout.setWidth("*");
        view.tBuilderLayout.setShowResizeBar(true);
        view.tBuilderLayout.setResizeBarTarget("next");

        view.tBuilderLayout.addMember(view.tElementLayout);
        view.buildTransformRulePane();

        // Build param information stack
        view.builderPaneLayout = new HLayout();
        view.buildParamSettingsPane();
        // Add the panes to pane layout of designer
        view.builderPaneLayout.addMember(view.tBuilderLayout);
        view.builderPaneLayout.addMember(view.paramInfoStack);

        // Add to parent panel
        view.addMember(view.tComponentBoxStack);
        view.addMember(view.builderPaneLayout);

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
        TransformProjectStorageActionHandler transformProj = new TransformProjectStorageActionHandler(
                dataTransformationBuilderPane);
        toolBar = new ToolStrip();
        toolBar.setWidth100();
        toolBar.setHeight(30);
        ToolStripButton newButton = new ToolStripButton();
        newButton.setID(newProjectButtonOfTransformDesignID);
        newButton.setIcon("action/new-project.png");
        newButton.addClickHandler(transformProj);
        toolBar.addButton(newButton);
        ToolStripButton openButton = new ToolStripButton();
        openButton.setID(openProjectButtonOfTransformDesignID);
        openButton.setIcon("action/open-project.png");
        openButton.addClickHandler(transformProj);
        toolBar.addButton(openButton);
        ToolStripButton saveButton = new ToolStripButton();
        saveButton.setID(saveProjectButtonOfTransformDesignID);
        saveButton.setIcon("action/save-project.png");
        saveButton.addClickHandler(transformProj);
        toolBar.addButton(saveButton);
        tElementLayout.addMember(toolBar);
    }

    private void buildTransformDesignPane()
    {
        dataTransformationBuilderPane = new DataTransformDesignPane();
        dataTransformationBuilderPane.setWidth100();
        dataTransformationBuilderPane.setHeight100();
        dataTransformationBuilderPane.setOverflow(Overflow.HIDDEN);
        dataTransformationBuilderPane.setBackgroundColor(BACKGROUD_COLOR);
        dataTransformationBuilderPane.setCursor(Cursor.AUTO);
        dataTransformationBuilderPane.setBuilderPaneLayout(builderPaneLayout);
        dataTransformationBuilderPane.setBuilderLayout(tBuilderLayout);
        tElementLayout.addMember(dataTransformationBuilderPane);
    }

    private static TreeGrid buildComponentCatalog(String iconFolder)
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
        CComponentTypeNode[] ds = new CComponentTypeNode[1];
        ds[0] = new CComponentTypeNode(new CComponentTypeRegistry("", "", "../loading.gif",
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
                new ElementServiceAsyncCallback(
                DATASOURCE_SECTION_ID, treeGrid));
    }

    private void buildParamSettingsPane()
    {
        paramInfoStack = new SectionStack();
        paramInfoStack.setVisibilityMode(VisibilityMode.MULTIPLE);
        paramInfoStack.setOverflow(Overflow.HIDDEN);
        paramInfoStack.setHeight100();
        paramInfoStack.setWidth(280);
        ComponentSettingsListGrid paramList = buildParamListPane(
                paramInfoStack);
        dataTransformationBuilderPane.setParamList(paramList);
    }

    private static ComponentSettingsListGrid buildParamListPane(
            SectionStack paramInfoStack)
    {
        VLayout paramPane = new VLayout();
        paramPane.setHeight100();
        paramPane.setWidth100();

        ToolStrip paramBar = new ToolStrip();
        paramBar.setShowEdges(false);
        paramBar.setWidth100();
        paramBar.setHeight(30);
        paramBar.addSpacer(10);
        ToolStripButton applyButton = new ToolStripButton();
        applyButton.setID(applyParamsButtonOfSettingsID);
        applyButton.setIcon("action/accept-params.png");
        paramBar.addButton(applyButton);
        paramBar.addSpacer(10);
        ToolStripButton helpButton = new ToolStripButton();
        helpButton.setIcon("help.png");
        paramBar.addButton(helpButton);
        paramPane.addMember(paramBar);

        ComponentSettingsListGrid paramList = new ComponentSettingsListGrid();
        paramList.setHeight100();
        paramList.setWidth100();
        paramList.setBaseStyle("boxedGridCell");
        paramList.setShowHeader(false);
        paramList.setBackgroundColor(ViewConstants.BACKGROUD_COLOR);
        ListGridField[] fields = new ListGridField[]
        {
            new ListGridField("name", "Name", 120),
            new ListGridField("value", "Value", 150)
        };
        fields[1].setCanEdit(true);
        paramList.setFields(fields);
        paramPane.addMember(paramList);

        SectionStackSection parameterSection = new SectionStackSection(
                messages.datasource_param_section());
        parameterSection.setExpanded(false);
        parameterSection.setID(DATASOURCE_PARAMS_ID);
        parameterSection.addItem(paramPane);
        paramInfoStack.addSection(parameterSection);

        ComponentSettingsApplyHandler apply = new ComponentSettingsApplyHandler(
                paramList);
        applyButton.addClickHandler(apply);

        return paramList;
    }

    private void buildTransformRulePane()
    {
        // Build param information stack
        transformConfigPane = new TabSet();
        transformConfigPane.setWidth100();
        transformConfigPane.setHeight("20%");
        Tab tElemTab = new Tab();
        tElemTab.setTitle("Configuration");
        transformConfigPane.addTab(tElemTab);
        Tab tElemFormatTab = new Tab();
        tElemFormatTab.setTitle("Transform format");
        transformConfigPane.addTab(tElemFormatTab);
        Tab tElemTransformRuleTab = new Tab();
        tElemTransformRuleTab.setTitle("Transform rule");
        transformConfigPane.addTab(tElemTransformRuleTab);
        tBuilderLayout.addMember(transformConfigPane);
    }
}