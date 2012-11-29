/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.client.view.transformdesign;

import com.lumens.client.rpc.beans.ComponentParameter;
import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 *
 * @author shaofeng wang (shaofeng.cjpw@gmail.com)
 */
public class ParameterRecord extends ListGridRecord
{
    private ComponentParameter param;

    public ParameterRecord(ComponentParameter param)
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

    public void onCancel()
    {
        setValue(param.getValue());
    }
}
