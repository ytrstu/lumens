/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.webservice.soap;

import com.lumens.model.DataFormat;
import com.lumens.model.Format;
import com.lumens.model.Format.Form;
import com.lumens.model.Type;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.wsdl.Definition;
import javax.wsdl.Input;
import javax.wsdl.Message;
import javax.wsdl.Operation;
import javax.wsdl.Part;
import javax.wsdl.PortType;
import javax.wsdl.extensions.schema.Schema;
import javax.wsdl.extensions.schema.SchemaImport;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLReader;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.xerces.impl.xs.XMLSchemaLoader;
import org.apache.xerces.xni.XMLResourceIdentifier;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLEntityResolver;
import org.apache.xerces.xni.parser.XMLInputSource;
import org.apache.xerces.xs.XSAttributeDeclaration;
import org.apache.xerces.xs.XSAttributeUse;
import org.apache.xerces.xs.XSComplexTypeDefinition;
import org.apache.xerces.xs.XSElementDeclaration;
import org.apache.xerces.xs.XSModel;
import org.apache.xerces.xs.XSModelGroup;
import org.apache.xerces.xs.XSObjectList;
import org.apache.xerces.xs.XSParticle;
import org.apache.xerces.xs.XSSimpleTypeDefinition;
import org.apache.xerces.xs.XSTerm;
import org.apache.xerces.xs.XSTypeDefinition;
import org.apache.xerces.xs.XSWildcard;
import org.apache.xml.serialize.DOMSerializer;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.Element;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSInput;
import org.xml.sax.InputSource;

/**
 *
 * @author shaofeng wang
 */
public class SOAPClient implements SOAPConstants, XMLEntityResolver
{
    private String wsdlURL;
    private Definition definition;
    private XSModel xsModel;
    private Map<String, Element> schemaCache;
    private static final Map<String, Type> buildinTypes = new HashMap<String, Type>();

    static
    {
        buildinTypes.put("string", Type.STRING);
        buildinTypes.put("byte", Type.BYTE);
        buildinTypes.put("boolean", Type.BOOLEAN);
        buildinTypes.put("int", Type.INT);
        buildinTypes.put("nonPositiveInteger", Type.LONG);
        buildinTypes.put("negativeInteger", Type.LONG);
        buildinTypes.put("nonNegativeInteger", Type.LONG);
        buildinTypes.put("nonPositiveInteger", Type.LONG);
        buildinTypes.put("integer", Type.LONG);
        buildinTypes.put("long", Type.LONG);
        buildinTypes.put("float", Type.FLOAT);
        buildinTypes.put("double", Type.DOUBLE);
        buildinTypes.put("decimal", Type.DOUBLE);
        buildinTypes.put("time", Type.DATE);
        buildinTypes.put("date", Type.DATE);
        buildinTypes.put("dateTime", Type.DATE);
        buildinTypes.put("gDay", Type.STRING);
        buildinTypes.put("gMonth", Type.STRING);
        buildinTypes.put("gMonthDay", Type.STRING);
        buildinTypes.put("gYear", Type.STRING);
        buildinTypes.put("gYearMonth", Type.STRING);
        buildinTypes.put("base64Binary", Type.BINARY);
        buildinTypes.put("hexBinary", Type.BINARY);
    }

    public SOAPClient(String wsdlURL, String user, String password)
    {
        this.wsdlURL = wsdlURL;
    }

    public void open()
    {
        try
        {
            WSDLReader wsdlReader11 = WSDLFactory.newInstance().newWSDLReader();
            definition = wsdlReader11.readWSDL(wsdlURL);
        }
        catch (Exception ex)
        {
            throw new RuntimeException(ex);
        }
    }

    public void close()
    {
        schemaCache = null;
    }

    public Format getFormats()
    {
        Format services = new DataFormat("Services");
        Map ports = definition.getPortTypes();
        for (Object portObj : ports.values())
        {
            PortType port = (PortType) portObj;
            List ops = port.getOperations();
            for (Object op : ops)
            {
                Operation oper = (Operation) op;
                // input in message
                Input i = oper.getInput();
                Message inMsg = i.getMessage();
                for (Object i1 : inMsg.getParts().values())
                {
                    Part part = (Part) i1;
                    QName eqName = part.getElementName();
                    Format service = services.addChild(eqName.getLocalPart(), Form.STRUCT);
                    service.setProperty(NAMESPACE, eqName.getNamespaceURI());
                }
            }
        }
        List schemas = definition.getTypes().getExtensibilityElements();
        Set<Map.Entry<String, String>> namespaceSet = definition.getNamespaces().entrySet();
        if (schemas.size() > 1)
        {
            throw new RuntimeException("Not support multiple schema in one WSDL");
        }
        for (Object o : schemas)
        {
            if (o instanceof Schema)
            {
                try
                {
                    Schema s = (Schema) o;
                    Element schema = s.getElement();
                    for (Map.Entry<String, String> entry : namespaceSet)
                    {
                        StringBuilder b = new StringBuilder();
                        if (entry.getKey().isEmpty())
                            b.append("xmlns");
                        else
                            b.append("xmlns:").append(entry.getKey());
                        String attrNS = b.toString();
                        if (!schema.hasAttribute(attrNS))
                            schema.setAttribute(attrNS, entry.getValue());
                    }
                    schemaCache = new HashMap<String, Element>();
                    Map imports = s.getImports();
                    for (Object importO : imports.values())
                    {
                        Collection schemaVec = (Collection) importO;
                        for (Object vecO : schemaVec)
                        {
                            SchemaImport sImport = (SchemaImport) vecO;
                            buildSchemaCache(sImport.getReferencedSchema());
                        }
                    }
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    toString(schema, out);
                    XMLSchemaLoader loader = new XMLSchemaLoader();
                    DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
                    domFactory.setNamespaceAware(true);
                    domFactory.setValidating(false);
                    DocumentBuilder builder = domFactory.newDocumentBuilder();
                    DOMImplementationLS domLS = (DOMImplementationLS) builder.getDOMImplementation();
                    LSInput input = domLS.createLSInput();
                    ByteArrayInputStream bais = new ByteArrayInputStream(out.toByteArray());
                    InputSource is = new InputSource(bais);
                    input.setEncoding(is.getEncoding());
                    input.setPublicId(is.getPublicId());
                    input.setSystemId(is.getSystemId());
                    input.setCharacterStream(is.getCharacterStream());
                    input.setByteStream(is.getByteStream());
                    loader.setEntityResolver(this);
                    xsModel = loader.load(input);
                    schemaCache = null;
                }
                catch (Exception ex)
                {
                    throw new RuntimeException(ex);
                }
            }
        }
        return services;
    }

    public Format getFormat(Format format)
    {
        String name = format.getName();
        String namespace = (String) format.getProperty(NAMESPACE);
        XSElementDeclaration xsElement = xsModel.getElementDeclaration(name, namespace);
        XSTypeDefinition xsTypeDef = xsElement.getTypeDefinition();
        if (xsTypeDef.getTypeCategory() == XSTypeDefinition.COMPLEX_TYPE)
        {
            XSComplexTypeDefinition xsComplex = (XSComplexTypeDefinition) xsTypeDef;
            buildFieldFromXSAttributeList(format, xsComplex);
            buildFormatFromXSComplexType(format, null, null, xsComplex, false);
        }
        else if (xsTypeDef.getTypeCategory() == XSTypeDefinition.SIMPLE_TYPE)
        {
            XSSimpleTypeDefinition xsSimple = (XSSimpleTypeDefinition) xsTypeDef;
            buildFormatFromXSSimpleType(format, name, namespace, xsSimple, false);
        }
        return null;
    }

    private static void buildFromatFromElement(Format parent, XSElementDeclaration xsElement,
                                               boolean isArray)
    {
        String name = xsElement.getName();
        String namespace = xsElement.getNamespace();

        XSTypeDefinition xsTypeDef = xsElement.getTypeDefinition();
        if (xsTypeDef.getTypeCategory() == XSTypeDefinition.COMPLEX_TYPE)
        {

            XSComplexTypeDefinition xsComplex = (XSComplexTypeDefinition) xsTypeDef;
            buildFormatFromXSComplexType(parent, name, namespace, xsComplex, isArray);
        }
        else if (xsTypeDef.getTypeCategory() == XSTypeDefinition.SIMPLE_TYPE)
        {

            XSSimpleTypeDefinition xsSimple = (XSSimpleTypeDefinition) xsTypeDef;
            buildFormatFromXSSimpleType(parent, name, namespace, xsSimple, isArray);
        }
    }

    private static void buildFormatFromXSComplexType(Format parent, String name, String namespace,
                                                     XSComplexTypeDefinition xsComplex,
                                                     boolean isArray)
    {
        short contentType = xsComplex.getContentType();
        if (contentType == XSComplexTypeDefinition.CONTENTTYPE_SIMPLE)
        {
            XSTypeDefinition baseType = xsComplex.getBaseType();
            int max = 20;
            while (baseType != null
                   && baseType.getType() == XSComplexTypeDefinition.CONTENTTYPE_SIMPLE && --max > 0)
            {
                baseType = baseType.getBaseType();
            }
            Format format = buildFormatFromXSSimpleType(parent, name, namespace,
                                                        (XSSimpleTypeDefinition) baseType,
                                                        isArray);
            buildFieldFromXSAttributeList(format, xsComplex);

            if (format.getChildren() != null && format.isField())
                format.setForm(Form.STRUCT);

        }
        else if (contentType == XSComplexTypeDefinition.SIMPLE_TYPE)
        {
            buildFormatFromXSSimpleType(parent, name, namespace, xsComplex.getSimpleType(), isArray);
        }
        else if (contentType == XSComplexTypeDefinition.CONTENTTYPE_ELEMENT)
        {
            if (name != null)
            {
                Format format = parent.getChild(name);
                if (format == null)
                {
                    Form form = isArray ? Form.ARRAY : Form.STRUCT;
                    format = parent.addChild(name, form);
                    format.setProperty(NAMESPACE, namespace);
                    buildFieldFromXSAttributeList(format, xsComplex);
                }
                parent = format;
            }
            XSParticle xsPartice = xsComplex.getParticle();
            buildFromFromXSParticle(parent, xsPartice);
        }
    }

    private static Format buildFormatFromXSSimpleType(Format parent, String name, String namespace,
                                                      XSSimpleTypeDefinition xsSimple,
                                                      boolean isArray)
    {
        if (name != null)
        {
            Format format = parent.getChild(name);
            if (format == null)
            {
                Form form = isArray ? Form.ARRAY : Form.FIELD;
                format = parent.addChild(name, form);
                format.setProperty(NAMESPACE, namespace);
                String simpleName = xsSimple.getName();
                if (XMLSCHEMAXSD.equalsIgnoreCase(xsSimple.getNamespace()))
                {
                    Type type = buildinTypes.get(simpleName);
                    if (type != null)
                    {
                        format.setType(type);
                        return format;
                    }
                }

                // TODO it should log an error not throw an exception
                throw new RuntimeException(
                        "Not supported type \"" + xsSimple.getNamespace() + ':' + simpleName + "\"");

            }
        }
        return null;
    }

    private static void buildFromFromXSParticle(Format parent, XSParticle xsParticle)
    {
        boolean isUnbounded = xsParticle.getMaxOccursUnbounded();
        int maxOccurs = xsParticle.getMaxOccurs();
        boolean isArray = isUnbounded || (maxOccurs > 1);
        XSTerm term = xsParticle.getTerm();
        if (term instanceof XSElementDeclaration)
        {
            XSElementDeclaration xsElement = (XSElementDeclaration) term;
            buildFromatFromElement(parent, xsElement, isArray);
        }
        else if (term instanceof XSModelGroup)
        {
            XSModelGroup xsGroup = (XSModelGroup) term;
            XSObjectList list = xsGroup.getParticles();

            for (Object xsObj : list)
            {
                XSParticle xsPart = (XSParticle) xsObj;
                buildFromFromXSParticle(parent, xsPart);
            }
        }
        else if (term instanceof XSWildcard)
        {
        }
        assert term != null;
    }

    private static void toString(Element element, OutputStream out) throws Exception
    {
        OutputFormat outputformat = new OutputFormat();
        outputformat.setOmitXMLDeclaration(true);
        outputformat.setIndenting(true);
        outputformat.setPreserveSpace(false);
        outputformat.setEncoding("UTF-8");
        XMLSerializer xmlSerializer = new XMLSerializer(out, outputformat);
        DOMSerializer domSerializer = xmlSerializer.asDOMSerializer();
        domSerializer.serialize(element);
    }

    @Override
    public XMLInputSource resolveEntity(XMLResourceIdentifier xmlri) throws XNIException, IOException
    {
        try
        {
            String ns = xmlri.getNamespace();
            Element schema = schemaCache.get(ns);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            toString(schema, out);
            ByteArrayInputStream bais = new ByteArrayInputStream(out.toByteArray());
            InputSource is = new InputSource(bais);
            is.setPublicId(ns);
            XMLInputSource xmlInputSource = new XMLInputSource(is.getPublicId(), is.getSystemId(),
                                                               xmlri.getBaseSystemId());
            if (is.getCharacterStream() != null)
                xmlInputSource.setCharacterStream(is.getCharacterStream());
            if (is.getByteStream() != null)
                xmlInputSource.setByteStream(is.getByteStream());
            xmlInputSource.setEncoding(is.getEncoding());
            return xmlInputSource;
        }
        catch (Exception ex)
        {
            throw new XNIException(ex);
        }
    }

    private void buildSchemaCache(Schema schema)
    {
        Element e = schema.getElement();
        schemaCache.put(e.getAttribute(TARGETNAMESPACE), e);
        Map imports = schema.getImports();
        if (imports != null && imports.values() != null)
        {
            for (Object o : imports.values())
            {
                Collection schemaVec = (Collection) o;
                for (Object vecO : schemaVec)
                {
                    SchemaImport sImport = (SchemaImport) vecO;
                    buildSchemaCache(sImport.getReferencedSchema());
                }
            }
        }
    }

    private static void buildFieldFromXSAttributeList(Format format,
                                                      XSComplexTypeDefinition xsComplex)
    {
        XSObjectList attrUses = xsComplex.getAttributeUses();
        for (Object o : attrUses)
        {
            XSAttributeUse attrUse = (XSAttributeUse) o;
            XSAttributeDeclaration attrDecl = attrUse.getAttrDeclaration();
            String attrName = attrDecl.getName();
            String typeName = attrUse.getAttrDeclaration().getTypeDefinition().getName();
            if (format.getChild(attrName) == null)
            {
                Type type = buildinTypes.get(typeName);
                if (type != null)
                    format.addChild(attrName, Form.FIELD, type);
            }
        }
    }
}
