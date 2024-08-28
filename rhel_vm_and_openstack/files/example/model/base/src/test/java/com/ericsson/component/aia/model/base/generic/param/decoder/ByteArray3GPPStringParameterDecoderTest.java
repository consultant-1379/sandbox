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

public class ByteArray3GPPStringParameterDecoderTest extends GenericEventParameterDecoderTest {

    @Test
    public void testByteArray3GPPStringParameterDecoderifUseValidFalse() {
        final byte[] data = WORD.getBytes();
        final byte[] datalength = new byte[] { (byte) data.length };
        final byte[] resultByteArray = concatByteArrays(datalength, data);
        final GenericParameterDecoder decoder = getTHREE_3_GPPStringParameterDecoder(false, false, false, data.length - 1);
        assertEquals(WORD, decoder.getDecodeValue(resultByteArray, 0));
    }

    @Test
    public void testByteArray3GPPStringParameterDecoderifUseValidTrueAndValidtyBitInValid() {
        final byte[] data = WORD.getBytes();
        final byte[] datalength = new byte[] { (byte) data.length };
        final byte[] validtyByte = new byte[] { (byte) 0xff };
        final byte[] resultByteArray = concatByteArrays(validtyByte, datalength, data);
        final GenericParameterDecoder decoder = getTHREE_3_GPPStringParameterDecoder(true, false, false, data.length);
        assertNull(decoder.getDecodeValue(resultByteArray, 0));
    }

    @Test
    public void testByteArray3GPPStringParameterDecoderifUseValidTrueAndValidtyBitValid() {
        final byte[] data = WORD.getBytes();
        final byte[] datalength = new byte[] { (byte) data.length };
        final byte[] validtyByte = new byte[] { (byte) 0x00 };
        final byte[] resultByteArray = concatByteArrays(validtyByte, datalength, data);
        final GenericParameterDecoder decoder = getTHREE_3_GPPStringParameterDecoder(true, false, false, data.length - 1);
        assertEquals(WORD, decoder.getDecodeValue(resultByteArray, 0));
    }

    @Test
    public void testByteArray3GPPStringParameterDecoderifOptionalTrueAndValidtyBitValid() {
        final byte[] data = WORD.getBytes();
        final byte[] datalength = new byte[] { (byte) data.length };
        final byte[] validtyByte = new byte[] { (byte) 0x00 };
        final byte[] resultByteArray = concatByteArrays(validtyByte, datalength, data);
        final GenericParameterDecoder decoder = getTHREE_3_GPPStringParameterDecoder(false, true, false, data.length - 1);
        assertEquals(WORD, decoder.getDecodeValue(resultByteArray, 0));
    }
    
    private GenericParameterDecoder getTHREE_3_GPPStringParameterDecoder(final boolean isUserValid, final boolean isOptional, final boolean isValideLteEmbeddedBitFlag, final int numberOfBytes) {
        final EventParameter eventParameter = mockedEventParameter(isUserValid, isOptional, isValideLteEmbeddedBitFlag, numberOfBytes);
        return new THREE_3_GPPStringParameterDecoder(eventParameter);
    }

}
