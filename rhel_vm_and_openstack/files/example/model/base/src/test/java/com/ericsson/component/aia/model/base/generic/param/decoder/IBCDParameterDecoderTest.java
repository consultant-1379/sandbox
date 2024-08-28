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

public class IBCDParameterDecoderTest extends GenericEventParameterDecoderTest {
    
    @Test
    public void testByteArrayIBCD() {
        final byte[] data = new byte[] { 3, 4 };
        final GenericParameterDecoder decode = getIBCDParameterDecoder(false, true, true, data.length, 16);
        assertEquals(4030, decode.getDecodeValue(data, 0));
    }

    @Test
    public void testByteArrayIBCDWithInValidFlag() {
        final byte[] data = new byte[] { -128, 3, 4 };
        final GenericParameterDecoder decode = getIBCDParameterDecoder(true, false, false, data.length - 1, 16);
        assertNull(decode.getDecodeValue(data, 0));
    }

    @Test
    public void testByteArrayIBCDWithValidFlag() {
        final byte[] data = new byte[] { 0, 3, 4 };
        final GenericParameterDecoder decode = getIBCDParameterDecoder(true, false, false, data.length - 1, 16);
        assertEquals(4030, decode.getDecodeValue(data, 0));
    }
    
    private GenericParameterDecoder getIBCDParameterDecoder(final boolean isUserValid, final boolean isOptional, final boolean isValideLteEmbeddedBitFlag, final int numberOfBytes, final int numberOfBits) {
        final EventParameter eventParameter = mockedEventParameter(isUserValid, isOptional, isValideLteEmbeddedBitFlag, numberOfBytes, numberOfBits);
        return new IBCDParameterDecoder(eventParameter);
    }
}
