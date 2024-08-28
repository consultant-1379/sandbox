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

public class LongParameterDecoderTest extends GenericEventParameterDecoderTest {
    @Test
    public void testLongParameterDecoder() {
        final Long expectedvalue = Long.MAX_VALUE;
        final byte[] data = getLongByteArray(expectedvalue);
        final GenericParameterDecoder decode = getLongParameterDecoder(true, false, true, data.length);
        assertEquals(expectedvalue, decode.getDecodeValue(data, 0));
    }
    
    @Test
    public void testLongAWithValidBitFalse() {
        final byte[] data = getLongByteArray(Long.MAX_VALUE);
        final byte[] bytesWithValidtyByte = new byte[9];
        System.arraycopy(data, 0, bytesWithValidtyByte, 1, data.length);
        bytesWithValidtyByte[0] = (byte) 0xff;

        final GenericParameterDecoder decode = getLongParameterDecoder(true, false, true, data.length);
        assertNull(decode.getDecodeValue(bytesWithValidtyByte, 0));
    }

    @Test
    public void testLongAWithOptionalBitFalse() {
        final byte[] data = getLongByteArray(Long.MAX_VALUE);
        final byte[] bytesWithValidtyByte = new byte[9];
        System.arraycopy(data, 0, bytesWithValidtyByte, 1, data.length);
        bytesWithValidtyByte[0] = (byte) 0xff;
        final GenericParameterDecoder decode = getLongParameterDecoder(false, true, true, data.length);
        assertNull(decode.getDecodeValue(bytesWithValidtyByte, 0));
    }
    
    private GenericParameterDecoder getLongParameterDecoder(final boolean isUserValid, final boolean isOptional, final boolean isValideLteEmbeddedBitFlag, final int numberOfBytes) {
        final EventParameter eventParameter = mockedEventParameter(isUserValid, isOptional, isValideLteEmbeddedBitFlag, numberOfBytes);
        final GenericParameterDecoder decode = new LongParameterDecoder(eventParameter);
        return decode;
    }
}
