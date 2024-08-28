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

public class StringArrayParameterDecoderTest extends GenericEventParameterDecoderTest {
   
    @Test
    public void testStringArrayParameterDecoderifUseValidTrue() {
        final byte[] data = getStringBytes((byte) 0);
        final byte[] padding = new byte[] { (byte) 0xff };
        final byte[] concatData = concatByteArrays(data, padding, padding, data, padding, padding, data);
        final GenericParameterDecoder decode = getStringArrayParameterDecoder(false, false, false, data.length, 1, 1, 3);
        assertArrayEquals(new String[] { WORD, WORD, WORD }, (String[]) decode.getDecodeValue(concatData, 0));
    }

    @Test
    public void testStringParameterDecoderifUseValidTrueAndInValidFlag() {
        final byte[] data = getStringBytes((byte) 0xff);
        final byte[] inValidFlag = new byte[] { (byte) 0xff };
        final byte[] concatData = concatByteArrays(data, inValidFlag, data, inValidFlag, data);
        final GenericParameterDecoder decode = getStringArrayParameterDecoder(true, false, false, data.length -1, 0, 1, 3);
        assertArrayEquals(new String[] { null, null, null }, (String[]) decode.getDecodeValue(concatData, 0));
    }

    @Test
    public void testStringParameterDecoderifOtionalBitIsSetToTrueAndInValidFlag() {
        final byte[] data = getStringBytes((byte) 0xff);
        final byte[] inValidFlag = new byte[] { (byte) 0xff };
        final byte[] concatData = concatByteArrays(data, inValidFlag, data, inValidFlag, data);
        final GenericParameterDecoder decode = getStringArrayParameterDecoder(false, true, false, data.length -1, 0, 1, 3);
        assertArrayEquals(new String[] { null, null, null }, (String[]) decode.getDecodeValue(concatData, 0));
    }

    @Test
    public void testStringParameterDecoderifOtionalBitIsSetToTrueAndValidFlag() {
        final byte[] data = getStringBytes((byte) 0);
        final byte[] inValidFlag = new byte[] { (byte) 0xff };
        final byte[] concatData = concatByteArrays(data, inValidFlag, data, inValidFlag, data);
        final GenericParameterDecoder decode = getStringArrayParameterDecoder(true, false, false, data.length -1, 0, 1, 3);
        assertArrayEquals(new String[] { WORD, WORD, WORD }, (String[]) decode.getDecodeValue(concatData, 0));
    }
    
    private GenericParameterDecoder getStringArrayParameterDecoder(final boolean isUserValid, final boolean isOptional, final boolean isValideLteEmbeddedBitFlag, final int numberOfBytes, final int startSkip, final int endSkip, final int structArraySize) {
        final EventParameter eventParameter = mockedEventParameter(isUserValid, isOptional, isValideLteEmbeddedBitFlag, numberOfBytes, startSkip, endSkip, structArraySize);
        final GenericParameterDecoder decode = new StringArrayParameterDecoder(eventParameter);
        return decode;
    }
}
