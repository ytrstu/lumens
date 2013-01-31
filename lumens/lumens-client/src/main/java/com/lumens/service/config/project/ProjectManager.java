/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.service.config.project;

import com.lumens.client.rpc.beans.ProjectItem;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author shaofeng wang (shaofeng.cjpw@gmail.com)
 */
public class ProjectManager
{
    private static final List<ProjectItem> projectList = new ArrayList<ProjectItem>();

    static
    { // TODO now initialize the list staticly, it needs to be loaded dynamicly later
        projectList.add(new ProjectItem("demo", "demo test", "project.xml"));
    }

    public List<ProjectItem> getProject()
    {
        return projectList;
    }
}
