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

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.ericsson.component.aia.model.base.exception.ResourceNotFoundException;

public class ResourceFileFinder {
    
    private static List<ReadResource> classes = new ArrayList<ReadResource>();
    
    static {
        classes.add(new ReadResourceFileFromClassPath());
    }

    public InputStream getFileResourceAsStream(final String fileName) throws ResourceNotFoundException {
        for (final ReadResource readResource : classes) {
            final InputStream resourceFileInputStream = readResource.findResourceFile(fileName);

            if (resourceFileInputStream != null) {
                return resourceFileInputStream;
            }
        }
        throw new ResourceNotFoundException("cound not get file resource: " + fileName);

    }

    public URL getFileResourceURL(final String fileName) throws ResourceNotFoundException {
        for (final ReadResource readResource : classes) {
            final URL resourceFileURL = readResource.findResourceFileAsURL(fileName);
            if (resourceFileURL != null) {
                return resourceFileURL;
            }
        }
        throw new ResourceNotFoundException("cound not get file resource: " + fileName);
    }

    public String getFileResourcePath(final String fileName) throws ResourceNotFoundException {
        return getFileResourceURL(fileName).toString();
    }

}
