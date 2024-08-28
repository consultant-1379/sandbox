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
package com.ericsson.component.aia.model.base.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import com.ericsson.component.aia.model.base.meta.schema.Event;
import org.junit.Test;

import com.ericsson.component.aia.model.base.meta.schema.EventParameter;

public class MappedEventTest {

    @Test
    public void setParameters_noParametersSet_expectAllNewParametersToBeAdded() {
        final List<EventParameter> parameters = createEventParameterList("Event1");

        final Event mockedEvent = createMockedEvent(parameters);
        final Event mockedEvent2 = createMockedEvent(parameters);

        final MappedEvent objUndertest = new MappedEvent(mockedEvent);

        objUndertest.setParameters(mockedEvent2);

        assertEquals(1, objUndertest.getParameterSet().size());
        assertTrue(objUndertest.getParameterSet().containsAll(parameters));
    }

    @Test
    public void setParameters_parametersAlreadySet_expectOnlyNewParametersToBeAdded() {
        final List<EventParameter> parameters = createEventParameterList("Event1", "Event2", "Event3", "Event4");
        final Event mockedEvent = createMockedEvent(parameters);

        final MappedEvent objUndertest = new MappedEvent(mockedEvent);

        objUndertest.setParameters(mockedEvent);
        assertEquals(4, objUndertest.getParameterSet().size());

        parameters.add(createEventParameter("Event5"));
        parameters.add(createEventParameter("Event6"));
        final Event mockedEvent2 = createMockedEvent(parameters);

        objUndertest.setParameters(mockedEvent2);
        assertEquals(6, objUndertest.getParameterSet().size());

        assertTrue(objUndertest.getParameterSet().containsAll(parameters));
    }

    private Event createMockedEvent(final List<EventParameter> parameters) {
        final Event mockedEvent = mock(Event.class);
        when(mockedEvent.getParameterList()).thenReturn(parameters);
        return mockedEvent;
    }

    private List<EventParameter> createEventParameterList(final String... eventNames) {
        final List<EventParameter> parameters = new ArrayList<EventParameter>();
        for (final String eventName : eventNames) {
            parameters.add(createEventParameter(eventName));
        }
        return parameters;
    }

    private EventParameter createEventParameter(final String eventName) {
        final EventParameter value = mock(EventParameter.class);
        value.setName(eventName);
        return value;
    }

}
