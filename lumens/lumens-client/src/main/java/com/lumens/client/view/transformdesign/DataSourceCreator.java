/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lumens.client.view.transformdesign;

import com.lumens.client.WebClientController;
import com.lumens.client.rpc.beans.CComponent;
import com.lumens.client.rpc.beans.CComponentTypeRegistry;
import com.smartgwt.client.util.BooleanCallback;
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
    private DataTransformDesignPane designPane;

    public DataSourceCreator(DataTransformDesignPane designPane)
    {
        this.designPane = designPane;
    }

    @Override
    public void onDrop(DropEvent event)
    {
        if (!designPane.hasProject())
        {
            // TODO error message is needed here
            SC.ask("Do you want create a new project ?", new BooleanCallback()
            {
                @Override
                public void execute(final Boolean value)
                {
                    if (value)
                        designPane.newProject("new project");
                }
            });
            if (!designPane.hasProject())
                return;
        }

        Canvas target = EventHandler.getDragTarget();
        if (target instanceof TreeGrid)
        {
            TreeGrid tree = (TreeGrid) target;
            ListGridRecord record = tree.getSelectedRecord();
            if (record instanceof CComponentTypeNode)
            {
                CComponentTypeNode componentNode = (CComponentTypeNode) record;
                CComponentTypeRegistry component = WebClientController.componentManager.
                        lookupDataSource(componentNode.getComponentID());
                if (component != null)
                {
                    CComponent worker = new CComponent(
                            component.copy(), designPane);
                    worker.setTop(event.getY() - designPane.getAbsoluteTop());
                    worker.setLeft(event.getX() - designPane.getAbsoluteLeft());
                    designPane.addElement(worker);
                    // TODO add data source in to server cache
                }
            }
        }
    }
}
