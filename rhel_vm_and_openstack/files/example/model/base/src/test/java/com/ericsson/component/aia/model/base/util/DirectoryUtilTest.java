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

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.*;
import org.junit.rules.TemporaryFolder;

public class DirectoryUtilTest {
    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    private File xml;

    private File generated;

    @Before
    public void setup() {
        try {
			xml = tempFolder.newFolder("xml");
			generated = tempFolder.newFolder("generated");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }


    @Test
    public void createDirectoryIfNotExists() throws IOException {
        xml.delete();
        new DirectoryUtil().createDirectoryIfNotExists(xml);
        assertTrue("xml folder do not exists", xml.exists());
    }

    @Test
    public void assertThatitCreatesGeneratedDirectoryifNotExists() throws IOException {
        generated.delete();
        new DirectoryUtil(xml, generated).initializeDirectoryStructure();
        assertTrue("generated Folder do not exists", generated.exists());
    }

    @After
    public void destroy() {
        xml.delete();
        generated.delete();
    }
}
