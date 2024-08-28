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

import java.util.*;

import com.ericsson.component.aia.model.base.exception.SchemaException;
import org.jaxen.JaxenException;
import org.jaxen.SimpleNamespaceContext;
import org.jaxen.jdom.JDOMXPath;
import org.jdom.Document;
import org.jdom.Element;

/**
 * This class holds all the events defined in a schema
 * 
 */
public class EventHandler extends SchemaComponentHandler {
    protected static final String EVENT_TYPES_TABLE_NAME = "event_types"; // NOPMD

    protected static final String EVENT_PARAM_MAP_TABLE_NAME = "event_param_map"; // NOPMD

    private static final String EVT_XPATH_EXPR = "/pe:eventspecification/pe:events/pe:event";

    private final Map<Integer, Event> eventMap;

    private final String schematype;

    /**
     * Constructor to initialize the handler for this schema
     *
     * @param schema
     *            The schema for which this map is being built
     * @param schematype
     */
    public EventHandler(final Schema schema, final String schematype) {
        super(schema);
        this.eventMap = new LinkedHashMap<Integer, Event>();
        this.schematype = schematype;
    }

    /**
     * Builds the event map from elements in the XML file.
     * 
     * @param eventDocument
     *            the event XML document
     * @param nameSpaceMap
     *            the name spaces used in the XML document
     * @throws SchemaException
     * @throws JaxenException
     */
    @Override
    public void buildMap(final Document eventDocument) throws SchemaException, JaxenException {
        final JDOMXPath xPathEvent = new JDOMXPath(EVT_XPATH_EXPR);
        xPathEvent.setNamespaceContext(new SimpleNamespaceContext(schema.getNameSpaceMap()));

        @SuppressWarnings("unchecked")
        final List<Element> eventNodeList = xPathEvent.selectNodes(eventDocument);

        for (final Element event : eventNodeList) {
            final Event currentEvent = new Event(this, event, schema.getNameSpace());

            eventMap.put(currentEvent.getId(), currentEvent);
        }
    }

    /**
     * Get the event mapping
     * 
     * @return The event mapping
     */
    public Map<Integer, Event> getMap() {
        return eventMap;
    }
}
