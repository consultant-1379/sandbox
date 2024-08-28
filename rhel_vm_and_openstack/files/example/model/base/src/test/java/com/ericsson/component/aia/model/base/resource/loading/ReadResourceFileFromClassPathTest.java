package com.ericsson.component.aia.model.base.resource.loading;

import java.io.InputStream;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;

import junit.framework.Assert;

public class ReadResourceFileFromClassPathTest {

	private ReadResourceFileFromClassPath resourceReader;

	@Before
	public void setUp() throws Exception {
		resourceReader = new ReadResourceFileFromClassPath();
	}

	@Test
	public void testNonExistentFile() {
		InputStream nonExistent = resourceReader.findResourceFile("/THIS_DOES_NOT_EXIST.TXT");
		Assert.assertNull(nonExistent);
		URL nonExistentURL = resourceReader.findResourceFileAsURL("/this_also_does_not_exist.txt");
		Assert.assertNull(nonExistentURL);
	}

	@Test
	public void testFileExists() {
		InputStream is = resourceReader.findResourceFile("/testResourceLoadingFile.txt");
		Assert.assertNotNull(is);
		InputStream pathOnlyIs = resourceReader.findResourceFile("/not/used/path/testResourceLoadingFile.txt");
		Assert.assertNotNull(pathOnlyIs);
		URL existingURL = resourceReader.findResourceFileAsURL("java/lang/String.class");
		Assert.assertNotNull(existingURL);
	}

}
