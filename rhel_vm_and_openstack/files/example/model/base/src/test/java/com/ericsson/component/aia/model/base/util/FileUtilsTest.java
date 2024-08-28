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

import static junitparams.JUnitParamsRunner.*;
import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.io.*;
import java.util.*;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.Appender;

@RunWith(JUnitParamsRunner.class)
public class FileUtilsTest {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @Test
    public void listFiles_noFilterFlatDirectory_expectAllFiles() throws IOException {
        tempFolder.newFile("fileName1");
        tempFolder.newFile("fileName2");

        final Set<String> expectedFileName = new HashSet<String>();
        expectedFileName.add("fileName1");
        expectedFileName.add("fileName2");

        final List<File> result = FileUtils.listFiles(tempFolder.getRoot(), null);
        final Set<String> foundFileName = getFileNames(result);
        assertTrue(foundFileName.containsAll(expectedFileName));
        assertTrue(expectedFileName.containsAll(foundFileName));
    }

    @Test
    public void listFiles_withFilterFlatDirectory_expectAllFiles() throws IOException {
        tempFolder.newFile("fileName1");
        tempFolder.newFile("fileName2");
        tempFolder.newFile("notfileName2");

        final Set<String> expectedFileName = new HashSet<String>();
        expectedFileName.add("fileName1");
        expectedFileName.add("fileName2");

        final List<File> result = FileUtils.listFiles(tempFolder.getRoot(), new StartsWithFilenameFilter());
        final Set<String> foundFileName = getFileNames(result);
        assertTrue(foundFileName.containsAll(expectedFileName));
        assertTrue(expectedFileName.containsAll(foundFileName));
    }

    @Test
    public void listFiles_withFilterNestedtDirectory_expectAllFiles() throws IOException {
        tempFolder.newFile("fileName1");
        tempFolder.newFile("fileName2");
        final File newFolder = tempFolder.newFolder();
        new File(newFolder, "fileName3").createNewFile();
        final File newFolder2 = tempFolder.newFolder();
        new File(newFolder2, "dfsfileName3").createNewFile();

        final Set<String> expectedFileName = new HashSet<String>();
        expectedFileName.add("fileName1");
        expectedFileName.add("fileName2");
        expectedFileName.add("fileName3");

        final List<File> result = FileUtils.listFiles(tempFolder.getRoot(), new StartsWithFilenameFilter());
        final Set<String> foundFileName = getFileNames(result);
        assertTrue(foundFileName.containsAll(expectedFileName));
        assertTrue(expectedFileName.containsAll(foundFileName));
    }

    @Test
    public void pathToNEName_invalidPath_expectUNKNOWN_NE_NAME() {
        assertEquals(FileUtils.UNKNOWN_NE_NAME, FileUtils.pathToNEName("Somegibberish"));
    }

    @Test
    public void pathToNEName_MeContext_expectMeContext() {
        assertEquals("LTE01ERBS00001", FileUtils.pathToNEName("MeContext=LTE01ERBS00001/A20130828.1500+0100-1515+0100_SubNetwork=ONRM_RootMo_R"));
    }

    @Test
    public void pathToNEName_managedElement_expectManagedElement() {
        assertEquals("LTE01ERBS00001", FileUtils.pathToNEName("ManagedElement=LTE01ERBS00001/A20130828.1500+0100-1515+0100_SubNetwork=ONRM_RootMo_R"));
    }

    @Test
    public void pathToNEName_PathOnly_expectDirectoryNameAsFilename() {
        assertEquals("LTE01ERBS00001", FileUtils.pathToNEName("LTE01ERBS00001%~%A20130828.1500+0100-1515+0100_SubNetwork=ONRM_RootMo_R"));
    }

    @Test
    public void pathToNEName_SubPaths_expectLastDirectoryNameAsNeName() {
        assertEquals("LTE01ERBS00001", FileUtils.pathToNEName("SomePath%~%LTE01ERBS00001%~%A20131007.1300-0200-20131007.1315-0200_34_ebs.35"));
    }

    @Test(expected = StringIndexOutOfBoundsException.class)
    public void getUTCOffset_doesntNotHandleEmptyFileNames() {
        FileUtils.getUTCOffset("");
    }

    @Test
    @Parameters
    public void getUTCOffset(final String fileName, final int expectedOffset) {
        final long offset = FileUtils.getUTCOffset(fileName);
        final double result = convertOffsetInMillisToHours(offset);
        assertEquals(expectedOffset, result, 0.1);
    }

    @Test
    @Parameters
    public void getUTCOffsetStandard(final String fileName, final double expectedOffset) throws InvalidTimeException {
        final double offset = FileUtils.getUTCOffsetStandard(fileName);
        final double result = convertOffsetInMillisToHours(offset);
        assertEquals(expectedOffset, result, 0.1);
    }

    @Test
    /*
     * This is a terrible test case. invlid filenames are not handled by Exceptions, but rather exceptions are squashed and log. This is
     * the only way at the moment to verify that we did detect an invalid filename
     */
    public void getUTCOffset_invalidDateOnFileName() {
        final Appender<ILoggingEvent> mockAppender = getLogFileVerifierMock();

        FileUtils.getUTCOffset("A20130828.1500^0100-1515^0100_");

        verify(mockAppender).doAppend(argThat(new ArgumentMatcherExtension("invalid date on file name: A20130828.1500^0100-1515^0100_")));
    }

    private Appender<ILoggingEvent> getLogFileVerifierMock() {
        final Logger root = (Logger) LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
        final Appender<ILoggingEvent> mockAppender = getAppenderMock();
        when(mockAppender.getName()).thenReturn("MOCK");
        root.addAppender(mockAppender);
        return mockAppender;
    }

    private Appender<ILoggingEvent> getAppenderMock() {
        @SuppressWarnings("unchecked")
        final Appender<ILoggingEvent> mock = mock(Appender.class);
        return mock;
    }

    protected Object[] parametersForGetUTCOffset() {
        return $($("A20130828.1500+0100-1515+0100_SubNetwork=ONRM_RootMo_R,MeContext=LTE01ERBS00001_celltracefile_1.bin.gz", 1),
                $("A20130828.1500-0100-1515-0100_SubNetwork=ONRM_RootMo_R,MeContext=LTE01ERBS00001_celltracefile_1.bin.gz", -1),
                $("A20130828.1345-1400_SubNetwork=ONRM_RootMo_R,MeContext=LTE01ERBS00001_celltracefile_1.bin.gz", 0),
                $("A20110405.0800-0815_CellTrace_DUL1_1.bin.gz", 0), $("A20131007.1300+0200-20131007.1315+0200_34_ebs.35", 2),
                $("A20131007.1300-0200-20131007.1315-0200_34_ebs.35", -2));
    }

    protected Object[] parametersForGetUTCOffsetStandard() {
        return $($("+01:00", 1.0), $("-01:00", -1.0), $("-02:00", -2.0), $("-01:30", -1.5));
    }

    private double convertOffsetInMillisToHours(final double offset) {
        return offset / 1000 / 60 / 60;
    }

    private Set<String> getFileNames(final List<File> result) {
        final Set<String> fileNames = new HashSet<String>();
        for (final File file : result) {
            fileNames.add(file.getName());
        }
        return fileNames;
    }

    private final class ArgumentMatcherExtension extends ArgumentMatcher<ILoggingEvent> {

        private final String logEntry;

        public ArgumentMatcherExtension(final String logEntry) {
            this.logEntry = logEntry;

        }

        @Override
        public boolean matches(final Object argument) {
            return ((LoggingEvent) argument).getFormattedMessage().contains(logEntry);
        }
    }

}

class StartsWithFilenameFilter implements FilenameFilter {

    @Override
    public boolean accept(final File dir, final String name) {
        return name.startsWith("fileName");
    }

}
