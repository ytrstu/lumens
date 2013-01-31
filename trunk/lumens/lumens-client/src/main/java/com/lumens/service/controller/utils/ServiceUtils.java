/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.service.controller.utils;

import com.lumens.client.rpc.beans.CProject;
import com.lumens.engine.TransformProject;
import com.lumens.engine.component.DataSource;
import com.lumens.engine.component.DataTransformation;
import java.util.List;

/**
 *
 * @author shaofeng wang (shaofeng.cjpw@gmail.com)
 */
public class ServiceUtils
{
    public static TransformProject convertAsServerProject(CProject project)
    {
        return null;
    }

    public static CProject convertAsClientProject(TransformProject project)
    {
        CProject clientProject = new CProject();
        clientProject.setName(project.getName());
        List<DataSource> dataSrcList = project.getDatasourceList();
        List<DataTransformation> transformList = project.getDataTransformationList();
        for (DataSource ds : dataSrcList)
        {
            ds.getTargetList();
        }
        return null;
    }
}
