/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.client.view.transformdesign;

import com.lumens.client.rpc.beans.CComponentParameter;
import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 *
 * @author shaofeng wang (shaofeng.cjpw@gmail.com)
 */
public class ParameterRecord extends ListGridRecord
{
    private CComponentParameter param;

    public ParameterRecord(CComponentParameter param)
    {
        this.param = param;
        this.setAttribute("id", param.getId());
        this.setAttribute("name", param.getName());
        this.setAttribute("value", param.getValue());
    }

    public String getValue()
    {
        return this.getAttributeAsString("value");
    }

    public void setValue(String value)
    {
        this.setAttribute("value", value);
    }

    public void onApply()
    {
        param.setValue(getValue());
    }
}
