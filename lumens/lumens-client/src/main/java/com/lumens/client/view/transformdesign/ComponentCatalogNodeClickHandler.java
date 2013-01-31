/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.client.view.transformdesign;

import com.google.gwt.core.client.GWT;
import com.lumens.client.WebClientController;
import com.lumens.client.constant.ViewConstants;
import com.lumens.client.rpc.ComponentServiceAsyncCallback;
import com.lumens.client.rpc.LumensService;
import com.lumens.client.rpc.LumensServiceAsync;
import com.lumens.client.rpc.beans.ComponentRegistry;
import com.smartgwt.client.types.TreeModelType;
import com.smartgwt.client.widgets.grid.CellFormatter;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.events.SectionHeaderClickEvent;
import com.smartgwt.client.widgets.layout.events.SectionHeaderClickHandler;
import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeGrid;
import com.smartgwt.client.widgets.tree.TreeGridField;
import com.smartgwt.client.widgets.tree.TreeNode;

/**
 *
 * @author shaofeng wang (shaofeng.cjpw@gmail.com)
 */
public class ComponentCatalogNodeClickHandler implements
        SectionHeaderClickHandler,
        ViewConstants
{
    private SectionStack sectionStack;

    public ComponentCatalogNodeClickHandler(SectionStack sectionStack)
    {
        this.sectionStack = sectionStack;
    }

    @Override
    public void onSectionHeaderClick(SectionHeaderClickEvent event)
    {
        SectionStackSection section = event.getSection();
        if (PROCESSOR_SECTION_ID.equals(section.getID())
            && WebClientController.componentManager.getProcessorCount() == 0)
        {
            TreeGrid treeGrid = (TreeGrid) section.getItems()[0];
            ComponentNode[] ds = new ComponentNode[1];
            ds[0] = new ComponentNode(new ComponentRegistry("", "",
                                                            "../loading.gif", "",
                                                            null));
            TreeNode root = new TreeNode("ComponentRoot", ds);

            Tree dataSourceTree = new Tree();
            dataSourceTree.setModelType(TreeModelType.CHILDREN);
            dataSourceTree.setNameProperty(COMPONENT_NAME);
            dataSourceTree.setRoot(root);
            // group tree grid
            TreeGridField connectorsField = new TreeGridField(
                    "ComponentField");
            connectorsField.setShowHover(false);
            connectorsField.setCellFormatter(new CellFormatter()
            {
                @Override
                public String format(Object value, ListGridRecord record,
                                     int rowNum,
                                     int colNum)
                {
                    return "Loading ...";
                }
            });
            treeGrid.setData(dataSourceTree);
            treeGrid.setFields(connectorsField);
            // Make a http call to read the datasource list from server
            getProcessorCatalogGroup(treeGrid);
        }
    }

    private void getProcessorCatalogGroup(TreeGrid treeGrid)
    {
        LumensServiceAsync dsService = (LumensServiceAsync) GWT.create(
                LumensService.class);
        dsService.getProcessorCatalog(
                new ComponentServiceAsyncCallback(PROCESSOR_SECTION_ID,
                                                  treeGrid));
    }
}
