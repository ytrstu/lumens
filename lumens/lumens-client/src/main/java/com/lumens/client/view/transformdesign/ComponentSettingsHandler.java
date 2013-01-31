/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.client.view.transformdesign;

import com.lumens.client.rpc.beans.CComponent;
import com.lumens.client.rpc.beans.CComponentParameter;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import java.util.Collection;

/**
 *
 * @author shaofeng wang (shaofeng.cjpw@gmail.com)
 */
public class ComponentSettingsHandler implements ClickHandler
{
    private CComponent tElement;
    private DataTransformDesignPane designerPane;

    public ComponentSettingsHandler(CComponent tElement,
                                    DataTransformDesignPane designerPane)
    {
        this.tElement = tElement;
        this.designerPane = designerPane;
    }

    @Override
    public void onClick(ClickEvent event)
    {
        ComponentSettingsListGrid paramList = designerPane.getParamList();
        Collection<CComponentParameter> params = tElement.getParameters();
        ParameterRecord[] paramRecords = new ParameterRecord[params.size()];
        int i = 0;
        for (CComponentParameter param : params)
            paramRecords[i++] = new ParameterRecord(param);
        paramList.setParameterRecords(paramRecords);
    }
}
