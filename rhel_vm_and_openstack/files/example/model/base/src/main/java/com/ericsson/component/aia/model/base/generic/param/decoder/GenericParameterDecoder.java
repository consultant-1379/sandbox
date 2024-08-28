/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2012
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
import com.ericsson.component.aia.model.base.util.DataConverterHelper;

public abstract class GenericParameterDecoder {

    protected final EventParameter eventParameter;
    protected boolean isGenericRecord = false;
    protected DataConverterHelper dataConverterHelper;

    /**
     * Constructor
     *
     * @param EventParameter
     * @param
     */
    public GenericParameterDecoder(final EventParameter eventParameter) {
        this.eventParameter = eventParameter;
        this.dataConverterHelper = new DataConverterHelper();
    }

    protected abstract Object decode(final byte[] data, final int offset);

    public Object getDecodeValue(final byte[] data, final int offset) {
        int offsetToUse = offset;
        if (eventParameter.isUseValid() || eventParameter.isOptional()) {
            if (dataConverterHelper.isMarkedInvalid(data, offset)) {
                return null;
            }
            if (eventParameter.isValidLTEembeddedbitFlag()) {
                // get rest of 7 bit from bytes and replace original byte i.e. result byte has no validity bit
                data[offset] = dataConverterHelper.getParamByte(data[offset], 1, 7);
            } else {
                offsetToUse = offset + 1;
            }
        }
        return decode(data, offsetToUse);
    }

    public Object getDecodeValueForGenericRecord(final byte[] data, final int offset) {
        int offsetToUse = offset;
        if (eventParameter.isUseValid() || eventParameter.isOptional()) {
            if (dataConverterHelper.isMarkedInvalid(data, offset)) {
                return getDefaultValue();
            }
            if (eventParameter.isValidLTEembeddedbitFlag()) {
                data[offset] = dataConverterHelper.getParamByte(data[offset], 1, 7);
            } else {
                offsetToUse = offset + 1;
            }
        }

        return decodeValue(data, offsetToUse, true);
    }

    public Object decodeValue(final byte[] data, final int offsetToUse, final boolean isGenericRecord) {
        return decode(data, offsetToUse);
    }

    public int adjustOffset(final int offset) {
        int adjustedOffset = offset;
        if ((eventParameter.isUseValid() || eventParameter.isOptional()) && !eventParameter.isValidLTEembeddedbitFlag()) {
            adjustedOffset++;
        }
        adjustedOffset += eventParameter.getNumberOfBytes();
        return adjustedOffset;

    }

    protected abstract Object getDefaultValue();

}
