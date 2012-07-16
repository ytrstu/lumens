/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lumens.client.demo;

import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 *
 * @author washaofe
 */
public class DataLoader
{
    public static ListGridRecord[] getAgents()
    {
        return new DataTransformationRecord[]
                {
                    new DataTransformationRecord("0001", "ws-to-mssql 1", "Error",
                                                 "this is a testing", 50, 70, 85),
                    new DataTransformationRecord("0002", "ws-to-mssql 2", "Running",
                                                 "this is a testing", 20, 70, 85),
                    new DataTransformationRecord("0003", "ws-to-mssql 3", "Error",
                                                 "this is a testing", 20, 70, 85),
                    new DataTransformationRecord("0004", "ws-to-mssql 4", "Running",
                                                 "this is a testing", 20, 70, 85),
                    new DataTransformationRecord("0005", "ws-to-mssql 5", "Running",
                                                 "this is a testing", 20, 70, 85),
                    new DataTransformationRecord("0006", "ws-to-mssql 6", "Running",
                                                 "this is a testing", 20, 70, 85),
                    new DataTransformationRecord("0007", "ws-to-mssql 7", "Error",
                                                 "this is a testing", 20, 70, 85),
                    new DataTransformationRecord("0008", "ws-to-mssql 8", "Running",
                                                 "this is a testing", 20, 70, 85),
                    new DataTransformationRecord("0009", "ws-to-mssql 9", "Running",
                                                 "this is a testing", 20, 70, 85),
                    new DataTransformationRecord("0010", "ws-to-mssql 10", "Error",
                                                 "this is a testing", 20, 70, 85),
                    new DataTransformationRecord("0201", "ws-to-mssql 12", "Error",
                                                 "this is a testing", 20, 70, 85),
                    new DataTransformationRecord("0501", "ws-to-mssql 22", "Running",
                                                 "this is a testing", 20, 70, 85)
                };
    }

    public static AgentTreeNode getAgentNodes()
    {
        AgentTreeNode[] children1 = new AgentTreeNode[]
        {
            new AgentTreeNode(1, "Connect-It Agent 1", false, "Running.png", 3),
            new AgentTreeNode(2, "Connect-It Agent 2", false, "Running.png", 4),
            new AgentTreeNode(3, "Connect-It Agent 3", false, "Running.png", 3),
            new AgentTreeNode(4, "Connect-It Agent 4", false, "Running.png", 5),
            new AgentTreeNode(5, "Connect-It Agent 5", false, "Running.png", 3),
            new AgentTreeNode(6, "Connect-It Agent 6", false, "Running.png", 7),
            new AgentTreeNode(7, "Connect-It Agent 7", false, "Error.png", 8),
            new AgentTreeNode(8, "Connect-It Agent 8", false, "Running.png", 6),
            new AgentTreeNode(9, "Connect-It Agent 9", false, "Running.png", 4),
            new AgentTreeNode(10, "Connect-It Agent 10", false, "Running.png", 6),
            new AgentTreeNode(11, "Connect-It Agent 11", false, "Error.png", 5),
            new AgentTreeNode(13, "Connect-It Agent 13", false, "Running.png", 4),
            new AgentTreeNode(14, "Connect-It Agent 14", false, "Running.png", 8),
            new AgentTreeNode(12, "Connect-It Agent 12", false, "Running.png", 6),
            new AgentTreeNode(15, "Connect-It Agent 15", false, "Error.png", 5),
            new AgentTreeNode(16, "Connect-It Agent 16", false, "Running.png", 7),
            new AgentTreeNode(17, "Connect-It Agent 17", false, "Running.png", 3),
            new AgentTreeNode(18, "Connect-It Agent 18", false, "Running.png", 2)
        };
        AgentTreeNode[] children2 = new AgentTreeNode[]
        {
            new AgentTreeNode(19, "Connect-It Agent 19", false, "Running.png", 2),
            new AgentTreeNode(21, "Connect-It Agent 21", false, "Running.png", 1),
            new AgentTreeNode(31, "Connect-It Agent 31", false, "Running.png", 7),
            new AgentTreeNode(41, "Connect-It Agent 41", false, "Error.png", 8),
            new AgentTreeNode(51, "Connect-It Agent 51", false, "Error.png", 4),
            new AgentTreeNode(22, "Connect-It Agent 22", false, "Running.png", 1),
            new AgentTreeNode(23, "Connect-It Agent 23", false, "Error.png", 3),
            new AgentTreeNode(25, "Connect-It Agent 25", false, "Running.png", 9),
            new AgentTreeNode(25, "Connect-It Agent 25", false, "Running.png", 7)
        };
        return new AgentTreeNode(1000, "Root", true, null, 0, new AgentTreeNode[]
                {
                    new AgentTreeNode(1001, "WS-to-MSSQLDB Transform", false, "Group.png", 0,
                                      children1),
                    new AgentTreeNode(1002, "XML-to-MSSQLDB Transform", false, "Group.png", 0,
                                      children2)
                });
    }
}
