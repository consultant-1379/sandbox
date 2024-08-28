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

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

import org.junit.*;
import org.junit.rules.TemporaryFolder;

/**
 * In these tests we have a sleep of 1000ms. This is so that there is a difference of at least 1ms between files in the source and destination
 * folders.
 * This is needed as the lowest granularity of time is 1ms and we need to ensure that there is at least that between the timestamps otherwise
 * we get get wrong results from the generationRequired method.
 */
public class GenerateUtilsTest {

    private static final int SLEEP_TIME = 1000;

    @Rule
    public TemporaryFolder temporaryRoot = new TemporaryFolder();

    @Test
    public void addPathToClasspath() throws IOException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        final File tempFolder = temporaryRoot.newFolder();

        final URL[] classPath = getCurrentClassPath();
        GenerateUtils.addDirectoryToClasspath(tempFolder);
        final URL[] result = getCurrentClassPath();

        assertEquals(classPath.length + 1, result.length);
        assertEquals(true, classpathContains(result, tempFolder.getName()));
    }

    private URL[] getCurrentClassPath() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        final Method method = getGetURLsMethodFromURLClassLoader();
        final URL[] classPath = (URL[]) method.invoke(ClassLoader.getSystemClassLoader(), (Object[]) null);
        return classPath;
    }

    private Method getGetURLsMethodFromURLClassLoader() throws NoSuchMethodException {
        final Method method = URLClassLoader.class.getDeclaredMethod("getURLs");
        method.setAccessible(true);
        return method;
    }

    private boolean classpathContains(final URL[] result, final String name) {
        for (int x = 0; x < result.length; x++) {
            if (result[x].getPath().contains(name)) {
                return true;
            }
        }
        return false;
    }

    @Test
    public void generateRequired_twoEmptyDirectories_expectFalse() throws IOException {
        assertEquals(false, GenerateUtils.generateRequired(temporaryRoot.newFolder(), temporaryRoot.newFolder()));
    }

    @Test
    public void generateRequired_twoFilesSourceOlderThanTarget_expectFalse() throws IOException, InterruptedException {
        final File source = temporaryRoot.newFile();

        Thread.sleep(SLEEP_TIME);

        final File target = temporaryRoot.newFile();

        assertEquals(false, GenerateUtils.generateRequired(source, target));
    }

    @Test
    public void generateRequired_twoFilesTargetOlderThanSource_expectFalse() throws InterruptedException, IOException {
        final File target = temporaryRoot.newFile();

        Thread.sleep(SLEEP_TIME);

        final File source = temporaryRoot.newFile();
        assertEquals(true, GenerateUtils.generateRequired(source, target));
    }

    @Test
    public void generateRequired_sourceDirectoryAndTargetFile_expectFalse() throws IOException, InterruptedException {
        final File source = makeSubFolderAndPopulateFiles(temporaryRoot.getRoot(), "source");

        Thread.sleep(SLEEP_TIME);

        final File target = temporaryRoot.newFile();

        assertEquals(false, GenerateUtils.generateRequired(source, target));
    }

    @Test
    public void generateRequired_sourceDirectoryAndEmptyTargetDirectory_expectTrue() throws InterruptedException, IOException {
        final File source = makeSubFolderAndPopulateFiles(temporaryRoot.getRoot(), "source");

        Thread.sleep(SLEEP_TIME);

        final File target = temporaryRoot.newFolder();

        assertEquals(true, GenerateUtils.generateRequired(source, target));
    }

    @Test
    public void generateRequired_sourceDirectoryAndNewerTargetDirectory_expectFalse() throws IOException, InterruptedException {
        final File source = makeSubFolderAndPopulateFiles(temporaryRoot.getRoot(), "source");

        Thread.sleep(SLEEP_TIME);

        final File target = makeSubFolderAndPopulateFiles(temporaryRoot.getRoot(), "target");

        assertEquals(false, GenerateUtils.generateRequired(source, target));
    }

    @Test
    public void generateRequired_sourceDirectoryAndOlderTargetDirectory_expectTrue() throws IOException, InterruptedException {
        final File target = makeSubFolderAndPopulateFiles(temporaryRoot.getRoot(), "target");

        Thread.sleep(SLEEP_TIME);

        final File source = makeSubFolderAndPopulateFiles(temporaryRoot.getRoot(), "source");
        assertEquals(true, GenerateUtils.generateRequired(source, target));
    }

    @Test
    public void generateRequired_nestedSourceDirectoryAndNewerTargetDirectory_expectFalse() throws IOException, InterruptedException {
        final File source = makeSubFolderAndPopulateFiles(temporaryRoot.getRoot(), "source");
        makeSubFolderAndPopulateFiles(source, "subfolder");
        Thread.sleep(SLEEP_TIME);

        final File target = makeSubFolderAndPopulateFiles(temporaryRoot.getRoot(), "target");

        assertEquals(false, GenerateUtils.generateRequired(source, target));
    }

    @Test
    public void generateRequired_nestedSourceDirectoryAndOlderTargetDirectory_expectTrue() throws IOException, InterruptedException {
        final File target = makeSubFolderAndPopulateFiles(temporaryRoot.getRoot(), "target");
        makeSubFolderAndPopulateFiles(target, "subfolder");
        Thread.sleep(SLEEP_TIME);

        final File source = makeSubFolderAndPopulateFiles(temporaryRoot.getRoot(), "source");
        assertEquals(true, GenerateUtils.generateRequired(source, target));
    }

    private File makeSubFolderAndPopulateFiles(final File target, final String folderName) throws IOException {
        final File subFolder = new File(target, folderName);
        subFolder.mkdir();
        createTempFiles(subFolder);
        return subFolder;
    }

    private void createTempFiles(final File source) throws IOException {
        for (int x = 0; x < 4; x++) {
            File.createTempFile("junit", ".tmp", source);
        }
    }

    @After
    public void destroy() {
        temporaryRoot.delete();
    }

}
