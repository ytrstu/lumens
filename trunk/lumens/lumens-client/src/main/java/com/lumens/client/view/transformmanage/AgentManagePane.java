package com.lumens.client.view.transformmanage;

import com.lumens.client.demo.DataLoader;
import com.lumens.client.constant.ViewConstants;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Cursor;
import com.smartgwt.client.types.DragAppearance;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.TreeModelType;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.grid.CellFormatter;
import com.smartgwt.client.widgets.grid.HoverCustomizer;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.Layout;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VStack;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeGrid;
import com.smartgwt.client.widgets.tree.TreeGridField;

/**
 *
 * @author shaofeng wang
 */
public class AgentManagePane extends SectionStack implements
        RecordDoubleClickHandler
{
    private SectionStackSection agentListSection;
    private SectionStackSection agentDetailSection;
    private HLayout hLayout;
    private TreeGrid agentGroupTreeGrid;
    private Tree agentGroupData;
    private ListGrid scnList;
    private Layout agentDetailPane;
    private TabSet detailPane;

    private static final class HoverCustomizerImpl extends HoverCustomizer
    {
        @Override
        public String hoverHTML(Object value, ListGridRecord record, int rowNum,
                                int colNum)
        {
            return "<HTML><BODY>Scenaro: 3<br> Scenario Running: 2<br>Scenario Error: 1</BODY></HTML>";
        }
    }

    private AgentManagePane()
    {
        super();
        hLayout = new HLayout();
        hLayout.setHeight("80%");
        hLayout.setWidth100();
        agentListSection = new SectionStackSection();
        agentListSection.setShowHeader(false);
        agentListSection.setExpanded(true);
        agentListSection.setItems(hLayout);
        addSection(agentListSection);
    }

    public static AgentManagePane build()
    {
        AgentManagePane pane = new AgentManagePane();
        pane.setVisibilityMode(VisibilityMode.MULTIPLE);
        pane.setHeight100();
        pane.setWidth100();
        pane.setHeaderHeight(20);
        pane.buildAgentList();
        pane.buildDetailPane();
        pane.buildLogPane();
        return pane;
    }

    private void buildAgentList()
    {
        // group tree
        agentGroupData = new Tree();
        agentGroupData.setModelType(TreeModelType.CHILDREN);
        agentGroupData.setNameProperty("name");
        agentGroupData.setOpenProperty("isOpen");
        agentGroupData.setRoot(DataLoader.getAgentNodes());
        // group tree grid
        TreeGridField agentField = new TreeGridField("Agent Group");
        agentField.setShowHover(true);
        agentField.setHoverCustomizer(new HoverCustomizerImpl());
        agentField.setCellFormatter(new CellFormatter()
        {
            @Override
            public String format(Object value, ListGridRecord record, int rowNum,
                                 int colNum)
            {
                return record.getAttributeAsString("name");
            }
        });

        agentGroupTreeGrid = new TreeGrid();
        agentGroupTreeGrid.setWidth(250);
        agentGroupTreeGrid.setHeight100();
        agentGroupTreeGrid.setShowHeader(false);
        agentGroupTreeGrid.setShowResizeBar(true);
        agentGroupTreeGrid.setAppImgDir("agent/16/");
        agentGroupTreeGrid.setCanReorderRecords(true);
        agentGroupTreeGrid.setCanAcceptDroppedRecords(true);
        agentGroupTreeGrid.setShowOpenIcons(false);
        agentGroupTreeGrid.setOpenIconSuffix("");
        agentGroupTreeGrid.setDropIconSuffix("");
        agentGroupTreeGrid.setClosedIconSuffix("");
        agentGroupTreeGrid.setData(agentGroupData);
        agentGroupTreeGrid.setFields(agentField);
        hLayout.addMember(agentGroupTreeGrid);
    }

    private void buildDetailPane()
    {
        detailPane = new TabSet();
        detailPane.setWidth100();
        detailPane.setHeight100();
        this.buildAgentDetailTab();
        this.buildScnListTab();
        hLayout.addMember(detailPane);
    }

    private void buildAgentDetailTab()
    {
        Tab agentTab = new Tab();
        agentTab.setTitle("Agent");
        PortalLayout agentLayout = new PortalLayout(3);
        agentLayout.setShowEdges(false);
        agentLayout.setLayoutMargin(0);
        Portlet portlet = new Portlet();
        portlet.setHeight(250);
        portlet.setContents(
                "Agent name, description, ip, created time, scenario number");
        agentLayout.addPortlet(portlet);
        portlet = new Portlet();
        portlet.setHeight(250);
        portlet.setContents("Agent resource CPU, Disk, Memory Chart");
        agentLayout.addPortlet(portlet);
        agentLayout.setWidth100();
        agentLayout.setHeight100();
        agentTab.setPane(agentLayout);
        detailPane.addTab(agentTab);
    }

    private void buildScnListTab()
    {
        scnList = new ListGrid();
        scnList.setHeight100();
        scnList.setWidth100();
        scnList.addRecordDoubleClickHandler(this);
        scnList.setBackgroundColor(ViewConstants.BACKGROUD_COLOR);
        ListGridField[] fields = new ListGridField[]
        {
            new ListGridField("id", "Transform Job ID", 100),
            new ListGridField("name", "Transform Job Name", 150),
            new ListGridField("status", "Status", 100),
            new ListGridField("description", "Description", 100),
            new ListGridField("CPU", "CPU(%)", 100), new ListGridField("memory",
                                                                       "Memory(%)",
                                                                       100),
            new ListGridField("disk", "Disk(%)", 100)
        };
        ListGridField status = fields[2];
        status.setAlign(Alignment.CENTER);
        status.setType(ListGridFieldType.IMAGE);
        status.setImageURLPrefix("status/16/");
        status.setImageURLSuffix(".png");
        scnList.setFields(fields);
        scnList.setData(DataLoader.getAgents());
        Tab scnListTab = new Tab();
        scnListTab.setTitle("Transform Job");
        detailPane.addTab(scnListTab);
        scnListTab.setPane(scnList);
    }

    private void buildLogPane()
    {
        agentDetailPane = new Layout();
        agentDetailPane.setWidth100();
        agentDetailPane.setHeight("20%");
        agentDetailSection = new SectionStackSection();
        agentDetailSection.setExpanded(false);
        agentDetailSection.setItems(agentDetailPane);
        addSection(agentDetailSection);
    }

    @Override
    public void onRecordDoubleClick(RecordDoubleClickEvent event)
    {
        Record record = event.getRecord();
        if (record != null)
        {
            record.getAttributeAsString("id");
            record.getAttributeAsString("name");
            record.getAttributeAsString("status");
            record.getAttributeAsString("description");
            record.getAttributeAsInt("CPU");
            record.getAttributeAsInt("memory");
            record.getAttributeAsInt("disk");
        }
    }
}

class PortalColumn extends VStack
{
    public PortalColumn()
    {

        // leave some space between portlets
        setMembersMargin(6);

        // enable predefined component animation
        setAnimateMembers(true);
        setAnimateMemberTime(300);

        // enable drop handling
        setCanAcceptDrop(true);

        // change appearance of drag placeholder and drop indicator
        setDropLineThickness(4);

        Canvas dropLineProperties = new Canvas();
        dropLineProperties.setBackgroundColor("aqua");
        setDropLineProperties(dropLineProperties);

        setShowDragPlaceHolder(true);

        Canvas placeHolderProperties = new Canvas();
        placeHolderProperties.setBorder("2px solid #8289A6");
        setPlaceHolderProperties(placeHolderProperties);
    }
}

class Portlet extends Canvas
{
    public Portlet()
    {
        setCursor(Cursor.MOVE);
        setOverflow(Overflow.HIDDEN);
        setBorder("1px solid #6a6a6a");
        setBackgroundColor("#C3D9FF");
        setCanDragReposition(true);
        setCanDragResize(true);
        setDragAppearance(DragAppearance.TARGET);
        setShowShadow(false);
        setCanDrop(true);
        setDragOpacity(30);
    }
}

class PortalLayout extends HLayout
{
    public PortalLayout(int numColumns)
    {
        setMembersMargin(6);
        for (int i = 0; i < numColumns; i++)
        {
            addMember(new PortalColumn());
        }
    }

    public PortalColumn addPortlet(Portlet portlet)
    {
        // find the column with the fewest portlets
        int fewestPortlets = Integer.MAX_VALUE;
        PortalColumn fewestPortletsColumn = null;
        for (int i = 0; i < getMembers().length; i++)
        {
            int numPortlets = ((PortalColumn) getMember(i)).getMembers().length;
            if (numPortlets < fewestPortlets)
            {
                fewestPortlets = numPortlets;
                fewestPortletsColumn = (PortalColumn) getMember(i);
            }
        }
        fewestPortletsColumn.addMember(portlet);
        return fewestPortletsColumn;
    }
}
