/*
 * To change this template, choose Tools | Templates
 * and initialize the template in the editor.
 */
package com.lumens.engine;

import java.util.List;

/**
 *
 * @author washaofe
 */
public class Router implements Component
{

    @Override
    public void to(Component to)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<ExecuteContext> execute(ExecuteContext context)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void initialize()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void cleanup()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getName()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setName(String name)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean accept(String name)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
