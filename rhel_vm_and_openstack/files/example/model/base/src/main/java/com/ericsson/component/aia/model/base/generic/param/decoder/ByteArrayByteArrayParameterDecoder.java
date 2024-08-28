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

import java.nio.ByteBuffer;

import com.ericsson.component.aia.model.base.meta.schema.EventParameter;
import com.ericsson.component.aia.model.base.util.DataConverters;
import com.ericsson.component.aia.model.eventbean.DefaultValues;

public class ByteArrayByteArrayParameterDecoder extends GenericParameterDecoder {

    /**
     * @param eventParameter
     */
    public ByteArrayByteArrayParameterDecoder(final EventParameter eventParameter) {
        super(eventParameter);
    }

    @Override
    protected Object decode(final byte[] data, final int offset) {
        return decodeValue(data, offset, false);
    }

    @Override
    public Object decodeValue(final byte[] data, final int offsetToUse, final boolean isGenericRecord) {
        if (isGenericRecord) {
            return ByteBuffer.wrap(getByteArrayByteArray(data, offsetToUse, eventParameter.getNumberOfBytes()));
        } else {
            return dataConverterHelper.getByteArrayByteArray(data, offsetToUse, eventParameter.getNumberOfBytes());
        }
    }

    public byte[] getByteArrayByteArray(final byte[] data, final int offset, int bytes) {
        final int offsetToUse = offset;
        if (bytes == -1) {
            bytes = (int) dataConverterHelper.getByteArrayInteger(data, offsetToUse - 2, 2, DataConverters.UNSIGNED_FLAG);
        }

        if (bytes < 0) {
            return new byte[] { 0 };
        }

        byte[] subByteArray;

        if (offsetToUse == 0) {
            subByteArray = data;
        } else {
            subByteArray = new byte[bytes];

            for (int i = 0; i < bytes; i++) {
                subByteArray[i] = data[offsetToUse + i];
            }
        }

        return subByteArray;
    }

    @Override
    protected Object getDefaultValue() {
        return ByteBuffer.wrap(DefaultValues.DEFAULT_BYTE_ARRAY_VALUE);
    }
}