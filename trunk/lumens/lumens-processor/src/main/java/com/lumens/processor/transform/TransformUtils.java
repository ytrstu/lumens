/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.processor.transform;

import com.lumens.model.Element;

/**
 *
 * @author shaofeng wang
 */
public class TransformUtils
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

    public static boolean checkTransformParameters(Object... args)
    {
        if (args == null || args.length < 1)
            return false;
        if (!(args[0] instanceof TransformRule)
            || (args.length == 2 && (args[1] != null && !(args[1] instanceof Element))))
            return false;

        return true;
    }
}
