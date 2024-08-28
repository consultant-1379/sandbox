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

public class DottedDecimalStringParameterDecoderTest extends GenericEventParameterDecoderTest {
    @Test
    public void testByteArrayDottedDecimalStringifUseValidTrue() {
        final byte[] data = new byte[] { 0, 127, 0, 0, 1 };
        final GenericParameterDecoder decoder = getDottedDecimalStringParameterDecoder(true, false, false, data.length - 1);
        assertEquals("127.0.0.1", decoder.getDecodeValue(data, 0));
    }
    
    @Test
    public void testByteArrayDottedDecimalStringifUseValidTrueAndInValidFlag() {
        final byte[] data = new byte[] { (byte) 0xff, 127, 0, 0, 1 };
        final GenericParameterDecoder decoder = getDottedDecimalStringParameterDecoder(true, false, false, data.length - 1);
        assertNull(decoder.getDecodeValue(data, 0));
    }
    
    @Test
    public void testByteArrayDottedDecimalStringifOtionalBitIsSetToTrueAndInValidFlag() {
        final byte[] data = new byte[] { (byte) 0xff, 127, 0, 0, 1 };
        final GenericParameterDecoder decoder = getDottedDecimalStringParameterDecoder(false, true, false, data.length - 1);
        assertNull(decoder.getDecodeValue(data, 0));
    }
    
    private GenericParameterDecoder getDottedDecimalStringParameterDecoder(final boolean isUserValid, final boolean isOptional, final boolean isValideLteEmbeddedBitFlag, final int numberOfBytes) {
        final EventParameter eventParameter = mockedEventParameter(isUserValid, isOptional, isValideLteEmbeddedBitFlag, numberOfBytes);
        return new DottedDecimalStringParameterDecoder(eventParameter);
    }
}
