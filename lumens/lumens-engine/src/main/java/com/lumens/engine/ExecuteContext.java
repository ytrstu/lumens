/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lumens.engine;

/**
 *
 * @author shaofeng wang
 */
public interface ExecuteContext
{
    public String getResultFormatName();

    public Object getInput();

    public Component getNextComponent();
}
