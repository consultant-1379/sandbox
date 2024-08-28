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

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Test;

import com.ericsson.component.aia.model.base.config.bean.SchemaEnum;
import com.ericsson.component.aia.model.base.exception.SchemaException;

public class CellTraceEventTest extends BaseEvent {

    private static final String YES_VALID_FLAG = "Yes";

    private static final String[] PARAMETERS = new String[] { "TIMESTAMP_HOUR", "TIMESTAMP_MINUTE", "TIMESTAMP_SECOND",
            "TIMESTAMP_MILLISEC", "SCANNER_ID", "GLOBAL_CELL_ID", "ENBS1APID", "GUMMEI", "INITIAL_UE_IDENTITY" };

    @Test
    public void testCelltraceEventAndCheckLengthItShouldIncludeValidtyBytes() throws SchemaException {
        final Event event = new Event(getSchemaComponentHandler(), getEventElement(), getNameSpace());
        assertEquals(28, event.getLength());
        assertEquals(false, event.isBitpacked());
        assertEquals(false, event.isVariableLength());
    }

    @Override
    String getSchemaName() {
        return SchemaEnum.CELLTRACE.value();
    }

    @Override
    Map<String, Parameter> getParameterMap(final Namespace namespace) throws SchemaException {

        final Map<String, Parameter> parameters = new HashMap<String, Parameter>();
        Element parameterElement = getParameterElement(namespace, PARAMETERS[0], "UINT", "1", YES_VALID_FLAG);
        parameters.put(PARAMETERS[0], new Parameter(parameterElement, namespace, PARAM_PREAMBLE, PARAM_PREAMBLE,
                getSchemaName()));

        parameterElement = getParameterElement(namespace, PARAMETERS[1], "UINT", "1", YES_VALID_FLAG);
        parameters.put(PARAMETERS[1], new Parameter(parameterElement, namespace, PARAM_PREAMBLE, PARAM_PREAMBLE,
                getSchemaName()));

        parameterElement = getParameterElement(namespace, PARAMETERS[2], "UINT", "1", YES_VALID_FLAG);
        parameters.put(PARAMETERS[2], new Parameter(parameterElement, namespace, PARAM_PREAMBLE, PARAM_PREAMBLE,
                getSchemaName()));

        parameterElement = getParameterElement(namespace, PARAMETERS[3], "UINT", "2", YES_VALID_FLAG);
        parameters.put(PARAMETERS[3], new Parameter(parameterElement, namespace, PARAM_PREAMBLE, PARAM_PREAMBLE,
                getSchemaName()));

        parameterElement = getParameterElement(namespace, PARAMETERS[4], "UINT", "3", YES_VALID_FLAG);
        parameters.put(PARAMETERS[4], new Parameter(parameterElement, namespace, PARAM_PREAMBLE, PARAM_PREAMBLE,
                getSchemaName()));

        parameterElement = getParameterElement(namespace, PARAMETERS[5], "UINT", "4", YES_VALID_FLAG);
        parameters.put(PARAMETERS[5], new Parameter(parameterElement, namespace, PARAM_PREAMBLE, PARAM_PREAMBLE,
                getSchemaName()));

        parameterElement = getParameterElement(namespace, PARAMETERS[6], "UINT", "3", YES_VALID_FLAG);
        parameters.put(PARAMETERS[6], new Parameter(parameterElement, namespace, PARAM_PREAMBLE, PARAM_PREAMBLE,
                getSchemaName()));

        parameterElement = getParameterElement(namespace, PARAMETERS[7], "BINARY", "7", YES_VALID_FLAG);
        parameters.put(PARAMETERS[7], new Parameter(parameterElement, namespace, PARAM_PREAMBLE, PARAM_PREAMBLE,
                getSchemaName()));

        parameterElement = getParameterElement(namespace, PARAMETERS[8], "BINARY", "6", YES_VALID_FLAG);
        parameters.put(PARAMETERS[8], new Parameter(parameterElement, namespace, PARAM_PREAMBLE, PARAM_PREAMBLE,
                getSchemaName()));

        return parameters;

    }

    private Element getParameterElement(final Namespace namespace, final String parameterName, final String type,
            final String numberofbytes, final String isUsevalid) {
        final Element element = getParameterElement(namespace, parameterName, type);
        element.addContent(new Element("numberofbytes", namespace).setText(numberofbytes));
        element.addContent(new Element("usevalid", namespace).setText(isUsevalid));
        return element;
    }

    private Element getEventElement() {
        final Namespace nameSpace = getNameSpace();
        final Element eventElement = new Element("parametertype", nameSpace);
        eventElement.addContent(new Element("name", nameSpace).setText("INTERNAL_PROC_RRC_CONN_SETUP"));
        eventElement.addContent(new Element("id", nameSpace).setText("4097"));
        eventElement.addContent(new Element("elements", nameSpace).setContent(getEventParamChilds(nameSpace)));
        return eventElement;
    }

    private List<Element> getEventParamChilds(final Namespace namespace) {
        final ArrayList<Element> newContent = new ArrayList<Element>();
        for (final String param : PARAMETERS) {
            newContent.add(getParameterElement(namespace, PARAM_PREAMBLE + param));
        }
        return newContent;
    }

}
