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

import static junitparams.JUnitParamsRunner.*;
import static org.junit.Assert.*;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class TypeConversionTest {

    @Test
    @Parameters
    public void typeEventParameter2Sql(final String parameterType, final Integer numberOfBytes, final int expectedType) {
        assertEquals(expectedType, TypeConversionParamToParserTypes.typeEventParameter2ParserTypes(parameterType, numberOfBytes));
    }

    @Test
    @Parameters
    public void typeSqlToJava(final int sqlType, final String expectedType) {
        assertEquals(expectedType, TypeConversionParserTypesToJava.typeSqlToJava(sqlType));
    }

    protected Object[] parametersForTypeEventParameter2Sql() {
        return $($("SomeUnkownType", 0, ParserTypes.OTHER), $("DNSNAME", 0, ParserTypes.DNSNAME), $("STRING", 0, ParserTypes.STRING),
                $("CCSTRING", 0, ParserTypes.CCSTRING), $("TBCD", 0, ParserTypes.TBCD), $("HEXSTRING", 0, ParserTypes.HEXSTRING),
                $("IPADDRESS", 0, ParserTypes.IPADDRESS), $("IPADDRESSV6", 0, ParserTypes.IPADDRESSV6), $("BINARY", 0, ParserTypes.BYTE_ARRAY),
                $("BYTEARRAY", 0, ParserTypes.BYTE_ARRAY), $("FROREF", 0, ParserTypes.BYTE_ARRAY), $("TIMESTAMP", 0, ParserTypes.LONG),
                $("BOOLEAN", 0, ParserTypes.BOOLEAN), $("FLOAT", 0, ParserTypes.FLOAT), $("DOUBLE", 0, ParserTypes.DOUBLE), $("DNSNAME", 0, ParserTypes.DNSNAME),
                $("ENUM", 0, ParserTypes.BYTE), $("ENUM", 1, ParserTypes.BYTE), $("ENUM", 2, ParserTypes.INTEGER), $("ENUM", 3, ParserTypes.LONG),
                $("ENUM", 8, ParserTypes.LONG), $("ENUM", 9, ParserTypes.OTHER), $("IBCD", 0, ParserTypes.IBCD), $("UINT", 0, ParserTypes.BYTE),
                $("LONG", 0, ParserTypes.BYTE), $("SHORT", 2006, ParserTypes.SHORT), $("SHORTARRAY", 2007, ParserTypes.SHORT_ARRAY));
    }

    protected Object[] parametersForTypeSqlToJava() {
        return $($(ParserTypes.STRING, "String"), $(ParserTypes.BYTE, "byte"), $(ParserTypes.SHORT, "short"), $(ParserTypes.INTEGER, "int"),
                $(ParserTypes.LONG, "long"), $(ParserTypes.BYTE, "byte"), $(ParserTypes.BYTE_ARRAY, "byte[]"), $(ParserTypes.LONG, "long"), $(ParserTypes.BOOLEAN, "boolean"),
                $(ParserTypes.FLOAT, "float"), $(ParserTypes.DOUBLE, "double"), $(Integer.MAX_VALUE, "null"), $(ParserTypes.SHORT, "short"),
                $(ParserTypes.SHORT_ARRAY, "short[]"));
    }

}
