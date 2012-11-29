package com.lumens.client.view.transformdesign;

import com.smartgwt.client.types.Cursor;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.drawing.DrawPane;
import com.smartgwt.client.widgets.drawing.DrawPath;
import com.smartgwt.client.widgets.events.DoubleClickEvent;
import com.smartgwt.client.widgets.events.DoubleClickHandler;
import com.smartgwt.client.widgets.events.DropOutEvent;
import com.smartgwt.client.widgets.events.DropOutHandler;
import com.smartgwt.client.widgets.events.DropOverEvent;
import com.smartgwt.client.widgets.events.DropOverHandler;
import com.smartgwt.client.widgets.events.MouseDownEvent;
import com.smartgwt.client.widgets.events.MouseDownHandler;
import com.smartgwt.client.widgets.events.MouseMoveEvent;
import com.smartgwt.client.widgets.events.MouseMoveHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author shaofeng wang
 */
public class DataTransformDesignerPane extends Canvas implements
        MouseMoveHandler, DoubleClickHandler, MouseDownHandler
{
    private List<ElementLink> links = new ArrayList<ElementLink>();
    private List<TransformElement> workers = new ArrayList<TransformElement>();
    private VirtualTransformElement vElement;
    private TransformElement focusTransformElem;
    private DrawPane drawPane;
    private ComponentSettingsListGrid paramList;
    private HLayout builderPaneLayout;
    private VLayout builderLayout;

    public DataTransformDesignerPane()
    {
        drawPane = new DrawPane();
        drawPane.setWidth(3000);
        drawPane.setHeight(3000);
        drawPane.setOverflow(Overflow.HIDDEN);
        drawPane.setCursor(Cursor.AUTO);
        addChild(drawPane);
        setCanAcceptDrop(true);
        setCanDrop(true);
        addDropHandler(new DataSourceCreator(this));
        addDropOverHandler(new DropOverHandler()
        {
            @Override
            public void onDropOver(DropOverEvent event)
            {
                setBackgroundColor("#F2F2F2");
            }
        });

        addDropOutHandler(new DropOutHandler()
        {
            @Override
            public void onDropOut(DropOutEvent event)
            {
                setBackgroundColor("#ffffff");
            }
        });

        this.addMouseMoveHandler(this);
        this.addMouseDownHandler(this);
        this.addDoubleClickHandler(this);
    }

    public void addElement(Object element)
    {
        if (element instanceof ElementLink)
        {
            ElementLink link = (ElementLink) element;
            if (!links.contains(link))
            {
                links.add(link);
                drawPane.addDrawItem((DrawPath) link, true);
                drawPane.addDrawItem(link.getAnchorPoint(), true);
            }
        } else if (element instanceof TransformElement)
        {
            TransformElement tElement = (TransformElement) element;
            if (!workers.contains(tElement))
            {
                workers.add(tElement);
                addChild(tElement);
            }
        } else
            throw new RuntimeException("Not support element type");
    }

    public void setVirtualElement(VirtualTransformElement vElement)
    {
        this.vElement = vElement;
        addChild(vElement);
    }

    public void removeVirtualElement()
    {
        if (vElement != null)
        {
            ElementLink[] linkList = vElement.getInLinkList();
            for (ElementLink link : linkList)
            {
                link.remove();
                links.remove(link);
            }
            vElement.remove();
            vElement = null;
        }
    }

    public VirtualTransformElement getVirtualElement()
    {
        return vElement;
    }

    public void paint()
    {
        if (drawPane != null)
            drawPane.erase();
        for (ElementLink e : links)
        {
            drawPane.addDrawItem((DrawPath) e, true);
            drawPane.addDrawItem(e.getAnchorPoint(), true);
        }
    }

    @Override
    public void onMouseMove(MouseMoveEvent event)
    {
        if (vElement != null)
        {
            vElement.setLeft(event.getX() - getAbsoluteLeft());
            vElement.setTop(event.getY() - getAbsoluteTop());
            vElement.onDragMove(null);
        }
    }

    @Override
    public void onDoubleClick(DoubleClickEvent event)
    {
        removeVirtualElement();
        paint();
    }

    @Override
    public void onMouseDown(MouseDownEvent event)
    {
        Object source = event.getSource();
        if (focusTransformElem != null)
            focusTransformElem.setFocus(false);

        if (source instanceof TransformElement)
        {
            TransformElement tElement = (TransformElement) source;
            focusTransformElem = tElement;
            focusTransformElem.setFocus(true);
        }
    }

    public void setParamList(ComponentSettingsListGrid paramList)
    {
        this.paramList = paramList;
    }

    public ComponentSettingsListGrid getParamList()
    {
        return this.paramList;
    }

    public void setBuilderLayout(VLayout builderLayout)
    {
        this.builderLayout = builderLayout;
    }

    public VLayout getBuilderLayout()
    {
        return this.builderLayout;
    }

    public void setBuilderPaneLayout(HLayout builderPaneLayout)
    {
        this.builderPaneLayout = builderPaneLayout;
    }

    public HLayout getBuilderPaneLayout()
    {
        return this.builderPaneLayout;
    }
}
