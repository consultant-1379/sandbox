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

public class StringIPv6ParameterDecoderTest extends GenericEventParameterDecoderTest {

    @Test
    public void testStringIPv6ifUseValidFalse() {
        final byte[] data = new byte[] { 0x20, 0x01, 0x0d, (byte) 0xb8, (byte) 0x85, (byte) 0xa3, 0x00, 0x00, 0x00,
                0x00, (byte) 0x8a, 0x2e, 0x03, 0x70, 0x73, 0x34 };
        final GenericParameterDecoder decoder = getStringIPv6Paramerdecoder(false, false, false, data.length - 1);
        assertEquals("2001:db8:85a3:0:0:8a2e:370:7334", decoder.getDecodeValue(data, 0));
    }

    @Test
    public void testStringIPv6ifUseValidTrueAndFlagIsValid() {
        final byte[] data = new byte[] { 0x00, 0x26, 0x00, 0x03, 0x00, 0x20, 0x20, 0x1e, (byte) 0xff, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00, 0x05 };
        final GenericParameterDecoder decoder = getStringIPv6Paramerdecoder(true, false, false, data.length - 1);
        assertEquals("2600:300:2020:1eff:0:0:0:5", decoder.getDecodeValue(data, 0));
    }

    @Test
    public void testStringIPv6ifUseValidTrueAndFlagIsInValid() {
        final byte[] data = new byte[] { (byte) 0xff, 0x26, 0x00, 0x03, 0x00, 0x20, 0x20, 0x1e, (byte) 0xff, 0x00,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x05 };
        final GenericParameterDecoder decoder = getStringIPv6Paramerdecoder(true, false, false, data.length - 1);
        assertNull(decoder.getDecodeValue(data, 0));
    }

    @Test
    public void testStringIPv6ifOtionalBitIsSetToTrueAndValidFlag() {
        final byte[] data = new byte[] { 0x00, 32, 0x01, 0x0d, (byte) 0xb8, (byte) 0xac, 0x10, (byte) 0xfe, 0x01, 0x00,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };
        final GenericParameterDecoder decoder = getStringIPv6Paramerdecoder(false, true, false, data.length - 1);
        assertEquals("2001:db8:ac10:fe01:0:0:0:0", decoder.getDecodeValue(data, 0));
    }
    
    @Test
    public void testStringIPv6ifOtionalBitIsSetToTrueAndInValidFlag() {
        final byte[] data = new byte[] { (byte) 0xff, 32, 0x01, 0x0d, (byte) 0xb8, (byte) 0xac, 0x10, (byte) 0xfe, 0x01, 0x00,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };
        final GenericParameterDecoder decoder = getStringIPv6Paramerdecoder(false, true, false, data.length - 1);
        assertNull(decoder.getDecodeValue(data, 0));
    }

    private GenericParameterDecoder getStringIPv6Paramerdecoder(final boolean isUserValid, final boolean isOptional,
            final boolean isValideLteEmbeddedBitFlag, final int numberOfBytes) {
        final EventParameter eventParameter = mockedEventParameter(isUserValid, isOptional, isValideLteEmbeddedBitFlag,
                numberOfBytes);
        return new StringIPv6ParameterDecoder(eventParameter);
    }
}
