/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lumens.client.view.transformdesign;

import com.lumens.client.WebClientController;
import com.lumens.client.rpc.beans.ClientElementLink;
import com.lumens.client.rpc.beans.ClientTransformElement;
import com.lumens.client.rpc.beans.ComponentRegistry;
import com.smartgwt.client.util.EventHandler;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.DropEvent;
import com.smartgwt.client.widgets.events.DropHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.tree.TreeGrid;

/**
 *
 * @author Shaofeng Wang
 */
public class ProcessorCreator implements DropHandler, ClickHandler
{
    private DataTransformDesignerPane designerPane;
    private ClientTransformElement tElement;

    public ProcessorCreator(ClientTransformElement tElement,
                            DataTransformDesignerPane designer)
    {
        this.tElement = tElement;
        this.designerPane = designer;
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
                        lookupProcessor(componentNode.getComponentID());
                if (component != null)
                {
                    ClientTransformElement processor = new ClientTransformElement(component,
                                                                      designerPane);
                    VirtualTransformElement vElement = new VirtualTransformElement(
                            processor);
                    vElement.
                            setTop(event.getY() - designerPane.getAbsoluteTop());
                    vElement.setLeft(event.getX() - designerPane.
                            getAbsoluteLeft());
                    ClientElementLink link = new ClientElementLink();
                    link.set(tElement, vElement);
                    designerPane.addElement(link);
                    designerPane.setVirtualElement(vElement);
                }
            }
        }
    }

    @Override
    public void onClick(ClickEvent event)
    {
        VirtualTransformElement vElement = designerPane.getVirtualElement();
        if (vElement != null)
        {
            ClientTransformElement processor = vElement.getTransformElement();
            ClientElementLink link = vElement.getInLinkList()[0];
            designerPane.removeVirtualElement();
            ClientTransformElement tSrc = link.getIn();
            processor.setLeft((tSrc.getLeft() + tElement.getLeft()) / 2);
            processor.setTop((tSrc.getTop() + tElement.getTop()) / 2);
            link.set(link.getIn(), processor);
            processor.updatePosition();
            ClientElementLink link2 = new ClientElementLink();
            link2.set(processor, tElement);
            designerPane.addElement(link);
            designerPane.addElement(processor);
            designerPane.addElement(link2);
            // TODO Save the link between a transform processor and a datasource
            // TODO Add these information into server cache
        }
    }
}
