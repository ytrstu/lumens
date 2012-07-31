/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lumens.engine;

/**
 *
 * @author shaofeng wang
 */
public class ExecuteContextImpl implements ExecuteContext
{
    private Component component;
    private Object input;
    private String resultFormatName;

    public ExecuteContextImpl(Component component, Object input, String resultFormatName)
    {
        this.component = component;
        this.input = input;
        this.resultFormatName = resultFormatName;
    }

    @Override
    public Object getInput()
    {
        return input;
    }

    @Override
    public Component getNextComponent()
    {
        return component;
    }

    @Override
    public String getResultFormatName()
    {
        return resultFormatName;
    }
}
