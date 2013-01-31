package com.lumens.client.view;

import com.lumens.client.utils.CConstants;
import com.lumens.client.view.transformmanage.AgentManagePane;
import com.smartgwt.client.widgets.layout.Layout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;

/**
 *
 * @author shaofeng wang (shaofeng.cjpw@gmail.com)
 */
public class DataTransformManageView extends VLayout implements ActiveView
{
    private ToolStrip statBar;
    private AgentManagePane agentManagePane;
    private boolean active;
    private Layout layoutContainer;

    private DataTransformManageView()
    {
        super();
    }

    public static DataTransformManageView build()
    {
        DataTransformManageView view = new DataTransformManageView();
        view.setWidth100();
        view.setHeight100();
        view.setBackgroundColor(CConstants.BACKGROUD_COLOR);
        view.buildAgentMangePane();
        view.buildStatusBar();
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

    private void buildStatusBar()
    {
        statBar = new ToolStrip();
        statBar.setWidth100();
        statBar.setHeight(20);
        addMember(statBar);
    }

    private void buildAgentMangePane()
    {
        agentManagePane = AgentManagePane.build();
        addMember(agentManagePane);
    }
}
