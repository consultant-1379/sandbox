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

import static org.mockito.Mockito.*;

import java.nio.ByteBuffer;
import java.util.Arrays;

import com.ericsson.component.aia.model.base.meta.schema.EventParameter;

public class GenericEventParameterDecoderTest {
    
    protected static final String WORD = "word";

    protected EventParameter mockedEventParameter(final boolean isUserValid, final boolean isOptional, final boolean isValideLteEmbeddedBitFlag, final int numberOfBytes) {
        final EventParameter mockedEventParameter = mock(EventParameter.class);
        when(mockedEventParameter.isUseValid()).thenReturn(isUserValid);
        when(mockedEventParameter.isOptional()).thenReturn(isOptional);
        when(mockedEventParameter.isValidLTEembeddedbitFlag()).thenReturn(isValideLteEmbeddedBitFlag);
        when(mockedEventParameter.getNumberOfBytes()).thenReturn(numberOfBytes);
        return mockedEventParameter;
    }

    protected EventParameter mockedEventParameter(final boolean isUserValid, final boolean isOptional, final boolean isValideLteEmbeddedBitFlag, final int numberOfBytes, final int numberOfbits) {
        final EventParameter mockedEventParameter = mockedEventParameter(isUserValid, isOptional, isValideLteEmbeddedBitFlag, numberOfBytes);
        when(mockedEventParameter.getNumberOfBits()).thenReturn(numberOfbits);
        return mockedEventParameter;
    }
    
    protected EventParameter mockedEventParameter(final boolean isUserValid, final boolean isOptional, final boolean isValideLteEmbeddedBitFlag, final int numberOfBytes, final int startSkip, final int endSkip, final int structArraySize) {
        final EventParameter mockedEventParameter = mockedEventParameter(isUserValid, isOptional, isValideLteEmbeddedBitFlag, numberOfBytes);
        when(mockedEventParameter.isStructArray()).thenReturn(true);
        when(mockedEventParameter.getStartSkip()).thenReturn(startSkip);
        when(mockedEventParameter.getEndSkip()).thenReturn(endSkip);
        when(mockedEventParameter.getValidStructureArraySize()).thenReturn(structArraySize);
        return mockedEventParameter;
    }
    
    protected EventParameter mockedEventParameter(final boolean isUserValid, final boolean isOptional, final boolean isValideLteEmbeddedBitFlag, final int numberOfBytes, final int numberOfBits, final int startSkip, final int endSkip, final int structArraySize) {
        final EventParameter mockedEventParameter = mockedEventParameter(isUserValid, isOptional, isValideLteEmbeddedBitFlag, numberOfBytes, numberOfBits);
        when(mockedEventParameter.isStructArray()).thenReturn(true);
        when(mockedEventParameter.getStartSkip()).thenReturn(startSkip);
        when(mockedEventParameter.getEndSkip()).thenReturn(endSkip);
        when(mockedEventParameter.getValidStructureArraySize()).thenReturn(structArraySize);
        return mockedEventParameter;
    }


    protected byte[] getIntegerByteArray(final int Value) {
        final ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.putInt(Value);
        return buffer.array();
    }
    
    protected byte[] getLongByteArray(final long value) {
        final ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.putLong(value);
        return buffer.array();
    }
    
    
    protected byte[] concatByteArrays(final byte[] first, final byte[]... rest) {
        int totalLength = first.length;
        for (final byte[] array : rest) {
          totalLength += array.length;
        }
        final byte[] result = Arrays.copyOf(first, totalLength);
        int offset = first.length;
        for (final byte[] array : rest) {
          System.arraycopy(array, 0, result, offset, array.length);
          offset += array.length;
        }
        return result;
      }
    
    
    protected byte[] getStringBytes(final byte validityByte) {
        final byte[] wordBytes = WORD.getBytes();
        final int wordLength = wordBytes.length;
        final int dataLength = wordLength + 1;
        final byte[] data = new byte[dataLength];
        data[0] = validityByte;
        for (int i = 0; i < wordLength; i++) {
            data[i + 1] = wordBytes[i];
        }
        return data;
    }
    
    protected byte[] getDoubleByteArray(final double expectedvalue) {
        final byte[] data = new byte[8];
        ByteBuffer.wrap(data).putDouble(expectedvalue);
        return data;
    }
}
