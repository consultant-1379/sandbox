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

public class BooleanArrayParameterDecoder extends GenericArrayParameterDecoder {

    private final GenericParameterDecoder booleanParameterDecoder;

    /**
     * @param eventParameter
     */
    public BooleanArrayParameterDecoder(final EventParameter eventParameter) {
        super(eventParameter);
        booleanParameterDecoder = new BooleanParameterDecoder(eventParameter);
    }

    @Override
    protected Object decode(final byte[] data, final int offset) {
        return booleanParameterDecoder.decode(data, offset);
    }

    @Override
    protected Object[] initializeArray() {
        return new Boolean[eventParameter.getValidStructureArraySize()];
    }

    @Override
    protected Object getDefaultValue() {
        return DefaultValues.DEFAULT_BOOLEAN_ARRAY_VALUE;
    }

}
