/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.lumens.client.rpc.beans.CProject;
import com.lumens.client.view.transformdesign.DataTransformDesignPane;

/**
 *
 * @author shaofeng wang (shaofeng.cjpw@gmail.com)
 */
public class TransformOpenProjectServiceAsyncCallback implements
        AsyncCallback<CProject>
{
    private DataTransformDesignPane dataTransformationBuilderPane;

    public TransformOpenProjectServiceAsyncCallback(
            DataTransformDesignPane dataTransformationBuilderPane)
    {
        this.dataTransformationBuilderPane = dataTransformationBuilderPane;
    }

    @Override
    public void onFailure(Throwable caught)
    {
    }

    @Override
    public void onSuccess(CProject result)
    {
    }
}
