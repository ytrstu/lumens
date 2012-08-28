/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lumens.client.view;

import com.smartgwt.client.widgets.layout.Layout;

/**
 *
 * @author washaofe
 */
public interface ActiveView
{
    public boolean isActive();

    public void setActive(boolean active);

    public void setLayoutContainer(Layout layout);
}
