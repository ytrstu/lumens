/*
 * To change this template, choose Tools | Templates
 * and initialize the template in the editor.
 */
package com.lumens.engine;

import java.util.List;

/**
 *
 * @author shaofeng wang
 */
public interface Component
{
    public void to(Component to);

    public void initialize();

    public void cleanup();

    public List<ExecuteContext> execute(ExecuteContext context);

    public boolean accept(String name);

    public String getName();

    public void setName(String name);
}
