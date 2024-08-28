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

public class ByteArrayParameterDecoderTest extends GenericEventParameterDecoderTest{

    @Test
    public void testByteArray_ValidityByteNotSet() {
        final GenericParameterDecoder decode = getByteArrayParameterDecoder(false, false, false, 1, 0, 1, 3);
        final byte[] data = new byte[] { 1, 0, 2, 0, 3, 0 };
        assertArrayEquals(new Byte[] { 1, 2, 3 }, (Byte[]) decode.getDecodeValue(data, 0));
    }
    
    @Test
    public void testByteArray_ValidityByteSet() {
        final GenericParameterDecoder decode = getByteArrayParameterDecoder(true, false, false, 1, 0, 1, 3);
        final byte[] data = new byte[] { 0, 1, 0, 0, 2, 0, 0, 3, 0 };
        assertArrayEquals(new Byte[] { 1, 2, 3 }, (Byte[]) decode.getDecodeValue(data, 0));
    }
    
    @Test
    public void testByteArray_ValidityByteSetToInvalid() {
        final GenericParameterDecoder decode = getByteArrayParameterDecoder(true, false, false, 1, 0, 1, 3);
        final byte[] data = new byte[] { (byte) 0xff, 1, 0, (byte) 0xff, 2, 0, (byte) 0xff, 3, 0 };
        assertArrayEquals(new Byte[] { null, null, null }, (Byte[]) decode.getDecodeValue(data, 0));
    }

    @Test
    public void testByteArray_OptionalByteSet() {
        final GenericParameterDecoder decode = getByteArrayParameterDecoder(false, true, false, 1, 0, 1, 3);
        final byte[] data = new byte[] { 0, 1, 0, 0, 2, 0, 0, 3, 0 };

        assertArrayEquals(new Byte[] { 1, 2, 3 }, (Byte[]) decode.getDecodeValue(data, 0));
    }

    private GenericParameterDecoder getByteArrayParameterDecoder(final boolean isUserValid, final boolean isOptional, final boolean isValideLteEmbeddedBitFlag, final int numberOfBytes, final int startSkip, final int endSkip, final int structArraySize) {
        final EventParameter eventParameter = mockedEventParameter(isUserValid, isOptional, isValideLteEmbeddedBitFlag, numberOfBytes, startSkip, endSkip, structArraySize);
        final GenericParameterDecoder decode = new ByteArrayParameterDecoder(eventParameter);
        return decode;
    }
}
