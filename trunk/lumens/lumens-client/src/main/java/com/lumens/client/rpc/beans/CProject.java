/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.client.rpc.beans;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author shaofeng wang (shaofeng.cjpw@gmail.com)
 */
public class CProject implements IsSerializable
{
    private String projectName;
    private List<CLink> links = new ArrayList<CLink>();
    private List<CComponent> workers = new ArrayList<CComponent>();

    public void setName(String name)
    {
        this.projectName = name;
    }

    public String getName()
    {
        return this.projectName;
    }

    public List<CLink> getLinkList()
    {
        return this.links;
    }

    public List<CComponent> getWorkList()
    {
        return this.workers;
    }

    public void add(CLink link)
    {
        links.add(link);
    }

    public void add(CComponent element)
    {
        workers.add(element);
    }

    public void remove(CLink link)
    {
        links.remove(link);
    }

    public void remove(CComponent element)
    {
        workers.remove(element);
    }

    public boolean contains(CLink link)
    {
        return links.contains(link);
    }

    public boolean contains(CComponent element)
    {
        return workers.contains(element);
    }
}
