/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2016
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.component.aia.model.base.schema.handler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.HashSet;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeSet;

import com.ericsson.component.aia.model.base.exception.ResourceNotFoundException;
import com.ericsson.component.aia.model.base.util.FfvFivKey;
import com.ericsson.component.aia.model.base.config.bean.SchemaEnum;
import com.ericsson.component.aia.model.base.exception.SchemaException;
import com.ericsson.component.aia.model.base.meta.schema.GeneralHandler;
import org.jaxen.JaxenException;
import org.jdom.JDOMException;
import org.junit.Test;

import com.ericsson.component.aia.model.base.meta.SchemaTypeHandler;
import com.ericsson.component.aia.model.base.meta.schema.General;
import com.ericsson.component.aia.model.base.meta.schema.Schema;

public class FileBasedLazyLoadingSchemaHandlerTest {

    private final SchemaTypeHandler mockedSchemaTypeHandler = mock(SchemaTypeHandler.class);

    @Test
    public void buildKeysMap_assumingCorrectCelltraceNaming_expectKeysMapOK() throws ResourceNotFoundException, SchemaException {
        final FileBasedLazyLoadingSchemaHandlerForTest objectUnderTest = new FileBasedLazyLoadingSchemaHandlerForTest(SchemaEnum.CELLTRACE);

        final Set<String> xmlNames = new HashSet<String>();
        xmlNames.add("xml/celltrace/M_V1.xml");
        xmlNames.add("xml/celltrace/S_AA11.xml");
        xmlNames.add("xml/celltrace/T_R5A_21.xml");
        xmlNames.add("xml/celltrace/T_R10A_24.xml");
        xmlNames.add("xml/celltrace/T_R2A_25.xml");
        xmlNames.add("xml/celltrace/T_R6A_25.xml");
        when(mockedSchemaTypeHandler.getXmlFilesSet()).thenReturn(xmlNames);

        objectUnderTest.buildKeysMap();

        final NavigableSet<FfvFivKey> keysMap = new TreeSet<FfvFivKey>();
        keysMap.add(new FfvFivKey("", "M", "V1"));
        keysMap.add(new FfvFivKey("", "S", "AA11"));
        keysMap.add(new FfvFivKey("21", "T", "R5A"));
        keysMap.add(new FfvFivKey("24", "T", "R10A"));
        keysMap.add(new FfvFivKey("25", "T", "R2A"));
        keysMap.add(new FfvFivKey("25", "T", "R6A"));

        assertEquals(keysMap, objectUnderTest.getSchemaKeyMap());
    }

    @Test
    public void buildKeysMap_assumingCorrectEBMNaming_expectKeysMapOK() throws ResourceNotFoundException, SchemaException {
        final FileBasedLazyLoadingSchemaHandlerForTest objectUnderTest = new FileBasedLazyLoadingSchemaHandlerForTest(SchemaEnum.EBM);

        final Set<String> xmlNames = new HashSet<String>();
        xmlNames.add("xml/ebm/4_11.xml");
        xmlNames.add("xml/ebm/4_30.xml");
        when(mockedSchemaTypeHandler.getXmlFilesSet()).thenReturn(xmlNames);

        objectUnderTest.buildKeysMap();

        final NavigableSet<FfvFivKey> keysMap = new TreeSet<FfvFivKey>();
        keysMap.add(new FfvFivKey("", "4", "11"));
        keysMap.add(new FfvFivKey("", "4", "30"));

        assertEquals(keysMap, objectUnderTest.getSchemaKeyMap());
    }

    @Test
    public void buildKeysMap_assumingBadNaming_expectFileIgnored() throws ResourceNotFoundException, SchemaException {
        final FileBasedLazyLoadingSchemaHandlerForTest objectUnderTest = new FileBasedLazyLoadingSchemaHandlerForTest(SchemaEnum.CELLTRACE);

        final Set<String> xmlNames = new HashSet<String>();
        xmlNames.add("xml/celltrace/M_V1_A.xml");
        when(mockedSchemaTypeHandler.getXmlFilesSet()).thenReturn(xmlNames);

        objectUnderTest.buildKeysMap();

        @SuppressWarnings("unchecked")
        final NavigableSet<FfvFivKey> keys = objectUnderTest.getSchemaKeyMap();
        assertEquals(0, keys.size());
    }

    @Test
    public void getSchema_providingValidKey_expectSchemaReturned() throws JaxenException, SchemaException, JDOMException, IOException {
        final FfvFivKey key = new FfvFivKey("24", "T", "R10A");

        final FileBasedLazyLoadingSchemaHandlerForTest handlerReal = new FileBasedLazyLoadingSchemaHandlerForTest(SchemaEnum.CELLTRACE);
        final FileBasedLazyLoadingSchemaHandlerForTest objectUnderTest = spy(handlerReal);

        final Schema mockedSchema = mock(Schema.class);
        final GeneralHandler genHandler = mock(GeneralHandler.class);
        final General general = mock(General.class);
        when(general.getFfvFivKey()).thenReturn(key);
        when(genHandler.getGeneralInfo()).thenReturn(general);
        when(mockedSchema.getGeneralHandler()).thenReturn(genHandler);
        doReturn(mockedSchema).when(objectUnderTest).createSchema(anyString());

        final Set<String> xmlNames = new HashSet<String>();
        xmlNames.add("xml/celltrace/T_R10A_24.xml");
        when(mockedSchemaTypeHandler.getXmlFilesSet()).thenReturn(xmlNames);

        final Schema result = objectUnderTest.getSchema(key);

        assertNotNull(result);

        verify(objectUnderTest, times(1)).createSchema(anyString());

        @SuppressWarnings("unchecked")
        final NavigableMap<FfvFivKey, Schema> schemaMap = objectUnderTest.getSchemaMap();
        assertEquals(result, schemaMap.get(key));
        assertEquals(1, schemaMap.size());

        final Schema result2 = objectUnderTest.getSchema(key);

        assertSame(result, result2);
        assertEquals(1, schemaMap.size());
    }

    @Test
    public void getSchema_providingValidValues_expectSchemaReturned() throws JaxenException, SchemaException, JDOMException, IOException {
        final FfvFivKey key = new FfvFivKey("24", "T", "R10A");

        final FileBasedLazyLoadingSchemaHandlerForTest handlerReal = new FileBasedLazyLoadingSchemaHandlerForTest(SchemaEnum.CELLTRACE);
        final FileBasedLazyLoadingSchemaHandlerForTest objectUnderTest = spy(handlerReal);

        final Schema mockedSchema = mock(Schema.class);
        final GeneralHandler genHandler = mock(GeneralHandler.class);
        final General general = mock(General.class);
        when(general.getFfvFivKey()).thenReturn(key);
        when(genHandler.getGeneralInfo()).thenReturn(general);
        when(mockedSchema.getGeneralHandler()).thenReturn(genHandler);
        doReturn(mockedSchema).when(objectUnderTest).createSchema(anyString());

        final Set<String> xmlNames = new HashSet<String>();
        xmlNames.add("xml/celltrace/T_R10A_24.xml");
        when(mockedSchemaTypeHandler.getXmlFilesSet()).thenReturn(xmlNames);

        final Schema result = objectUnderTest.getSchema("24", "T", "R10A");

        assertNotNull(result);

        verify(objectUnderTest, times(1)).createSchema(anyString());

        @SuppressWarnings("unchecked")
        final NavigableMap<FfvFivKey, Schema> schemaMap = objectUnderTest.getSchemaMap();
        assertEquals(result, schemaMap.get(key));
        assertEquals(1, schemaMap.size());

        final Schema result2 = objectUnderTest.getSchema(key);

        assertSame(result, result2);
        assertEquals(1, schemaMap.size());
    }

    @Test
    public void getSchema_providingInvalidValues_expectNullReturned() {
        final FileBasedLazyLoadingSchemaHandlerForTest objectUnderTest = new FileBasedLazyLoadingSchemaHandlerForTest(SchemaEnum.CELLTRACE);

        final Set<String> xmlNames = new HashSet<String>();
        xmlNames.add("xml/celltrace/T_R10A_24.xml");
        when(mockedSchemaTypeHandler.getXmlFilesSet()).thenReturn(xmlNames);

        final Schema result = objectUnderTest.getSchema("24", "T", "X10A");

        assertNull(result);
    }

    @Test
    public void getTreatAsSchema_providingNotExisitingSchema_expectClosestSchemaFound() throws JaxenException, SchemaException, JDOMException,
            IOException {
        final FfvFivKey key = new FfvFivKey("24", "T", "R10A");

        final FileBasedLazyLoadingSchemaHandlerForTest handlerReal = new FileBasedLazyLoadingSchemaHandlerForTest(SchemaEnum.CELLTRACE);
        final FileBasedLazyLoadingSchemaHandlerForTest objectUnderTest = spy(handlerReal);

        final Schema mockedSchema = mock(Schema.class);
        final GeneralHandler genHandler = mock(GeneralHandler.class);
        final General general = mock(General.class);
        when(general.getFfvFivKey()).thenReturn(key);
        when(genHandler.getGeneralInfo()).thenReturn(general);
        when(mockedSchema.getGeneralHandler()).thenReturn(genHandler);
        doReturn(mockedSchema).when(objectUnderTest).createSchema(anyString());

        final Set<String> xmlNames = new HashSet<String>();
        xmlNames.add("xml/celltrace/T_R10A_24.xml");
        when(mockedSchemaTypeHandler.getXmlFilesSet()).thenReturn(xmlNames);

        final NavigableSet<FfvFivKey> keysMap = new TreeSet<FfvFivKey>();
        keysMap.add(key);

        objectUnderTest.setKeysMap(keysMap);

        final Schema result = objectUnderTest.getTreatAsSchema("25", "T", "R10A");

        assertNotNull(result);

        verify(objectUnderTest, times(1)).createSchema(anyString());

        @SuppressWarnings("unchecked")
        final NavigableMap<FfvFivKey, Schema> schemaMap = objectUnderTest.getSchemaMap();
        assertEquals(result, schemaMap.get(key));
        assertEquals(1, schemaMap.size());

        final Schema result2 = objectUnderTest.getSchema(key);

        assertSame(result, result2);
        assertEquals(1, schemaMap.size());
    }

    @Test
    public void getTreatAsSchema_providingBadFFV_expectNull() {
        final FileBasedLazyLoadingSchemaHandlerForTest objectUnderTest = new FileBasedLazyLoadingSchemaHandlerForTest(SchemaEnum.CELLTRACE);

        final SchemaTypeHandler schemaTypeHandler = mock(SchemaTypeHandler.class);
        final Set<String> xmlNames = new HashSet<String>();
        xmlNames.add("xml/celltrace/T_R10A_24.xml");
        when(schemaTypeHandler.getXmlFilesSet()).thenReturn(xmlNames);

        when(schemaTypeHandler.getName()).thenReturn(SchemaEnum.CELLTRACE);
        when(schemaTypeHandler.getParamPreamble()).thenReturn("preamble");
        when(schemaTypeHandler.getValuePreamble()).thenReturn("preamble");

        final FfvFivKey key = new FfvFivKey("24", "T", "R10A");
        final NavigableSet<FfvFivKey> keysMap = new TreeSet<FfvFivKey>();
        keysMap.add(key);

        objectUnderTest.setKeysMap(keysMap);

        final Schema result = objectUnderTest.getTreatAsSchema("25", "U", "R10A");

        assertNull(result);

        @SuppressWarnings("unchecked")
        final NavigableMap<FfvFivKey, Schema> schemaMap = objectUnderTest.getSchemaMap();
        assertEquals(0, schemaMap.size());
    }

    private class FileBasedLazyLoadingSchemaHandlerForTest extends FileBasedLazyLoadingSchemaHandler {
        public FileBasedLazyLoadingSchemaHandlerForTest(final SchemaEnum neType) {
            super(neType);
        }

        @Override
        public SchemaTypeHandler getSchemaType() {
            return mockedSchemaTypeHandler;
        }

        public NavigableSet<FfvFivKey> getSchemaKeyMap() {
            return keysMap;
        }

        public NavigableMap<FfvFivKey, Schema> getSchemaMap() {
            return schemaMap;
        }

        public void setKeysMap(final NavigableSet<FfvFivKey> keysMap) {
            this.keysMap = keysMap;
        }
    }
}
