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
import java.nio.file.Paths;

public class ReadResourceFileFromClassPath implements ReadResource {
    @Override
    public InputStream findResourceFile(final String resourceName) {

        final ClassLoader classLoader = ClassLoader.getSystemClassLoader();

        InputStream inputStream = classLoader.getResourceAsStream(resourceName);
        if (inputStream != null) {
            LOGGER.trace("on system class path, for {} found {}", resourceName, inputStream);
            return inputStream;
        }

        final String lastResourceNameInPath = Paths.get(resourceName).getFileName().toString();
        LOGGER.trace("looking for resource {} without full path {} ", lastResourceNameInPath);

        inputStream = classLoader.getResourceAsStream(lastResourceNameInPath);
        if (inputStream != null) {
            LOGGER.trace("on system class path, for {} found {}", lastResourceNameInPath, inputStream);
            return inputStream;
        }

        inputStream = ReadResourceFileFromClassPath.class.getClassLoader().getResourceAsStream(resourceName);
        if (inputStream != null) {
            LOGGER.trace("on class's class path, for {} found {}", lastResourceNameInPath, inputStream);
            return inputStream;
        }

        return ReadResourceFileFromClassPath.class.getClassLoader().getResourceAsStream(lastResourceNameInPath);

    }

    @Override
    public URL findResourceFileAsURL(final String resourceName) {
        final ClassLoader classLoader = ClassLoader.getSystemClassLoader();

        URL fileURL = classLoader.getResource(resourceName);
        if (fileURL != null) {
            LOGGER.trace("on system class path, for {} found {}", resourceName, fileURL);
            return fileURL;
        }

        final String lastResourceNameInPath = Paths.get(resourceName).getFileName().toString();
        LOGGER.trace("looking for resource {} without full path {} ", lastResourceNameInPath);

        fileURL = classLoader.getResource(lastResourceNameInPath);
        if (fileURL != null) {
            LOGGER.trace("on system class path, for {} found {}", lastResourceNameInPath, fileURL);
            return fileURL;
        }

        fileURL = ReadResourceFileFromClassPath.class.getClassLoader().getResource(resourceName);

        if (fileURL != null) {
            LOGGER.trace("on class's class path, for {} found {}", lastResourceNameInPath, fileURL);
            return fileURL;
        }

        return ReadResourceFileFromClassPath.class.getClassLoader().getResource(lastResourceNameInPath);

    }

}
