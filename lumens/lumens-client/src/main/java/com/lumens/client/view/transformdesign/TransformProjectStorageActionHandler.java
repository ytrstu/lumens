/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.client.view.transformdesign;

import com.lumens.client.constant.ViewConstants;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;

/**
 *
 * @author shaofeng wang (shaofeng.cjpw@gmail.com)
 */
public class TransformProjectStorageActionHandler implements ClickHandler
{
    private DataTransformDesignerPane dataTransformationBuilderPane;

    public TransformProjectStorageActionHandler(
            DataTransformDesignerPane dataTransformationBuilderPane)
    {
        this.dataTransformationBuilderPane = dataTransformationBuilderPane;
    }

    @Override
    public void onClick(ClickEvent event)
    {
        Canvas src = (Canvas) event.getSource();
        if (ViewConstants.newProjectButtonOfTransformDesignID.
                equals(src.getID()))
        {
        } else if (ViewConstants.openProjectButtonOfTransformDesignID.
                equals(src.getID()))
        {
        } else if (ViewConstants.saveProjectButtonOfTransformDesignID.
                equals(src.getID()))
        {
        }
    }
}