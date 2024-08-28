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

import static com.ericsson.component.aia.model.base.util.GenerateEventBeansUtil.*;

import java.io.*;
import java.net.MalformedURLException;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stringtemplate.v4.*;

import com.ericsson.component.aia.model.base.config.bean.SchemaEnum;
import com.ericsson.component.aia.model.base.exception.ResourceNotFoundException;
import com.ericsson.component.aia.model.base.meta.schema.Event;
import com.ericsson.component.aia.model.base.meta.schema.EventParameter;
import com.ericsson.component.aia.model.base.resource.loading.ResourceFileFinder;

public class JavaClassGenerator {
    private static final Logger LOG = LoggerFactory.getLogger(JavaClassGenerator.class);

    private static final Pattern EVENT_ARRAY_PATTERN = Pattern.compile("^(EVENT_ARRAY_.*)_[0-9]+$");

    /**
     * Generate a base java class for all events of this type in the schema
     *
     * @param packageDir
     *            The directory in which to generate the Java class
     * @param mappedEvent
     *            Set of parameters for this event
     * @param packageName
     *            package name
     * @throws IOException
     */
    public void generateBaseJavaClass(final File packageDir, final MappedEvent mappedEvent, final String packageName) throws IOException {
        final File eventBeanFile = new File(packageDir, mappedEvent.getEvent().getName() + JAVA_EXTENSION);
        final PrintWriter classWriter = new PrintWriter(new BufferedWriter(new FileWriter(eventBeanFile)));
        final STGroup group = getTemplateFile(BASE_EVENT_BEAN_STG_FILE_WITH_DIRECTORY);
        final ST stringTemplate = group.getInstanceOf("main");
        final Map<String, ArrayParameter> arrayParameterMap = new HashMap<String, ArrayParameter>();
        String isDataConversionNeeded = null;
        String asn1Parameter = NULL_STRING;
        String parameterJavaType = null;
        String parameterInitValue = null;
        String[] arrayElements;
        String arrayParameter;
        boolean shouldBeArrayElement;
        for (final EventParameter parameter : mappedEvent.getParameterSet()) {
            parameterJavaType = TypeConversionParserTypesToJava.typeSqlToJava(parameter.getParserType());
            parameterInitValue = getParameterInitValue(parameter);
            arrayElements = null;
            if (parameter.isStructArray()) {
                parameterJavaType += ARRAY_BRACKETS;
                parameterInitValue = getParameterArrayInitValue(parameterInitValue, parameter.getMaxStructArraySize());
                arrayElements = initializeArrayElement(parameter);
            }
            shouldBeArrayElement = false;
            arrayParameter = getArrayParameter(parameter);
            int arrayElementIndex = 0;
            if (arrayParameter != null) {
                shouldBeArrayElement = true;
                ArrayParameter aParam = null;
                if (arrayParameterMap.containsKey(arrayParameter)) {
                    aParam = arrayParameterMap.get(arrayParameter);
                } else {
                    aParam = new ArrayParameter(arrayParameter, parameterJavaType);
                    arrayParameterMap.put(arrayParameter, aParam);
                }
                aParam.addParameter(parameter.getName());

            }
            stringTemplate.addAggr(
                    "parameter.{type, name, initValue, arrayElements, isByteArray, isTimestamp, isString, shouldBeArrayElement, arrayElementName, arrayElementIndex}",
                    parameterJavaType, parameter.getName(), parameterInitValue, arrayElements, getBinaryFlag(parameter), isTimeStampType(parameter),
                    isStringType(parameterJavaType), shouldBeArrayElement, arrayParameter, arrayElementIndex);
            if (isTypeBinary(parameter)) {
                isDataConversionNeeded = TRUE_FLAG;
            }

            //There would be just one ASN.1 parameter in an event ,so asn1Parameter is set once and never overridden
            asn1Parameter = asn1Parameter.equals(NULL_STRING) ? getAsn1Param(parameter) : asn1Parameter;
        }
        processArrayParameters(stringTemplate, arrayParameterMap);
        addPackageNameToClass(stringTemplate, packageName);
        addSchemaTypeToClass(stringTemplate, packageDir, mappedEvent, isDataConversionNeeded, asn1Parameter);
        writeClassFile(stringTemplate, classWriter);
    }

    /**
     * Generate a base java class for all events of this type in the schema
     *
     * @param packageDir
     *            The directory in which to generate the Java class
     * @param mappedEvent
     *            Set of parameters for this event
     * @param packageName
     *            package name
     * @throws IOException
     */
    public void generateBeanJavaClass(final File packageDir, final MappedEvent mappedEvent, final String packageName) throws IOException {
        final File eventBeanFile = new File(packageDir, mappedEvent.getEvent().getName() + JAVA_EXTENSION);
        final PrintWriter classWriter = new PrintWriter(new BufferedWriter(new FileWriter(eventBeanFile)));
        final STGroup group = getTemplateFile(EVENT_BEAN_STG_FILE_WITH_DIRECTORY);
        final ST stringTemplate = group.getInstanceOf("main");
        final Map<String, ArrayParameter> arrayParameterMap = new HashMap<String, ArrayParameter>();
        String isDataConversionNeeded = null;
        String asn1Parameter = NULL_STRING;
        String parameterJavaType = null;
        String parameterInitValue = null;
        String[] arrayElements;
        String arrayParameter;
        boolean shouldBeArrayElement;
        for (final EventParameter parameter : mappedEvent.getParameterSet()) {
            parameterJavaType = TypeConversionParserTypesToJava.typeSqlToJava(parameter.getParserType());
            parameterInitValue = getParameterInitValue(parameter);
            arrayElements = null;
            if (parameter.isStructArray()) {
                parameterJavaType += ARRAY_BRACKETS;
                parameterInitValue = getParameterArrayInitValue(parameterInitValue, parameter.getMaxStructArraySize());
                arrayElements = initializeArrayElement(parameter);
            }
            shouldBeArrayElement = false;
            arrayParameter = getArrayParameter(parameter);
            int arrayElementIndex = 0;
            if (arrayParameter != null) {
                shouldBeArrayElement = true;
                ArrayParameter aParam = null;
                if (arrayParameterMap.containsKey(arrayParameter)) {
                    aParam = arrayParameterMap.get(arrayParameter);
                } else {
                    aParam = new ArrayParameter(arrayParameter, parameterJavaType);
                    arrayParameterMap.put(arrayParameter, aParam);
                }
                aParam.addParameter(parameter.getName());

            }
            stringTemplate.addAggr(
                    "parameter.{type, name, initValue, arrayElements, isByteArray, isTimestamp, isString, shouldBeArrayElement, arrayElementName, arrayElementIndex}",
                    parameterJavaType, parameter.getName(), parameterInitValue, arrayElements, getBinaryFlag(parameter), isTimeStampType(parameter),
                    isStringType(parameterJavaType), shouldBeArrayElement, arrayParameter, arrayElementIndex);
            if (isTypeBinary(parameter)) {
                isDataConversionNeeded = TRUE_FLAG;
            }

            //There would be just one ASN.1 parameter in an event ,so asn1Parameter is set once and never overridden
            asn1Parameter = asn1Parameter.equals(NULL_STRING) ? getAsn1Param(parameter) : asn1Parameter;
        }
        processArrayParameters(stringTemplate, arrayParameterMap);
        addPackageNameToClass(stringTemplate, packageName);
        addSchemaTypeToClass(stringTemplate, packageDir, mappedEvent, isDataConversionNeeded, asn1Parameter);
        writeClassFile(stringTemplate, classWriter);
    }

    private STGroupFile getTemplateFile(final String templateFile) throws ResourceNotFoundException, MalformedURLException {
        return new STGroupFile(new ResourceFileFinder().getFileResourceURL(templateFile), STGroup.defaultGroup.encoding,
                STGroup.defaultGroup.delimiterStartChar, STGroup.defaultGroup.delimiterStopChar);
    }

    private void addPackageNameToClass(final ST stringTemplate, final String packageName) {
        stringTemplate.add("packageName", packageName);
    }

    /**
     * @param stringTemplate
     * @param packageDir
     * @param mappedEvent
     * @param isDataConversionNeeded
     * @param asn1Parameter
     */
    private void addSchemaTypeToClass(final ST stringTemplate, final File packageDir, final MappedEvent mappedEvent,
                                      final String isDataConversionNeeded, final String asn1Parameter) {
        stringTemplate.add("schemaType", packageDir.getName());
        stringTemplate.addAggr("event.{name, dataConvertersNeeded, asn1Parameter}", mappedEvent.getEvent().getName(), isDataConversionNeeded,
                asn1Parameter);
    }

    /**
     * @param parameterJavaType
     * @return
     */
    private String isStringType(final String parameterJavaType) {
        return "String".equals(parameterJavaType) ? TRUE_FLAG : null;
    }

    /**
     * @param parameter
     * @return
     */
    private String isTimeStampType(final EventParameter parameter) {
        return parameter.getParserType() == java.sql.Types.TIMESTAMP ? TRUE_FLAG : null;
    }

    /**
     * @param parameter
     * @return
     */
    private String getBinaryFlag(final EventParameter parameter) {
        return isTypeBinary(parameter) ? TRUE_FLAG : null;
    }

    /**
     * @param stringTemplate
     * @param classWriter
     */
    private void writeClassFile(final ST stringTemplate, final PrintWriter classWriter) {
        classWriter.println(stringTemplate.render());
        classWriter.close();
    }

    /**
     * Generate a java sub class this event in this schema that inherits from the base java class
     *
     * @param schemaPackageDir
     *            The directory in which to generate the Java class
     * @param schemaType
     *            The base schema type for this schema
     * @param event
     *            The event to generate the mapping for
     * @param packageName
     *            package name
     * @throws IOException
     */
    public void generateSubJavaClass(final File schemaPackageDir, final SchemaEnum schemaType, final Event event, final String packageName)
            throws IOException {
        final File eventBeanFile = new File(schemaPackageDir, event.getName() + JAVA_EXTENSION);

        final PrintWriter classWriter = new PrintWriter(new BufferedWriter(new FileWriter(eventBeanFile)));

        final STGroup stringTemplateGroup = getTemplateFile(SUB_EVENT_BEAN_STG_FILE);

        final ST stringTemplate = stringTemplateGroup.getInstanceOf("main");

        stringTemplate.add("schema", schemaPackageDir.getName());
        stringTemplate.add("schemaType", schemaType.value());
        stringTemplate.add("event", event.getName());
        stringTemplate.add("packageName", packageName);

        processSubClassEventParameter(event, stringTemplate);
        writeClassFile(stringTemplate, classWriter);
    }

    /**
     * @param event
     * @param stringTemplate
     */
    private static void processSubClassEventParameter(final Event event, final ST stringTemplate) {
        final List<EventParameter> eventParameterList = event.getParameterList();

        final Map<String, ArrayParameter> arrayParameters = new HashMap<String, ArrayParameter>();

        boolean isoffsetToUseNeed = false;
        String setOffSetToUse = null;

        String defaultValuesNeeded = null;

        for (final EventParameter parameter : eventParameterList) {

            final String parameterJavaType = TypeConversionParserTypesToJava.typeSqlToJava(parameter.getParserType());

            String[] arrayElements = null;

            if (parameter.isStructArray()) {
                if (!isoffsetToUseNeed) {
                    isoffsetToUseNeed = true;
                    setOffSetToUse = TRUE_FLAG;
                } else {
                    setOffSetToUse = null;
                }
                arrayElements = initializeArrayElement(parameter);
            }

            final String arrayParameterName = getArrayParameter(parameter);
            if (arrayParameterName != null) {
                ArrayParameter aParam = null;
                if (arrayParameters.containsKey(arrayParameterName)) {
                    aParam = arrayParameters.get(arrayParameterName);
                } else {
                    aParam = new ArrayParameter(arrayParameterName, parameterJavaType);
                    arrayParameters.put(arrayParameterName, aParam);
                }
                aParam.addParameter(parameter.getName());
            }
            if (null == defaultValuesNeeded && isAsn1Parameter(parameter)) {
                defaultValuesNeeded = TRUE_FLAG;
            }

            stringTemplate.addAggr("parameter.{type, name, arrayElements, isLastStructParam, isoffsetToUseNeed, dataMethod, bits, asn1Parameter}",
                    parameterJavaType, parameter.getName(), arrayElements, parameter.isStructLastParameter() ? TRUE_FLAG : null, setOffSetToUse,
                    getJavaDataMethod(parameter), getNumberOfBits(parameter), isAsn1Parameter(parameter) ? TRUE_FLAG : null);
        }

        processArrayParameters(stringTemplate, arrayParameters);
        stringTemplate.add("defaultValuesNeeded", defaultValuesNeeded);

    }

    private static void processArrayParameters(final ST stringTemplate, final Map<String, ArrayParameter> arrayParamMap) {
        for (final Entry<String, ArrayParameter> entry : arrayParamMap.entrySet()) {
            final String paramName = entry.getKey();
            final ArrayParameter arrayParameter = entry.getValue();
            final String paramType = arrayParameter.getType();
            final StringBuilder arrayItems = getStringArrayForArrayElements(arrayParameter.getParameterNames());
            stringTemplate.addAggr("arrayParameter.{name, type, arrayItems}", paramName, paramType, arrayItems);
        }
    }

    /**
     * @param parameter
     * @return
     */
    private static Integer getNumberOfBits(final EventParameter parameter) {
        return parameter.getType().equals(TypeConversionParamToParserTypes.P_IBCD) ? parameter.getNumberOfBits() : null;
    }

    /**
     * @param parameter
     * @return
     */
    private static boolean isTypeBinary(final EventParameter parameter) {
        return parameter.getParserType() == ParserTypes.BYTE_ARRAY;
    }

    /**
     * Method to return the initial value for an array parameter
     *
     * @param elementInitValue
     *            The initial value of an array element
     * @param maxArraySize
     *            The maximum number of elements in the array
     * @return the initial value as a string
     */
    private static String getParameterArrayInitValue(final String parameterInitValue, final int maxArraySize) {
        final StringBuilder builder = new StringBuilder();

        builder.append('{');

        for (int i = 0; i < maxArraySize; i++) {
            if (i > 0) {
                builder.append(',');
            }
            builder.append(parameterInitValue);
        }

        builder.append('}');

        return builder.toString();
    }

    protected static StringBuilder getStringArrayForArrayElements(final List<String> names) {
        final StringBuilder builder = new StringBuilder();
        for (final String name : names) {
            builder.append(name).append(", ");
        }
        if (builder.length() > 0) {
            builder.deleteCharAt(builder.length() - 2);
        }
        return builder;
    }

    /**
     * Method to return the getData method for a Parser type
     *
     * @param parameter
     *            The parameter to get the method for
     * @return the method as a string
     */
    public static String getJavaDataMethod(final EventParameter parameter) {
        final String sqlDataMethod = JavaDataTypeEnum.getValue(parameter.getParserType());
        if (sqlDataMethod != null) {
            return sqlDataMethod;
        }
        LOG.warn("parameter with incorrect type \"" + parameter.getParserType() + "\" found: " + parameter);
        return "";
    }

    /**
     * Method to return the initial value for a parameter
     *
     * @param parameter
     *            The parameter to get the method for
     * @return the initial value as a string
     */
    public static String getParameterInitValue(final EventParameter parameter) {
        final String paramInitValue = JavaDataTypeEnum.getInitValue(parameter.getParserType());
        if (paramInitValue != null) {
            return paramInitValue;
        }
        LOG.warn("parameter with incorrect type \"" + parameter.getParserType() + "\" found: " + parameter);
        return "";
    }

    /**
     * @param parameter
     * @return
     */
    protected static String[] initializeArrayElement(final EventParameter parameter) {
        final String[] arrayElements = new String[parameter.getMaxStructArraySize()];

        for (int i = 0; i < parameter.getMaxStructArraySize(); i++) {
            arrayElements[i] = new String(Integer.toString(i));
        }
        return arrayElements;
    }

    /**
     * @param parameter
     * @return
     */
    protected static String getArrayParameter(final EventParameter parameter) {
        final Matcher matcher = EVENT_ARRAY_PATTERN.matcher(parameter.getName());
        String paramName = parameter.getName();
        if (matcher.find()) {
            paramName = matcher.group(1);
            return paramName;
        }
        return null;
    }

    /**
     * check if it contains Asn1 part
     *
     * @param parameter
     * @return get method for asn1 parameter
     */
    protected static String getAsn1Param(final EventParameter parameter) {
        if (isAsn1Parameter(parameter)) {
            return "get" + parameter.getName() + "()";
        }
        return NULL_STRING;
    }

    private static boolean isAsn1Parameter(final EventParameter parameter) {
        return parameter.getName().endsWith("MESSAGE_CONTENTS");
    }
}

class ArrayParameter {

    private final String name;
    private final String type;

    final List<String> parameterNames = new ArrayList<>();

    public ArrayParameter(final String name, final String type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public List<String> getParameterNames() {
        return parameterNames;
    }

    public void addParameter(final String name) {
        this.parameterNames.add(name);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((parameterNames == null) ? 0 : parameterNames.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof ArrayParameter) {
            final ArrayParameter other = (ArrayParameter) obj;
            return isEqual(name, other.name) && isEqual(type, other.type);
        }
        return false;
    }

    private boolean isEqual(final Object objectA, final Object objectB) {
        if (objectA == null) {
            return objectB == null;
        }
        return objectA.equals(objectB);
    }

}
