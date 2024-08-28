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

public enum JavaDataTypeEnum {

    STRING(ParserTypes.STRING, "getString", "DEFAULT_STRING_VALUE"), BYTE(ParserTypes.BYTE, "getByte", "DEFAULT_BYTE_VALUE"), SHORT(
            ParserTypes.SHORT, "getShort", "DEFAULT_SHORT_VALUE"), INTEGER(ParserTypes.INTEGER,
            "getUnsignedIntegerAsInteger", "DEFAULT_INT_VALUE"), LONG(ParserTypes.LONG, "getLong", "DEFAULT_LONG_VALUE"), BYTE_ARRAY(
            ParserTypes.BYTE_ARRAY, "getByteArrayByteArray", "DEFAULT_BYTE_ARRAY_VALUE"), BOOLEAN(ParserTypes.BOOLEAN,
            "getByteArrayBoolean", "DEFAULT_BOOLEAN_VALUE"), FLOAT(ParserTypes.FLOAT, "getByteArrayFloat",
            "DEFAULT_FLOAT_VALUE"), DOUBLE(ParserTypes.DOUBLE, "getByteArrayDouble", "DEFAULT_DOUBLE_VALUE"), SHORT_ARRAY(
            ParserTypes.SHORT_ARRAY, "getShortArray", "DEFAULT_SHORT_ARRAY_VALUE"),  IBCD(
            ParserTypes.IBCD, "getByteArrayIBCD", "DEFAULT_INT_VALUE"), TBCD(ParserTypes.TBCD, "getByteArrayTBCDString", "DEFAULT_STRING_VALUE"), IPADDRESS(
            ParserTypes.IPADDRESS, "getByteArrayDottedDecimalString", "DEFAULT_STRING_VALUE"), IPADDRESSV6(
            ParserTypes.IPADDRESSV6, "getByteArrayIPV6String", "DEFAULT_STRING_VALUE"), DNSNAME(
            ParserTypes.DNSNAME, "getByteArray3GPPString", "DEFAULT_STRING_VALUE"), HEXSTRING(ParserTypes.HEXSTRING,
            "getByteArrayHexString", "DEFAULT_STRING_VALUE"), CCSTRING(ParserTypes.CCSTRING, "getByteArrayCCString",
            "DEFAULT_STRING_VALUE");
    
    private final int type;

    private final String value;

    private final String initVale;

    private JavaDataTypeEnum(final int type, final String value, final String initVale) {
        this.type = type;
        this.value = value;
        this.initVale = initVale;
    }

    public int getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public static String getValue(final int type) {
        for (final JavaDataTypeEnum schema : JavaDataTypeEnum.values()) {
            if (schema.type == type) {
                return schema.getValue();
            }
        }
        return null;
    }

    public static String getInitValue(final int type) {
        for (final JavaDataTypeEnum schema : JavaDataTypeEnum.values()) {
            if (schema.type == type) {
                return schema.getInitVale();
            }
        }
        return null;
    }

    public String getInitVale() {
        return initVale;
    }
}
