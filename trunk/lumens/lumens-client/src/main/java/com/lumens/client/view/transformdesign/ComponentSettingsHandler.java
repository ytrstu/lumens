/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.client.view.transformdesign;

import com.lumens.client.rpc.beans.ClientTransformElement;
import com.lumens.client.rpc.beans.ComponentParameter;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import java.util.Collection;

/**
 *
 * @author shaofeng wang (shaofeng.cjpw@gmail.com)
 */
public class ComponentSettingsHandler implements ClickHandler
{
    private ClientTransformElement tElement;
    private DataTransformDesignerPane designerPane;

    public ComponentSettingsHandler(ClientTransformElement tElement,
                                    DataTransformDesignerPane designerPane)
    {
        this.tElement = tElement;
        this.designerPane = designerPane;
    }

    @Override
    public void onClick(ClickEvent event)
    {
        ComponentSettingsListGrid paramList = designerPane.getParamList();
        Collection<ComponentParameter> params = tElement.getParameters();
        ParameterRecord[] paramRecords = new ParameterRecord[params.size()];
        int i = 0;
        for (ComponentParameter param : params)
            paramRecords[i++] = new ParameterRecord(param);
        paramList.setParameterRecords(paramRecords);
    }
}
