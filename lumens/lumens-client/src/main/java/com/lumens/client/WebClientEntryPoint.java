/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.lumens.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import com.lumens.client.view.WebClientView;

/**
 * Main entry point.
 *
 * @author Shaofeng Wang
 */
public class WebClientEntryPoint implements EntryPoint
{
    public WebClientEntryPoint()
    {
    }

    /**
     * The entry point method, called automatically by loading a module that declares an
     * implementing class as an entry-point
     */
    @Override
    public void onModuleLoad()
    {
        Window.enableScrolling(false);
        Window.setMargin("0px");
        RootPanel.get().add(WebClientView.build());
    }
}
