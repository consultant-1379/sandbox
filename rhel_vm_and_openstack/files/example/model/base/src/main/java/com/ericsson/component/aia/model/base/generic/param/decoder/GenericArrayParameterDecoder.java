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

public abstract class GenericArrayParameterDecoder extends GenericParameterDecoder {

    /**
     * @param eventParameter
     */
    public GenericArrayParameterDecoder(final EventParameter eventParameter) {
        super(eventParameter);
    }

    protected abstract Object decode(final byte[] data, final int offset);

    protected abstract Object[] initializeArray();

    @Override
    public Object getDecodeValue(final byte[] data, final int offset) {
        final Object[] result = initializeArray();
        int offsetToUse = offset;
        for (int i = 0; i < eventParameter.getValidStructureArraySize(); i++) {
            Object decodeValue = super.getDecodeValue(data, offsetToUse);
            result[i] = decodeValue;
            offsetToUse = super.adjustOffset(offsetToUse) + eventParameter.getEndSkip() + eventParameter.getStartSkip();
        }
        return result;
    }
    
    @Override
    public int adjustOffset(int offset) {
        final int structureArraySize = eventParameter.getValidStructureArraySize();
        if (eventParameter.isStructLastParameter() && structureArraySize > 0) {
            offset += ((structureArraySize - 1)  * eventParameter.getStructSize());
        }
        return super.adjustOffset(offset);
    }

}
