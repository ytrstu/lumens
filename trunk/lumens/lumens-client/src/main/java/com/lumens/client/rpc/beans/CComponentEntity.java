/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.client.rpc.beans;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author shaofeng wang (shaofeng.cjpw@gmail.com)
 */
public class CComponentEntity
{
    private List<CLinkEntity> outLinks = new ArrayList<CLinkEntity>();
    private List<CLinkEntity> inLinks = new ArrayList<CLinkEntity>();
    private CComponentTypeRegistry component;
}
