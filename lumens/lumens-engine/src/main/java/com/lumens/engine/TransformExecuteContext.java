/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lumens.engine;

/**
 *
 * @author shaofeng wang
 */
public class TransformExecuteContext implements ExecuteContext
{
    private Object input;
    private String targetFomat;

    public TransformExecuteContext(Object input, String targetFomat)
    {
        this.input = input;
        this.targetFomat = targetFomat;
    }

    @Override
    public Object getInput()
    {
        return input;
    }

    @Override
    public String getTargetFormatName()
    {
        return targetFomat;
    }
}
