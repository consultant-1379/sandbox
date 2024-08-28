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
package com.ericsson.component.aia.model.base.meta.schema;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Test;

import com.ericsson.component.aia.model.base.config.bean.SchemaEnum;
import com.ericsson.component.aia.model.base.exception.SchemaException;

public class EbmEventTest extends BaseEvent {
    private static final String[] PARAMETERS = new String[] { "L_HEADER_HEADER_EVENT_ID",
            "L_HEADER_HEADER_EVENT_RESULT", "L_HEADER_HEADER_TIME_HOUR", "L_HEADER_HEADER_TIME_MINUTE",
            "L_HEADER_HEADER_TIME_SECOND", "L_HEADER_TIME_MILLISECOND", "CSG_MEMBERSHIP_STATUS", "DURATION_PSM" };


    private List<Element> getEventParamChilds(final Namespace namespace) {
        final ArrayList<Element> newContent = new ArrayList<Element>();
        
        newContent.add(getParameterElementWithOptionalAttribute(namespace, PARAMETERS[0], "false").setAttribute("usevalid", "true"));
        
        for (int index = 1; index < PARAMETERS.length - 1; index++) {
            newContent.add(getParameterElementWithOptionalAttribute(namespace, PARAMETERS[index], "false"));
        }

        newContent.add(getParameterElementWithOptionalAttribute(namespace, PARAMETERS[PARAMETERS.length - 1], "true"));
        return newContent;
    }

   private Element getParameterElementWithOptionalAttribute(final Namespace namespace, final String parameterName, final String isOptional) {
        return getParameterElement(namespace, parameterName).setAttribute("optional", isOptional);
    }

    @Test
    public void testStartSkipReturnCorrectValue() {
        final List<EventParameter> mockedEventParameterList = new ArrayList<EventParameter>();
        mockedEventParameterList.add(getMockedEventParameter());
        final int notAFirstElement = 1;
        assertThat(Event.calculateStartSkip(mockedEventParameterList, notAFirstElement, getMockedEventParameter()),
                is(1));
    }

    @Test
    public void testEndSkipReturnCorrectValue() {
        final List<EventParameter> mockedEventParameterList = new ArrayList<EventParameter>();
        mockedEventParameterList.add(getMockedEventParameter());
        mockedEventParameterList.add(getMockedEventParameter());
        final int notALastElement = 0;
        assertThat(Event.calculateEndSkip(mockedEventParameterList, notALastElement, getMockedEventParameter()), is(1));
    }

    @Test
    public void testEndSkipReturnCorrectValueIfUseValidisTrue() {
        final List<EventParameter> mockedEventParameterList = new ArrayList<EventParameter>();
        mockedEventParameterList.add(getMockedEventParameter(true));
        mockedEventParameterList.add(getMockedEventParameter(true));
        final int notALastElement = 0;
        assertThat(Event.calculateEndSkip(mockedEventParameterList, notALastElement, getMockedEventParameter()), is(2));
    }

    @Test
    public void testEbmEvent_return_correctEventLength_ifitContainsOptionalParameter() throws SchemaException {
        final Event event = new Event(getSchemaComponentHandler(), getEventElement(), getNameSpace());
        assertEquals(64, event.getLength());
        assertEquals(true, event.isBitpacked());
        assertEquals(true, event.isVariableLength());
    }

    private EventParameter getMockedEventParameter() {
        return getMockedEventParameter(false);
    }

    private EventParameter getMockedEventParameter(final boolean isUsedValid) {
        final EventParameter mockedEventParameter = getMockedEventParameter(isUsedValid, 1);
        when(mockedEventParameter.isStructLastParameter()).thenReturn(false);
        when(mockedEventParameter.isStructArray()).thenReturn(true);
        when(mockedEventParameter.getMaxStructArraySize()).thenReturn(11);
        return mockedEventParameter;
    }

    private EventParameter getMockedEventParameter(final boolean isUsedValid, final int numberofBytes) {
        final EventParameter mockedEventParameter = mock(EventParameter.class);
        when(mockedEventParameter.isUseValid()).thenReturn(isUsedValid);
        when(mockedEventParameter.getNumberOfBytes()).thenReturn(numberofBytes);
        return mockedEventParameter;
    }
    
    private Element getParameterElement(final Namespace namespace, final String parameterName, final String type,
            final String numberOfBits) {
        final Element element = getParameterElement(namespace, parameterName, type);
        element.addContent(new Element("numberofbits", namespace).setText(numberOfBits));
        return element;
    }
    
    private Element getEventElement() {
        final Namespace nameSpace = getNameSpace();
        final Element eventElement = new Element("parametertype", nameSpace);
        eventElement.addContent(new Element("name", nameSpace).setText("L_DETACH"));
        eventElement.addContent(new Element("id", nameSpace).setText("6"));
        eventElement.addContent(new Element("elements", nameSpace).setContent(getEventParamChilds(nameSpace)));
        return eventElement;
    }

    @Override
    Map<String, Parameter> getParameterMap(final Namespace nameSpace) throws SchemaException {
        final Map<String, Parameter> parameters = new HashMap<String, Parameter>();

        for (int index = 0; index < PARAMETERS.length; index++) {
            final Element parameterElement = getParameterElement(nameSpace, PARAMETERS[index], "UINT", "32");
            parameters.put(PARAMETERS[index], new Parameter(parameterElement, nameSpace, PARAM_PREAMBLE,
                    PARAM_PREAMBLE, getSchemaName()));

        }
        return parameters;
    }


    @Override
    String getSchemaName() {
        return SchemaEnum.EBM.value();
    }
}
