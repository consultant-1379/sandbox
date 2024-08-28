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

import java.util.*;

/**
 * This class handles conversion from Parser Types to Java type
 */
public class TypeConversionParserTypesToJava {
    private static final Map<Integer, String> MAPSQLTOJAVA;
    static {
        final Map<Integer, String> typeMap = new HashMap<Integer, String>();
        typeMap.put(ParserTypes.LONG, "long");
        typeMap.put(ParserTypes.BYTE_ARRAY, "byte[]");
        typeMap.put(ParserTypes.BOOLEAN, "boolean");
        typeMap.put(ParserTypes.STRING, "String");
        typeMap.put(ParserTypes.DOUBLE, "double");
        typeMap.put(ParserTypes.FLOAT, "float");
        typeMap.put(ParserTypes.INTEGER, "int");
        typeMap.put(ParserTypes.SHORT, "short");
        typeMap.put(ParserTypes.LONG, "long");
        typeMap.put(ParserTypes.BYTE, "byte");
        typeMap.put(ParserTypes.SHORT, "short");
        typeMap.put(ParserTypes.SHORT_ARRAY, "short[]");
        typeMap.put(ParserTypes.IBCD, "int");
        typeMap.put(ParserTypes.TBCD, "String");
        typeMap.put(ParserTypes.IPADDRESS, "String");
        typeMap.put(ParserTypes.IPADDRESSV6, "String");
        typeMap.put(ParserTypes.DNSNAME, "String");
        typeMap.put(ParserTypes.HEXSTRING, "String");
        typeMap.put(ParserTypes.CCSTRING, "String");
        MAPSQLTOJAVA = Collections.unmodifiableMap(typeMap);

    }

    /**
     * Method to return the java type of a Parser's type; this method only converts types used in xStream
     * @param sqlType
     * @return
     */
    public static String typeSqlToJava(final int sqlType) {
        final String returnType = MAPSQLTOJAVA.get(sqlType);
        return (returnType!=null)? returnType : "null";
    }
}