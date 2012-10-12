package com.lumens.client.view.transformdesign;

import com.smartgwt.client.types.Cursor;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.util.EventHandler;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.drawing.DrawPane;
import com.smartgwt.client.widgets.drawing.DrawPath;
import com.smartgwt.client.widgets.events.DropEvent;
import com.smartgwt.client.widgets.events.DropHandler;
import com.smartgwt.client.widgets.events.DropOutEvent;
import com.smartgwt.client.widgets.events.DropOutHandler;
import com.smartgwt.client.widgets.events.DropOverEvent;
import com.smartgwt.client.widgets.events.DropOverHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.tree.TreeGrid;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author shaofeng wang
 */
public class DataTransformDesignerPane extends Canvas implements DropHandler
{
    private List<ElementLink> links = new ArrayList<ElementLink>();
    private List<TransformationElement> workers = new ArrayList<TransformationElement>();
    private DrawPane drawPane;

    public DataTransformDesignerPane()
    {
        setCanAcceptDrop(true);
        setCanDrop(true);
        addDropHandler(this);
        addDropOverHandler(new DropOverHandler()
        {
            @Override
            public void onDropOver(DropOverEvent event)
            {
                setBackgroundColor("#FFFF88");
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
    }

    public void addElement(Object element)
    {
        if (element instanceof ElementLink)
            links.add((ElementLink) element);
        else if (element instanceof TransformationElement)
            workers.add((TransformationElement) element);
        else
            throw new RuntimeException("Not support element type");
    }

    public void buildDrawItems()
    {
        if (drawPane != null)
            this.removeChild(drawPane);
        else
        {
            drawPane = new DrawPane();
            drawPane.setWidth100();
            drawPane.setHeight100();
            drawPane.setOverflow(Overflow.HIDDEN);
            drawPane.setCursor(Cursor.AUTO);
        }
        addChild(drawPane);
        for (TransformationElement e : workers)
            drawPane.addChild((Canvas) e);
        for (ElementLink e : links)
        {
            drawPane.addDrawItem((DrawPath) e, true);
            drawPane.addDrawItem(e.getAnchorPoint(), true);
        }
    }

    @Override
    public void onDrop(DropEvent event)
    {
        Canvas target = EventHandler.getDragTarget();
        if (target instanceof TreeGrid)
        {
            TreeGrid tree = (TreeGrid) target;
            ListGridRecord record = tree.getSelectedRecord();
            if (record instanceof DataSourceNode)
            {
                DataSourceNode sourceNode = (DataSourceNode) record;
                SC.
                        say("You dropped the " + sourceNode.getDataSourceID() + ":" + sourceNode.
                        getDataSourceName());
            }
        }
    }
}
