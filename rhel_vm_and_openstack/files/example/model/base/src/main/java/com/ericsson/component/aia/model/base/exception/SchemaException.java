/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2014
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.component.aia.model.base.exception;

/**
 * This class represents an exception thrown by schema XML file parsing and mapping building
 * 
 */
public class SchemaException extends Exception {
    private static final long serialVersionUID = -1809026909717493068L;

    /**
     * This class simply wraps the standard Exception class
     */
    public SchemaException(final String message) {
        super(message);
    }

    public SchemaException(final String message, final Exception exception) {
        super(message, exception);
    }
}
