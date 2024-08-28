/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2014
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/

package com.ericsson.component.aia.model.base.meta;

import static com.ericsson.component.aia.model.base.util.SchemaUtil.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;

import com.ericsson.component.aia.model.base.config.bean.SchemaEnum;
import com.ericsson.component.aia.model.base.exception.SchemaException;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Before;
import org.junit.Test;

public class SchemaTypeHandlerTest {
    private Element schemaTypeElement;


    @Before
    public void setup() {
        schemaTypeElement = mock(Element.class);
        mockChildElements();

    }

    /**
     * 
     */
    private void mockChildElements() {
        final Element schemaNameElement = mock(Element.class);
        when(schemaNameElement.getText()).thenReturn(SchemaEnum.EBM.value());
        final Element preambleElement = mock(Element.class);
        when(preambleElement.getText()).thenReturn("test");
        final Element idLengthElement = mock(Element.class);
        when(idLengthElement.getText()).thenReturn("2");
        final Element idStartPostionElement = mock(Element.class);
        when(idStartPostionElement.getText()).thenReturn("1");
        final Element idInEventElement = mock(Element.class);
        when(idInEventElement.getText()).thenReturn("true");
        
        
        when(schemaTypeElement.getChild("name", Namespace.getNamespace(SCHEMA_TYPE_NAMESPACE))).thenReturn(schemaNameElement);
        when(schemaTypeElement.getChild("idLength", Namespace.getNamespace(SCHEMA_TYPE_NAMESPACE))).thenReturn(idLengthElement);
        when(schemaTypeElement.getChild("idStartPos", Namespace.getNamespace(SCHEMA_TYPE_NAMESPACE))).thenReturn(idStartPostionElement);
        when(schemaTypeElement.getChild("idInEvent", Namespace.getNamespace(SCHEMA_TYPE_NAMESPACE))).thenReturn(idInEventElement);
        when(schemaTypeElement.getChild("paramPreamble", Namespace.getNamespace(SCHEMA_TYPE_NAMESPACE))).thenReturn(preambleElement);
        when(schemaTypeElement.getChild("valuePreamble", Namespace.getNamespace(SCHEMA_TYPE_NAMESPACE))).thenReturn(preambleElement);
    }

    /**
     * @param value
     */
    private Element mockXmlFilePath(final String value) {
        final Element element = mock(Element.class);
        when(element.getText()).thenReturn(value);
        return element;
        
    }

    @Test
    public void testSchemaTypeHandlerInit() throws SchemaException {
        final SchemaTypeHandler schemaTypeHandler = new SchemaTypeHandler(schemaTypeElement);
        assertTrue(schemaTypeHandler.isIdInEvent());
        assertEquals(2, schemaTypeHandler.getIdLength());
        assertEquals(1, schemaTypeHandler.getIdStartPos());
        assertEquals("test", schemaTypeHandler.getParamPreamble());
        assertEquals(SchemaEnum.EBM, schemaTypeHandler.getName());
        assertEquals("test", schemaTypeHandler.getValuePreamble());
        assertTrue(schemaTypeHandler.getXmlFilesSet().isEmpty());

    }
    
    @Test
    public void testSchemaTypeHandlerInitWithXmlFilesDefined() throws SchemaException {
        final Element firstPath = mockXmlFilePath("a/b/c.xml");
        final Element secondPath = mockXmlFilePath("d/e/f.xml");
        final Element thirdPath = mockXmlFilePath("g/h/i.xml");
        
        final List<Element> elements = new ArrayList<Element>();
        elements.add(firstPath);
        elements.add(secondPath);
        elements.add(thirdPath);
        
        final Element xmlFilesElement = mock(Element.class);
        when(xmlFilesElement.getChildren("path", Namespace.getNamespace(SCHEMA_TYPE_NAMESPACE))).thenReturn(elements);
        when(schemaTypeElement.getChild("xmlFiles", Namespace.getNamespace(SCHEMA_TYPE_NAMESPACE))).thenReturn(xmlFilesElement);
        
        final SchemaTypeHandler schemaTypeHandler = new SchemaTypeHandler(schemaTypeElement);
        assertTrue(schemaTypeHandler.isIdInEvent());
        assertEquals(2, schemaTypeHandler.getIdLength());
        assertEquals(1, schemaTypeHandler.getIdStartPos());
        assertEquals("test", schemaTypeHandler.getParamPreamble());
        assertEquals(SchemaEnum.EBM, schemaTypeHandler.getName());
        assertEquals("test", schemaTypeHandler.getValuePreamble());
        
        final Set<String> expectedFilesPathSet = new HashSet<String>();
        expectedFilesPathSet.add(firstPath.getText());
        expectedFilesPathSet.add(secondPath.getText());
        expectedFilesPathSet.add(thirdPath.getText());
        
        assertArrayEquals(expectedFilesPathSet.toArray(), schemaTypeHandler.getXmlFilesSet().toArray());
    }
    
    
    @Test
    public void testSchemaTypeHandlerInitWithXmlFilesDefinedAndNoPaths() throws SchemaException {
        
        final Element xmlFilesElement = mock(Element.class);
        when(schemaTypeElement.getChild("xmlFiles", Namespace.getNamespace(SCHEMA_TYPE_NAMESPACE))).thenReturn(xmlFilesElement);
        
        final SchemaTypeHandler schemaTypeHandler = new SchemaTypeHandler(schemaTypeElement);
        assertTrue(schemaTypeHandler.isIdInEvent());
        assertEquals(2, schemaTypeHandler.getIdLength());
        assertEquals(1, schemaTypeHandler.getIdStartPos());
        assertEquals("test", schemaTypeHandler.getParamPreamble());
        assertEquals(SchemaEnum.EBM, schemaTypeHandler.getName());
        assertEquals("test", schemaTypeHandler.getValuePreamble());
        
        assertTrue(schemaTypeHandler.getXmlFilesSet().isEmpty());
    }
}