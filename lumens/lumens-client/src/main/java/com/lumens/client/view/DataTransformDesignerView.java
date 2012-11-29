package com.lumens.client.view;

import com.lumens.client.view.transformdesign.ApplyActionHandler;
import com.lumens.client.view.transformdesign.ComponentNode;
import com.google.gwt.core.client.GWT;
import com.lumens.client.constant.ViewConstants;
import com.lumens.client.rpc.ComponentServiceAsyncCallback;
import com.lumens.client.rpc.LumensService;
import com.lumens.client.rpc.LumensServiceAsync;
import com.lumens.client.rpc.beans.ComponentRegistry;
import com.lumens.client.view.transformdesign.ComponentCatalogNodeClickHandler;
import com.lumens.client.view.transformdesign.ComponentSettingsListGrid;
import com.lumens.client.view.transformdesign.DataTransformDesignerPane;
import com.lumens.client.view.transformdesign.TransformSaveHandler;
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
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;
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
    private SectionStack tComponentBoxStack;
    private SectionStack paramInfoStack;
    private ToolStrip toolBar;
    private VLayout dtBuilderLayout;
    private boolean active;
    private Layout layoutContainer;
    private HLayout builderPaneLayout;
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
        view.tComponentBoxStack = new SectionStack();
        view.tComponentBoxStack.setVisibilityMode(VisibilityMode.MULTIPLE);
        view.tComponentBoxStack.setOverflow(Overflow.HIDDEN);
        view.tComponentBoxStack.setShowResizeBar(true);
        view.tComponentBoxStack.setResizeBarSize(1);
        view.tComponentBoxStack.setHeight100();
        view.tComponentBoxStack.setWidth(260);
        SectionHeaderClickHandler click = new ComponentCatalogNodeClickHandler(
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
        view.dtBuilderLayout = new VLayout();
        view.dtBuilderLayout.setWidth100();
        view.dtBuilderLayout.setHeight100();
        view.dtBuilderLayout.setBackgroundColor(ViewConstants.BACKGROUD_COLOR);

        // Build toolbar
        view.builderPaneLayout = new HLayout();
        view.buildToolBar();
        // Build logic pane and parameter pane
        view.buildTransformDesignPane();
        view.dtBuilderLayout.setWidth("*");
        view.dtBuilderLayout.setShowResizeBar(true);
        view.dtBuilderLayout.setResizeBarTarget("next");

        // Build param information stack
        view.paramInfoStack = new SectionStack();
        view.paramInfoStack.setVisibilityMode(VisibilityMode.MULTIPLE);
        view.paramInfoStack.setOverflow(Overflow.HIDDEN);
        view.paramInfoStack.setHeight100();
        view.paramInfoStack.setWidth(280);
        ComponentSettingsListGrid paramList = buildParamListPane(
                view.paramInfoStack);
        view.dataTransformationBuilderPane.setParamList(paramList);

        // Add the panes to pane layout of designer
        view.builderPaneLayout.addMember(view.dtBuilderLayout);
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
        toolBar = new ToolStrip();
        toolBar.setWidth100();
        toolBar.setHeight(30);
        ToolStripButton saveButton = new ToolStripButton();
        saveButton.setID(saveButtonOfTransformDesignID);
        saveButton.setIcon("action/save.png");
        saveButton.addClickHandler(new TransformSaveHandler(dataTransformationBuilderPane));
        toolBar.addButton(saveButton);
        dtBuilderLayout.addMember(toolBar);
    }

    private void buildTransformDesignPane()
    {
        dataTransformationBuilderPane = new DataTransformDesignerPane();
        dataTransformationBuilderPane.setWidth(3000);
        dataTransformationBuilderPane.setHeight(3000);
        dataTransformationBuilderPane.setOverflow(Overflow.SCROLL);
        dataTransformationBuilderPane.setBackgroundColor(BACKGROUD_COLOR);
        dataTransformationBuilderPane.setCursor(Cursor.AUTO);
        dataTransformationBuilderPane.setBuilderPaneLayout(builderPaneLayout);
        dataTransformationBuilderPane.setBuilderLayout(dtBuilderLayout);
        dtBuilderLayout.addMember(dataTransformationBuilderPane);
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
        applyButton.setID(applyButtonOfSettingsID);
        applyButton.setIcon("action/apply.png");
        paramBar.addButton(applyButton);
        paramBar.addSpacer(10);
        ToolStripButton helpButton = new ToolStripButton();
        helpButton.setID(undoButtonOfSettingsID);
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

        ApplyActionHandler apply = new ApplyActionHandler(paramList);
        applyButton.addClickHandler(apply);

        return paramList;
    }

    private static void buildTransformRulePane()
    {
    }
}
