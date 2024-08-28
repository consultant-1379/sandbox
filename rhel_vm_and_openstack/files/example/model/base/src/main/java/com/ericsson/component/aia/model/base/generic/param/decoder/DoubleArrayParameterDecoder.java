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

import com.ericsson.component.aia.model.base.meta.schema.EventParameter;
import com.ericsson.component.aia.model.eventbean.DefaultValues;

public class DoubleArrayParameterDecoder extends GenericArrayParameterDecoder {

    private final GenericParameterDecoder doubleParameterDecoder;

    /**
     * @param eventParameter
     */
    public DoubleArrayParameterDecoder(final EventParameter eventParameter) {
        super(eventParameter);
        doubleParameterDecoder = new DoubleParameterDecoder(eventParameter);
    }

    @Override
    protected Object decode(final byte[] data, final int offset) {
        return doubleParameterDecoder.decode(data, offset);
    }

    @Override
    protected Object[] initializeArray() {
        return new Double[eventParameter.getValidStructureArraySize()];
    }

    @Override
    protected Object getDefaultValue() {
        return DefaultValues.DEFAULT_DOUBLE_ARRAY_VALUE;
    }

}
