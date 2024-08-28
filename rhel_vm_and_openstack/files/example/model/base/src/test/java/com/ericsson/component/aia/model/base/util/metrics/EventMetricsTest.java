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
package com.ericsson.component.aia.model.base.util.metrics;

import static org.junit.Assert.*;

import org.junit.*;

public class EventMetricsTest {

    private EventMetrics objUnderTest;

    @Before
    public void init() {
        objUnderTest = new EventMetrics();
    }

    @Test
    public void testIncrementMethodbyValue_attributesAreIncremented() {
        final int incrementAmount = 10;
        objUnderTest.incrementEventsProcessed(incrementAmount);
        objUnderTest.incrementFiles(incrementAmount);
        objUnderTest.incrementErroneousFiles(incrementAmount);
        objUnderTest.incrementIgnoredEvents(incrementAmount);
        objUnderTest.incrementInvalidEvents(incrementAmount);
        objUnderTest.incrementRecords(incrementAmount);
        
        assertEquals(incrementAmount, objUnderTest.getEventsProcessed());
        assertEquals(incrementAmount, objUnderTest.getFiles());
        assertEquals(incrementAmount, objUnderTest.getErroneousFiles());
        assertEquals(incrementAmount, objUnderTest.getIgnoredEvents());
        assertEquals(incrementAmount, objUnderTest.getInvalidEvents());
        assertEquals(incrementAmount, objUnderTest.getRecords());
    }
    
    @Test
    public void testIncrementMethod_attributesAreIncrementedByOne() {
        objUnderTest.incrementEventsProcessed();
        objUnderTest.incrementFiles();
        objUnderTest.incrementErroneousFiles();
        objUnderTest.incrementIgnoredEvents();
        objUnderTest.incrementInvalidEvents();
        objUnderTest.incrementRecords();
        
        assertEquals(1, objUnderTest.getEventsProcessed());
        assertEquals(1, objUnderTest.getFiles());
        assertEquals(1, objUnderTest.getErroneousFiles());
        assertEquals(1, objUnderTest.getIgnoredEvents());
        assertEquals(1, objUnderTest.getInvalidEvents());
        assertEquals(1, objUnderTest.getRecords());
    }
}
