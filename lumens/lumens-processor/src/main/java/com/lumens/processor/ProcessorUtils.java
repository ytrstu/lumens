/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.processor;

/**
 *
 * @author shaofeng wang
 */
public class ProcessorUtils
{
    public static boolean isScript(String input)
    {
        if (input.charAt(0) == '@')
            return true;
        return false;
    }

    public static String getPath(String input)
    {
        if (isScript(input))
            return input.substring(1);
        return input;
    }
}
