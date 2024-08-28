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
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import org.junit.Test;

import com.ericsson.component.aia.model.base.meta.schema.EventParameter;

public class CellTraceAsn1ContentByteArrayParameterDecoderTest extends GenericEventParameterDecoderTest {

    @Test
    public void testDecodeValueWithLengthInLast2Bytes() {
        final byte[] expected = new byte[] { 68, -16, 0, -128, 0, 3 };
        final byte[] byteLength = new byte[] { 0x00, (byte) expected.length };
        final byte[] data = concatByteArrays(byteLength, expected);
        final EventParameter eventParameter = getEventParameter(false, false, false, -1);
        final GenericParameterDecoder decode = getByteArrayByteArrayParameterDecoder(eventParameter);
        assertArrayEquals(new Byte[] { 68, -16, 0, -128, 0, 3 }, (Byte[]) decode.getDecodeValue(data, byteLength.length));
        verify(eventParameter).setNumberOfBytes(eq(expected.length));
    }

    @Test
    public void testDecodeValueWithInValidOptionalflag() {
        final byte[] expected = new byte[] { (byte) 0xff, 68, -16, 0, -128, 0, 3 };
        final EventParameter eventParameter = getEventParameter(false, true, false, expected.length);
        final GenericParameterDecoder decode = getByteArrayByteArrayParameterDecoder(eventParameter);
        assertNull(decode.getDecodeValue(expected, 0));
        verify(eventParameter).setNumberOfBytes(eq(0));
    }

    @Test
    public void testDecodeValueWithLengthInLast2BytesAndLengthIsZero() {
        final byte[] expected = new byte[] {};
        final byte[] byteLength = new byte[] { 0x00, 0x00 };
        final byte[] data = concatByteArrays(byteLength, expected);
        final EventParameter eventParameter = getEventParameter(false, false, false, -1);
        final GenericParameterDecoder decode = getByteArrayByteArrayParameterDecoder(eventParameter);
        assertArrayEquals(new Byte[] {}, (Byte[]) decode.getDecodeValue(data, byteLength.length));
        verify(eventParameter).setNumberOfBytes(eq(expected.length));
    }

    @Test
    public void testDecodeValueWithLengthInLast2BytesWithInValidValidtyByte() {
        final byte[] expected = new byte[] { 68, -16, 0, -128, 0, 3 };
        final byte[] lengthWithInValidFlag = new byte[] { (byte) 0xff, (byte) expected.length };
        final byte[] data = concatByteArrays(lengthWithInValidFlag, expected);
        final EventParameter eventParameter = getEventParameter(false, false, false, -1);
        final GenericParameterDecoder decode = getByteArrayByteArrayParameterDecoder(eventParameter);
        assertNull(decode.getDecodeValue(data, lengthWithInValidFlag.length));
        verify(eventParameter).setNumberOfBytes(eq(0));
    }

    private GenericParameterDecoder getByteArrayByteArrayParameterDecoder(final EventParameter eventParameter) {
        return new CellTraceAsn1ContentByteArrayParameterDecoder(eventParameter);
    }

    private EventParameter getEventParameter(final boolean isUserValid, final boolean isOptional, final boolean isValideLteEmbeddedBitFlag,
                                             final int numberOfBytes) {
        final EventParameter eventParameter = mockedEventParameter(isUserValid, isOptional, isValideLteEmbeddedBitFlag, numberOfBytes);
        return eventParameter;
    }
}
