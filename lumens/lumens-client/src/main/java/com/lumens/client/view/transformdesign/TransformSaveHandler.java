/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.client.view.transformdesign;

import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;

/**
 *
 * @author shaofeng wang (shaofeng.cjpw@gmail.com)
 */
public class TransformSaveHandler implements ClickHandler
{
    private DataTransformDesignerPane dataTransformationBuilderPane;

    public TransformSaveHandler(
            DataTransformDesignerPane dataTransformationBuilderPane)
    {
        this.dataTransformationBuilderPane = dataTransformationBuilderPane;
    }

    @Override
    public void onClick(ClickEvent event)
    {
    }
}
