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

import com.ericsson.component.aia.model.base.generic.param.decoder.GenericParameterDecoder;
import com.ericsson.component.aia.model.base.util.ParameterDecoderFactory;
import com.ericsson.component.aia.model.base.exception.SchemaException;
import org.jdom.DataConversionException;
import org.jdom.Element;

/**
 * This class holds information on a specific parameter on an event. An event can have more than one
 * instance of a particular parameter type so this class holds those instances. It also holds
 * mapping information for parameters to table columns.
 * 
 */
public class EventParameter implements Comparable<EventParameter>, Comparator<EventParameter> {

    public static final int NOT_SET = -1;

    private String name = "";

    private final Parameter parameter;

    private final String type;

    private final int parserType;

    private int numberOfBytes;

    private final int numberOfBits;

    private final boolean variableLength;

    private boolean useValid = false;

    private boolean optional = false;

    private boolean structArray = false;

    private int maxStructArraySize = 0;

    private boolean structFirstParameter = false;

    private boolean structLastParameter = false;

    private boolean validLTEembeddedbitFlag = false;
    
    private int validStructureArraySize = 0;
    
    private int startSkip = 0;

    private int endSkip = 0;
    
    private int structSize = 0;
    
    private GenericParameterDecoder parameterDecoder = null;

    private EventParameter(final Parameter parameter) throws SchemaException {
        this.parameter = parameter;
        this.name = parameter.getName();
        this.type = parameter.getType();
        this.parserType = parameter.getParserType();
        this.numberOfBytes = parameter.getNumberOfBytes();
        this.numberOfBits = parameter.getNumberOfBits();
        this.variableLength = parameter.isVariableLength();
        this.useValid = parameter.isUseValid();
        this.validLTEembeddedbitFlag = parameter.isValidLTEembeddedbitFlag();
    }

    /**
     * This method returns a list of parameters from a list of XML elements containing struct and param elements
     * 
     * @param handler A SchemaHandler used to look up parameters
     * @param children The list of XML elements
     * @throws SchemaException
     * @throws DataConversionException
     */
    protected static List<EventParameter> getParameters(final SchemaComponentHandler handler,
            final List<Element> children) throws DataConversionException, SchemaException {
        final List<EventParameter> parameterList = new ArrayList<EventParameter>();

        for (final Element child : children) {
            if (child.getName().equals("param")) {
                parameterList.add(getSingleParameter(handler, child));
            } else if (child.getName().equals("struct")) {
                parameterList.addAll(getStructParameters(handler, child));
            }
        }

        return parameterList;
    }

    /**
     * This method creates a new parameter type object from a param XML element in the event schema file
     * @param handler A schemahandler to look up parameter types
     * @param structElement The struct XML element
     * @return returns a list of EventParam objects describing the structure instance
     * @throws SchemaException
     * @throws DataConversionException
     */
    private static List<EventParameter> getStructParameters(final SchemaComponentHandler handler,
            final Element structElement) throws SchemaException, DataConversionException {
        final String structureType = structElement.getAttribute("type").getValue();

        String structureName = structureType;

        if (structElement.getText() != null && structElement.getText().trim().length() > 0) {
            structureName = structElement.getText().trim();
        }

        final Structure structure = handler.getSchema().getStructureHandler().getMap().get(structureType);

        if (structure == null) {
            throw new SchemaException("Unknown structure \"" + structureType + "\" specified on event");
        }

        boolean structArray = false;
        int maxStructArraySize = 0;
        if (structElement.getAttribute("seqmaxlen") != null) {
            structArray = true;
            maxStructArraySize = structElement.getAttribute("seqmaxlen").getIntValue();
        }

        final List<EventParameter> structureParameterList = new ArrayList<EventParameter>(structure.getParameterList()
                .size());

        setStructureWideValuesOnParameters(structureName, structure, structArray, maxStructArraySize, 
                structureParameterList);

        return structureParameterList;
    }

    /**
     * @param structureName
     * @param structure
     * @param structArray
     * @param maxStructArraySize
     * @param ignoreFlag
     * @param structureParameterList
     * @throws SchemaException
     */
    private static void setStructureWideValuesOnParameters(final String structureName, final Structure structure,
            final boolean structArray, final int maxStructArraySize, 
            final List<EventParameter> structureParameterList) throws SchemaException {
        final List<EventParameter> structParameterList = structure.getParameterList();

        for (int i = 0; i < structParameterList.size(); i++) {
            final EventParameter newEventParameter = new EventParameter(structParameterList.get(i).parameter);

            newEventParameter.name = structureName + '_' + structParameterList.get(i).name;

            newEventParameter.useValid = structParameterList.get(i).useValid;
            newEventParameter.optional = structParameterList.get(i).optional;

            newEventParameter.structArray = structArray;
            newEventParameter.maxStructArraySize = maxStructArraySize;
            newEventParameter.validStructureArraySize = maxStructArraySize;
            
            newEventParameter.parameterDecoder= ParameterDecoderFactory.getParameterDecoder(newEventParameter);

            if (i == 0) {
                newEventParameter.structFirstParameter = true;
            }

            if (i == structParameterList.size() - 1) {
                newEventParameter.structLastParameter = true;
            }

            structureParameterList.add(newEventParameter);
        }
    }

    /**
     * This method creates a new parameter type object from a param XML element in the event schema file
     * 
     * @param handler A schemahandler to look up parameter types
     * @param paramElement The param XML element
     * @return returns an EventParam object describing the parameter instance
     * @throws SchemaException
     * @throws DataConversionException
     */
    private static EventParameter getSingleParameter(final SchemaComponentHandler handler, final Element paramElement)
            throws SchemaException, DataConversionException {
        final String paramPreamble = handler.getSchema().getParamPreamble();

        String parameterName = paramElement.getText().trim().replaceFirst(paramPreamble, "");

        parameterName = addPrefixIfParamterNameStartsWithDigit(parameterName);

        final String parameterType = getParameterTypeIfSpecified(paramElement, parameterName);

        final Parameter parameter = handler.getSchema().getParameterHandler().getMap().get(parameterType);

        if (parameter == null) {
            throw new SchemaException("Unknown parameter \"" + parameterType + "\" specified on event");
        }

        boolean useValidFlag = parameter.isUseValid();
        if (paramElement.getAttribute("usevalid") != null) {
            useValidFlag = paramElement.getAttribute("usevalid").getBooleanValue();
        }

        boolean optionalFlag = false;
        if (paramElement.getAttribute("optional") != null) {
            optionalFlag = paramElement.getAttribute("optional").getBooleanValue();
        }

        final EventParameter newEventParameter = new EventParameter(parameter);

        newEventParameter.name = parameterName;
        newEventParameter.useValid = useValidFlag;
        newEventParameter.optional = optionalFlag;
        newEventParameter.parameterDecoder = ParameterDecoderFactory.getParameterDecoder(newEventParameter);

        return newEventParameter;
    }

    /**
     * @param parameterName
     * @return
     */
    private static String addPrefixIfParamterNameStartsWithDigit(String parameterName) {
        if (Character.isDigit(parameterName.charAt(0))) {
            parameterName = "P_" + parameterName;
        }
        return parameterName;
    }

    /**
     * @param paramElement
     * @param parameterType
     * @return
     */
    private static String getParameterTypeIfSpecified(final Element paramElement, String parameterType) {
        if (paramElement.getAttribute("type") != null) {
            parameterType = paramElement.getAttribute("type").getValue().trim();
        }
        return parameterType;
    }

    /**
     * Get the name of the parameter
     * 
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Get the definition of this parameter
     * 
     * @return the definition of the parameter
     */
    public Parameter getParameter() {
        return parameter;
    }

    /**
     * Return the Parser type for this parameter
     * 
     * @return the Parser type
     */
    public int getParserType() {
        return parserType;
    }

    /**
     * Return the type for this parameter
     * 
     * @return the type
     */
    public String getType() {
        return type;
    }
    
    /**
     * Return the number of bytes used by this parameter
     * 
     * @return the size (or minimum size if variable length) of this parameter in bytes
     */
    public int getNumberOfBytes() {
        return numberOfBytes;
    }
    
    /**
     * Return Start offset/Skip for Structured Attribute
     * @return
     */
    public int getStartSkip() {
        return startSkip;
    }

    /**
     * Return End offset/Skip for Structured Attribute
     * @return
     */
    public int getEndSkip() {
        return endSkip;
    }
    
    /**
     * Return Structure Size
     * @return
     */
    public int getStructSize() {
        return structSize;
    }

    /**
     * @param structSize
     */
    public void setStructSize(final int structSize) {
        this.structSize = structSize;
    }
    
    /**
     * Set Start offset/Skip for Structured Attribute
     * @param startSkip
     */
    public void setStartSkip(final int startSkip) {
        this.startSkip = startSkip;
    }

    /**
     * Set End offset/Skip for Structured Attribute
     * @param endSkip
     */
    public void setEndSkip(final int endSkip) {
        this.endSkip = endSkip;
    }

    /**
     * Set the number of bytes in this parameter, used on variable length parameters
     * 
     * @param numberOfBytes
     */
    public void setNumberOfBytes(final int numberOfBytes) {
        this.numberOfBytes = numberOfBytes;
    }

    /**
     * Return the number of bits used by this parameter if it is bit packed
     * 
     * @return the size (or minimum size if variable length) of this parameter packed in bits
     */
    public int getNumberOfBits() {
        return numberOfBits;
    }

    /**
     * Return if use of this parameter is valid
     * 
     * @return true if the use of this parameter is valid
     */
    public boolean isUseValid() {
        return useValid;
    }

    /**
     * Return if this parameter is of variable length
     * 
     * @return true if the parameter is variable length
     */
    public boolean isVariableLength() {
        return variableLength;
    }

    /**
     * Return if use of this parameter is optional
     * 
     * @return true if the use of this parameter is optional
     */
    public boolean isOptional() {
        return optional;
    }

    /**
     * Return if this parameter is a structure array parameter
     * 
     * @return true if the parameter is an array parameter
     */
    public boolean isStructArray() {
        return structArray;
    }

    /**
     * Return true if this parameter is the first parameter in a structure
     * 
     * @return true if this parameter is the first parameter in a structure
     */
    public boolean isStructFirstParameter() {
        return structFirstParameter;
    }

    /**
     * Return true if this parameter is the last parameter in a structure
     * 
     * @return true if this parameter is the last parameter in a structure
     */
    public boolean isStructLastParameter() {
        return structLastParameter;
    }

    /**
     * Return the maximum number of elements in the array if this parameter is a structure array parameter
     * 
     * @return The maximum number of elements
     */
    public int getMaxStructArraySize() {
        return maxStructArraySize;
    }
    
    /**
     * Return the maximum number of valid elements in the array
     * 
     * @return The maximum number of elements
     */
    public int getValidStructureArraySize() {
        return validStructureArraySize;
    }
    

    /**
     * Set Valid Structure Array Size
     * @param validStructureArraySize
     */
    public void setValidStructureArraySize(final int validStructureArraySize) {
        this.validStructureArraySize = validStructureArraySize;
    }

    /**
     * Set the name of the parameter, used to eliminate duplicate parameter names on an event
     * and to prefix parameters with struct names
     * 
     * @param name The parameter name
     */
    public void setName(final String name) {
        this.name = name;
    }


    /**
     * Returns a string representation of this object
     */
    @Override
    public String toString() {
        return "EventParameter [name=" + name + ", parameter=" + parameter + ", type=" + type + ", parserType="
                + parserType + ", numberOfBytes=" + numberOfBytes + ", numberOfBits=" + numberOfBits
                + ", variableLength=" + variableLength + ", useValid=" + useValid + ", validLTEembeddedbitFlag="
                + validLTEembeddedbitFlag + ", optional=" + optional + ", structArray="
                + structArray + ", structFirstParameter=" + structFirstParameter + ", structLastParameter="
                + structLastParameter + ", maxStructArraySize=" + maxStructArraySize + ", validStructureArraySize="
                + validStructureArraySize  + ", startSkip=" + startSkip
                + ", endSkip=" + endSkip + ", GenericParameterDecoder=" + (parameterDecoder != null ? parameterDecoder
                .getClass().getCanonicalName() : "") + "]";
    }

    /**
     * Compare this parameter to another parameter, comparison based on name
     * 
     * @param comparedEventParameter
     * @return the comparison value
     */
    @Override
    public int compareTo(final EventParameter comparedEventParameter) {
        return name.compareTo(comparedEventParameter.name);
    }

    /**
     * Compare two parameters, comparison based on name
     * 
     * @param firstParameter the first parameter
     * @param secondParameter the second parameter
     * @return the comparison value
     */
    @Override
    public int compare(final EventParameter firstParameter, final EventParameter secondParameter) {
        return firstParameter.compareTo(secondParameter);
    }

    /**
     * Override the equals method for hash maps of event parameters
     */
    @Override
    public boolean equals(final Object other) {
        if (other instanceof EventParameter) {
            final EventParameter otherEventParameter = (EventParameter) other;
            return name.equals(otherEventParameter.getName());
        }
        return false;
    }

    /**
     * Override the hashCode hash maps of event parameters
     * 
     * @return The hash code of the event parameter name
     */
    @Override
    public int hashCode() {
        return name.hashCode();
    }

    /**
     * @return the validLTEembeddedbitFlag
     */
    public boolean isValidLTEembeddedbitFlag() {
        return validLTEembeddedbitFlag;
    }
    
    /**
     * @return Parameter Decoder
     */
    public GenericParameterDecoder getParameterDecoder() {
        return parameterDecoder;
    }


}
