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
package com.ericsson.component.aia.model.base.meta.schema;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.Attribute;
import org.jdom.DataConversionException;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Before;
import org.junit.Test;

import com.ericsson.component.aia.model.base.exception.SchemaException;

public class CategoryTest {
	private Element schemaTypeElementp;
	private static final String SCHEMA_TYPE_NAMESPACE = "http://www.ericsson.com/SchemaTypes";
	private SchemaComponentHandler handler;
	private List<Event> eventList;
	
	
	@Before
	public void setup() throws DataConversionException {
		final int[] events = new int[2];
        events[0] = 1;
        events[1] = 2;
        
        final Map<Integer, Event> eventMap = new HashMap<Integer, Event>();
        final Event mockedEvent = mock(Event.class);
        when(mockedEvent.getName()).thenReturn("dummyEvent");
        eventList = new ArrayList<Event>();

        for (final int event : events) {
            eventMap.put(event, mockedEvent);
            if (event == 1) {
            eventList.add(mockedEvent);
            }
        }
		
        mockSchemaComponentHandler(eventMap);
 
		mockElement(mockedEvent.getName());

	}

	private void mockElement(final String eventName)
			throws DataConversionException {
		final Element schemaTypeElement1 = mock(Element.class);
		when(schemaTypeElement1.getText()).thenReturn("test");

		final Attribute attribute1 = mock(Attribute.class);
		stub(attribute1.getValue()).toReturn(eventName);
		final Attribute attribute2 = mock(Attribute.class);
		when(attribute2.getIntValue()).thenReturn(1);

		final Element schemaTypeElementi1 = mock(Element.class);
		when(schemaTypeElementi1.getAttribute("name")).thenReturn(attribute1);
		when(schemaTypeElementi1.getAttribute("id")).thenReturn(attribute2);
		
        final List <Element> schemaTypeChildren = new ArrayList<Element>();
		schemaTypeChildren.add(schemaTypeElementi1);
		
		schemaTypeElementp = mock(Element.class);
		when(schemaTypeElementp.getChild("name", Namespace.getNamespace(SCHEMA_TYPE_NAMESPACE))).thenReturn(schemaTypeElement1);
		when(schemaTypeElementp.getChildren("eventref", Namespace.getNamespace(SCHEMA_TYPE_NAMESPACE))).thenReturn(schemaTypeChildren);
		when(schemaTypeElementp.getName()).thenReturn("Test");
	}

	private void mockSchemaComponentHandler(final Map<Integer, Event> eventMap) {
		final Schema schema = mock(Schema.class);
		final EventHandler eventHandler = mock(EventHandler.class);
		
		handler = mock(SchemaComponentHandler.class);
		when(eventHandler.getMap()).thenReturn(eventMap);
        when(schema.getEventHandler()).thenReturn(eventHandler);
        when(handler.getSchema()).thenReturn(schema);
	}
	
	@Test
	public void testCategoryConstructorInit() throws SchemaException, DataConversionException {
		final Category category = new Category(handler, schemaTypeElementp, Namespace.getNamespace(SCHEMA_TYPE_NAMESPACE));
		assertEquals("test", category.getName());
		assertEquals(eventList, category.getEventList());
		
	}
}