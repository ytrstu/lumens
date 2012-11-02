/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lumens.client.view.transformdesign;

import com.lumens.client.WebClientController;
import com.lumens.client.rpc.beans.ComponentRegistry;
import com.lumens.client.view.ComponentNode;
import com.smartgwt.client.util.EventHandler;
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
    private DataTransformDesignerPane designer;

    public DataSourceCreator(DataTransformDesignerPane designer)
    {
        this.designer = designer;
    }

    @Override
    public void onDrop(DropEvent event)
    {
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
                    TransformElement worker = new TransformElement(component.
                            getID(), component.
                            getComponentIcon(), component.getName(), designer);
                    worker.setTop(event.getY() - designer.getAbsoluteTop());
                    worker.setLeft(event.getX() - designer.getAbsoluteLeft());
                    designer.addElement(worker);
                }
            }
        }
    }
}
