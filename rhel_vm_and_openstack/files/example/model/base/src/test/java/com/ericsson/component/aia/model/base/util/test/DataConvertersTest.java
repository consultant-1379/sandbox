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
package com.ericsson.component.aia.model.base.util.test;

import static com.ericsson.component.aia.model.eventbean.DefaultValues.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.ericsson.component.aia.model.base.util.DataConverters;

/**
 *
 */
public class DataConvertersTest {

    private static final String WORD = "word";

    @Test
    public void testGetShort_IsValidIsFalse() {
        testGetShort(new byte[] { 1, 2 }, false, false, false, (short) 258);
    }

    @Test
    public void testGetShort_ValidityByteSetToOK() {
        final short expectedValue = 258;
        final byte[] data = new byte[] { 1, 2, 3 };
        testGetShort(data, true, false, true, expectedValue);
    }

    @Test
    public void testGetShort_ValidityByteNotSetToOK() {
        final short expectedValue = DEFAULT_SHORT_VALUE;
        final byte[] data = new byte[] { -128, 1, 2, 3 };
        testGetShort(data, true, false, false, expectedValue);
    }

    private void testGetShort(final byte[] data, final boolean useValid, final boolean isOptional, final boolean validLTEembeddedbitFlag,
                              final short expectedValue) {
        final short result = DataConverters.getShort(data, 0, useValid, isOptional, validLTEembeddedbitFlag, 2);
        assertThat(result, is(expectedValue));
    }

    @Test
    public void testGetByte_ValidityByteNotSet() {
        testGetByte(new byte[] { 1 }, false, false, false, (byte) 1);
    }

    @Test
    public void testGetByte_ValidityByteSetToNotOK() {
        testGetByte(new byte[] { -1, 0 }, true, false, false, DEFAULT_BYTE_VALUE);
    }

    @Test
    public void testGetByte_ValidityByteSetToOK() {
        final byte expectedValue = 0;
        final byte[] data = new byte[] { 0, expectedValue };
        testGetByte(data, true, false, false, expectedValue);
    }

    private void testGetByte(final byte[] data, final boolean useValid, final boolean isOptional, final boolean validLTEembeddedbitFlag,
                             final byte expectedValue) {
        final int offset = 0;
        final int numberOfBytes = 1;
        final byte result = DataConverters.getByte(data, offset, useValid, isOptional, validLTEembeddedbitFlag, numberOfBytes);
        assertThat(result, is(expectedValue));
    }

    @Test
    public void testgetByteArrayDottedDecimalString() {
        final byte[] data = "10.10.10.10".getBytes();
        final String result = DataConverters.getByteArrayDottedDecimalString(data, 0, false, false, false, data.length);
        assertThat(result, is("49.48.46.49.48.46.49.48.46.49.48"));
    }

    @Test
    public void testgetByteArrayDottedDecimalStringifUseValidTrue() {
        final byte[] data = new byte[] { 0, 10, 0, 0, 1 };
        final String result = DataConverters.getByteArrayDottedDecimalString(data, 0, true, false, false, data.length - 1);
        assertThat(result, is("10.0.0.1"));
    }

    @Test
    public void testgetByteArrayDottedDecimalStringifUseValidTrueWithFlag1() {
        final byte[] data = new byte[] { -128, 10, 0, 0, 1 };
        final String result = DataConverters.getByteArrayDottedDecimalString(data, 0, true, false, false, data.length - 1);
        assertEquals(null, result);
    }

    @Test
    public void testgetByteArrayDottedDecimalString_nullString_expectNull() {
        final byte[] data = "10.10.10.10".getBytes();
        final String result = DataConverters.getByteArrayDottedDecimalString(data, 0, false, false, false, 0);
        assertEquals(null, result);
    }

    @Test
    public void testGetString_NoValidityByte() {
        testGetString(WORD.getBytes(), WORD, false, false, false);
    }

    @Test
    public void testGetString_ValidityByteSetToOK() {
        testGetStringWithValidityByte((byte) 0, WORD, false, false, false);
    }

    @Test
    public void testGetString_ValidityByteSetToNotOK() {
        testGetStringWithValidityByte((byte) -128, null, true, false, false);
    }

    @Test
    public void testGetString_ValidityByteSetToOKAndEmbeddedFlagTrue() {
        testGetStringWithValidityByte((byte) 0, WORD, true, false, true);
    }

    private void testGetStringWithValidityByte(final byte validityByte, final String expectedResult, final boolean isUseValid,
                                               final boolean isOptional, final boolean validLTEembeddedbitFlag) {
        final byte[] data = getStringBytes(validityByte);
        testGetString(data, expectedResult, isUseValid, isOptional, validLTEembeddedbitFlag);
    }

    private byte[] getStringBytes(final byte validityByte) {
        final byte[] wordBytes = WORD.getBytes();
        final int wordLength = wordBytes.length;
        final int dataLength = wordLength + 1;
        final byte[] data = new byte[dataLength];
        data[0] = validityByte;
        for (int i = 0; i < wordLength; i++) {
            data[i + 1] = wordBytes[i];
        }
        return data;
    }

    private void testGetString(final byte[] data, final String expectedResult, final boolean isUseValid, final boolean isOptional,
                               final boolean validLTEembeddedbitFlag) {
        assertThat(DataConverters.getString(data, 0, isUseValid, isOptional, validLTEembeddedbitFlag, data.length), is(expectedResult));
    }

    @Test
    public void testgetByteArrayTBCDString() {
        final byte[] data = new byte[] { 7, 5, 6, 4, (byte) 0xff, (byte) 0xff, (byte) 0xff };
        final int offset = 0;
        final int bytes = 7;
        final String result = DataConverters.getByteArrayTBCDString(data, offset, false, false, false, bytes);
        assertThat(result, is("70506040"));
    }

    @Test
    public void testgetByteArrayTBCDStringwithOutPadding() {
        final byte[] data = new byte[] { 0, 16, 3, 96, 0, -111, -107, -106, 16 };
        final int offset = 0;
        final int bytes = 8;
        final String result = DataConverters.getByteArrayTBCDString(data, offset, true, false, false, bytes);
        assertThat(result, is("0130060019596901"));
    }

    @Test
    public void testgetByteArrayTBCDStringwithPadding() {
        final byte[] data = new byte[] { 88, -110, -121, 36, 38, -11, (byte) 0xff, (byte) 0xff, (byte) 0xff };
        final int offset = 0;
        final int bytes = 9;
        final String result = DataConverters.getByteArrayTBCDString(data, offset, false, false, false, bytes);
        assertThat(result, is("85297842625"));
    }

    @Test
    public void testgetByteArrayTBCDStringwithPaddingWithHighNibble() {
        final byte[] data = new byte[] { 88, -110, -121, 36, 38, -11, (byte) 0x8f, (byte) 0xff, (byte) 0xff };
        final int offset = 0;
        final int bytes = 9;
        final String result = DataConverters.getByteArrayTBCDString(data, offset, false, false, false, bytes);
        assertThat(result, is("852978426258"));
    }

    @Test
    public void testgetByteArrayTBCDStringwithPaddingWithLLowNibble() {
        final byte[] data = new byte[] { 88, -110, -121, 36, 38, -11, (byte) 0xf8, (byte) 0xff, (byte) 0xff };
        final int offset = 0;
        final int bytes = 9;
        final String result = DataConverters.getByteArrayTBCDString(data, offset, false, false, false, bytes);
        assertThat(result, is("852978426258"));
    }

    @Test
    public void testgetByteArrayTBCDStringWithIsUseValidTrue() {
        final byte[] data = new byte[] { 0, 7, 5, 6, 4 };
        final int offset = 0;
        final int bytes = 4;
        final String result = DataConverters.getByteArrayTBCDString(data, offset, true, false, false, bytes);
        assertThat(result, is("70506040"));
    }

    @Test
    public void testgetByteArrayTBCDStringWithIsUseValidTrueAndValidtyBytefalse() {
        final byte[] data = new byte[] { -128, 7, 5, 6, 4 };
        final int offset = 0;
        final int bytes = 4;
        final String result = DataConverters.getByteArrayTBCDString(data, offset, true, false, false, bytes);
        assertEquals(result, null);
    }

    @Test
    public void getByteArrayTBCDInteger() {
        final byte[] data = new byte[] { 7, 5, 6, 4 };
        final int offset = 0;
        final int bytes = 4;
        final long result = DataConverters.getByteArrayTBCDInteger(data, offset, bytes);
        assertEquals(70506040, result);

    }

    @Test
    public void testgetByteArrayIBCD() {
        final byte[] data = new byte[] { 3, 4 };
        final int offset = 0;
        final int bytes = 2;
        final int bits = 16;
        final int result = DataConverters.getByteArrayIBCD(data, offset, false, false, false, bytes, bits);
        assertThat(result, is(4030));
    }

    @Test
    public void testgetByteArrayIBCDWithUseValidFlag() {
        final byte[] data = new byte[] { -128, 3, 4 };
        final int offset = 0;
        final int bytes = 2;
        final int bits = 16;
        final int result = DataConverters.getByteArrayIBCD(data, offset, true, false, false, bytes, bits);
        assertThat(result, is(DEFAULT_INT_VALUE));
    }

    @Test
    public void testgetByteArrayIBCDWithValidFlag() {
        final byte[] data = new byte[] { 0, 3, 4 };
        final int offset = 0;
        final int bytes = 2;
        final int bits = 16;
        final int result = DataConverters.getByteArrayIBCD(data, offset, true, false, false, bytes, bits);
        assertThat(result, is(4030));
    }

    @Test
    public void testgetByteArray3GPPString_EmptyWord() {
        getGetByteArray3GPPString("", null);
    }

    @Test
    public void testgetByteArray3GPPString_ValidWord() {
        getGetByteArray3GPPString("a", "a");
    }

    private void getGetByteArray3GPPString(final String wordToEncode, final String expectedResult) {
        final byte[] wordInBytes = wordToEncode.getBytes();
        final List<Byte> inputString = new ArrayList<Byte>();
        inputString.add((byte) wordInBytes.length); //length of word
        for (final Byte element : wordInBytes) {
            inputString.add(element);
        }

        final byte[] data = new byte[inputString.size()];
        for (int i = 0; i < inputString.size(); i++) {
            data[i] = inputString.get(i);
        }

        final String result = DataConverters.getByteArray3GPPString(data, 0, false, false, false, data.length);
        assertThat(result, is(expectedResult));

    }

    @Test
    public void testgetByteArray_NoValidityByte() {
        getGetByteArray(new byte[] { 0, 1, 2, 3 }, new byte[] { 1, 2, 3 });
    }

    private void getGetByteArray(final byte[] data, final byte[] expectedResult) {
        final byte[] result = DataConverters.getByteArrayByteArray(data, 1, false, false, false, 3);
        assertThat(result, is(expectedResult));
    }

    @Test
    public void testgetByteArrayHexString() {
        final String result = DataConverters.getByteArrayHexString(new byte[] { 1, 2, 3 }, 1, 2);
        assertThat(result, is("0203"));
    }

    @Test
    public void testGetBoolean_NoValidityByte() {
        final boolean result = DataConverters.getByteArrayBoolean(new byte[] { 0, -128, 1 }, 1, true, false, false, 1);
        assertThat(result, is(false));
    }

    @Test
    public void testGetBoolean_NoValidityByteAndEmbeddedFlagTrue() {
        final boolean result = DataConverters.getByteArrayBoolean(new byte[] { 0, 0, 0 }, 0, true, false, true, 1);
        assertThat(result, is(true));
    }

    @Test
    public void testGetBoolean_ValidityByteSetTo1() {
        testGetBooleanWithValidityByte((byte) 1, (byte) 0, false);
    }

    @Test
    public void testGetBoolean_ValidityByteSetTo0() {
        testGetBooleanWithValidityByte((byte) 0, (byte) 0, true);
    }

    private void testGetBooleanWithValidityByte(final byte validityByte, final byte data, final boolean expectedResult) {
        final boolean result = DataConverters.getByteArrayBoolean(new byte[] { 0, 0, validityByte, data }, 2, false, false, false, 1);
        assertThat(result, is(expectedResult));
    }

    @Test
    public void getByteArraySignedInteger() {
        final long result = DataConverters.getByteArraySignedInteger(new byte[] { 0, 1, (byte) 255, 127, (byte) 128, 5 }, 2, 4);
        assertEquals(-8421371L, result);
    }

    @Test
    public void getByteArrayUnSignedInteger() {
        final long result = DataConverters.getByteArrayUnsignedInteger(new byte[] { 0, 1, (byte) 255, 127, (byte) 128, 5 }, 2, 4);
        assertEquals(4286545925L, result);
    }

    @Test
    public void flagString_true_expectFirstString() {
        assertEquals("one", DataConverters.flagString(true, "one", "two"));
    }

    @Test
    public void flagString_flase_expectFirstString() {
        assertEquals("two", DataConverters.flagString(false, "one", "two"));
    }

    @Test
    public void getLongHexString_zeroNumberZeroDigits_emptyString() {
        assertEquals("", DataConverters.getLongHexString(0, 0));
    }

    @Test
    public void getLongHexString_zeroNumberOneDigit_stringWithOneZero() {
        assertEquals("0", DataConverters.getLongHexString(0, 1));
    }

    @Test
    public void getLongHexString_NumberOneDigit_stringWithNumberNoLeadingZeros() {
        assertEquals("1", DataConverters.getLongHexString(1, 1));
    }

    @Test
    public void getLongHexString_NumberTwoDigits_stringWithNumberWithLeadingZeros() {
        assertEquals("01", DataConverters.getLongHexString(1, 2));
    }

    @Test
    public void compareByteArrays_firstArrayShorter() {
        final byte[] array1 = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7 };
        final byte[] array2 = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8 };
        assertEquals(-1, DataConverters.compareByteArrays(array1, array2));
    }

    @Test
    public void compareByteArrays_secondArrayShorter() {
        final byte[] array1 = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7 };
        final byte[] array2 = new byte[] { 0, 1, 2, 3, 4 };
        assertEquals(3, DataConverters.compareByteArrays(array1, array2));
    }

    @Test
    public void compareByteArrays_equalArrays() {
        final byte[] array1 = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7 };
        final byte[] array2 = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7 };
        assertEquals(0, DataConverters.compareByteArrays(array1, array2));
    }

    @Test
    public void compareByteArrays_unequalArrays() {
        final byte[] array1 = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7 };
        final byte[] array2 = new byte[] { 0, 1, 2, 5, 4, 5, 6, 7 };
        assertEquals(-2, DataConverters.compareByteArrays(array1, array2));
    }

    @Test
    public void getByteArrayCCString_lengthMinus1() {
        assertEquals("TTTUV", DataConverters.getByteArrayCCString(new byte[] { 0, 5, 84, 84, 84, 85, 86, 87 }, 2, false, false, false, -1));
    }

    @Test
    public void getByteArrayCCString_lengthMinus2() {
        assertEquals(null, DataConverters.getByteArrayCCString(new byte[] { 0, 5, 84, 84, 84, 85, 86, 87 }, 2, false, false, false, -2));
    }

    @Test
    public void getByteArrayCCString_normalLength() {
        assertEquals("TTTTTUVW", DataConverters.getByteArrayCCString(new byte[] { 84, 84, 84, 84, 84, 85, 86, 87 }, 0, false, false, false, 8));
    }

    @Test
    public void testgetByteArrayByteArrayWithvalidFlag() {
        final byte[] expected = new byte[] { 68, -16, 0, -128, 0, 3 };
        assertArrayEquals(expected, DataConverters.getByteArrayByteArray(new byte[] { 0, 68, -16, 0, -128, 0, 3 }, 0, true, false, false, 6));
    }

    @Test
    public void testgetByteArrayByteArrayWithLengthInLast2Bytes() {
        final byte[] expected = new byte[] { 68, -16, 0, -128, 0, 3 };
        assertArrayEquals(expected, DataConverters.getByteArrayByteArray(new byte[] { 0, 6, 68, -16, 0, -128, 0, 3 }, 2, false, false, false, -1));
    }

    @Test
    public void testgetByteArrayByteArrayWithLengthInLast2BytesAndLengthIsZero() {
        final byte[] expected = new byte[] {};
        assertArrayEquals(expected, DataConverters.getByteArrayByteArray(new byte[] { 0, 0, 68, -16, 0, -128, 0, 3 }, 2, false, false, false, -1));
    }

    @Test
    public void testgetByteArrayByteArrayWithInvalidFlage() {
        final byte[] expected = new byte[] {};
        assertArrayEquals(expected, DataConverters.getByteArrayByteArray(new byte[] { -128, 68, -16, 0, -128, 0, 3 }, 0, true, false, false, 6));
    }

    @Test
    public void testgetByteArrayByteArrayWithvalidFlagEmbedded() {
        final byte[] expected = new byte[] { 68, -16, 0, -128, 0, 3 };
        assertArrayEquals(expected, DataConverters.getByteArrayByteArray(new byte[] { 68, -16, 0, -128, 0, 3 }, 0, true, false, true, 6));
    }

    @Test
    public void testgetBytevalidFlage() {
        assertEquals(6, DataConverters.getByte(new byte[] { 6 }, 0, true, false, true, 1));
    }

    @Test
    public void testgetByteInvalidFlage() {
        assertEquals(DEFAULT_BYTE_VALUE, DataConverters.getByte(new byte[] { -128 }, 0, true, false, true, 1));
    }

    @Test
    public void testgetByteArrayDouble() {
        final double expectedvalue = 2.25;
        final byte[] data = getDoubleByteArray(expectedvalue);
        assertEquals(expectedvalue, DataConverters.getByteArrayDouble(data, 0, false, false, false, data.length), 0);
    }

    @Test
    public void testgetByteArrayDoubleAWithValidBit() {
        final double expectedvalue = 2.25;
        final byte[] data = getDoubleByteArray(expectedvalue);
        final byte[] bytesWithValidtyByte = new byte[9];
        System.arraycopy(data, 0, bytesWithValidtyByte, 1, data.length);
        bytesWithValidtyByte[0] = 0;
        assertEquals(expectedvalue, DataConverters.getByteArrayDouble(bytesWithValidtyByte, 0, true, false, false, data.length), 0);
    }

    @Test
    public void testgetByteArrayDoubleAWithValidBitFalse() {
        final double expectedvalue = 2.25;
        final byte[] data = getDoubleByteArray(expectedvalue);
        final byte[] bytesWithValidtyByte = new byte[9];
        System.arraycopy(data, 0, bytesWithValidtyByte, 1, data.length);
        bytesWithValidtyByte[0] = -128;
        assertEquals(DEFAULT_DOUBLE_VALUE, DataConverters.getByteArrayDouble(bytesWithValidtyByte, 0, true, false, false, data.length), 0);
    }

    @Test
    public void testgetByteArrayDoubleAWitOptionalBitFalse() {
        final double expectedvalue = 2.25;
        final byte[] data = getDoubleByteArray(expectedvalue);
        final byte[] bytesWithValidtyByte = new byte[9];
        System.arraycopy(data, 0, bytesWithValidtyByte, 1, data.length);
        bytesWithValidtyByte[0] = -128;
        assertEquals(DEFAULT_DOUBLE_VALUE, DataConverters.getByteArrayDouble(bytesWithValidtyByte, 0, false, true, false, data.length), 0);
    }

    @Test
    public void testgetByteArrayDoubleWithValidBitTrue() {
        final double expectedvalue = 2.25;
        final byte[] data = getDoubleByteArray(expectedvalue);
        assertEquals(expectedvalue, DataConverters.getByteArrayDouble(data, 0, true, false, true, data.length), 0);
    }

    @Test
    public void testgetByteArrayDoubleWitOptionalBitTrue() {
        final double expectedvalue = 2.25;
        final byte[] data = getDoubleByteArray(expectedvalue);
        assertEquals(expectedvalue, DataConverters.getByteArrayDouble(data, 0, false, true, true, data.length), 0);
    }

    private byte[] getDoubleByteArray(final double expectedvalue) {
        final byte[] data = new byte[8];
        ByteBuffer.wrap(data).putDouble(expectedvalue);
        return data;
    }

    @Test
    public void testgetLong() {
        final long expectedvalue = Long.MAX_VALUE;
        final byte[] data = getLongByteArray(expectedvalue);
        assertEquals(expectedvalue, DataConverters.getLong(data, 0, true, false, true, data.length), 0);
    }

    @Test
    public void testgetLongAWithValidBitFalse() {
        final long expectedvalue = Long.MAX_VALUE;
        final byte[] data = getLongByteArray(expectedvalue);
        final byte[] bytesWithValidtyByte = new byte[9];
        System.arraycopy(data, 0, bytesWithValidtyByte, 1, data.length);
        bytesWithValidtyByte[0] = -128;
        assertEquals(DEFAULT_LONG_VALUE, DataConverters.getLong(bytesWithValidtyByte, 0, true, false, true, data.length), 0);
    }

    @Test
    public void testgetLongAWithOptionalBitFalse() {
        final long expectedvalue = Long.MAX_VALUE;
        final byte[] data = getLongByteArray(expectedvalue);
        final byte[] bytesWithValidtyByte = new byte[9];
        System.arraycopy(data, 0, bytesWithValidtyByte, 1, data.length);
        bytesWithValidtyByte[0] = -128;
        assertEquals(DEFAULT_LONG_VALUE, DataConverters.getLong(bytesWithValidtyByte, 0, false, true, true, data.length), 0);
    }

    private byte[] getLongByteArray(final long value) {
        final ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.putLong(value);
        return buffer.array();
    }

    @Test
    public void testgetUnsignedIntegerAsInteger() {
        final int expectedvalue = Integer.MAX_VALUE;
        final byte[] data = getIntegerByteArray(expectedvalue);
        assertEquals(expectedvalue, DataConverters.getUnsignedIntegerAsInteger(data, 0, true, false, true, data.length), 0);
    }

    @Test
    public void testgetUnsignedIntegerAsIntegerWithOptionalBitTrue() {
        final int expectedvalue = Integer.MAX_VALUE;
        final byte[] data = getIntegerByteArray(expectedvalue);
        assertEquals(expectedvalue, DataConverters.getUnsignedIntegerAsInteger(data, 0, false, true, true, data.length), 0);
    }

    @Test
    public void testgetUnsignedIntegerAsIntegerAWithValidBitFalse() {
        final int expectedvalue = Integer.MAX_VALUE;
        final byte[] data = getIntegerByteArray(expectedvalue);
        final byte[] bytesWithValidtyByte = new byte[9];
        System.arraycopy(data, 0, bytesWithValidtyByte, 1, data.length);
        bytesWithValidtyByte[0] = -128;
        assertEquals(Integer.MIN_VALUE, DataConverters.getUnsignedIntegerAsInteger(bytesWithValidtyByte, 0, true, false, true, data.length), 0);
    }

    @Test
    public void testgetUnsignedIntegerAsIntegerAWithOptionlBitTrue() {
        final int expectedvalue = Integer.MAX_VALUE;
        final byte[] data = getIntegerByteArray(expectedvalue);
        final byte[] bytesWithValidtyByte = new byte[9];
        System.arraycopy(data, 0, bytesWithValidtyByte, 1, data.length);
        bytesWithValidtyByte[0] = -128;
        assertEquals(Integer.MIN_VALUE, DataConverters.getUnsignedIntegerAsInteger(bytesWithValidtyByte, 0, false, true, true, data.length), 0);
    }

    @Test
    public void testgetShortArray() {
        final short[] expectedValye = new short[] {};
        final byte[] data = new byte[] { 0, 0, 0, 0 };
        assertArrayEquals(expectedValye, DataConverters.getShortArray(data, 0, true, false, true, data.length));
    }

    private byte[] getIntegerByteArray(final int Value) {
        final ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.putInt(Value);
        return buffer.array();
    }

}
