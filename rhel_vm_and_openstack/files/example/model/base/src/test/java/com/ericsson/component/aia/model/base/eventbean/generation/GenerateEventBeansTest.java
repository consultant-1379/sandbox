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
package com.ericsson.component.aia.model.base.eventbean.generation;

import static org.mockito.Mockito.*;

import static com.ericsson.component.aia.model.base.util.SchemaUtil.*;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.ericsson.component.aia.model.base.exception.ResourceNotFoundException;
import com.ericsson.component.aia.model.base.meta.SchemaTypeHandler;
import com.ericsson.component.aia.model.base.meta.schema.Event;
import com.ericsson.component.aia.model.base.meta.schema.EventHandler;
import com.ericsson.component.aia.model.base.meta.schema.Schema;
import com.ericsson.component.aia.model.base.util.FfvFivKey;
import com.ericsson.component.aia.model.base.util.JavaClassGenerator;
import com.ericsson.component.aia.model.base.util.MappedEvent;
import com.ericsson.component.aia.model.base.config.bean.SchemaEnum;
import com.ericsson.component.aia.model.base.exception.SchemaException;
import org.junit.*;
import org.junit.rules.TemporaryFolder;

public class GenerateEventBeansTest {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    private String packageDir;

    private String packageRoot;

    private String schemaXmlFile;

    private String schemaXsdFile;

    private MappedEvent mappedEvent;

    private Event event;

    private JavaClassGenerator classGenerator;

    private Event mockedEvent;

    private GenerateEventBeans.PojoGenSchemaHandler schemaHandler;

    @Before
    public void setup() {
        packageDir = tempFolder.getRoot() + File.pathSeparator + "temp";
        packageRoot = packageDir + '/' + DEFAULT_EVENT_CLASS_PACKAGE.replaceAll("\\.", "/");
        schemaXmlFile = DEFAULT_SCHEMA_TYPE_XML_FILE;
        schemaXsdFile = DEFAULT_SCHEMA_TYPE_SCHEMA_FILE;
        ;
    }

    @Test
    public void testIfSchemaTypeXmlHasNoRecordForEbmShouldntCreateDirectory() throws Exception {
        final File basePackageForKey1 = new File(packageRoot + File.separator + SchemaEnum.CELLTRACE.value());
        final File basePackageForKey2 = new File(packageRoot + File.separator + SchemaEnum.EBM.value());
        final File subPackageForKey1 = new File(packageRoot + File.separator + SchemaEnum.CELLTRACE.value() + "_1_2");
        final File subPackageForKey2 = new File(packageRoot + File.separator + SchemaEnum.EBM.value() + "_2_3");

        final String[] args = { "", packageDir, schemaXmlFile, schemaXsdFile };

        createMockedObjects();

        final SchemaTypeHandler mockSchemaTypeHandler = mock(SchemaTypeHandler.class);
        when(mockSchemaTypeHandler.getName()).thenReturn(SchemaEnum.CELLTRACE);
        when(schemaHandler.getSchemaType()).thenReturn(mockSchemaTypeHandler).thenCallRealMethod();

        final GenerateEventBeans objUndertest = new StubbedGenerateEventBeans(schemaHandler, classGenerator);

        objUndertest.generateEvents(args);

        verify(classGenerator).generateBaseJavaClass(basePackageForKey1, mappedEvent, DEFAULT_EVENT_CLASS_PACKAGE);
        verify(classGenerator, times(0)).generateBaseJavaClass(basePackageForKey2, mappedEvent,
                DEFAULT_EVENT_CLASS_PACKAGE);
        verify(classGenerator, times(2)).generateSubJavaClass(subPackageForKey1, SchemaEnum.CELLTRACE, mockedEvent,
                DEFAULT_EVENT_CLASS_PACKAGE);
        verify(classGenerator, times(0)).generateSubJavaClass(subPackageForKey2, SchemaEnum.EBM, mockedEvent,
                DEFAULT_EVENT_CLASS_PACKAGE);
    }

    @Test
    public void testIfDirectoryIsCreatedIfSchemaXmlHasEntryforEBM() throws Exception {
        final File basePackageForKey1 = new File(packageRoot + File.separator + SchemaEnum.CELLTRACE.value());
        final File basePackageForKey2 = new File(packageRoot + File.separator + SchemaEnum.EBM.value());
        final File subPackageForKey1 = new File(packageRoot + File.separator + SchemaEnum.CELLTRACE.value() + "_1_2");
        final File subPackageForKey2 = new File(packageRoot + File.separator + SchemaEnum.EBM.value() + "_2_3");

        final String[] args = { "", packageDir, schemaXmlFile, schemaXsdFile };

        createMockedObjects();

        final SchemaTypeHandler firstMockSchemaTypeHandler = mock(SchemaTypeHandler.class);
        when(firstMockSchemaTypeHandler.getName()).thenReturn(SchemaEnum.CELLTRACE);
        final SchemaTypeHandler SecondtMockSchemaTypeHandler = mock(SchemaTypeHandler.class);
        when(SecondtMockSchemaTypeHandler.getName()).thenReturn(SchemaEnum.EBM);

        when(schemaHandler.getSchemaType()).thenReturn(firstMockSchemaTypeHandler).thenReturn(
                SecondtMockSchemaTypeHandler);

        final GenerateEventBeans objUndertest = new StubbedGenerateEventBeans(schemaHandler, classGenerator);

        objUndertest.generateEvents(args);

        verify(classGenerator).generateBaseJavaClass(basePackageForKey1, mappedEvent, DEFAULT_EVENT_CLASS_PACKAGE);
        verify(classGenerator, times(1)).generateBaseJavaClass(basePackageForKey2, mappedEvent,
                DEFAULT_EVENT_CLASS_PACKAGE);
        verify(classGenerator, times(2)).generateSubJavaClass(subPackageForKey1, SchemaEnum.CELLTRACE, mockedEvent,
                DEFAULT_EVENT_CLASS_PACKAGE);
        verify(classGenerator, times(2)).generateSubJavaClass(subPackageForKey2, SchemaEnum.EBM, mockedEvent,
                DEFAULT_EVENT_CLASS_PACKAGE);
    }

    @Test
    public void checkArguments_validNumber_expectNoException() {
        final GenerateEventBeans objUndertest = new GenerateEventBeans();
        objUndertest.checkArguments(new String[] { "", "", "", "" });
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkArguments_notEnoughArguments() {
        final GenerateEventBeans objUndertest = new GenerateEventBeans();
        objUndertest.checkArguments(new String[] { "" });
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkArguments_tooManyArguments() {
        final GenerateEventBeans objUndertest = new GenerateEventBeans();
        objUndertest.checkArguments(new String[] { "", "", "", "", "", "" });
    }

    /**
     * 
     */
    private void createMockedObjects() {
        event = mock(Event.class);
        mappedEvent = mock(MappedEvent.class);
        final int[] eventIds = new int[2];
        eventIds[0] = 1;
        eventIds[1] = 2;
        createMockedSchemaHandler(event, mappedEvent, eventIds);
        classGenerator = mock(JavaClassGenerator.class);
    }

    /**
     * @param schema
     * @return
     */
    private GenerateEventBeans.PojoGenSchemaHandler createMockedSchemaHandler(final Event evt, final MappedEvent mevt, final int[] events) {
        schemaHandler = mock(GenerateEventBeans.PojoGenSchemaHandler.class);
        final Map<Event, MappedEvent> mappedEvents = new HashMap<Event, MappedEvent>();
        mappedEvents.put(evt, mevt);
        when(schemaHandler.getEventMap()).thenReturn(mappedEvents);
        final Map<Integer, Event> eventMap = new HashMap<Integer, Event>();
        mockedEvent = mock(Event.class);
        for (final int event : events) {
            eventMap.put(event, mockedEvent);
        }
        final EventHandler eventHandler = mock(EventHandler.class);
        when(eventHandler.getMap()).thenReturn(eventMap);
        final Map<FfvFivKey, Schema> schemaMap = new HashMap<FfvFivKey, Schema>();
        schemaMap.put(new FfvFivKey(null, "1", "2"), getMockedSchema(eventHandler, "celltrace_1_2"));
        schemaMap.put(new FfvFivKey(null, "2", "3"), getMockedSchema(eventHandler, "ebm_2_3"));
        when(schemaHandler.getSchemaMap()).thenReturn(schemaMap);
        return schemaHandler;
    }

    private Schema getMockedSchema(final EventHandler eventHandler, final String packageName) {
        final Schema mockedSchema = mock(Schema.class);
        when(mockedSchema.getEventHandler()).thenReturn(eventHandler);
        when(mockedSchema.getPackageName()).thenReturn(packageName);
        return mockedSchema;
    }
}

class StubbedGenerateEventBeans extends GenerateEventBeans {

    private final PojoGenSchemaHandler schemaHandler;

    private final JavaClassGenerator classGenerator;

    public StubbedGenerateEventBeans(final PojoGenSchemaHandler schemaHandler, final JavaClassGenerator classGenerator) {
        this.schemaHandler = schemaHandler;
        this.classGenerator = classGenerator;
    }

    protected PojoGenSchemaHandler getSchemaHandler(final SchemaEnum schemaType) throws ResourceNotFoundException,
            SchemaException {
        return schemaHandler;
    }

    @Override
    protected JavaClassGenerator getJavaClassGenerator() {
        return classGenerator;
    }
}
