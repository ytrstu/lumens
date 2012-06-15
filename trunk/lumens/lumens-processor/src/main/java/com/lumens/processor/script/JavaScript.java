/*
 * (C) Copyright Hewlett-Packard Development Company, L.P. All Rights Reserved.
 */
package com.lumens.processor.script;

import com.lumens.processor.Context;
import com.lumens.processor.ProcessorUtils;
import com.lumens.processor.Script;
import com.lumens.processor.transform.TransformContext;
import com.lumens.processor.transform.TransformInput;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;

public class JavaScript implements Script
{
    private JavaScriptBuilder builder = new JavaScriptBuilder();
    private final String orignalScript;
    private Scriptable scope;
    private org.mozilla.javascript.Context jsCTX;
    private Function jsFunction;

    public JavaScript(String script) throws Exception
    {
        // TODO refine here, put the context initialization to gloabl place, in
        // order to load build in function only once
        this.orignalScript = script;
        jsCTX = org.mozilla.javascript.Context.enter();
        scope = jsCTX.initStandardObjects();
        jsCTX.evaluateString(scope,
                             ProcessorUtils.loadJS("com/lumens/processor/script/build-in.js"),
                             "build-in", 1, null);
        jsFunction = jsCTX.compileFunction(scope, builder.build(orignalScript), "node-script", 1, null);
    }

    @Override
    protected void finalize() throws Throwable
    {
        try
        {
            super.finalize();
        }
        finally
        {
            org.mozilla.javascript.Context.exit();
        }
    }

    @Override
    public Object execute(Context ctx)
    {
        if (ctx instanceof TransformContext)
        {
            Object[] args =
            {
                ctx
            };
            return jsFunction.call(jsCTX, scope, scope, args);
        }
        return null;
    }
}
