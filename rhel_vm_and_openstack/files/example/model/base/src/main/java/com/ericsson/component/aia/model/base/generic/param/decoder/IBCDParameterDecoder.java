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

public class IBCDParameterDecoder extends GenericParameterDecoder {

    /**
     * @param eventParameter
     */
    public IBCDParameterDecoder(final EventParameter eventParameter) {
        super(eventParameter);
    }

    @Override
    protected Integer decode(final byte[] data, final int offset) {
        return dataConverterHelper.getByteArrayIBCD(data, offset, eventParameter.getNumberOfBytes(), eventParameter.getNumberOfBits());
    }

    @Override
    protected Object getDefaultValue() {
        return DefaultValues.DEFAULT_INT_VALUE;
    }

}
