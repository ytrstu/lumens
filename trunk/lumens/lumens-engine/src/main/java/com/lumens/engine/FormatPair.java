/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lumens.engine;

import com.lumens.model.Format;
import com.lumens.processor.Pair;

/**
 *
 * @author shaofeng wang
 */
public class FormatPair extends Pair<Format, Format>
{
    public FormatPair(Format first, Format second)
    {
        super(first, second);
    }

    public FormatPair()
    {
        super();
    }
}
