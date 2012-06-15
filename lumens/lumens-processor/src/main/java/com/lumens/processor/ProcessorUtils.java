/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.processor;

import com.lumens.model.Element;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author shaofeng wang
 */
public class ProcessorUtils
{
    public static boolean isPathFormat(String input)
    {
        if (input != null && !input.isEmpty() && input.charAt(0) == '@')
            return true;
        return false;
    }

    public static String getAccessPath(String input)
    {
        if (isPathFormat(input))
            return input.substring(1);
        return null;
    }

    private static InputStream getInputStream(String name) throws Exception
    {
        return ProcessorUtils.class.getClassLoader().getResourceAsStream(name);
    }

    public static String loadJS(String name) throws Exception
    {
        InputStream in = getInputStream(name);
        try
        {
            return IOUtils.toString(in);
        }
        finally
        {
            IOUtils.closeQuietly(in);
        }
    }

    public Element getElement(Context ctx, String path)
    {
        return null;
    }
}
