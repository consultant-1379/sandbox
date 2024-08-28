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
package com.ericsson.component.aia.model.generation.avro.json;

import java.util.*;

import com.ericsson.component.aia.model.base.util.ParserTypes;

/**
 * This class handles conversion from Parser Types to Java type
 */
public class TypeConversionParserTypesToJson {
    private static final Map<Integer, String> MAPSQLTOJAVA;
    static {
        final Map<Integer, String> typeMap = new HashMap<Integer, String>();
        typeMap.put(ParserTypes.LONG, "long");
        typeMap.put(ParserTypes.BYTE_ARRAY, "bytes");
        typeMap.put(ParserTypes.BOOLEAN, "boolean");
        typeMap.put(ParserTypes.STRING, "string");
        typeMap.put(ParserTypes.DOUBLE, "double");
        typeMap.put(ParserTypes.FLOAT, "float");
        typeMap.put(ParserTypes.INTEGER, "int");
        typeMap.put(ParserTypes.SHORT, "short");
        typeMap.put(ParserTypes.LONG, "long");
        typeMap.put(ParserTypes.BYTE, "int");
        typeMap.put(ParserTypes.SHORT, "short");
        typeMap.put(ParserTypes.SHORT_ARRAY, "bytes");
        typeMap.put(ParserTypes.IBCD, "int");
        typeMap.put(ParserTypes.TBCD, "string");
        typeMap.put(ParserTypes.IPADDRESS, "string");
        typeMap.put(ParserTypes.IPADDRESSV6, "string");
        typeMap.put(ParserTypes.DNSNAME, "string");
        typeMap.put(ParserTypes.HEXSTRING, "string");
        typeMap.put(ParserTypes.CCSTRING, "string");
        MAPSQLTOJAVA = Collections.unmodifiableMap(typeMap);

    }

    /**
     * Method to return the java type of a Parser's type; this method only converts types used in xStream
     * 
     * @param sqlType
     * @return
     */
    public static String typeSqlToJava(final int sqlType) {
        final String returnType = MAPSQLTOJAVA.get(sqlType);
        return (returnType != null) ? returnType : "null";
    }
}