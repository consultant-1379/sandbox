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
package com.ericsson.component.aia.model.base.util;

/**
 * Enclosing class for utility exceptions
 * 
 * 
 */
public class UtilityException extends Exception {
    private static final long serialVersionUID = -6052785998652498220L;

    public UtilityException(final String message) {
        super(message);
    }

    public UtilityException(final String message, final Exception exception) {
        super(message, exception);
    }
}
