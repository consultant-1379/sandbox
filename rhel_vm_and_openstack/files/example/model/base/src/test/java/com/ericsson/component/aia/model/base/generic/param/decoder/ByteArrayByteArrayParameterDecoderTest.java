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

public class ByteArrayByteArrayParameterDecoderTest extends GenericEventParameterDecoderTest {
    
    @Test
    public void testByteArrayByteArrayParameterDecoder() {
        final byte[] expected = new byte[] { 68, -16, 0, -128, 0, 3 };
        final GenericParameterDecoder decode = getByteArrayByteArrayParameterDecoder(false, false, false, expected.length);
        assertArrayEquals(new Byte[] { 68, -16, 0, -128, 0, 3 }, (Byte[])decode.getDecodeValue(expected, 0));
    }
    
    @Test
    public void testByteArrayByteArrayParameterDecoderWithInValidOptionalflag() {
        final byte[] expected = new byte[] { (byte) 0xff, 68, -16, 0, -128, 0, 3 };
        final GenericParameterDecoder decode = getByteArrayByteArrayParameterDecoder(false, true, false, expected.length);
        assertNull(decode.getDecodeValue(expected, 0));
    }

    @Test
    public void testByteArrayByteArrayParameterDecoderWithValidOptionalflag() {
        final byte[] expected = new byte[] { (byte) 0x00, 68, -16, 0, -128, 0, 3 };
        final GenericParameterDecoder decode = getByteArrayByteArrayParameterDecoder(false, true, false, expected.length -1 );
        assertArrayEquals(new Byte[] { 68, -16, 0, -128, 0, 3 }, (Byte[]) decode.getDecodeValue(expected, 0));
    }
    
    @Test
    public void testByteArrayByteArrayWithLengthInLast2Bytes() {
        final byte[] expected = new byte[] { 68, -16, 0, -128, 0, 3 };
        final byte[] byteLength = new byte[] { 0, (byte) expected.length };
        final byte[] data = concatByteArrays(byteLength, expected);
        final GenericParameterDecoder decode = getByteArrayByteArrayParameterDecoder(false, false, false, -1);
        assertArrayEquals(new Byte[] { 68, -16, 0, -128, 0, 3 }, (Byte[])decode.getDecodeValue(data, byteLength.length));
    }
    
    @Test
    public void testByteArrayByteArrayWithLengthInLast2BytesAndLengthIsZero() {
        final byte[] expected = new byte[] {};
        final byte[] byteLength = new byte[] { 0, 0 };
        final byte[] data = concatByteArrays(byteLength, expected);
        final GenericParameterDecoder decode = getByteArrayByteArrayParameterDecoder(false, false, false, -1);
        assertArrayEquals(new Byte[] {}, (Byte[])decode.getDecodeValue(data, byteLength.length));
    }
    
    @Test
    public void testByteArrayByteArrayWithLengthInLast2BytesWithInValidValidtyByte() {
        final byte[] expected = new byte[] { 68, -16, 0, -128, 0, 3 };
        final byte[] byteLength = new byte[] { 0, (byte) expected.length };
        final byte[] inValidFlag = new byte[] { (byte) 0xff };
        final byte[] data = concatByteArrays(inValidFlag, byteLength, expected);
        final GenericParameterDecoder decode = getByteArrayByteArrayParameterDecoder(true, false, false, -1);
        assertNull(decode.getDecodeValue(data, 0));
    }
    
    private GenericParameterDecoder getByteArrayByteArrayParameterDecoder(final boolean isUserValid, final boolean isOptional, final boolean isValideLteEmbeddedBitFlag, final int numberOfBytes) {
        final EventParameter eventParameter = mockedEventParameter(isUserValid, isOptional, isValideLteEmbeddedBitFlag, numberOfBytes);
        return new ByteArrayByteArrayParameterDecoder(eventParameter);
    }
}
