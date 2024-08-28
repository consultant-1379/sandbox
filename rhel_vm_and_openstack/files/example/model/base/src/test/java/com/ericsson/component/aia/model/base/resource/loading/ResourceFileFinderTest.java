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
package com.ericsson.component.aia.model.base.resource.loading;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import com.ericsson.component.aia.model.base.eventbean.generation.GenerateUtils;
import com.ericsson.component.aia.model.base.exception.ResourceNotFoundException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

public class ResourceFileFinderTest {
    @Rule
    public TemporaryFolder rootFolder = new TemporaryFolder();
    
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    
    private ResourceFileFinder resourceFileFinder = new ResourceFileFinder();

    @Test
    public void getFileResource_specifyingNonExistantFile_expectNull() throws ResourceNotFoundException {
        thrown.expect(ResourceNotFoundException.class);
        resourceFileFinder.getFileResourcePath("SomeunknownFileName");
    }

    @Test
    public void getFileResource_specifyingExistingFileNameOnly_expectFile() throws IOException {
        GenerateUtils.addDirectoryToClasspath(rootFolder.getRoot());
        final File newFile = rootFolder.newFile();

        assertEquals(constructUriFrom(newFile), getFilePath(resourceFileFinder.getFileResourcePath(newFile.getName())));
    }

    private String getOsIndependantFileName(final String newFile) {
        if (newFile.startsWith("/")) {
            return newFile.substring(1);
        }
        return newFile;
    }
    
    private String getFilePath(final String fileResource) throws MalformedURLException {
        final URL domain = new URL(fileResource);
        return getOsIndependantFileName(domain.getFile().toString());
    }


    @Test
    public void getFileResource_specifyingPathToFileWhenFileExistsElsewhere_expectFile() throws IOException {
        GenerateUtils.addDirectoryToClasspath(rootFolder.getRoot());
        final File newFile = rootFolder.newFile();

        assertEquals(constructUriFrom(newFile), getFilePath(resourceFileFinder.getFileResourcePath("subFolder/" + newFile.getName())));
    }
    
    private String constructUriFrom(final File newFile) {
        final StringBuilder path = new StringBuilder();
        path.append(newFile.getAbsolutePath().replaceAll("\\\\", "/"));

        return getOsIndependantFileName(path.toString());
    }
}
