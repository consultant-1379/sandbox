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

public class TBCDStringParameterDecoderTest extends GenericEventParameterDecoderTest {
    
    @Test
    public void testByteArrayTBCDString() {
        final byte[] data = new byte[] { 7, 5, 6, 4, (byte) 0xff, (byte) 0xff, (byte) 0xff };
        final GenericParameterDecoder decode = getTBCDStringParameterDecoder(false, false, false, data.length);
        assertEquals("70506040", decode.getDecodeValue(data, 0));
    }

    @Test
    public void testByteArrayTBCDStringwithOutPadding() {
        final byte[] data = new byte[] { 0x00, 16, 3, 96, 0, -111, -107, -106, 16 };
        final GenericParameterDecoder decode = getTBCDStringParameterDecoder(true, false, false, 8);
        assertEquals("0130060019596901", decode.getDecodeValue(data, 0));
    }

    @Test
    public void testByteArrayTBCDStringwithPadding() {
        final byte[] data = new byte[] { 88, -110, -121, 36, 38, -11, (byte) 0xff, (byte) 0xff, (byte) 0xff };
        final GenericParameterDecoder decode = getTBCDStringParameterDecoder(false, false, false, 9);
        assertEquals("85297842625", decode.getDecodeValue(data, 0));
    }

    @Test
    public void testByteArrayTBCDStringwithPaddingWithHighNibble() {
        final byte[] data = new byte[] { 88, -110, -121, 36, 38, -11, (byte) 0x8f, (byte) 0xff, (byte) 0xff };
        final GenericParameterDecoder decode = getTBCDStringParameterDecoder(false, false, false, 9);
        assertEquals("852978426258", decode.getDecodeValue(data, 0));
    }

    @Test
    public void testByteArrayTBCDStringwithPaddingWithLLowNibble() {
        final byte[] data = new byte[] { 88, -110, -121, 36, 38, -11, (byte) 0xf8, (byte) 0xff, (byte) 0xff };
        final GenericParameterDecoder decode = getTBCDStringParameterDecoder(false, false, false, 9);
        assertEquals("852978426258", decode.getDecodeValue(data, 0));
    }

    @Test
    public void testByteArrayTBCDStringWithIsUseValidTrue() {
        final byte[] data = new byte[] { 0, 7, 5, 6, 4 };
        final GenericParameterDecoder decode = getTBCDStringParameterDecoder(true, false, false, 4);
        assertEquals("70506040", decode.getDecodeValue(data, 0));
    }

    private GenericParameterDecoder getTBCDStringParameterDecoder(final boolean isUserValid, final boolean isOptional, final boolean isValideLteEmbeddedBitFlag, final int numberOfBytes) {
        final EventParameter eventParameter = mockedEventParameter(isUserValid, isOptional, isValideLteEmbeddedBitFlag, numberOfBytes);
        final GenericParameterDecoder decode = new TBCDStringParameterDecoder(eventParameter);
        return decode;
    }
}
