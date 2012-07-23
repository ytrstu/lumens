/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lumens.connector.webservice;

import com.lumens.connector.Operation;
import com.lumens.connector.webservice.soap.SOAPClient;
import com.lumens.model.Element;
import com.lumens.model.Format;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author shaofeng wang
 */
public class WebServiceOperation implements Operation
{
    private SOAPClient client;

    WebServiceOperation(SOAPClient client)
    {
        this.client = client;
    }

    @Override
    public List<Element> execute(Object... args) throws Exception
    {
        if (args == null || args.length != 2)
            throw new IllegalArgumentException("Argument can't be null or its length must be 2");
        if (!(args[0] instanceof Element) || !(args[1] instanceof Format))
            throw new IllegalArgumentException("Argument type are not support, it must be 'Element' and 'Format'");
        Element response = client.execute((Element) args[0], (Format) args[1]);
        List<Element> result = new ArrayList<Element>();
        result.add(response);
        return result;
    }
}
