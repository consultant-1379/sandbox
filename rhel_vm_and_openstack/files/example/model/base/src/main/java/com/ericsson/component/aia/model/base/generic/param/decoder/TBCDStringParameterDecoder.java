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

public class TBCDStringParameterDecoder extends GenericParameterDecoder {

    /**
     * @param eventParameter
     */
    public TBCDStringParameterDecoder(final EventParameter eventParameter) {
        super(eventParameter);
    }

    @Override
    protected String decode(final byte[] data, final int offset) {
        final byte[] tbcdValue = dataConverterHelper.getByteArrayTBCD(data, offset, eventParameter.getNumberOfBytes());

        return dataConverterHelper.getByteArrayHexStringNoF(tbcdValue, 0, tbcdValue.length);
    }

    @Override
    protected Object getDefaultValue() {
        return DefaultValues.DEFAULT_STRING_VALUE;
    }

}
