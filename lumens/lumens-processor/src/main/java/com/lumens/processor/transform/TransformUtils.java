/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.processor.transform;

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

}
