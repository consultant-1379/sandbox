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

public class DoubleParameterDecoderTest extends GenericEventParameterDecoderTest {

    @Test
    public void testDoubleParameterDecoderifUseValidFalse() {
        final double expectedvalue = 2.25;
        final byte[] data = getDoubleByteArray(expectedvalue);
        final GenericParameterDecoder decoder = getDoubleParameterDecoder(false, false, false, data.length);
        assertEquals(expectedvalue, decoder.getDecodeValue(data, 0));
    }

    @Test
    public void testDoubleParameterDecoderWithValidBit() {
        final double expectedvalue = 2.25;
        final byte[] data = getDoubleByteArray(expectedvalue);
        final byte[] validFlag = new byte[]{(byte) 0x00};
        final byte[] bytesWithValidtyByte = concatByteArrays(validFlag, data);
        final GenericParameterDecoder decoder = getDoubleParameterDecoder(true, false, false, data.length);
        assertEquals(expectedvalue, decoder.getDecodeValue(bytesWithValidtyByte, 0));
    }

    @Test
    public void testDoubleParameterDecoderWithValidBitFalse() {
        final double expectedvalue = 2.25;
        final byte[] data = getDoubleByteArray(expectedvalue);
        final byte[] inValidFlag = new byte[]{(byte) 0xff};
        final byte[] bytesWithValidtyByte = concatByteArrays(inValidFlag, data);
        final GenericParameterDecoder decoder = getDoubleParameterDecoder(true, false, false, data.length);
        assertNull(decoder.getDecodeValue(bytesWithValidtyByte, 0));
    }
    
    @Test
    public void testDoubleParameterDecoderWitOptionalBitFalse() {
        final double expectedvalue = 2.25;
        final byte[] data = getDoubleByteArray(expectedvalue);
        final byte[] inValidFlag = new byte[]{(byte) 0xff};
        final byte[] bytesWithValidtyByte = concatByteArrays(inValidFlag, data);
        final GenericParameterDecoder decoder = getDoubleParameterDecoder(false, true, false, data.length);
        assertNull(decoder.getDecodeValue(bytesWithValidtyByte, 0));
    }
    
    
    @Test
    public void testgetByteArrayDoubleWitOptionalBitTrue() {
        final double expectedvalue = 2.25;
        final byte[] data = getDoubleByteArray(expectedvalue);
        final byte[] validFlag = new byte[]{(byte) 0x00};
        final byte[] bytesWithValidtyByte = concatByteArrays(validFlag, data);
        final GenericParameterDecoder decoder = getDoubleParameterDecoder(false, true, false, data.length);
        assertEquals(expectedvalue, decoder.getDecodeValue(bytesWithValidtyByte, 0));
    }

    private GenericParameterDecoder getDoubleParameterDecoder(final boolean isUserValid, final boolean isOptional, final boolean isValideLteEmbeddedBitFlag, final int numberOfBytes) {
        final EventParameter eventParameter = mockedEventParameter(isUserValid, isOptional, isValideLteEmbeddedBitFlag, numberOfBytes);
        return new DoubleParameterDecoder(eventParameter);
    }
}
