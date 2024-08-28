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

public class DoubleArrayParameterDecoderTest extends GenericEventParameterDecoderTest {
    
    @Test
    public void testDoubleArrayParameterDecoderifUseValidFalse() {
        final double expectedvalue = 2.25;
        final byte[] data = getDoubleByteArray(expectedvalue);
        final byte[] validFlag = new byte[]{(byte) 0x00};
        final byte[] concatData = concatByteArrays(data, validFlag, validFlag, data, validFlag, validFlag, data, validFlag);
        final GenericParameterDecoder decoder = getDoubleArrayParameterDecoder(false, false, false, data.length, 1, 1, 3);
        assertArrayEquals(new Double[]{expectedvalue, expectedvalue, expectedvalue}, (Double[])decoder.getDecodeValue(concatData, 0));
    }

    @Test
    public void testDoubleParameterDecoderWithValidBit() {
        final double expectedvalue = 2.25;
        final byte[] data = getDoubleByteArray(expectedvalue);
        final byte[] validFlag = new byte[]{(byte) 0x00};
        final byte[] concatData = concatByteArrays( validFlag, data, validFlag, validFlag, validFlag, data, validFlag, validFlag, validFlag, data);
        final GenericParameterDecoder decoder = getDoubleArrayParameterDecoder(true, false, false, data.length, 1, 1, 3);
        assertArrayEquals(new Double[]{expectedvalue, expectedvalue, expectedvalue}, (Double[])decoder.getDecodeValue(concatData, 0));
    }

    @Test
    public void testDoubleParameterDecoderWithValidBitFalse() {
        final double expectedvalue = 2.25;
        final byte[] data = getDoubleByteArray(expectedvalue);
        final byte[] inValidFlag = new byte[]{(byte) 0xff};
        final byte[] concatData = concatByteArrays(inValidFlag, inValidFlag, data, inValidFlag, inValidFlag, inValidFlag, data, inValidFlag, inValidFlag, inValidFlag, data);
        final GenericParameterDecoder decoder = getDoubleArrayParameterDecoder(true, false, false, data.length, 1, 1, 3);
        assertArrayEquals(new Double[]{null, null, null}, (Double[])decoder.getDecodeValue(concatData, 0));
    }
    
    @Test
    public void testDoubleParameterDecoderWitOptionalBitFalse() {
        final double expectedvalue = 2.25;
        final byte[] data = getDoubleByteArray(expectedvalue);
        final byte[] inValidFlag = new byte[]{(byte) 0xff};
        final byte[] concatData = concatByteArrays(inValidFlag, inValidFlag, data, inValidFlag, inValidFlag, inValidFlag, data, inValidFlag, inValidFlag, inValidFlag, data);
        final GenericParameterDecoder decoder = getDoubleArrayParameterDecoder(false, true, false, data.length, 1, 1, 3);
        assertArrayEquals(new Double[]{null, null, null}, (Double[])decoder.getDecodeValue(concatData, 0));
    }
    
    
    @Test
    public void testgetByteArrayDoubleWitOptionalBitTrue() {
        final double expectedvalue = 2.25;
        final byte[] data = getDoubleByteArray(expectedvalue);
        final byte[] validFlag = new byte[]{(byte) 0x00};
        final byte[] concatData = concatByteArrays(validFlag, data, validFlag, validFlag,  validFlag, data, validFlag, validFlag, validFlag, data);
        final GenericParameterDecoder decoder = getDoubleArrayParameterDecoder(false, true, false, data.length, 1, 1, 3);
        assertArrayEquals(new Double[]{expectedvalue, expectedvalue, expectedvalue}, (Double[])decoder.getDecodeValue(concatData, 0));
    }
    
    private GenericParameterDecoder getDoubleArrayParameterDecoder(final boolean isUserValid, final boolean isOptional, final boolean isValideLteEmbeddedBitFlag, final int numberOfBytes, final int startSkip, final int endSkip, final int structArraySize) {
        final EventParameter eventParameter = mockedEventParameter(isUserValid, isOptional, isValideLteEmbeddedBitFlag, numberOfBytes, startSkip, endSkip, structArraySize);
        final GenericParameterDecoder decode = new DoubleArrayParameterDecoder(eventParameter);
        return decode;
    }
}
