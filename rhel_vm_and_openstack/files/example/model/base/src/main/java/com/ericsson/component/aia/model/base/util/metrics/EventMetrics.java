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
package com.ericsson.component.aia.model.base.util.metrics;


/**
 * This class is used to report metrics on file loading
 */
public class EventMetrics {
    private long records = 0;

    private long eventsProcessed = 0;

    private int files = 0;
    
    private int erroneousFiles = 0; 

    private long invalidEvents = 0;

    private long ignoredEvents = 0;

    private long startTimeinMilliSec = 0;

    private long endTimeInMilliSec = 0;

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("processing time:");
        builder.append(endTimeInMilliSec - startTimeinMilliSec).append(" ms");
        builder.append(", files:").append(files);
        builder.append(", erroneousFiles:").append(erroneousFiles);
        builder.append(", records:").append(records);
        builder.append(", invalid events:").append(invalidEvents);
        builder.append(", ignored events:").append(ignoredEvents);
        builder.append(", processed events:").append(eventsProcessed);

        return builder.toString();
    }

    public void incrementRecords(final long incrementAmount) {
        records += incrementAmount;
    }

    public void incrementRecords() {
        records++;
    }

    public void incrementFiles(final long incrementAmount) {
        files += incrementAmount;
    }

    public void incrementFiles() {
        files++;
    }
    
    public void incrementErroneousFiles(final long incrementAmount) {
        this.erroneousFiles += incrementAmount;
    }

    public void incrementErroneousFiles() {
        erroneousFiles++;
    }

    public void incrementEventsProcessed(final long incrementAmount) {
        eventsProcessed += incrementAmount;
    }

    public void incrementEventsProcessed() {
        eventsProcessed++;
    }

    public void incrementInvalidEvents(final long incrementAmount) {
        invalidEvents += incrementAmount;
    }

    public void incrementIgnoredEvents(final long incrementAmount) {
        ignoredEvents += incrementAmount;
    }
    
    public void incrementIgnoredEvents() {
        ignoredEvents ++;
    }

    public void incrementInvalidEvents() {
        invalidEvents++;
    }

    public long getRecords() {
        return records;
    }

    public void setRecords(final long records) {
        this.records = records;
    }

    public long getEventsProcessed() {
        return eventsProcessed;
    }

    public void setEventsProcessed(final long events) {
        this.eventsProcessed = events;
    }

    public int getFiles() {
        return files;
    }

    public void setFiles(final int files) {
        this.files = files;
    }

    public long getInvalidEvents() {
        return invalidEvents;
    }

    public void setInvalidEvents(final long invalidEvents) {
        this.invalidEvents = invalidEvents;
    }

    public long getStartTimeInMilliSec() {
        return startTimeinMilliSec;
    }

    public void setStartTimeInMilliSec(final long startTimeInMiliSec) {
        this.startTimeinMilliSec = startTimeInMiliSec;
    }

    public long getEndTimeInMilliSec() {
        return endTimeInMilliSec;
    }

    public void setEndTimeInMilliSec(final long endTimeInMilliSec) {
        this.endTimeInMilliSec = endTimeInMilliSec;
    }

    public long getIgnoredEvents() {
        return ignoredEvents;
    }

    public void setIgnoredEvents(final long ignoredEvents) {
        this.ignoredEvents = ignoredEvents;
    }

    public long getProcessingTimeInMilliSec() {
        return startTimeinMilliSec - endTimeInMilliSec;
    }

    public int getErroneousFiles() {
        return erroneousFiles;
    }

    public void setErroneousFiles(int erroneousFiles) {
        this.erroneousFiles = erroneousFiles;
    }
}
