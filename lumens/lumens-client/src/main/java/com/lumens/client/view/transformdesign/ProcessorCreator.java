/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lumens.client.view.transformdesign;

import com.lumens.client.WebClientController;
import com.lumens.client.rpc.beans.CLink;
import com.lumens.client.rpc.beans.CComponent;
import com.lumens.client.rpc.beans.CComponentTypeRegistry;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.EventHandler;
import com.smartgwt.client.util.SC;
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
    private DataTransformDesignPane designPane;
    private CComponent tElement;

    public ProcessorCreator(CComponent tElement,
                            DataTransformDesignPane designer)
    {
        this.tElement = tElement;
        this.designPane = designer;
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
                        lookupProcessor(componentNode.getComponentID());
                if (component != null)
                {
                    CComponent processor = new CComponent(
                            component.copy(), designPane);
                    VirtualTransformElement vElement = new VirtualTransformElement(
                            processor);
                    vElement.
                            setTop(event.getY() - designPane.getAbsoluteTop());
                    vElement.setLeft(event.getX() - designPane.
                            getAbsoluteLeft());
                    CLink link = new CLink();
                    link.set(tElement, vElement);
                    designPane.addElement(link);
                    designPane.setVirtualElement(vElement);
                }
            }
        }
    }

    @Override
    public void onClick(ClickEvent event)
    {
        VirtualTransformElement vElement = designPane.getVirtualElement();
        if (vElement != null)
        {
            CComponent processor = vElement.getTransformElement();
            CLink link = vElement.getInLinkList()[0];
            designPane.removeVirtualElement();
            CComponent tSrc = link.getIn();
            processor.setLeft((tSrc.getLeft() + tElement.getLeft()) / 2);
            processor.setTop((tSrc.getTop() + tElement.getTop()) / 2);
            link.set(link.getIn(), processor);
            processor.updatePosition();
            CLink link2 = new CLink();
            link2.set(processor, tElement);
            designPane.addElement(link);
            designPane.addElement(processor);
            designPane.addElement(link2);
            // TODO Save the link between a transform processor and a datasource
            // TODO Add these information into server cache
        }
    }
}
