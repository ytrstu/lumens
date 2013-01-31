package com.lumens.client.view.transformdesign;

import com.lumens.client.rpc.beans.ClientTransformElement;
import com.lumens.client.rpc.beans.ClientElementLink;
import com.lumens.client.rpc.beans.ClientTransformProject;
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

/**
 *
 * @author shaofeng wang
 */
public class DataTransformDesignerPane extends Canvas implements
        MouseMoveHandler, DoubleClickHandler, MouseDownHandler
{
    private ClientTransformProject project;
    private VirtualTransformElement vElement;
    private ClientTransformElement focusTransformElem;
    private DrawPane drawPane;
    private ComponentSettingsListGrid paramList;
    private HLayout builderPaneLayout;
    private VLayout builderLayout;

    public DataTransformDesignerPane()
    {
        drawPane = new DrawPane();
        drawPane.setWidth("100%");
        drawPane.setHeight("100%");
        drawPane.setOverflow(Overflow.AUTO);
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

    public void openProject(String name)
    {
    }

    public void newProject(String name)
    {
        project = new ClientTransformProject();
        project.setName(name);
    }

    public void closeProject()
    {
        //TODO clean the objects in the transform workflow design pane
        project = null;
    }

    public boolean hasProject()
    {
        return project != null;
    }

    public void addElement(Object element)
    {
        if (element instanceof ClientElementLink)
        {
            ClientElementLink link = (ClientElementLink) element;
            if (!project.contains(link))
            {
                project.add(link);
                drawPane.addDrawItem((DrawPath) link, true);
                drawPane.addDrawItem(link.getAnchorPoint(), true);
            }
        } else if (element instanceof ClientTransformElement)
        {
            ClientTransformElement tElement = (ClientTransformElement) element;
            if (!project.contains(tElement))
            {
                project.add(tElement);
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
            ClientElementLink[] linkList = vElement.getInLinkList();
            for (ClientElementLink link : linkList)
            {
                link.remove();
                project.remove(link);
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

        if (project != null)
        {
            for (ClientElementLink e : project.getLinkList())
            {
                drawPane.addDrawItem((DrawPath) e, true);
                drawPane.addDrawItem(e.getAnchorPoint(), true);
            }
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

        if (source instanceof ClientTransformElement)
        {
            ClientTransformElement tElement = (ClientTransformElement) source;
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
