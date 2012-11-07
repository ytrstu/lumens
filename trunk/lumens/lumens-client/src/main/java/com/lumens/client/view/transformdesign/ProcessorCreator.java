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
    private TransformElement tElement;

    public ProcessorCreator(TransformElement tElement,
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
                    TransformElement processor = new TransformElement(component.
                            getID(), component.
                            getComponentIcon(), component.getName(),
                                                                      designerPane);
                    VirtualTransformElement vElement = new VirtualTransformElement(
                            processor);
                    vElement.
                            setTop(event.getY() - designerPane.getAbsoluteTop());
                    vElement.setLeft(event.getX() - designerPane.
                            getAbsoluteLeft());
                    ElementLink link = new ElementLink();
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
            TransformElement processor = vElement.getTransformElement();
            ElementLink link = vElement.getInLinkList()[0];
            designerPane.removeVirtualElement();
            TransformElement tSrc = link.getIn();
            processor.setLeft((tSrc.getLeft() + tElement.getLeft()) / 2);
            processor.setTop((tSrc.getTop() + tElement.getTop()) / 2);
            link.set(link.getIn(), processor);
            processor.updatePosition();
            ElementLink link2 = new ElementLink();
            link2.set(processor, tElement);
            designerPane.addElement(link);
            designerPane.addElement(processor);
            designerPane.addElement(link2);
        }
    }
}
