/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lumens.engine;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author shaofeng wang
 */
public class SingleThreadExecuteStack extends LinkedList<ExecuteContext>
{
    public void push(List<ExecuteContext> contextList)
    {
        super.addAll(0, contextList);
    }
}
