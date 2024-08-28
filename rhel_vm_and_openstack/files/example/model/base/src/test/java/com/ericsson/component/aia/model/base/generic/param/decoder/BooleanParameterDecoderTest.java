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

public class BooleanParameterDecoderTest extends GenericEventParameterDecoderTest {

    @Test
    public void testBooleanParameterDecoderifUseValidFalse() {
        final byte[] data = new byte[] { 0, 0 };
        final GenericParameterDecoder decoder = getBooleanParameterDecoder(false, false, false, data.length - 1);
        assertTrue((boolean) decoder.getDecodeValue(data, 0));
    }

    @Test
    public void testBooleanParameterDecoderifUseValidTrueAndValidtyBitInValid() {
        final byte[] data = new byte[] { (byte) 0xff, 0 };
        final GenericParameterDecoder decoder = getBooleanParameterDecoder(true, false, false, data.length - 1);
        assertNull(decoder.getDecodeValue(data, 0));
    }

    @Test
    public void testStringParameterDecoderifUseValidTrueAndValidtyBitValid() {
        final byte[] data = new byte[] { (byte) 0x00, 1 };
        final GenericParameterDecoder decoder = getBooleanParameterDecoder(true, false, false, data.length - 1);
        assertFalse((boolean) decoder.getDecodeValue(data, 0));
    }

    @Test
    public void testStringParameterDecoderifOptionalTrueAndValidtyBitValid() {
        final byte[] data = new byte[] { (byte) 0x00, 1 };
        final GenericParameterDecoder decoder = getBooleanParameterDecoder(false, true, false, data.length - 1);
        assertFalse((boolean) decoder.getDecodeValue(data, 0));
    }
    
    private GenericParameterDecoder getBooleanParameterDecoder(final boolean isUserValid, final boolean isOptional, final boolean isValideLteEmbeddedBitFlag, final int numberOfBytes) {
        final EventParameter eventParameter = mockedEventParameter(isUserValid, isOptional, isValideLteEmbeddedBitFlag, numberOfBytes);
        return new BooleanParameterDecoder(eventParameter);
    }
}
