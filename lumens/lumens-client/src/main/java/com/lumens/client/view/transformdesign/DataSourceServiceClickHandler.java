/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lumens.client.view.transformdesign;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.lumens.client.rpc.beans.ComponentRegistry;
import com.lumens.client.rpc.DataSourceService;
import com.lumens.client.rpc.DataSourceServiceAsync;
import com.lumens.client.view.ViewConstants;
import com.smartgwt.client.types.TreeModelType;
import com.smartgwt.client.widgets.grid.CellFormatter;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.SectionHeader;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.events.OnSectionHeaderClickHandler;
import com.smartgwt.client.widgets.layout.events.SectionHeaderClickEvent;
import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeGrid;
import com.smartgwt.client.widgets.tree.TreeGridField;
import com.smartgwt.client.widgets.tree.TreeNode;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author washaofe
 */
public class DataSourceServiceClickHandler implements
        OnSectionHeaderClickHandler,
        ViewConstants
{
    private SectionStack sectionStack;
    private Map<String, DataSourceNode> dataSourceList = new HashMap<String, DataSourceNode>();
    private Map<String, DataSourceNode> processorList = new HashMap<String, DataSourceNode>();

    public DataSourceServiceClickHandler(SectionStack sectionStack)
    {
        this.sectionStack = sectionStack;
    }

    @Override
    public void onOnSectionHeaderClick(SectionHeaderClickEvent event)
    {
        SectionHeader header = event.getSection();
        if (DATASOURCE_SECTION_ID.equals(header.getSection().getID())
            && dataSourceList.isEmpty())
        {
            SectionStackSection section = header.getSection();
            TreeGrid tree = (TreeGrid) section.getItems()[0];
            // Make a http call to read the datasource list from server
            getDataSourceCatalogGroup(tree);
        }
    }

    private void getDataSourceCatalogGroup(TreeGrid treeGrid)
    {
        DataSourceServiceAsync dsService = (DataSourceServiceAsync) GWT.create(
                DataSourceService.class);
        dsService.getDataSourceCatalog(new DataSourceAsyncCallback(treeGrid));
    }

    private class DataSourceAsyncCallback implements
            AsyncCallback<ComponentRegistry[]>
    {
        private TreeGrid treeGrid;

        public DataSourceAsyncCallback(TreeGrid treeGrid)
        {
            this.treeGrid = treeGrid;
        }

        @Override
        public void onFailure(Throwable caught)
        {
        }

        @Override
        public void onSuccess(ComponentRegistry[] result)
        {
            DataSourceNode[] ds = new DataSourceNode[result.length];
            int index = 0;
            for (ComponentRegistry registry : result)
            {
                ds[index++] = new DataSourceNode(registry.getID(), registry.
                        getName(), registry.getIcon());
            }
            for (DataSourceNode dsn : ds)
                dataSourceList.put(dsn.getDataSourceName(), dsn);
            TreeNode root = new TreeNode("Root", ds);

            Tree dataSourceTree = new Tree();
            dataSourceTree.setModelType(TreeModelType.CHILDREN);
            dataSourceTree.setNameProperty(DATASOURCE_NAME);
            dataSourceTree.setRoot(root);
            // group tree grid
            TreeGridField connectorsField = new TreeGridField(
                    "DatasourceGroup");
            connectorsField.setShowHover(false);
            connectorsField.setCellFormatter(new CellFormatter()
            {
                @Override
                public String format(Object value, ListGridRecord record,
                                     int rowNum,
                                     int colNum)
                {
                    return record.getAttributeAsString(DATASOURCE_NAME);
                }
            });
            treeGrid.setData(dataSourceTree);
            treeGrid.setFields(connectorsField);
        }
    }
}
