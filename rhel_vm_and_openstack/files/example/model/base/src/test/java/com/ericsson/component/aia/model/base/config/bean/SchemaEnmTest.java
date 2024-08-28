/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2012
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.component.aia.model.base.config.bean;

import static com.ericsson.component.aia.model.base.config.bean.SchemaEnum.CELLTRACE;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class SchemaEnmTest {

	@Test
	public void shouldGetSchemaWhenSchemaWithThatNameExists() {
		String schemaName = "cellTrace";
		SchemaEnum schemEnum = SchemaEnum.fromValue(schemaName);
		assertThat(schemEnum, equalTo(CELLTRACE));
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldFailToGetSchemaWhenThereIsNoSchemaWithThatName() {
		String schemaName = "noSchemHasThisAsAnName";
		SchemaEnum.fromValue(schemaName);
	}

}
