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
public class DataTransformationRecord extends ListGridRecord
{
    public DataTransformationRecord(String id, String name, String status, String description,
                                    int CPU, int memory, int disk)
    {
        this.setAttribute("id", id);
        this.setAttribute("name", name);
        this.setAttribute("status", status);
        this.setAttribute("description", description);
        this.setAttribute("CPU", CPU);
        this.setAttribute("memory", memory);
        this.setAttribute("disk", disk);
    }
}
