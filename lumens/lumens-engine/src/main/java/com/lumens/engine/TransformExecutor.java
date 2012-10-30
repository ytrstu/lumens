/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lumens.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author shaofeng wang (shaofeng.cjpw@gmail.com)
 */
public class TransformExecutor implements Executor
{
    private TransformComponent tComponent;
    private ExecuteContext executeContext;

    public TransformExecutor(TransformComponent tComponent,
                             ExecuteContext executeContext)
    {
        this.tComponent = tComponent;
        this.executeContext = executeContext;
    }

    @Override
    public List<Executor> execute()
    {
        List<Executor> tExList = new ArrayList<Executor>();
        List<ExecuteContext> exList = tComponent.execute(executeContext);
        if (tComponent.hasTo())
        {
            Map<String, TransformComponent> toList = tComponent.getToList();
            for (TransformComponent to : toList.values())
            {
                for (ExecuteContext ctx : exList)
                {
                    if (to.accept(ctx))
                        tExList.add(new TransformExecutor(to, ctx));
                }
            }
        }
        if (!exList.isEmpty() && tExList.isEmpty())
        {
            // TODO need log system
            for (ExecuteContext ctx : exList)
                System.out.println(String.format(
                        "No target component to process '%s'", ctx.
                        getTargetFormatName()));
        }
        return tExList;
    }

    @Override
    public TransformComponent getTransformComponent()
    {
        return tComponent;
    }
}
