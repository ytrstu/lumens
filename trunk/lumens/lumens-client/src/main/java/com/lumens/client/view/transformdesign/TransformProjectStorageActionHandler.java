/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.client.view.transformdesign;

import com.google.gwt.core.client.GWT;
import com.lumens.client.utils.CConstants;
import com.lumens.client.rpc.LumensService;
import com.lumens.client.rpc.LumensServiceAsync;
import com.lumens.client.rpc.TransformOpenProjectServiceAsyncCallback;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;

/**
 *
 * @author shaofeng wang (shaofeng.cjpw@gmail.com)
 */
public class TransformProjectStorageActionHandler implements ClickHandler
{
    private DataTransformDesignPane dataTransformationBuilderPane;

    public TransformProjectStorageActionHandler(
            DataTransformDesignPane dataTransformationBuilderPane)
    {
        this.dataTransformationBuilderPane = dataTransformationBuilderPane;
    }

    @Override
    public void onClick(ClickEvent event)
    {
        Canvas src = (Canvas) event.getSource();
        if (CConstants.newProjectButtonOfTransformDesignID.
                equals(src.getID()))
        {
        } else if (CConstants.openProjectButtonOfTransformDesignID.
                equals(src.getID()))
        {
            // TODO show a dialog and let user to select a project to open
            String projectName = "";
            //Open a project
            LumensServiceAsync dsService = (LumensServiceAsync) GWT.create(
                    LumensService.class);
            dsService.openTransformProject(projectName,
                                           new TransformOpenProjectServiceAsyncCallback(
                    dataTransformationBuilderPane));
        } else if (CConstants.saveProjectButtonOfTransformDesignID.
                equals(src.getID()))
        {
        }
    }
}