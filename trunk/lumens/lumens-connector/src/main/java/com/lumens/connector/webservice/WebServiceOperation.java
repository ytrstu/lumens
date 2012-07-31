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
    public List<Element> execute(Element input, Format resultFormat) throws Exception
    {
        Element response = client.execute(input, resultFormat);
        List<Element> result = new ArrayList<Element>();
        result.add(response);
        return result;
    }
}
