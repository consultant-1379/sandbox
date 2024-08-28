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

public class LongArrayParameterDecoderTest extends GenericEventParameterDecoderTest {
    
    @Test
    public void testArrayLongParameterDecoder() {
        final byte[] padding = new byte[] { 0x00 };
        final byte[] data = concatByteArrays(getLongByteArray(111_111_111), padding, getLongByteArray(222_222_222), padding, getLongByteArray(333_333_333), padding);
        final GenericParameterDecoder decode = getLongArrayParameterDecoder(false, false, false, 8, 0, 1, 3);
        assertArrayEquals(new Long[] { (long) 111_111_111, (long) 222_222_222, (long) 333_333_333 }, (Long[]) decode.getDecodeValue(data, 0));
    }
    
    @Test
    public void testArrayLongParameterDecoderWithValidtyBitSetToTrueAndInValidFlag() {
        final byte[] inValidFlag = new byte[]{(byte) 0xff};
        final byte[] validFlag = new byte[] { 0 };
        final byte[] data = concatByteArrays(inValidFlag, getLongByteArray(111_111_111), validFlag, inValidFlag, getLongByteArray(222_222_222), validFlag, inValidFlag, getLongByteArray(333_333_333), validFlag);
        final GenericParameterDecoder decode = getLongArrayParameterDecoder(true, false, false, 8, 0, 1, 3);
        assertArrayEquals(new Long[] {null, null, null}, (Long[]) decode.getDecodeValue(data, 0));
    }
    
    @Test
    public void testArrayLongParameterDecoderWithValidtyBitSetToTrueAndValidFlag() {
        final byte[] validFlag = new byte[] { 0 };
        final byte[] data = concatByteArrays(validFlag, getLongByteArray(111_111_111), validFlag, validFlag, getLongByteArray(222_222_222), validFlag, validFlag, getLongByteArray(333_333_333), validFlag);
        final GenericParameterDecoder decode = getLongArrayParameterDecoder(true, false, false, 8, 0, 1, 3);
        assertArrayEquals(new Long[] { (long) 111_111_111, (long) 222_222_222, (long) 333_333_333 }, (Long[]) decode.getDecodeValue(data, 0));
    }
    
    @Test
    public void testArrayLongParameterDecoderWitOptionalBitSetToTrueAndValidFlag() {
        final byte[] validFlag = new byte[] { 0 };
        final byte[] data = concatByteArrays(validFlag, getLongByteArray(111_111_111), validFlag, validFlag, getLongByteArray(222_222_222), validFlag, validFlag, getLongByteArray(333_333_333), validFlag);
        final GenericParameterDecoder decode = getLongArrayParameterDecoder(false, true, false, 8, 0, 1, 3);
        assertArrayEquals(new Long[] { (long) 111_111_111, (long) 222_222_222, (long) 333_333_333 }, (Long[]) decode.getDecodeValue(data, 0));
    }
    
    private GenericParameterDecoder getLongArrayParameterDecoder(final boolean isUserValid, final boolean isOptional, final boolean isValideLteEmbeddedBitFlag, final int numberOfBytes, final int startSkip, final int endSkip, final int structArraySize) {
        final EventParameter eventParameter = mockedEventParameter(isUserValid, isOptional, isValideLteEmbeddedBitFlag, numberOfBytes, startSkip, endSkip, structArraySize);
        final GenericParameterDecoder decode = new LongArrayParameterDecoder(eventParameter);
        return decode;
    }
}
