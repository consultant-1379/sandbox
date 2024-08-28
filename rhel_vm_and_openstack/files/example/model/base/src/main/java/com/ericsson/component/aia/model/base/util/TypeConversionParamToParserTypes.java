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

import static com.ericsson.component.aia.model.base.util.ParserTypes.*;

import java.util.*;
import com.ericsson.component.aia.model.base.exception.SchemaException;

/**
 * This class handles conversion to Parser's Types
 */
public class TypeConversionParamToParserTypes {
    
    private static final String P_LONG = "LONG";

    private static final String UINT = "UINT";

    private static final String ENUM = "ENUM";

    public static final String P_IBCD = "IBCD";

    private static final Map<String, Integer> MAP_PARAM_TO_JAVA_TYPES;
    static {
        final Map<String, Integer> typeMap = new HashMap<String, Integer>();
        typeMap.put("BIGINT", LONG);
        typeMap.put("BINARY", BYTE_ARRAY);
        typeMap.put("BYTEARRAY", BYTE_ARRAY);
        typeMap.put("FROREF", BYTE_ARRAY);
        typeMap.put("BOOLEAN", BOOLEAN);
        typeMap.put("DNSNAME", DNSNAME);
        typeMap.put("STRING", STRING);
        typeMap.put("CCSTRING", CCSTRING);
        typeMap.put("HEXSTRING", HEXSTRING);
        typeMap.put("IPADDRESS", IPADDRESS);
        typeMap.put("IPADDRESSV6", IPADDRESSV6);
        typeMap.put("TBCD", TBCD);
        typeMap.put("DOUBLE", DOUBLE);
        typeMap.put("FLOAT", FLOAT);
        typeMap.put("TIMESTAMP", LONG);
        typeMap.put("SHORTARRAY", SHORT_ARRAY);
        typeMap.put("SHORT", SHORT);
        MAP_PARAM_TO_JAVA_TYPES = Collections.unmodifiableMap(typeMap);

    }

    /**
     * Convert an Event Parameter type to Parser's Types
     * 
     * @param parameterType
     * @return The parameter type as Parser's Types
     * @throws SchemaException
     */
    public static int typeEventParameter2ParserTypes(final String parameterType, final int numberOfBytes) {

        if(P_IBCD.equals(parameterType)){
            return IBCD;
        }
        
        if (ENUM.equals(parameterType) || UINT.equals(parameterType) || P_LONG.equals(parameterType)){
           return  numericTypeConversions(numberOfBytes);
        }

        return getFromMap(parameterType);
    }

    /**
     * @param parameterType
     * @return
     */
    private static int getFromMap(final String parameterType) {
        final Integer returnType = MAP_PARAM_TO_JAVA_TYPES.get(parameterType);
        return (returnType  != null)? returnType : OTHER;
    }

    /**
     * @param numberOfBytes
     * @return
     */
    private static int numericTypeConversions(final int numberOfBytes) {
        if (isByte(numberOfBytes)) {
            return BYTE;
        } else if (isUnassignedInteger(numberOfBytes)) {
            return INTEGER;
        } else if (isLong(numberOfBytes)) {
            return LONG;
        }
        return OTHER;
    }

    /**
     * @param numberOfBytes
     * @return
     */
    private static boolean isLong(final int numberOfBytes) {
        return numberOfBytes >= 3 & numberOfBytes <= 8;
    }

    /**
     * @param numberOfBytes
     * @return
     */
    private static boolean isUnassignedInteger(final int numberOfBytes) {
        return numberOfBytes == 2;
    }

    /**
     * @param numberOfBytes
     * @return
     */
    private static boolean isByte(final int numberOfBytes) {
        return numberOfBytes >= 0 & numberOfBytes <= 1;
    }
}