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

import java.util.ArrayList;
import java.util.List;

import org.jdom.DataConversionException;
import org.jdom.Element;
import org.jdom.Namespace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.component.aia.model.base.exception.SchemaException;

/**
 * This class is used to hold a single event type
 * 
 */
public class Event implements Comparable<Event> {

    private static final Logger LOGGER = LoggerFactory.getLogger(Event.class);

    private String name;

    private final int identifier;

    private Category category = null;

    private int length = 0;

    private boolean variableLength = false;

    private boolean bitPacked = false;

    private List<EventParameter> parameterList = new ArrayList<EventParameter>();

    /**
     * Instantiate an event as an object
     * 
     * @param handler
     *            The event handler for this event, used to look up parameters
     * @param event
     *            The event XML element
     * @param schematype
     * @throws SchemaException
     */
    protected Event(final SchemaComponentHandler handler, final Element event, final Namespace namespace) throws SchemaException {
        try {
            name = event.getChild("name", namespace).getText().trim();
            identifier = Integer.valueOf(event.getChild("id", namespace).getText().trim());

            loadParameters(handler, event.getChild("elements", namespace));
        } catch (final Exception exception) {
            throw new SchemaException("Error parsing event schema element [" + event.getName() + "=" + name + "] "
                    + exception);
        }
    }

    /**
     * Set the parameter list for this event
     * 
     * @param handler
     *            The event handler for this event, used to look up parameters
     * @param elementNode
     *            The element XML element that has parameters as children
     * @throws SchemaException
     * @throws DataConversionException
     */
    private void loadParameters(final SchemaComponentHandler handler, final Element elementNode)
            throws SchemaException, DataConversionException {
        @SuppressWarnings("unchecked")
        final List<Element> children = elementNode.getChildren();

        parameterList = EventParameter.getParameters(handler, children);

        bitPacked = parameterList.get(0).getParameter().isBitPacked();
        final String firstParamName = parameterList.get(0).getParameter().getName();

        int parPosition = 0;

        for (final EventParameter parameter : parameterList) {
            if (parameter.getParameter().isBitPacked() != bitPacked) {
                final String paramName = parameter.getParameter().getName();
                throw new SchemaException("Mixed bit packed and non bit packed parameters specified on event: " + name
                        + ". Difference between parameters '" + firstParamName + "' and '" + paramName + "'");
            }

            if (parameter.isVariableLength() || parameter.isOptional() || parameter.isStructArray()) {
                variableLength = true;
            }

            if (parameter.isStructArray()) {
                length += parameter.getNumberOfBytes() * parameter.getMaxStructArraySize();
                int structFirstParamPos = parPosition;
                while (!parameterList.get(structFirstParamPos).isStructFirstParameter()) {
                    structFirstParamPos--;
                }
                parameter.setStartSkip(calculateStartSkip(parameterList, parPosition, parameter));
                parameter.setEndSkip(calculateEndSkip(parameterList, parPosition, parameter));
                parameter.setStructSize(calculateStructSize(parameterList, structFirstParamPos));
            } else {
                length += parameter.getNumberOfBytes();
                length += getUseValidFlagLength(parameter);
                length += bitPacked && parameter.isOptional() ? 1 : 0;
            }
            parPosition++;
        }

        if (bitPacked) {
            padLengthToNext32BitBoundary();
        }

        eliminateDuplicateParameterNames();

        if (length == -1) {
            LOGGER.error("Name:  " + name + "---Size:  " + length);
        }
    }

    private int getUseValidFlagLength(final EventParameter parameter) {
        return parameter.isUseValid() && !parameter.isValidLTEembeddedbitFlag() ? 1 : 0;
    }

    /**
     * Eliminate Duplicate Parameter Names
     */
    private void eliminateDuplicateParameterNames() {
        for (int index = 0; index < parameterList.size(); index++) {
            final String name = parameterList.get(index).getName();

            checkForDuplicates(index, name);
        }
    }

    /**
     * @param parameterIndex
     * @param name
     */
    private void checkForDuplicates(final int parameterIndex, final String name) {
        for (int index = parameterIndex + 1, suffix = 2; index < parameterList.size(); index++) {
            if (name.equalsIgnoreCase(parameterList.get(index).getName())) {
                parameterList.get(parameterIndex).setName(name + "_1");
                parameterList.get(index).setName(name + "_" + suffix++);
            }
        }
    }

    private void padLengthToNext32BitBoundary() {
        if (length % 32 > 0) {
            length += 32 - length % 32;
        }
    }

    /**
     * Get the name of an event
     * 
     * @return The event name
     */
    public String getName() {
        return name;
    }

    /**
     * Get the ID of an event
     * 
     * @return The event ID
     */
    public int getId() {
        return identifier;
    }

    /**
     * returns true if this event is bit packed
     * 
     * @return true if this event is bit packed
     */
    public boolean isBitpacked() {
        return bitPacked;
    }

    /**
     * Get the total length of this record
     * 
     * @return The record length
     */
    public int getLength() {
        return length;
    }

    /**
     * Check if this event is of variable length
     * 
     * @return true if the record is of variable length
     */
    public boolean isVariableLength() {
        return variableLength;
    }

    /**
     * 
     * @return the parameter list
     */
    public List<EventParameter> getParameterList() {
        return parameterList;
    }

    /**
     * Get the category of this event
     * 
     * @return the category of the event
     */
    public Category getCategory() {
        return category;
    }

    /**
     * Set the category of this event
     * 
     * @param category
     *            the category of the event
     */
    public void setCategory(final Category category) {
        this.category = category;
    }

    /**
     * Method to dump the event to a string
     * 
     * @return string containing the parameter
     */
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();

        builder.append(name);
        builder.append(",");
        builder.append(identifier);
        builder.append(",");
        builder.append(category);

        for (final EventParameter parameter : parameterList) {
            builder.append("[");
            builder.append(parameter.toString());
            builder.append("]");
        }

        return builder.toString();
    }

    /**
     * Compare this event to another event, comparison based on name
     * 
     * @param comparedEvent
     * @return the comparison value
     */
    @Override
    public int compareTo(final Event comparedEvent) {
        return name.compareTo(comparedEvent.name);
    }

    @Override
    public boolean equals(final Object other) {
        if (other instanceof Event) {
            final Event otherEvent = (Event) other;
            return name.equals(otherEvent.getName());
        }
        return false;
    }

    /**
     * @return The hash code of the event name
     */
    @Override
    public int hashCode() {
        return name.hashCode();
    }

    /**
     * @param eventParameterList
     * @param parPostion
     * @param parameter
     * @return
     */
    public static int calculateStartSkip(final List<EventParameter> eventParameterList, final int parPostion,
            final EventParameter parameter) {
        int startSkip = 0;
        if (!parameter.isStructFirstParameter()) {
            for (int firstPar = parPostion - 1; firstPar >= 0; firstPar--) {
                final EventParameter eventParameter = eventParameterList.get(firstPar);
                startSkip += getNumberOfBytesWithValidtyByte(eventParameter);
                if (eventParameterList.get(firstPar).isStructFirstParameter()) {
                    break;
                }
            }
        }
        return startSkip;
    }

    private static int getNumberOfBytesWithValidtyByte(final EventParameter eventParameter) {
        int bytes = eventParameter.getNumberOfBytes();
        bytes += ((eventParameter.isUseValid() || eventParameter.isOptional()) && !eventParameter
                .isValidLTEembeddedbitFlag()) ? 1 : 0;
        return bytes;
    }

    /**
     * @param eventParameterList
     * @param parPosition
     * @param parameter
     * @return
     */
    public static int calculateEndSkip(final List<EventParameter> eventParameterList, final int parPosition,
            final EventParameter parameter) {
        int endSkip = 0;
        if (!parameter.isStructLastParameter()) {
            for (int lastPar = parPosition + 1; lastPar < eventParameterList.size(); lastPar++) {
                final EventParameter eventParameter = eventParameterList.get(lastPar);
                endSkip += getNumberOfBytesWithValidtyByte(eventParameter);
                if (eventParameter.isStructLastParameter()) {
                    break;
                }
            }
        }
        return endSkip;
    }

    /**
     * @param eventParameterList
     * @param parameter
     * @param structSize
     * @param structPar
     * @return
     */
    private int calculateStructSize(final List<EventParameter> eventParameterList, final int parPosition) {
        int structSize = 0;
        for (int structPar = parPosition; structPar < eventParameterList.size(); structPar++) {
            final EventParameter eventParameter = eventParameterList.get(structPar);
            structSize += getNumberOfBytesWithValidtyByte(eventParameter);
            if (eventParameterList.get(structPar).isStructLastParameter()) {
                break;
            }
        }
        return structSize;
    }
}
