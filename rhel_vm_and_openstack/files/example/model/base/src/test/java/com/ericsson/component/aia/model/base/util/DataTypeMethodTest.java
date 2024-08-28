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
import static org.mockito.Mockito.*;

import com.ericsson.component.aia.model.base.meta.schema.EventParameter;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class DataTypeMethodTest {
    private EventParameter mockedEventParameter;

    @Before
    public void setup() {
        mockedEventParameter = mock(EventParameter.class);
    }

    @Test
    @Parameters
    public void parameterInitValue(final String typeName, final String expectedResult, final int type) {
        when(mockedEventParameter.getParserType()).thenReturn(type);
        assertEquals(typeName + " should be getting '" + expectedResult + "'", expectedResult, JavaClassGenerator.getParameterInitValue(mockedEventParameter));
    }

    protected Object[] parametersForParameterInitValue() {
        return $($(JavaDataTypeEnum.STRING.toString(), "DEFAULT_STRING_VALUE", ParserTypes.STRING),
                $(JavaDataTypeEnum.BYTE.toString(), "DEFAULT_BYTE_VALUE", ParserTypes.BYTE),
                $(JavaDataTypeEnum.SHORT.toString(), "DEFAULT_SHORT_VALUE", ParserTypes.SHORT),
                $(JavaDataTypeEnum.INTEGER.toString(), "DEFAULT_INT_VALUE", ParserTypes.INTEGER),
                $(JavaDataTypeEnum.LONG.toString(), "DEFAULT_LONG_VALUE", ParserTypes.LONG),
                $(JavaDataTypeEnum.BYTE_ARRAY.toString(), "DEFAULT_BYTE_ARRAY_VALUE", ParserTypes.BYTE_ARRAY),
                $(JavaDataTypeEnum.BOOLEAN.toString(), "DEFAULT_BOOLEAN_VALUE", ParserTypes.BOOLEAN),
                $(JavaDataTypeEnum.FLOAT.toString(), "DEFAULT_FLOAT_VALUE", ParserTypes.FLOAT),
                $(JavaDataTypeEnum.DOUBLE.toString(), "DEFAULT_DOUBLE_VALUE", ParserTypes.DOUBLE),
                $(JavaDataTypeEnum.SHORT_ARRAY.toString(), "DEFAULT_SHORT_ARRAY_VALUE", ParserTypes.SHORT_ARRAY),
                $(JavaDataTypeEnum.IBCD.toString(), "DEFAULT_INT_VALUE", ParserTypes.IBCD),
                $(JavaDataTypeEnum.TBCD.toString(), "DEFAULT_STRING_VALUE", ParserTypes.TBCD),
                $(JavaDataTypeEnum.IPADDRESS.toString(), "DEFAULT_STRING_VALUE", ParserTypes.IPADDRESS),
                $(JavaDataTypeEnum.IPADDRESSV6.toString(), "DEFAULT_STRING_VALUE", ParserTypes.IPADDRESSV6),
                $(JavaDataTypeEnum.DNSNAME.toString(), "DEFAULT_STRING_VALUE", ParserTypes.DNSNAME),
                $(JavaDataTypeEnum.HEXSTRING.toString(), "DEFAULT_STRING_VALUE", ParserTypes.HEXSTRING),
                $(JavaDataTypeEnum.CCSTRING.toString(), "DEFAULT_STRING_VALUE", ParserTypes.CCSTRING));
    }
    @Test
    @Parameters
    public void assertJavaDataType(final String typeName, final String expectedResult, final int type) {
        when(mockedEventParameter.getParserType()).thenReturn(type);
        assertEquals(typeName + " should be getting '" + expectedResult + "' method", expectedResult, JavaClassGenerator.getJavaDataMethod(mockedEventParameter));
    }
    
    protected Object[] parametersForAssertJavaDataType() {
        return $($(JavaDataTypeEnum.STRING.toString(), "getString", ParserTypes.STRING),
                $(JavaDataTypeEnum.BYTE.toString(), "getByte", ParserTypes.BYTE),
                $(JavaDataTypeEnum.SHORT.toString(), "getShort", ParserTypes.SHORT),
                $(JavaDataTypeEnum.INTEGER.toString(), "getUnsignedIntegerAsInteger", ParserTypes.INTEGER),
                $(JavaDataTypeEnum.LONG.toString(), "getLong", ParserTypes.LONG),
                $(JavaDataTypeEnum.BYTE_ARRAY.toString(), "getByteArrayByteArray", ParserTypes.BYTE_ARRAY),
                $(JavaDataTypeEnum.BOOLEAN.toString(), "getByteArrayBoolean", ParserTypes.BOOLEAN),
                $(JavaDataTypeEnum.FLOAT.toString(), "getByteArrayFloat", ParserTypes.FLOAT),
                $(JavaDataTypeEnum.DOUBLE.toString(), "getByteArrayDouble", ParserTypes.DOUBLE),
                $(JavaDataTypeEnum.SHORT_ARRAY.toString(), "getShortArray", ParserTypes.SHORT_ARRAY),
                $(JavaDataTypeEnum.IBCD.toString(), "getByteArrayIBCD", ParserTypes.IBCD),
                $(JavaDataTypeEnum.TBCD.toString(), "getByteArrayTBCDString", ParserTypes.TBCD),
                $(JavaDataTypeEnum.IPADDRESS.toString(), "getByteArrayDottedDecimalString", ParserTypes.IPADDRESS),
                $(JavaDataTypeEnum.IPADDRESSV6.toString(), "getByteArrayIPV6String", ParserTypes.IPADDRESSV6),
                $(JavaDataTypeEnum.DNSNAME.toString(), "getByteArray3GPPString", ParserTypes.DNSNAME),
                $(JavaDataTypeEnum.HEXSTRING.toString(), "getByteArrayHexString", ParserTypes.HEXSTRING),
                $(JavaDataTypeEnum.CCSTRING.toString(), "getByteArrayCCString", ParserTypes.CCSTRING));
    }
    
    
}
