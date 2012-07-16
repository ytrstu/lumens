/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lumens.client.view;

import com.smartgwt.client.widgets.layout.Layout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;

/**
 *
 * @author washaofe
 */
public class WebClientManagementView extends VLayout implements WebClientView
{
    private ToolStrip statBar;
    private AgentManagePane agentManagePane;
    private boolean active;
    private Layout layoutContainer;

    private WebClientManagementView()
    {
        super();
    }

    public static WebClientManagementView build()
    {
        WebClientManagementView view = new WebClientManagementView();
        view.setWidth100();
        view.setHeight100();
        view.setBackgroundColor(ViewConstants.BACKGROUD_COLOR);
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
