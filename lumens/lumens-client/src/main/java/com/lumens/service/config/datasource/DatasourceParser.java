/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.service.config.datasource;

import java.io.IOException;
import java.util.Stack;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.*;

/**
 *
 * @author shaofeng wang (shaofeng.cjpw@gmail.com)
 */
public class DatasourceParser implements ContentHandler
{
    private DatasourceHandler handler;
    private Stack context;
    private StringBuffer buffer;
    private EntityResolver resolver;

    /**
     *
     * Creates a parser instance.
     *
     * @param handler handler interface implementation (never <code>null</code>
     * @param resolver SAX entity resolver implementation or <code>null</code>. It is recommended
     * that it could be able to resolve at least the DTD.
     */
    public DatasourceParser(final DatasourceHandler handler,
                            final EntityResolver resolver)
    {
        this.handler = handler;
        this.resolver = resolver;
        buffer = new StringBuffer(111);
        context = new java.util.Stack();
    }

    /**
     *
     * This SAX interface method is implemented by the parser.
     */
    @Override
    public final void setDocumentLocator(Locator locator)
    {
    }

    /**
     *
     * This SAX interface method is implemented by the parser.
     */
    @Override
    public final void startDocument() throws SAXException
    {
    }

    /**
     *
     * This SAX interface method is implemented by the parser.
     */
    @Override
    public final void endDocument() throws SAXException
    {
    }

    /**
     *
     * This SAX interface method is implemented by the parser.
     */
    @Override
    public final void startElement(java.lang.String ns, java.lang.String name,
                                   java.lang.String qname,
                                   org.xml.sax.Attributes attrs) throws org.xml.sax.SAXException
    {
        dispatch(true);
        context.push(new Object[]
                {
                    qname,
                    new org.xml.sax.helpers.AttributesImpl(attrs)
                });
        if ("parameter-list".equals(qname))
        {
            handler.start_parameter_list(attrs);
        } else if ("parameter".equals(qname))
        {
            handler.handle_parameter(attrs);
        } else if ("datasource-list".equals(qname))
        {
            handler.start_datasource_list(attrs);
        } else if ("datasource".equals(qname))
        {
            handler.start_datasource(attrs);
        }
    }

    /**
     *
     * This SAX interface method is implemented by the parser.
     */
    @Override
    public final void endElement(java.lang.String ns, java.lang.String name,
                                 java.lang.String qname) throws org.xml.sax.SAXException
    {
        dispatch(false);
        context.pop();
        if ("parameter-list".equals(qname))
        {
            handler.end_parameter_list();
        } else if ("datasource-list".equals(qname))
        {
            handler.end_datasource_list();
        } else if ("datasource".equals(qname))
        {
            handler.end_datasource();
        }
    }

    /**
     *
     * This SAX interface method is implemented by the parser.
     */
    @Override
    public final void characters(char[] chars, int start, int len) throws SAXException
    {
        buffer.append(chars, start, len);
    }

    /**
     *
     * This SAX interface method is implemented by the parser.
     */
    @Override
    public final void ignorableWhitespace(char[] chars, int start, int len) throws SAXException
    {
    }

    /**
     *
     * This SAX interface method is implemented by the parser.
     */
    @Override
    public final void processingInstruction(String target, String data) throws SAXException
    {
    }

    @Override
    public final void startPrefixMapping(java.lang.String prefix,
                                         java.lang.String uri) throws org.xml.sax.SAXException
    {
    }

    @Override
    public final void endPrefixMapping(java.lang.String prefix) throws org.xml.sax.SAXException
    {
    }

    @Override
    public final void skippedEntity(java.lang.String name) throws org.xml.sax.SAXException
    {
    }

    private void dispatch(final boolean fireOnlyIfMixed) throws SAXException
    {
        if (fireOnlyIfMixed && buffer.length() == 0) return; //skip it
        Object[] ctx = (Object[]) context.peek();
        String here = (String) ctx[0];
        org.xml.sax.Attributes attrs = (org.xml.sax.Attributes) ctx[1];
        if ("id".equals(here))
        {
            if (fireOnlyIfMixed)
                throw new IllegalStateException("Unexpected characters() event! (Missing DTD?)");
            handler.handle_id(buffer.length() == 0 ? null : buffer.toString(), attrs);
        } else if ("icon".equals(here))
        {
            if (fireOnlyIfMixed)
                throw new IllegalStateException("Unexpected characters() event! (Missing DTD?)");
            handler.handle_icon(buffer.length() == 0 ? null : buffer.toString(), attrs);
        } else if ("name".equals(here))
        {
            if (fireOnlyIfMixed)
                throw new IllegalStateException("Unexpected characters() event! (Missing DTD?)");
            handler.handle_name(buffer.length() == 0 ? null : buffer.toString(), attrs);
        } else if ("instance-icon".equals(here))
        {
            if (fireOnlyIfMixed)
                throw new IllegalStateException("Unexpected characters() event! (Missing DTD?)");
            handler.handle_instance_icon(buffer.length() == 0 ? null : buffer.toString(), attrs);
        } else if ("class-name".equals(here))
        {
            if (fireOnlyIfMixed)
                throw new IllegalStateException("Unexpected characters() event! (Missing DTD?)");
            handler.handle_class_name(buffer.length() == 0 ? null : buffer.toString(), attrs);
        } else
        {
            //do not care
        }
        buffer.delete(0, buffer.length());
    }

    /**
     *
     * The recognizer entry method taking an InputSource.
     *
     * @param input InputSource to be parsed.
     * @throws java.io.IOException on I/O error
     * @throws org.xml.sax.SAXException propagated exception thrown by a DocumentHandler
     * @throws javax.xml.parsers.ParserConfigurationException a parser satisfying the requested
     * configuration cannot be created
     * @throws javax.xml.parsers.FactoryConfigurationRrror if the implementation cannot be
     * instantiated
     */
    public void parse(final org.xml.sax.InputSource input) throws SAXException, ParserConfigurationException, IOException
    {
        parse(input, this);
    }

    /**
     *
     * The recognizer entry method taking a URL.
     *
     * @param url URL Source to be parsed.
     * @throws java.io.IOException on I/O error
     * @throws org.xml.sax.SAXException propagated exception thrown by a DocumentHandler
     * @throws javax.xml.parsers.ParserConfigurationException a parser satisfying the requested
     * configuration cannot be created
     * @throws javax.xml.parsers.FactoryConfigurationRrror if the implementation cannot be
     * instantiated
     */
    public void parse(final java.net.URL url) throws SAXException, ParserConfigurationException, IOException
    {
        parse(new org.xml.sax.InputSource(url.toExternalForm()), this);
    }

    /**
     *
     * The recognizer entry method taking an Inputsource.
     *
     * @param input InputSource to be parsed.
     * @throws java.io.IOException on I/O error
     * @throws org.xml.sax.SAXException propagated exception thrown by a DocumentHandler
     * @throws javax.xml.parsers.ParserConfigurationException a parser satisfying the requested
     * configuration cannot be created
     * @throws javax.xml.parsers.FactoryConfigurationRrror if the implementation cannot be
     * instantiated
     */
    public static void parse(final org.xml.sax.InputSource input,
                             final DatasourceHandler handler) throws SAXException, ParserConfigurationException, IOException
    {
        parse(input, new DatasourceParser(handler, null));
    }

    /**
     *
     * The recognizer entry method taking a URL.
     *
     * @param url URL source to be parsed.
     * @throws java.io.IOException on I/O error
     * @throws org.xml.sax.SAXException propagated exception thrown by a DocumentHandler
     * @throws javax.xml.parsers.ParserConfigurationException a parser satisfying the requested
     * configuration cannot be created
     * @throws javax.xml.parsers.FactoryConfigurationRrror if the implementation cannot be
     * instantiated
     */
    public static void parse(final java.net.URL url,
                             final DatasourceHandler handler) throws SAXException, ParserConfigurationException, IOException
    {
        parse(new org.xml.sax.InputSource(url.toExternalForm()), handler);
    }

    private static void parse(final org.xml.sax.InputSource input,
                              final DatasourceParser recognizer) throws SAXException, ParserConfigurationException, IOException
    {
        javax.xml.parsers.SAXParserFactory factory = javax.xml.parsers.SAXParserFactory.
                newInstance();
        factory.setNamespaceAware(false); //the code was generated according DTD
        XMLReader parser = factory.newSAXParser().getXMLReader();
        parser.setContentHandler(recognizer);
        parser.setErrorHandler(recognizer.getDefaultErrorHandler());
        if (recognizer.resolver != null)
            parser.setEntityResolver(recognizer.resolver);
        parser.parse(input);
    }

    /**
     *
     * Creates default error handler used by this parser.
     *
     * @return org.xml.sax.ErrorHandler implementation
     */
    protected ErrorHandler getDefaultErrorHandler()
    {
        return new ErrorHandler()
        {
            @Override
            public void error(SAXParseException ex) throws SAXException
            {
                if (context.isEmpty()) System.err.println("Missing DOCTYPE.");
                throw ex;
            }

            @Override
            public void fatalError(SAXParseException ex) throws SAXException
            {
                throw ex;
            }

            @Override
            public void warning(SAXParseException ex) throws SAXException
            {
                // ignore
            }
        };
    }
}
