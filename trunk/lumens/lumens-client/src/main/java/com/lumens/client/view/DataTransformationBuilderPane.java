/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lumens.client.view;

import com.lumens.client.view.element.ElementLink;
import com.lumens.client.view.element.TransformationElement;
import com.smartgwt.client.types.Cursor;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.drawing.DrawPane;
import com.smartgwt.client.widgets.drawing.DrawPath;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author washaofe
 */
public class DataTransformationBuilderPane extends Canvas
{
    private List<ElementLink> links = new ArrayList<ElementLink>();
    private List<TransformationElement> workers = new ArrayList<TransformationElement>();
    private DrawPane drawPane;

    public DataTransformationBuilderPane()
    {
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
        drawPane = new DrawPane();
        drawPane.setWidth100();
        drawPane.setHeight100();
        drawPane.setOverflow(Overflow.HIDDEN);
        drawPane.setCursor(Cursor.AUTO);
        addChild(drawPane);
        for (TransformationElement e : workers)
            drawPane.addChild((Canvas) e);
        for (ElementLink e : links)
            drawPane.addDrawItem((DrawPath) e, true);
    }
}
