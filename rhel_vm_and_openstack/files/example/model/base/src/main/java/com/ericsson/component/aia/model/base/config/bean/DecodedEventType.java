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
package com.ericsson.component.aia.model.base.config.bean;

public enum DecodedEventType {
    POJO("pojo"), MAP("map"), GENERIC_RECORD("generic_record"), MAP_ORDERED_ENUM("map_ordered_enum");

    private final String value;

    DecodedEventType(final String eventType) {
        value = eventType.toLowerCase();
    }

    public String value() {
        return value;
    }

    public static DecodedEventType fromValue(final String eventTypeName) {
        final String lowerCaseEventTypeName = eventTypeName.toLowerCase();
        for (final DecodedEventType eventType : DecodedEventType.values()) {
            if (eventType.value.equals(lowerCaseEventTypeName)) {
                return eventType;
            }
        }
        throw new IllegalArgumentException(eventTypeName);
    }
}

