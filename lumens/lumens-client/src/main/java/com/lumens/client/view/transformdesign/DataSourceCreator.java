/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lumens.client.view.transformdesign;

import com.lumens.client.WebClientController;
import com.lumens.client.rpc.beans.ClientTransformElement;
import com.lumens.client.rpc.beans.ComponentRegistry;
import com.smartgwt.client.util.EventHandler;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.events.DropEvent;
import com.smartgwt.client.widgets.events.DropHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.tree.TreeGrid;

/**
 *
 * @author washaofe
 */
public class DataSourceCreator implements DropHandler
{
    private DataTransformDesignerPane designPane;

    public DataSourceCreator(DataTransformDesignerPane designPane)
    {
        this.designPane = designPane;
    }

    @Override
    public void onDrop(DropEvent event)
    {
        if (designPane.hasProject() == false)
        {
            // TODO error message is needed here
            SC.say("Please create a new project");
            return;
        }

        Canvas target = EventHandler.getDragTarget();
        if (target instanceof TreeGrid)
        {
            TreeGrid tree = (TreeGrid) target;
            ListGridRecord record = tree.getSelectedRecord();
            if (record instanceof ComponentNode)
            {
                ComponentNode componentNode = (ComponentNode) record;
                ComponentRegistry component = WebClientController.componentManager.
                        lookupDataSource(componentNode.getComponentID());
                if (component != null)
                {
                    ClientTransformElement worker = new ClientTransformElement(component,
                                                                               designPane);
                    worker.setTop(event.getY() - designPane.getAbsoluteTop());
                    worker.setLeft(event.getX() - designPane.getAbsoluteLeft());
                    designPane.addElement(worker);
                    // TODO add data source in to server cache
                }
            }
        }
    }
}
