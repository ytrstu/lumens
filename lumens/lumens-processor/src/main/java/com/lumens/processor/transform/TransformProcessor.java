/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.processor.transform;

import com.lumens.model.AccessPath;
import com.lumens.model.DataElement;
import com.lumens.model.Element;
import com.lumens.model.Path;
import com.lumens.model.PathToken;
import com.lumens.model.Type;
import com.lumens.processor.Input;
import com.lumens.processor.Processor;
import com.lumens.processor.Script;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author shaofeng wang
 */
public class TransformProcessor implements Processor
{
    // private boolean ignoreNull = Boolean.getBoolean("transform.ignore.null");
    @Override
    public Object process(Input input)
    {
        if (input instanceof TransformInput)
        {
            TransformInput transInput = (TransformInput) input;
            Element inputElement = transInput.getData();
            TransformRuleItem ruleItem = transInput.getRule().getRuleEntry();
            Element resultElement = new DataElement(ruleItem.getFormat());
            TransformContext ctx = new TransformContext(inputElement, resultElement);
            executeTransformRule(ctx, ruleItem, resultElement);
            ArrayDeque<TransformPair> queue = new ArrayDeque<TransformPair>();
            queue.add(new TransformPair(resultElement, ruleItem));
            TransformPair item;
            while (!queue.isEmpty())
            {
                item = queue.removeFirst();
                processRuleItems(ctx, item);
                putChildrenElementsIntoProcessQueue(item, queue);
            }
            return ctx.getResultElement();
        }
        return null;
    }

    private void processRuleItems(TransformContext ctx, TransformPair item)
    {
        Element currentElement = item.getFirst();
        TransformRuleItem ruleItem = item.getSecond();
        ctx.setCurrentElementSearchEntry(getElementSearchEntry(ctx, currentElement));

        if (currentElement.isArray())
        {
            List<Element> elemList = buildArrayIterationElementList(ctx, ruleItem);
            buildElementArrayItem(ctx, ruleItem, currentElement, elemList);
        }
        else if (currentElement.isStruct())
        {
            List<TransformRuleItem> children = ruleItem.getChildren();
            for (TransformRuleItem child : children)
            {
                Element childElem = executeTransformRule(ctx, child, currentElement.newChild(child.
                        getFormat()));
                if (childElem != null)
                    currentElement.addChild(childElem);
            }
        }
    }

    private Element getElementSearchEntry(TransformContext ctx, Element currentElement)
    {
        Element elementSearchEntry = ctx.getParentArrayElement(currentElement);

        if (elementSearchEntry == null)
            elementSearchEntry = ctx.getInputElement();

        return elementSearchEntry;
    }

    private void buildElementArrayItem(TransformContext ctx, TransformRuleItem ruleItem,
                                       Element currentElement, List<Element> arrayItems)
    {
        for (Element item : arrayItems)
        {
            Element childElem = executeTransformRule(ctx, ruleItem, currentElement.newArrayItem());
            if (childElem != null)
            {
                currentElement.addArrayItem(childElem);
                ctx.putParentArrayElement(childElem, item);
            }
        }
    }

    private List<Element> buildArrayIterationElementList(TransformContext ctx,
                                                         TransformRuleItem ruleItem)
    {
        Element elementSearchEntry = ctx.getCurrentElementSearchEntry();
        String arrayIterationPathStr = ruleItem.getArrayIterationPath();
        Path arrayIterationPath = new AccessPath(arrayIterationPathStr);

        if (elementSearchEntry != null && elementSearchEntry.getLevel() > 0)
            arrayIterationPath.removeLeft(elementSearchEntry.getLevel());

        return getAllElementsFromEntry(elementSearchEntry, arrayIterationPath);
    }

    private List<Element> getAllElementsFromEntry(Element arrayElementEntry, Path arrayIterationPath)
    {
        List<Element> itemList = new ArrayList<Element>();
        itemList.add(arrayElementEntry);
        String pathToken = null;
        Iterator<PathToken> it = arrayIterationPath.iterator();
        while (!itemList.isEmpty() && it.hasNext())
        {
            pathToken = it.next().toString();
            itemList = getChildItemsOfCurrentLevel(pathToken, itemList);
        }

        return itemList;
    }

    private List<Element> getChildItemsOfCurrentLevel(String pathToken,
                                                      List<Element> itemList)
    {
        ArrayList<Element> childItems = new ArrayList<Element>();
        for (Element item : itemList)
        {
            if (item.isStruct())
            {
                item = item.getChild(pathToken);
                if (item != null && item.isStruct())
                    childItems.add(item);
                else if (item != null && item.isArray() && item.getChildren().size() > 0)
                    childItems.addAll(item.getChildren());
            }
            else if (item.isField())
                throw new RuntimeException(
                        "Wrong path is used for array-to-array or array to struct transform rule");
        }

        return childItems;
    }

    private void putChildrenElementsIntoProcessQueue(TransformPair item,
                                                     ArrayDeque<TransformPair> queue)
    {
        Element currentElement = item.getFirst();
        if (currentElement.isField())
            return;

        TransformRuleItem ruleItem = item.getSecond();
        List<TransformRuleItem> ruleItemChildren = ruleItem.getChildren();
        List<Element> children = currentElement.getChildren();
        for (int i = 0; i < children.size(); ++i)
        {
            if (!currentElement.isArray())
                ruleItem = ruleItemChildren.get(i);
            queue.add(new TransformPair(children.get(i), ruleItem));
        }
    }

    private Element executeTransformRule(TransformContext ctx, TransformRuleItem ruleItem,
                                         Element result)
    {
        ctx.setCurrentElement(result);

        if (result.isArray())
            return result;

        Script script = ruleItem.getScript();
        if (script != null)
        {
            Object value = script.execute(ctx);
            if (result.getFormat().getType() != Type.NONE && value != null)
                result.setValue(value);
        }
        return result;
    }
}
