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

public class ByteParameterDecoderTest extends GenericEventParameterDecoderTest {
    @Test
    public void testByte_ValidityByteNotSet() {
        final GenericParameterDecoder decode = getByteParameterDecoder(false, false, false, 1);
        assertEquals((byte) 5, decode.getDecodeValue(new byte[] { 5 }, 0));
    }

    @Test
    public void testByte_ValidityByteSetToNotOK() {
        final GenericParameterDecoder decode = getByteParameterDecoder(true, false, false, 1);
        assertNull(decode.getDecodeValue(new byte[] { (byte) 0xff, 5 }, 0));
    }

    @Test
    public void testByte_ValidityByteSetToOK() {
        final GenericParameterDecoder decode = getByteParameterDecoder(true, false, false, 1);
        assertEquals((byte) 1, decode.getDecodeValue(new byte[] { (byte) 0x00, 1 }, 0));
    }

    private GenericParameterDecoder getByteParameterDecoder(final boolean isUserValid, final boolean isOptional, final boolean isValideLteEmbeddedBitFlag, final int numberOfBytes) {
        final EventParameter eventParameter = mockedEventParameter(isUserValid, isOptional, isValideLteEmbeddedBitFlag, numberOfBytes);
        return new ByteParameterDecoder(eventParameter);
    }
}
