/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.client.view.transformdesign;

import com.smartgwt.client.widgets.grid.ListGrid;

/**
 *
 * @author shaofeng wang (shaofeng.cjpw@gmail.com)
 */
public class ComponentSettingsListGrid extends ListGrid
{
    private ParameterRecord[] dataRecords;

    public ParameterRecord[] getParameterRecords()
    {
        return this.dataRecords;
    }

    public void setParameterRecords(ParameterRecord[] records)
    {
        this.dataRecords = records;
        super.setData(records);
    }
}
