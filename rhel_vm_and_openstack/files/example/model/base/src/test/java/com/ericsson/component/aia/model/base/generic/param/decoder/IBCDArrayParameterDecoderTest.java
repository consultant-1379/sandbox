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

public class IBCDArrayParameterDecoderTest extends GenericEventParameterDecoderTest {

    @Test
    public void testIBCDArrayParameterDecoderWithOutIsValidFlag() {
        final byte[] data = new byte[] { 3, 4, 0, 0, 0 };
        final byte[] concatedData = concatByteArrays(data, data, data);
        final GenericParameterDecoder decode = getIBCDArrayParameterDecoder(false, false, false, 2, 16, 1, 2, 3);
        assertArrayEquals(new Integer[] { 4030, 4030, 4030 }, (Integer[]) decode.getDecodeValue(concatedData, 0));
    }

    
    @Test
    public void testIBCDArrayParameterDecoderWithIsValidFlagAndFlagSetToInvalid() {
        final byte[] data = new byte[] {(byte) 0xff, 3, 4, 0, 0, 0 };
        final byte[] concatedData = concatByteArrays(data, data, data);
        final GenericParameterDecoder decode = getIBCDArrayParameterDecoder(true, false, false, 2, 16, 1, 2, 3);
        assertArrayEquals(new Integer[] { null, null, null }, (Integer[]) decode.getDecodeValue(concatedData, 0));
    }
    
    @Test
    public void testIBCDArrayParameterDecoderWithOptionalFlagAndFlagSetToInvalid() {
        final byte[] data = new byte[] {(byte) 0xff, 3, 4, 0, 0, 0 };
        final byte[] concatedData = concatByteArrays(data, data, data);
        final GenericParameterDecoder decode = getIBCDArrayParameterDecoder(true, false, false, 2, 16, 1, 2, 3);
        assertArrayEquals(new Integer[] { null, null, null }, (Integer[]) decode.getDecodeValue(concatedData, 0));
    }
    
    @Test
    public void testIBCDArrayParameterDecoderWithIsValidFlagAndFlagSetTovalid() {
        final byte[] data = new byte[] { 0x00, 3, 4, 0, 0, 0 };
        final byte[] concatedData = concatByteArrays(data, data, data);
        final GenericParameterDecoder decode = getIBCDArrayParameterDecoder(true, false, false, 2, 16, 1, 2, 3);
        assertArrayEquals(new Integer[] { 4030, 4030, 4030 }, (Integer[]) decode.getDecodeValue(concatedData, 0));
    }
    
    private GenericParameterDecoder getIBCDArrayParameterDecoder(final boolean isUserValid, final boolean isOptional, final boolean isValideLteEmbeddedBitFlag, final int numberOfBytes, final int numberOfBits, final int startSkip, final int endSkip, final int structArraySize) {
        final EventParameter eventParameter = mockedEventParameter(isUserValid, isOptional, isValideLteEmbeddedBitFlag, numberOfBytes, numberOfBits, startSkip, endSkip, structArraySize);
        return new IBCDArrayParameterDecoder(eventParameter);
    }
}
