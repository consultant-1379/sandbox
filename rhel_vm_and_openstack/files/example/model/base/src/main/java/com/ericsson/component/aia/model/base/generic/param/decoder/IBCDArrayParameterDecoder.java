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

public class IBCDArrayParameterDecoder extends GenericArrayParameterDecoder {

    private final GenericParameterDecoder ibcdParameterDecoder;

    /**
     * @param eventParameter
     */
    public IBCDArrayParameterDecoder(final EventParameter eventParameter) {
        super(eventParameter);
        ibcdParameterDecoder = new IBCDParameterDecoder(eventParameter);
    }

    @Override
    protected Object decode(final byte[] data, final int offset) {
        return ibcdParameterDecoder.decode(data, offset);
    }

    @Override
    protected Object[] initializeArray() {
        return new Integer[eventParameter.getValidStructureArraySize()];
    }

    @Override
    protected Object getDefaultValue() {
        return DefaultValues.DEFAULT_INT_ARRAY_VALUE;
    }
}
