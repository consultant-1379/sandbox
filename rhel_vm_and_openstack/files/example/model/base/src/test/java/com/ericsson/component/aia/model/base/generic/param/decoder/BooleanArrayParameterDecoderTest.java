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

public class BooleanArrayParameterDecoderTest extends GenericEventParameterDecoderTest {

    @Test
    public void testBooleanArrayParameterDecoderifUseValidTrue() {
        final byte[] data = new byte[] { 0, 0 };
        final byte[] inValidFlag = new byte[] { (byte) 0xff };
        final byte[] concatData = concatByteArrays(data, inValidFlag, data, inValidFlag, data);
        final GenericParameterDecoder decode = getBooleanArrayParameterDecoder(false, false, false, data.length -1, 1, 1, 3);
        assertArrayEquals(new Boolean[] { true, true, true }, (Boolean[]) decode.getDecodeValue(concatData, 0));
    }

    @Test
    public void testBooleanParameterDecoderifUseValidTrueAndInValidFlag() {
        final byte[] data = new byte[] { (byte) 0xff, 0 };
        final byte[] inValidFlag = new byte[] { (byte) 0xff };
        final byte[] concatData = concatByteArrays(data, inValidFlag, data, inValidFlag, data);
        final GenericParameterDecoder decode = getBooleanArrayParameterDecoder(true, false, false, data.length -1, 0, 1, 3);
        assertArrayEquals(new Boolean[] { null, null, null }, (Boolean[]) decode.getDecodeValue(concatData, 0));
    }

    @Test
    public void testBooleanParameterDecoderifOtionalBitIsSetToTrueAndInValidFlag() {
        final byte[] data = new byte[] { (byte) 0xff, 0 };
        final byte[] inValidFlag = new byte[] { (byte) 0xff };
        final byte[] concatData = concatByteArrays(data, inValidFlag, data, inValidFlag, data);
        final GenericParameterDecoder decode = getBooleanArrayParameterDecoder(false, true, false, data.length -1, 0, 1, 3);
        assertArrayEquals(new Boolean[] { null, null, null }, (Boolean[]) decode.getDecodeValue(concatData, 0));
    }

    @Test
    public void testBooleanParameterDecoderifOtionalBitIsSetToTrueAndValidFlag() {
        final byte[] data = new byte[] { (byte) 0x00, 1 };
        final byte[] inValidFlag = new byte[] { (byte) 0xff };
        final byte[] concatData = concatByteArrays(data, inValidFlag, data, inValidFlag, data);
        final GenericParameterDecoder decode = getBooleanArrayParameterDecoder(true, false, false, data.length -1, 0, 1, 3);
        assertArrayEquals(new Boolean[] { false, false, false }, (Boolean[]) decode.getDecodeValue(concatData, 0));
    }
    
    private GenericParameterDecoder getBooleanArrayParameterDecoder(final boolean isUserValid, final boolean isOptional, final boolean isValideLteEmbeddedBitFlag, final int numberOfBytes, final int startSkip, final int endSkip, final int structArraySize) {
        final EventParameter eventParameter = mockedEventParameter(isUserValid, isOptional, isValideLteEmbeddedBitFlag, numberOfBytes, startSkip, endSkip, structArraySize);
        final GenericParameterDecoder decode = new BooleanArrayParameterDecoder(eventParameter);
        return decode;
    }
}
