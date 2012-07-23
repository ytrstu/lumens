/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lumens.connector;

import com.lumens.model.Element;
import java.util.List;

/**
 *
 * @author washaofe
 */
public interface Operation
{
    public List<Element> execute(Object... args) throws Exception;
}
