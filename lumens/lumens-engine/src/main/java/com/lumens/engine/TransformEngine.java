/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.engine;

import com.lumens.engine.run.Executor;
import com.lumens.engine.run.SingleThreadExecuteStack;
import com.lumens.engine.run.TransformExecuteJob;
import com.lumens.engine.run.TransformExecutor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author shaofeng wang (shaofeng.cjpw@gmail.com)
 */
public class TransformEngine
{
    private Map<String, TransformExecuteJob> jobList = new HashMap<String, TransformExecuteJob>();

    public void initialize()
    {
    }

    public void execute(TransformProject project)
    {
        // Execute all start rules to drive the ws connector
        project.getStartEntryList();

    }

    public void executeImpl(TransformComponent transformEntry, String entryName)
    {
        SingleThreadExecuteStack executorStack = new SingleThreadExecuteStack();
        executorStack.push(
                new TransformExecutor(transformEntry, new TransformExecuteContext(entryName)));
        while (!executorStack.isEmpty())
        {
            Executor executor = executorStack.pop();
            List<Executor> tExList = executor.execute();
            executorStack.push(tExList);
        }
    }
}
