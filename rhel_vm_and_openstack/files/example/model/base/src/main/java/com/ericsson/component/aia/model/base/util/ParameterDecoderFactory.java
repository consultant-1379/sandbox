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
package com.ericsson.component.aia.model.base.util;

import static com.ericsson.component.aia.model.base.util.ParserTypes.*;

import com.ericsson.component.aia.model.base.generic.param.decoder.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.component.aia.model.base.meta.schema.EventParameter;

public class ParameterDecoderFactory {
    
    private static final Logger logger = LoggerFactory.getLogger(ParameterDecoderFactory.class);

    public static GenericParameterDecoder getParameterDecoder(final EventParameter eventParameter) {
        switch (eventParameter.getParserType()) {
        case STRING:
            if (eventParameter.isStructArray()) {
                return new StringArrayParameterDecoder(eventParameter);
            }
            return new StringParameterDecoder(eventParameter);
        case BYTE:
            if (eventParameter.isStructArray()) {
                return new ByteArrayParameterDecoder(eventParameter);
            }
            return new ByteParameterDecoder(eventParameter);
        case INTEGER:
            if (eventParameter.isStructArray()) {
                return new UnsignedIntegerArrayParameterDecoder(eventParameter);
            }
            return new UnsignedIntegerParameterDecoder(eventParameter);
        case LONG:
            if (eventParameter.isStructArray()) {
                return new LongArrayParameterDecoder(eventParameter);
            }
            return new LongParameterDecoder(eventParameter);
        case BYTE_ARRAY:
            if (isCellTraceAsn1Parameter(eventParameter)) {
                    return new CellTraceAsn1ContentByteArrayParameterDecoder(eventParameter);
            }
            return new ByteArrayByteArrayParameterDecoder(eventParameter);
        case BOOLEAN:
            if (eventParameter.isStructArray()) {
                return new BooleanArrayParameterDecoder(eventParameter);
            }
            return new BooleanParameterDecoder(eventParameter);
        case DOUBLE:
            if (eventParameter.isStructArray()) {
                return new DoubleArrayParameterDecoder(eventParameter);
            }
            return new DoubleParameterDecoder(eventParameter);
            
        case FLOAT:
            if (eventParameter.isStructArray()) {
                return new DoubleArrayParameterDecoder(eventParameter);
            }
            return new DoubleParameterDecoder(eventParameter);
        case IBCD:
            if (eventParameter.isStructArray()) {
                return new IBCDArrayParameterDecoder(eventParameter);
            }
            return new IBCDParameterDecoder(eventParameter);
        case TBCD:
            return new TBCDStringParameterDecoder(eventParameter);
        case IPADDRESS:
            if (eventParameter.isStructArray()) {
                return new DottedDecimalArrayStringParameterDecoder(eventParameter);
            }
            return new DottedDecimalStringParameterDecoder(eventParameter);
        case IPADDRESSV6:
            if (eventParameter.isStructArray()) {
                return new StringArrayIPv6ParameterDecoder(eventParameter);
            }
            return new StringIPv6ParameterDecoder(eventParameter);
            
        case DNSNAME:
            return new THREE_3_GPPStringParameterDecoder(eventParameter);
        
        default:
            logger.error("Invalid Parser Type, no ParameterDecorder found !!! : '{}' ", eventParameter.toString());
            return null;
        }
    }
	
	private static boolean isCellTraceAsn1Parameter(final EventParameter eventParameter) {
        return eventParameter.getName().endsWith("MESSAGE_CONTENTS") && eventParameter.isVariableLength();
    }
}
