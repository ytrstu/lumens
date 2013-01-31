/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.service.config.project;

import com.lumens.client.rpc.beans.ProjectItem;
import com.lumens.engine.TransformProject;
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

    public List<ProjectItem> getProjectList()
    {
        return projectList;
    }

    public TransformProject openProject(String projectName)
    {
        // TODO need to add a lock flag in order to protect the opened project change by other pepole
        for (ProjectItem item : projectList)
            if (item.getProjectName().equalsIgnoreCase(projectName))
            {
                // TODO load project from the xml
                String projectXml = item.getProjectXml();

            }
        return null;
    }
}
