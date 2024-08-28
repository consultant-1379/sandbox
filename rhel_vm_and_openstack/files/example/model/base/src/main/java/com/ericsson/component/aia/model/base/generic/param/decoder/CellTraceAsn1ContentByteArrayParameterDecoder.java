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

public class CellTraceAsn1ContentByteArrayParameterDecoder extends GenericParameterDecoder {

    public CellTraceAsn1ContentByteArrayParameterDecoder(final EventParameter eventParameter) {
        super(eventParameter);
    }

    @Override
    public Object getDecodeValue(final byte[] data, final int offset) {
        final Object decodeValue = super.getDecodeValue(data, offset);
        if (decodeValue == null) {
            eventParameter.setNumberOfBytes(0);
        }
        return decodeValue;
    }

    @Override
    protected Byte[] decode(final byte[] data, final int offset) {
        // it assume, previous parameter contains the length of asn1 message
        // i.e. EVENT_PARAM_L3MESSAGE_LENGTH
        final int messageLengthParamOffSet = offset - 2;
        if (dataConverterHelper.isMarkedInvalid(data, messageLengthParamOffSet)) {
            return null;
        }
        final int asn1Length = (int) dataConverterHelper.getByteArrayInteger(data, messageLengthParamOffSet, 2, false);
        final Byte[] decode = dataConverterHelper.getByteArrayByteArray(data, offset, asn1Length);
        eventParameter.setNumberOfBytes(asn1Length);
        return decode;
    }
	
    @Override
    protected Object getDefaultValue() {
        return DefaultValues.DEFAULT_BYTE_ARRAY_VALUE;
    }

}