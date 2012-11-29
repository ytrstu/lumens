package com.lumens.client.view.transformdesign;

import com.lumens.client.rpc.beans.ComponentParameter;
import com.lumens.client.rpc.beans.ComponentRegistry;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.DragAppearance;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.HeaderControl;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.DragMoveEvent;
import com.smartgwt.client.widgets.events.DragMoveHandler;
import com.smartgwt.client.widgets.events.MouseOutEvent;
import com.smartgwt.client.widgets.events.MouseOutHandler;
import com.smartgwt.client.widgets.events.MouseOverEvent;
import com.smartgwt.client.widgets.events.MouseOverHandler;
import com.smartgwt.client.widgets.layout.VStack;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author shaofeng wang
 */
public class TransformElement extends Canvas implements DragMoveHandler,
                                                        MouseOverHandler,
                                                        MouseOutHandler
{
    private List<ElementLink> outLinks = new ArrayList<ElementLink>();
    private List<ElementLink> inLinks = new ArrayList<ElementLink>();
    private DataTransformDesignerPane designerPane;
    private ToolStrip tool;
    private Label label;
    private boolean isFocus;
    private ComponentRegistry component;

    public TransformElement()
    {
        setWidth(1);
        setHeight(1);
        setCanDrag(true);
        setKeepInParentRect(true);
        setDragAppearance(DragAppearance.TARGET);
        addDragMoveHandler(this);
    }

    public TransformElement(ComponentRegistry component,
                            DataTransformDesignerPane designerPane)
    {
        this.component = component;
        this.designerPane = designerPane;
        int width = 150, height = 52;
        setWidth(width);
        setHeight(height);
        setShowEdges(true);
        setEdgeBackgroundColor("#cedff7");
        setEdgeSize(1);
        setBackgroundColor("#dae7f9");
        bringToFront();

        setKeepInParentRect(true);
        setDragAppearance(DragAppearance.TARGET);
        addDragMoveHandler(this);

        VStack vStack = new VStack();
        vStack.setWidth(width);
        vStack.setHeight(height);
        tool = new ToolStrip();
        tool.bringToFront();
        tool.setWidth(width);
        tool.setHeight(20);
        tool.addMouseOverHandler(this);
        tool.addMouseOutHandler(this);
        tool.setAlign(Alignment.RIGHT);
        HeaderControl settings = new HeaderControl(HeaderControl.SETTINGS);
        ComponentSettingsHandler pSettings = new ComponentSettingsHandler(this,
                                                                          this.designerPane);
        settings.addClickHandler(pSettings);
        tool.addMember(settings);
        HeaderControl link = new HeaderControl(HeaderControl.TRANSFER);
        link.setCanAcceptDrop(true);
        ProcessorCreator pCreator = new ProcessorCreator(this, this.designerPane);
        link.addDropHandler(pCreator);
        link.addClickHandler(pCreator);
        link.bringToFront();
        tool.addMember(link);
        vStack.addMember(tool);

        label = new Label();
        label.setWidth(width);
        label.setHeight(32);
        label.setIconSize(32);
        label.setIcon(component.getComponentIcon());
        label.setAlign(Alignment.LEFT);
        label.setContents(component.getName());
        vStack.addMember(label);
        this.addChild(vStack);
        this.addMouseDownHandler(designerPane);
    }

    public String getComponentID()
    {
        return component.getId();
    }

    public void addOutLink(ElementLink link)
    {
        outLinks.add(link);
    }

    public void addInLink(ElementLink link)
    {
        inLinks.add(link);
    }

    public ElementLink[] getInLinkList()
    {
        ElementLink[] links = new ElementLink[inLinks.size()];
        return inLinks.toArray(links);
    }

    public ElementLink[] getOutLinkList()
    {
        ElementLink[] links = new ElementLink[outLinks.size()];
        return outLinks.toArray(links);
    }

    public void removeOutLink(ElementLink link)
    {
        if (link != null)
            outLinks.remove(link);
        else
            outLinks.clear();
    }

    public void removeInLink(ElementLink link)
    {
        if (link != null)
            inLinks.remove(link);
        else
            inLinks.clear();
    }

    public void remove()
    {
        removeFromParent();
        destroy();
    }

    public void updatePosition()
    {
        for (ElementLink link : outLinks)
            link.updatePosition();
        for (ElementLink link : inLinks)
            link.updatePosition();
    }

    public Collection<ComponentParameter> getParameters()
    {
        return component.getParameters();
    }

    @Override
    public void onDragMove(DragMoveEvent event)
    {
        updatePosition();
    }

    @Override
    public void onMouseOut(MouseOutEvent event)
    {
        setCanDrag(false);
    }

    @Override
    public void onMouseOver(MouseOverEvent event)
    {
        setCanDrag(true);
    }

    public void setFocus(boolean isFocus)
    {
        this.isFocus = isFocus;
    }

    public boolean isFocus()
    {
        return this.isFocus;
    }
}
