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
import com.lumens.processor.AbstractProcessor;
import com.lumens.processor.Rule;
import com.lumens.processor.Script;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author shaofeng wang
 */
public class TransformProcessor extends AbstractProcessor
{
    // private boolean ignoreNull = Boolean.getBoolean("transform.ignore.null");
    @Override
    public Object execute(Rule rule, Element element)
    {
        if (rule instanceof TransformRule)
        {
            TransformRule transformRule = (TransformRule) rule;
            Element inputElement = element;
            List<Element> results = new ArrayList<Element>();
            TransformRuleItem ruleItem = transformRule.getRootRuleItem();
            String arrayIterationPath = ruleItem.getArrayIterationPath();
            List<Element> items;
            if (arrayIterationPath != null)
            {
                items = getAllElementsFromEntry(inputElement, new AccessPath(
                        arrayIterationPath));
            }
            else
            {
                items = new ArrayList<Element>();
                items.add(inputElement);
            }
            for (Element elem : items)
            {
                Element resultElement = new DataElement(ruleItem.getFormat());
                TransformContext ctx = new TransformContext(inputElement, resultElement);
                ctx.putParentArrayElement(resultElement, elem);
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
                results.add(ctx.getResultElement());
            }

            return results;
        }
        throw new IllegalArgumentException("Incorrect arguments data");
    }

    private void processRuleItems(TransformContext ctx, TransformPair item)
    {
        Element currentElement = item.getFirst();
        TransformRuleItem ruleItem = item.getSecond();
        ctx.setAccessPathEntry(getAccessPathEntry(ctx, currentElement));

        if (currentElement.isArray())
        {
            List<Element> elemList = buildArrayIterationElementList(ctx, ruleItem);
            buildElementArrayItem(ctx, ruleItem, currentElement, elemList);
        }
        else if (currentElement.isStruct())
        {
            List<TransformRuleItem> children = ruleItem.getChildren();
            if (children != null)
            {
                for (TransformRuleItem child : children)
                {
                    Element childElem = executeTransformRule(ctx, child, currentElement.
                            newChild(child.getFormat()));
                    if (childElem != null)
                        currentElement.addChild(childElem);
                }
            }
        }
    }

    private Element getAccessPathEntry(TransformContext ctx, Element currentElement)
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
        Element elementSearchEntry = ctx.getAccessPathEntry();
        String arrayIterationPathStr = ruleItem.getArrayIterationPath();
        Path arrayIterationPath = new AccessPath(arrayIterationPathStr);

        if (elementSearchEntry != null && elementSearchEntry.getLevel() > 0)
            arrayIterationPath = arrayIterationPath.removeLeft(elementSearchEntry.getLevel());

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
        if (ruleItemChildren != null)
        {
            List<Element> children = currentElement.getChildren();
            for (int i = 0; i < children.size(); ++i)
            {
                if (!currentElement.isArray())
                    ruleItem = ruleItemChildren.get(i);
                queue.add(new TransformPair(children.get(i), ruleItem));
            }
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
