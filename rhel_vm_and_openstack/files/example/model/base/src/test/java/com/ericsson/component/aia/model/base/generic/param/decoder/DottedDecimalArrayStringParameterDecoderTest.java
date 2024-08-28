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

public class DottedDecimalArrayStringParameterDecoderTest extends GenericEventParameterDecoderTest {
  
    @Test
    public void testDottedDecimalArrayStringifUseValidTrue() {
        final byte[] data = new byte[] {127, 0, 0, 1, 0 , 0};
        final byte[] concatData = concatByteArrays(data, data, data);
        final GenericParameterDecoder decode = getDottedDecimalArrayStringParameterDecoder(false, false, false, 4, 1, 1, 3);
        assertArrayEquals(new String[] { "127.0.0.1", "127.0.0.1", "127.0.0.1" }, (String[]) decode.getDecodeValue(concatData, 0));
    }
    
    @Test
    public void testDottedDecimalArrayStringifUseValidTrueAndInValidFlag() {
        final byte[] data = new byte[] { (byte) 0xff, 127, 0, 0, 1, 0, 0 };
        final byte[] concatData = concatByteArrays(data, data, data);
        final GenericParameterDecoder decode = getDottedDecimalArrayStringParameterDecoder(true, false, false, 4, 1, 1, 3);
        assertArrayEquals(new String[] { null, null, null }, (String[]) decode.getDecodeValue(concatData, 0));
    }
    
    @Test
    public void testDottedDecimalArrayStringifOtionalBitIsSetToTrueAndInValidFlag() {
        final byte[] data = new byte[] { (byte) 0xff, 127, 0, 0, 1, 0, 0 };
        final byte[] concatData = concatByteArrays(data, data, data);
        final GenericParameterDecoder decode = getDottedDecimalArrayStringParameterDecoder(false, true, false, 4, 1, 1, 3);
        assertArrayEquals(new String[] { null, null, null }, (String[]) decode.getDecodeValue(concatData, 0));
    }
    
    @Test
    public void testDottedDecimalArrayStringifOtionalBitIsSetToTrueAndValidFlag() {
        final byte[] data = new byte[] { 0x00, 127, 0, 0, 1, 0, 0 };
        final byte[] concatData = concatByteArrays(data, data, data);
        final GenericParameterDecoder decode = getDottedDecimalArrayStringParameterDecoder(false, true, false, 4, 1, 1, 3);
        assertArrayEquals(new String[] { "127.0.0.1", "127.0.0.1", "127.0.0.1" }, (String[]) decode.getDecodeValue(concatData, 0));
    }
    
    private GenericParameterDecoder getDottedDecimalArrayStringParameterDecoder(final boolean isUserValid, final boolean isOptional, final boolean isValideLteEmbeddedBitFlag, final int numberOfBytes, final int startSkip, final int endSkip, final int structArraySize) {
        final EventParameter eventParameter = mockedEventParameter(isUserValid, isOptional, isValideLteEmbeddedBitFlag, numberOfBytes, startSkip, endSkip, structArraySize);
        final GenericParameterDecoder decode = new DottedDecimalArrayStringParameterDecoder(eventParameter);
        return decode;
    }
}
