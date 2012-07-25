/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lumens.connector;

import com.lumens.model.Format;

/**
 *
 * @author shaofeng wang
 */
public interface FormatBuilder
{
    public Format getFormatList(Param param);

    public Format getFormat(Format format, Param param);
}