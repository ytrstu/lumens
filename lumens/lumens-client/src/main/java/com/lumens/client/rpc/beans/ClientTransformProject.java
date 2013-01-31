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
public class ClientTransformProject implements IsSerializable
{
    private String projectName;
    private List<ClientElementLink> links = new ArrayList<ClientElementLink>();
    private List<ClientTransformElement> workers = new ArrayList<ClientTransformElement>();

    public void setName(String name)
    {
        this.projectName = name;
    }

    public String getName()
    {
        return this.projectName;
    }

    public List<ClientElementLink> getLinkList()
    {
        return this.links;
    }

    public List<ClientTransformElement> getWorkList()
    {
        return this.workers;
    }

    public void add(ClientElementLink link)
    {
        links.add(link);
    }

    public void add(ClientTransformElement element)
    {
        workers.add(element);
    }

    public void remove(ClientElementLink link)
    {
        links.remove(link);
    }

    public void remove(ClientTransformElement element)
    {
        workers.remove(element);
    }

    public boolean contains(ClientElementLink link)
    {
        return links.contains(link);
    }

    public boolean contains(ClientTransformElement element)
    {
        return workers.contains(element);
    }
}
