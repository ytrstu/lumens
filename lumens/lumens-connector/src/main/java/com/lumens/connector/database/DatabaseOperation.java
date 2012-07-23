/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lumens.connector.database;

import com.lumens.connector.Operation;
import com.lumens.model.Element;
import java.util.List;

/**
 *
 * @author shaofeng wang
 */
public class DatabaseOperation implements Operation
{
    DatabaseOperation(Client dbClient)
    {
    }

    @Override
    public List<Element> execute(Object... args) throws Exception
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
