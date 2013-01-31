/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.client.view.transformdesign;

import com.lumens.client.constant.ViewConstants;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

/**
 *
 * @author shaofeng wang (shaofeng.cjpw@gmail.com)
 */
public class ComponentSettingsApplyHandler implements ClickHandler, ViewConstants
{
    private ComponentSettingsListGrid paramList;

    public ComponentSettingsApplyHandler(ComponentSettingsListGrid paramList)
    {
        this.paramList = paramList;
    }

    @Override
    public void onClick(ClickEvent event)
    {
        Object src = event.getSource();
        if (src instanceof ToolStripButton)
        {
            ParameterRecord[] params = paramList.getParameterRecords();
            ToolStripButton button = (ToolStripButton) src;
            if (applyParamsButtonOfSettingsID.equals(button.getID()))
                for (ParameterRecord param : params)
                    param.onApply();
        }
    }
}
