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

import static org.mockito.Mockito.*;

import java.util.*;

import com.ericsson.component.aia.model.base.exception.SchemaException;
import org.jdom.*;
import org.junit.Before;

public abstract class BaseEvent {

    protected static final String PARAM_PREAMBLE = "EVENT_PARAM_";

    private SchemaComponentHandler schemaComponentHandler;

    private Namespace nameSpace;

    @Before
    public void setup() throws SchemaException, DataConversionException {
        nameSpace = Namespace.getNamespace("http://www.ericsson.com/PmEvents");
        schemaComponentHandler = mock(SchemaComponentHandler.class);

        final Schema schema = mock(Schema.class);
        when(schemaComponentHandler.getSchema()).thenReturn(schema);
        when(schema.getParamPreamble()).thenReturn(PARAM_PREAMBLE);

        final ParameterHandler parameterHandler = mock(ParameterHandler.class);
        when(schema.getParameterHandler()).thenReturn(parameterHandler);
        when(parameterHandler.getMap()).thenReturn(getParameterMap(nameSpace));

    }

    abstract Map<String, Parameter> getParameterMap(final Namespace nameSpace) throws SchemaException;

    abstract String getSchemaName();

    Element getParameterElement(final Namespace namespace, final String parameterName, final String type) {
        final Element element = new Element("parametertype", namespace);
        element.addContent(new Element("name", namespace).setText(parameterName));
        element.addContent(new Element("type", namespace).setText(type));
        element.addContent(new Element("description", namespace).setText("dummy description of event"));
        return element;
    }

    Element getParameterElement(final Namespace namespace, final String parameterName) {
        return new Element("param", namespace).setText(parameterName);
    }

    Namespace getNameSpace() {
        return nameSpace;
    }

    SchemaComponentHandler getSchemaComponentHandler() {
        return schemaComponentHandler;
    }

}
