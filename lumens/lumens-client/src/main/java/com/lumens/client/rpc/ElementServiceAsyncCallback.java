/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lumens.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.lumens.client.WebClientController;
import com.lumens.client.utils.CConstants;
import com.lumens.client.rpc.beans.CComponentTypeRegistry;
import com.lumens.client.view.transformdesign.CComponentTypeNode;
import com.smartgwt.client.types.TreeModelType;
import com.smartgwt.client.widgets.grid.CellFormatter;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeGrid;
import com.smartgwt.client.widgets.tree.TreeGridField;
import com.smartgwt.client.widgets.tree.TreeNode;

/**
 *
 * @author washaofe
 */
public class ElementServiceAsyncCallback implements
        AsyncCallback<CComponentTypeRegistry[]>, CConstants
{
    private String sectionID;
    private TreeGrid treeGrid;

    public ElementServiceAsyncCallback(String sectionID, TreeGrid treeGrid)
    {
        this.sectionID = sectionID;
        this.treeGrid = treeGrid;
    }

    @Override
    public void onFailure(Throwable caught)
    {
    }

    @Override
    public void onSuccess(CComponentTypeRegistry[] result)
    {
        CComponentTypeNode[] ds = new CComponentTypeNode[result.length];
        int index = 0;
        for (CComponentTypeRegistry registry : result)
        {
            ds[index++] = new CComponentTypeNode(registry);
            if (PROCESSOR_SECTION_ID.equals(sectionID))
                WebClientController.componentManager.registerProcessor(
                        registry);
            else if (DATASOURCE_SECTION_ID.equals(sectionID))
                WebClientController.componentManager.registerDataSource(
                        registry);

        }
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
                return record.getAttributeAsString(COMPONENT_NAME);
            }
        });
        treeGrid.setData(dataSourceTree);
        treeGrid.setFields(connectorsField);
    }
}
