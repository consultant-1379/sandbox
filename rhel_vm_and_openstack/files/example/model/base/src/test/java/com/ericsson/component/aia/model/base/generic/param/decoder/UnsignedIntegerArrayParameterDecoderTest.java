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

import org.junit.Test;

import com.ericsson.component.aia.model.base.meta.schema.EventParameter;

public class UnsignedIntegerArrayParameterDecoderTest extends GenericEventParameterDecoderTest {
    
    @Test
    public void testGetArrayUnsignedIntegerAsInteger() {
        final byte[] padding = new byte[] { (byte) 0xff };
        final byte[] data = concatByteArrays(getIntegerByteArray(1), padding, padding, padding, getIntegerByteArray(2), padding, padding, padding, getIntegerByteArray(3));
        final GenericParameterDecoder decode = getUnsignedIntegerArrayParameterDecoder(false, false, false, 4, 3, 0, 3);
        assertArrayEquals(new Integer[] { 1, 2, 3 }, (Integer[]) decode.getDecodeValue(data, 0));
    }
    
    @Test
    public void testGetArrayUnsignedIntegerAsIntegerWithIsUseValidSetToTrue() {
        final byte[] padding = new byte[] { (byte) 0xff };
        final byte[] data = concatByteArrays(getIntegerByteArray(1), padding, padding, getIntegerByteArray(2), padding, padding, getIntegerByteArray(3));
        final GenericParameterDecoder decode = getUnsignedIntegerArrayParameterDecoder(true, false, false, 3, 1, 1, 3);
        assertArrayEquals(new Integer[] { 1, 2, 3 }, (Integer[]) decode.getDecodeValue(data, 0));
    }
    
    @Test
    public void testGetArrayUnsignedIntegerAsIntegerWithIsUseValidSetToTrueAndInValidValue() {
        final byte[] inValidFlag = new byte[]{(byte) 0xff};
        final byte[] data = concatByteArrays(inValidFlag, getIntegerByteArray(1), inValidFlag, getIntegerByteArray(2), inValidFlag, getIntegerByteArray(3));
        final GenericParameterDecoder decode = getUnsignedIntegerArrayParameterDecoder(true, false, false, 4, 0, 0, 3);
        assertArrayEquals(new Integer[] { null, null, null }, (Integer[]) decode.getDecodeValue(data, 0));
    }
    
    @Test
    public void testGetArrayUnsignedIntegerAsIntegerWithOptionalBitSetToTrueAndInValidValue() {
        final byte[] inValidFlag = new byte[]{(byte) 0xff};
        final byte[] data = concatByteArrays(inValidFlag, getIntegerByteArray(1), inValidFlag, getIntegerByteArray(2), inValidFlag, getIntegerByteArray(3));
        final GenericParameterDecoder decode = getUnsignedIntegerArrayParameterDecoder(false, true, false, 3, 0, 1, 3);
        assertArrayEquals(new Integer[] { null, null, null }, (Integer[]) decode.getDecodeValue(data, 0));
    }

    private GenericParameterDecoder getUnsignedIntegerArrayParameterDecoder(final boolean isUserValid, final boolean isOptional, final boolean isValideLteEmbeddedBitFlag, final int numberOfBytes, final int startSkip, final int endSkip, final int structArraySize) {
        final EventParameter eventParameter = mockedEventParameter(isUserValid, isOptional, isValideLteEmbeddedBitFlag, numberOfBytes, startSkip, endSkip, structArraySize);
        final GenericParameterDecoder decode = new UnsignedIntegerArrayParameterDecoder(eventParameter);
        return decode;
    }
}
