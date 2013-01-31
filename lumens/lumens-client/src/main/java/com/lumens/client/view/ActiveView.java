package com.lumens.client.view;

import com.smartgwt.client.widgets.layout.Layout;

/**
 *
 * @author shaofeng wang (shaofeng.cjpw@gmail.com)
 */
public interface ActiveView
{
    public boolean isActive();

    public void setActive(boolean active);

    public void setLayoutContainer(Layout layout);
}
