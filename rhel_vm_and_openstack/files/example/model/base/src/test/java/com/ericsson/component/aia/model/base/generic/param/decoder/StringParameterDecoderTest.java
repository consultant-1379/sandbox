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

public class StringParameterDecoderTest extends GenericEventParameterDecoderTest {
  
    @Test
    public void testStringParameterDecoderifUseValidFalse() {
        final byte[] data = getStringBytes((byte) 0);
        final GenericParameterDecoder decoder = getStringParameterDecoder(false, false, false, data.length - 1);
        assertEquals(WORD, decoder.getDecodeValue(data, 1));
    }

    @Test
    public void testStringParameterDecoderifUseValidTrueAndValidtyBitInValid() {
        final byte[] data = getStringBytes((byte) 0xff);
        final GenericParameterDecoder decoder = getStringParameterDecoder(true, false, false, data.length);
        assertNull(decoder.getDecodeValue(data, 0));
    }

    @Test
    public void testStringParameterDecoderifUseValidTrueAndValidtyBitValid() {
        final byte[] data = getStringBytes((byte) 0);
        final GenericParameterDecoder decoder = getStringParameterDecoder(true, false, false, data.length - 1);
        assertEquals(WORD, decoder.getDecodeValue(data, 0));
    }
    
    @Test
    public void testStringParameterDecoderifOptionalTrueAndValidtyBitValid() {
        final byte[] data = getStringBytes((byte) 0);
        final GenericParameterDecoder decoder = getStringParameterDecoder(false, true, false, data.length - 1);
        assertEquals(WORD, decoder.getDecodeValue(data, 0));
    }
    
    private GenericParameterDecoder getStringParameterDecoder(final boolean isUserValid, final boolean isOptional, final boolean isValideLteEmbeddedBitFlag, final int numberOfBytes) {
        final EventParameter eventParameter = mockedEventParameter(isUserValid, isOptional, isValideLteEmbeddedBitFlag, numberOfBytes);
        final GenericParameterDecoder decode = new StringParameterDecoder(eventParameter);
        return decode;
    }
}