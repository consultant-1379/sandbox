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
package com.ericsson.component.aia.model.base.meta.schema;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import com.ericsson.component.aia.model.base.exception.SchemaException;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Before;
import org.junit.Test;

public class ParameterTest {

    private Parameter objUnderTest;

    private Element gummeiElement;

    private Element mmes1apidElement;

    private Element enbIdElement;

    private Namespace mockedNamespace;

    @Before
    public void setup() throws SchemaException {
        mockedNamespace = Namespace.getNamespace("http://www.ericsson.com/PmEvents");

        gummeiElement = createElement("EVENT_PARAM_GUMMEI", "BINARY", "7", "yes");

        mmes1apidElement = createElement("EVENT_PARAM_MMES1APID", "BINARY", "7", "yes");

        enbIdElement = createElement("EVENT_PARAM_ENB_ID", "UINT", "7", "yes");
        objUnderTest = new Parameter(gummeiElement, mockedNamespace, "EVENT_PARAM_", "EVENT_PARAM_", "celltrace");
    }

    private Element createElement(final String name, final String type, final String numberOfBytes, final String useValid) {
        final Element element = new Element("parametertype", mockedNamespace);
        element.addContent(new Element("name", mockedNamespace).setText(name));
        element.addContent(new Element("type", mockedNamespace).setText(type));
        element.addContent(new Element("description", mockedNamespace).setText("Dummy Event Comment"));
        element.addContent(new Element("numberofbytes", mockedNamespace).setText(numberOfBytes));
        element.addContent(new Element("usevalid", mockedNamespace).setText(useValid));
        return element;
    }

    private Element createBitpackedElement(final String name, final String type, final String useValid, final String numberOfBits) {
        final Element element = new Element("parametertype", mockedNamespace);
        element.addContent(new Element("name", mockedNamespace).setText(name));
        element.addContent(new Element("type", mockedNamespace).setText(type));
        element.addContent(new Element("description", mockedNamespace).setText("Dummy Event Comment"));
        element.addContent(new Element("usevalid", mockedNamespace).setText(useValid));
        element.addContent(new Element("numberofbits", mockedNamespace).setText(numberOfBits));
        return element;
    }

    private Element createBitpackedElement(final String name, final String type, final String useValid) {
        return createBitpackedElement(name, type, useValid, "-1");
    }

    private Element createElementWithRange(final String name, final String type, final String numberOfBytes, final int low, final int high) {
        final Element element = createElement(name, type, numberOfBytes, "yes");

        final Element rangeElement = new Element("range", mockedNamespace);
        rangeElement.addContent(new Element("low", mockedNamespace).setText(String.valueOf(low)));
        rangeElement.addContent(new Element("high", mockedNamespace).setText(String.valueOf(high)));
        element.addContent(rangeElement);
        return element;
    }

    @Test
    public void testNumberofBytesAreNotSetToMinusOne() {
        assertThat(objUnderTest.getNumberOfBytes(), is(6));
    }

    @Test
    public void testVariableLengthIsFalseIfItIsBINARY() {
        assertThat(objUnderTest.isVariableLength(), is(false));
    }

    @Test
    public void testNameisTrimed() {
        assertThat(objUnderTest.getName(), is("GUMMEI"));
    }

    @Test
    public void testisValidLTEembeddedbitFlag() {
        assertFalse(objUnderTest.isValidLTEembeddedbitFlag());
    }

    @Test
    public void testIsValidIsSetToTrue() {
        assertThat(objUnderTest.isUseValid(), is(true));
    }

    @Test
    public void equalsHashcodeContract() throws SchemaException {
        final Parameter equalsObj = new Parameter(gummeiElement, mockedNamespace, "EVENT_PARAM_", "EVENT_PARAM_", "celltrace");
        final Parameter unequalsObj = new Parameter(mmes1apidElement, mockedNamespace, "EVENT_PARAM_", "EVENT_PARAM_", "celltrace");
        final Object differentObj = new Object();
        assertEquals(equalsObj, objUnderTest);
        assertEquals(equalsObj.hashCode(), objUnderTest.hashCode());
        assertFalse(objUnderTest.equals(unequalsObj));
        assertFalse(objUnderTest.equals(differentObj));
    }

    @Test
    public void verifyParametersAreSortedByName() throws SchemaException {
        final Parameter greaterObject = new Parameter(mmes1apidElement, mockedNamespace, "EVENT_PARAM_", "EVENT_PARAM_", "celltrace");
        assertTrue(greaterObject.compareTo(objUnderTest) > 0);
        assertTrue(greaterObject.compare(objUnderTest, greaterObject) < 0);
        assertTrue(objUnderTest.compareTo(greaterObject) < 0);
        assertTrue(greaterObject.compare(greaterObject, objUnderTest) > 0);
        assertTrue(objUnderTest.compareTo(objUnderTest) == 0);
        assertTrue(greaterObject.compare(objUnderTest, objUnderTest) == 0);
    }

    @Test
    public void checkRange_whenTypeOfParameterIsUint_shouldBeNumberOfBytesByNumberOfBitsToPowerOf2Minus1() throws SchemaException {
        final Element uintElement = createElement("EVENT_PARAM_MMES1APID", "UINT", "7", "no");
        final Parameter uintParam = new Parameter(uintElement, mockedNamespace, "EVENT_PARAM_", "EVENT_PARAM_", "celltrace");
        uintParam.checkRange(enbIdElement, mockedNamespace);
        assertArrayEquals(new long[] { 0L, (long) Math.pow(2, 7 * 8) - 1 }, uintParam.getRange());
    }

    @Test
    public void checkRange_whenTypeOfParameterIsLong_shouldBeNumberOfBytesByNumberOfBitsToPowerOf2Minus1() throws SchemaException {
        final Element uintElement = createElement("EVENT_PARAM_MMES1APID", "LONG", "7", "yes");
        final Parameter uintParam = new Parameter(uintElement, mockedNamespace, "EVENT_PARAM_", "EVENT_PARAM_", "celltrace");
        uintParam.checkRange(enbIdElement, mockedNamespace);
        assertArrayEquals(new long[] { 0L, (long) Math.pow(2, 7 * 8) - 1 }, uintParam.getRange());
    }

    @Test
    public void checkRange_whenTypeOfParameterHasRange_shouldBeThatRange() throws SchemaException {
        final Element longElement = createElementWithRange("EVENT_PARAM_MMES1APID", "LONG", "7", 12, 45576);
        final Parameter longParam = new Parameter(longElement, mockedNamespace, "EVENT_PARAM_", "EVENT_PARAM_", "celltrace");
        longParam.checkRange(longElement, mockedNamespace);
        assertArrayEquals(new long[] { 12L, 45576L }, longParam.getRange());
    }

    @Test
    public void handleNonVariableLength_numberOfBytesAndNumberOfBits_shouldBeSetAsPerNumberOfBytes() throws SchemaException{
        final Element uintElement = createElementWithRange("EVENT_PARAM_MMES1APID", "LONG", "7", 12, 45576);
        final Parameter uintParam = new Parameter(uintElement, mockedNamespace, "EVENT_PARAM_", "EVENT_PARAM_", "celltrace");
        uintParam.handleNonVariableLength(uintElement, mockedNamespace);
        assertEquals(56, uintParam.getNumberOfBits());
        assertEquals(7, uintParam.getNumberOfBytes());
    }

    @Test
    public void handleNonVariableLength_numberOfBytesIsMinus1_shouldSetToVariableLength() throws SchemaException {
        final Element uintElement = createElementWithRange("EVENT_PARAM_MMES1APID", "LONG", "-1", 12, 45576);
        final Parameter uintParam = new Parameter(uintElement, mockedNamespace, "EVENT_PARAM_", "EVENT_PARAM_", "celltrace");
        uintParam.handleNonVariableLength(uintElement, mockedNamespace);
        assertEquals(-1, uintParam.getNumberOfBits());
        assertEquals(-1, uintParam.getNumberOfBytes());
        assertEquals(true, uintParam.isVariableLength());
        assertEquals(false, uintParam.isBitPacked());
    }

    @Test
    public void handleNonVariableLength_bitPacked_shouldSetToBitPacked() throws SchemaException{
        final Element uintElement = createBitpackedElement("EVENT_PARAM_MMES1APID", "LONG", "yes", "17");
        final Parameter uintParam = new Parameter(uintElement, mockedNamespace, "EVENT_PARAM_", "EVENT_PARAM_", "celltrace");
        uintParam.handleNonVariableLength(uintElement, mockedNamespace);
        assertEquals(17, uintParam.getNumberOfBits());
        assertEquals(3, uintParam.getNumberOfBytes());
        assertEquals(true, uintParam.isBitPacked());
        assertEquals(false, uintParam.isVariableLength());
    }

    @Test
    public void handleNonVariableLength_bitPackedNumberOfBitsMinus1_shouldSetToBitPacked() throws SchemaException {
        final Element uintElement = createBitpackedElement("EVENT_PARAM_MMES1APID", "LONG", "yes");
        final Parameter uintParam = new Parameter(uintElement, mockedNamespace, "EVENT_PARAM_", "EVENT_PARAM_", "celltrace");
        uintParam.handleNonVariableLength(uintElement, mockedNamespace);
        assertEquals(-1, uintParam.getNumberOfBits());
        assertEquals(-1, uintParam.getNumberOfBytes());
        assertEquals(true, uintParam.isBitPacked());
        assertEquals(true, uintParam.isVariableLength());
    }

    @Test
    public void setVariableLengthFields_bitPacked1() throws SchemaException {
        final Element uintElement = createBitpackedElement("EVENT_PARAM_MMES1APID", "LONG", "yes");
        final Parameter uintParam = new Parameter(uintElement, mockedNamespace, "EVENT_PARAM_", "EVENT_PARAM_", "celltrace");
        uintParam.setVariableLengthFields(uintElement, mockedNamespace);
        assertEquals(-1, uintParam.getNumberOfBits());
        assertEquals(1, uintParam.getNumberOfBytes());
        assertEquals(true, uintParam.isBitPacked());
        assertEquals(true, uintParam.isVariableLength());
    }

    @Test
    public void setVariableLengthFields_bitPacked() throws SchemaException {
        final Element uintElement = createElement("EVENT_PARAM_MMES1APID", "LONG", "7", "yes");
        final Parameter uintParam = new Parameter(uintElement, mockedNamespace, "EVENT_PARAM_", "EVENT_PARAM_", "celltrace");
        uintParam.setVariableLengthFields(uintElement, mockedNamespace);
        assertEquals(56, uintParam.getNumberOfBits());
        assertEquals(-1, uintParam.getNumberOfBytes());
        assertEquals(false, uintParam.isBitPacked());
        assertEquals(true, uintParam.isVariableLength());
    }
}
