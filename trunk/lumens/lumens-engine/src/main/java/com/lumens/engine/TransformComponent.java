package com.lumens.engine;

import java.util.List;
import java.util.Map;

/**
 *
 * @author shaofeng wang
 */
public interface TransformComponent
{
    public void to(TransformComponent to);

    public boolean hasTo();

    public boolean isSingleTo();

    public Map<String, TransformComponent> getToList();

    public void initialize();

    public void cleanup();

    public List<ExecuteContext> execute(ExecuteContext context);

    public boolean accept(ExecuteContext ctx);

    public String getName();

    public void setName(String name);
}
