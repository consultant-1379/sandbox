/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2015
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.component.aia.model.base.generic.param.decoder;

import static org.junit.Assert.*;

import com.ericsson.component.aia.model.base.meta.schema.EventParameter;
import org.junit.Test;

public class StringArrayIPv6ParameterDecoderTest extends GenericEventParameterDecoderTest {

    @Test
    public void testStringArrayIPv6ParameterifUseValidTrue() {
        final byte[] data = new byte[] { 0x20, 0x01, 0x0d, (byte) 0xb8, (byte) 0x85, (byte) 0xa3, 0x00, 0x00, 0x00,
                0x00, (byte) 0x8a, 0x2e, 0x03, 0x70, 0x73, 0x34 };

        final byte[] padding = new byte[] { 0x00, 0x00, 0x00 };
        final byte[] concatData = concatByteArrays(data, padding, data, padding, data);
        final GenericParameterDecoder decode = getStringArrayIPv6ParameterDecoder(false, false, false, data.length,
                padding.length, 0, 3);
        assertArrayEquals(new String[] { "2001:db8:85a3:0:0:8a2e:370:7334", "2001:db8:85a3:0:0:8a2e:370:7334",
                "2001:db8:85a3:0:0:8a2e:370:7334" }, (String[]) decode.getDecodeValue(concatData, 0));
    }

    @Test
    public void testStringArrayIPv6ParameterDecoderifUseValidTrueAndInValidFlag() {
        final byte[] data = new byte[] { 0x20, 0x01, 0x0d, (byte) 0xb8, (byte) 0x85, (byte) 0xa3, 0x00, 0x00, 0x00,
                0x00, (byte) 0x8a, 0x2e, 0x03, 0x70, 0x73, 0x34 };
        final byte[] validityFlag = new byte[] { (byte) 0xff };
        final byte[] concatData = concatByteArrays(validityFlag, data, validityFlag, data, validityFlag, data);
        final GenericParameterDecoder decode = getStringArrayIPv6ParameterDecoder(true, false, false, data.length, 0,
                0, 3);
        assertArrayEquals(new String[] { null, null, null }, (String[]) decode.getDecodeValue(concatData, 0));
    }

    @Test
    public void testStringArrayIPv6ParameterDecoderifOtionalBitIsSetToTrueAndInValidFlag() {
        final byte[] data = new byte[] { 0x20, 0x01, 0x0d, (byte) 0xb8, (byte) 0x85, (byte) 0xa3, 0x00, 0x00, 0x00,
                0x00, (byte) 0x8a, 0x2e, 0x03, 0x70, 0x73, 0x34 };
        final byte[] validityFlag = new byte[] { (byte) 0xff };
        final byte[] concatData = concatByteArrays(validityFlag, data, validityFlag, data, validityFlag, data);
        final GenericParameterDecoder decode = getStringArrayIPv6ParameterDecoder(false, true, false, data.length, 0,
                0, 3);
        assertArrayEquals(new String[] { null, null, null }, (String[]) decode.getDecodeValue(concatData, 0));
    }

    @Test
    public void testStringArrayIPv6ParameterDecoderifOtionalBitIsSetToTrueAndValidFlag() {
        final byte[] data = new byte[] { 0x20, 0x01, 0x0d, (byte) 0xb8, (byte) 0x85, (byte) 0xa3, 0x00, 0x00, 0x00,
                0x00, (byte) 0x8a, 0x2e, 0x03, 0x70, 0x73, 0x34 };
        final byte[] validityFlag = new byte[] { 0x00 };
        final byte[] padding = new byte[] { 0x00, 0x00, 0x00 };
        final byte[] concatData = concatByteArrays(validityFlag, data, padding, validityFlag, data, padding,
                validityFlag, data);
        final GenericParameterDecoder decode = getStringArrayIPv6ParameterDecoder(false, true, false, data.length,
                padding.length, 0, 3);
        assertArrayEquals(new String[] { "2001:db8:85a3:0:0:8a2e:370:7334", "2001:db8:85a3:0:0:8a2e:370:7334",
                "2001:db8:85a3:0:0:8a2e:370:7334" }, (String[]) decode.getDecodeValue(concatData, 0));
    }

    private GenericParameterDecoder getStringArrayIPv6ParameterDecoder(final boolean isUserValid,
            final boolean isOptional, final boolean isValideLteEmbeddedBitFlag, final int numberOfBytes,
            final int startSkip, final int endSkip, final int structArraySize) {
        final EventParameter eventParameter = mockedEventParameter(isUserValid, isOptional, isValideLteEmbeddedBitFlag,
                numberOfBytes, startSkip, endSkip, structArraySize);
        final GenericParameterDecoder decode = new StringArrayIPv6ParameterDecoder(eventParameter);
        return decode;
    }
}
