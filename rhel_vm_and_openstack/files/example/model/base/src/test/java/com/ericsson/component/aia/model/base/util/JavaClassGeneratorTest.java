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

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.ericsson.component.aia.model.base.meta.schema.EventParameter;
import org.junit.*;
import org.junit.rules.ExpectedException;

public class JavaClassGeneratorTest {

    private static final String L3MESSAGE_CONTENTS = "L3MESSAGE_CONTENTS";

    private static final String EVENT_ARRAY_ERAB_RELEASE_FAILURE_3GPP_CAUSE = "EVENT_ARRAY_ERAB_RELEASE_FAILURE_3GPP_CAUSE";

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private EventParameter mockedEventParameter;

    @Before
    public void setup() {
        mockedEventParameter = mock(EventParameter.class);
    }

    @Test
    public void testAns1ParameterReturnCorrectMethodName() {
        when(mockedEventParameter.getName()).thenReturn(L3MESSAGE_CONTENTS);
        assertThat(JavaClassGenerator.getAsn1Param(mockedEventParameter), is("getL3MESSAGE_CONTENTS()"));
    }

    @Test
    public void testAns1ParameterReturnEmptyStringIfParameterNotEndsWithMessage() {
        when(mockedEventParameter.getName()).thenReturn(EVENT_ARRAY_ERAB_RELEASE_FAILURE_3GPP_CAUSE);
        assertThat(JavaClassGenerator.getAsn1Param(mockedEventParameter), is(GenerateEventBeansUtil.NULL_STRING));
    }

    @Test
    public void testifParamStartWithEventArraygetNameIncludingDigitAtEnditReturnsNamewithoutlastDigitChar() {
        when(mockedEventParameter.getName()).thenReturn(EVENT_ARRAY_ERAB_RELEASE_FAILURE_3GPP_CAUSE + "_1");
        assertThat(JavaClassGenerator.getArrayParameter(mockedEventParameter), is(EVENT_ARRAY_ERAB_RELEASE_FAILURE_3GPP_CAUSE));
    }

    @Test
    public void testifParamDontStartwithEventArrayReturnsNull() {
        when(mockedEventParameter.getName()).thenReturn(L3MESSAGE_CONTENTS);
        assertNull(JavaClassGenerator.getArrayParameter(mockedEventParameter));
    }

    @Test
    public void testInitializeArrayElement() {
        when(mockedEventParameter.getNumberOfBytes()).thenReturn(1);
        when(mockedEventParameter.isStructLastParameter()).thenReturn(false);
        when(mockedEventParameter.getMaxStructArraySize()).thenReturn(11);
        final String[] testArray = JavaClassGenerator.initializeArrayElement(mockedEventParameter);
        final String[] compareArray = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" };
        assertThat(testArray.length, is(11));
        assertThat(testArray, is(compareArray));

    }

}
