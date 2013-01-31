/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.client.rpc.beans;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 *
 * @author shaofeng wang (shaofeng.cjpw@gmail.com)
 */
public class ProjectItem implements IsSerializable
{
    private String projectName;
    private String description;
    private String projectXml;

    public ProjectItem(String projectName, String description, String projectXml)
    {
        this.projectName = projectName;
        this.description = description;
        this.projectXml = projectXml;
    }

    public String getProjectName()
    {
        return projectName;
    }

    public void setProjectName(String projectName)
    {
        this.projectName = projectName;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getProjectXml()
    {
        return projectXml;
    }
}
