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
package com.ericsson.component.aia.model.base.eventbean.generation;

public class GenerationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public GenerationException(final String message) {
        super(message);
    }

    public GenerationException(final Exception exception) {
        super(exception);
    }

}
