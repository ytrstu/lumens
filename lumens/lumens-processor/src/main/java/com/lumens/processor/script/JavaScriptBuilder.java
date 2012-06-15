/*
 * (C) Copyright Hewlett-Packard Development Company, L.P. All Rights Reserved.
 */
package com.lumens.processor.script;

import java.io.LineNumberReader;
import java.io.StringReader;

public class JavaScriptBuilder
{
    private final String functionPrefix = "function fLumensScript_";

    public String build(String script)
    {
        // TODO prepare the script and convert it to python function
        StringBuilder function = new StringBuilder();
        Long l = System.currentTimeMillis();
        function.append(functionPrefix).append(l).append("(ctx) {\n");
        function.append(replaceNodePathScriptToFunction(script));
        function.append('}');
        return function.toString();
    }

    private String replaceNodePathScriptToFunction(String script)
    {
        try
        {
            String token = null;
            LineNumberReader reader = new LineNumberReader(new StringReader(script));
            StringBuilder scriptWithoutComments = new StringBuilder();
            int lineCount = 0;
            while ((token = reader.readLine()) != null)
            {
                if (token.startsWith("//"))
                    continue;
                token = addNodeAccessor(token);
                scriptWithoutComments.append(token).append('\n');
                ++lineCount;
            }
            if (1 == lineCount)
                scriptWithoutComments.insert(0, "return ");
            return scriptWithoutComments.toString();
        }
        catch (Exception e)
        {
            throw new RuntimeException("Wroing parsing script");
        }
    }

    private String addNodeAccessor(String token)
    {
        StringBuilder builder = new StringBuilder(token);
        int i = 0;
        int start = 0, end = 0;
        while (0 <= i && i < builder.length())
        {
            // TODO need to change to parse path @a.b.c format
            start = builder.indexOf("[", i);
            end = builder.indexOf("]", start);
            i = end;
            if (0 <= start && start < end && end < builder.length())
            {
                String path = builder.substring(start + 1, end);
                builder.replace(start, end + 1, "getElementValue(ctx, \"" + path + "\")");
            }
        }
        return builder.toString();
    }
}
