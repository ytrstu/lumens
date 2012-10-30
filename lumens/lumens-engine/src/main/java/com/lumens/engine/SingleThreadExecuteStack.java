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
public class SingleThreadExecuteStack extends LinkedList<Executor>
{
    public void push(List<Executor> executorList)
    {
        super.addAll(0, executorList);
    }
}
