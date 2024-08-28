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
import com.ericsson.component.aia.model.eventbean.DefaultValues;

public class ByteArrayParameterDecoder extends GenericArrayParameterDecoder {

    private final GenericParameterDecoder byteParameterDecoder;

    /**
     * @param eventParameter
     */
    public ByteArrayParameterDecoder(final EventParameter eventParameter) {
        super(eventParameter);
        byteParameterDecoder = new ByteParameterDecoder(eventParameter);
    }

    @Override
    protected Object decode(final byte[] data, final int offset) {
        return byteParameterDecoder.decode(data, offset);
    }

    @Override
    protected Object[] initializeArray() {
        return new Byte[eventParameter.getValidStructureArraySize()];
    }

    @Override
    protected Object getDefaultValue() {
        return ByteBuffer.wrap(DefaultValues.DEFAULT_BYTE_ARRAY_VALUE);
    }

}
