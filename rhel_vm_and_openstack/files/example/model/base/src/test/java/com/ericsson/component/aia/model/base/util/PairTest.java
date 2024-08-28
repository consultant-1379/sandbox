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

import org.junit.Test;

public class PairTest {

    @Test
    public void equalsHashCode_equalObjects_sameHashcode() {
        final Pair<String, String> obj1 = new Pair<String, String>("first", "second");
        final Pair<String, String> obj2 = new Pair<String, String>("first", "second");

        assertEquals(obj1, obj2);
        assertEquals(obj1.hashCode(), obj2.hashCode());
    }

    @Test
    public void equalsHashCode_equalObjectsNullFirstValue_sameHashcode() {
        final Pair<String, String> obj1 = new Pair<String, String>(null, "second");
        final Pair<String, String> obj2 = new Pair<String, String>(null, "second");

        assertEquals(obj1, obj2);
        assertEquals(obj1.hashCode(), obj2.hashCode());
    }

    @Test
    public void equalsHashCode_equalObjectsNullSecondValue_sameHashcode() {
        final Pair<String, String> obj1 = new Pair<String, String>("first", null);
        final Pair<String, String> obj2 = new Pair<String, String>("first", null);

        assertEquals(obj1, obj2);
        assertEquals(obj1.hashCode(), obj2.hashCode());
    }

    @Test
    public void equals_differentObjects() {
        final Pair<String, String> obj1 = new Pair<String, String>("first", "second");
        final String obj2 = "first";

        assertFalse(obj1.equals(obj2));
    }

    @Test
    public void equals_sameObjectsDifferentSecondValues() {
        final Pair<String, String> obj1 = new Pair<String, String>("first", "second");
        final Pair<String, String> obj2 = new Pair<String, String>("first", "third");

        assertFalse(obj1.equals(obj2));
    }

    @Test
    public void equals_sameObjectsFirsValueNull() {
        final Pair<String, String> obj1 = new Pair<String, String>(null, "second");
        final Pair<String, String> obj2 = new Pair<String, String>("first", "second");

        assertFalse(obj1.equals(obj2));
    }

    @Test
    public void equals_sameObjectsFirstValueNull2() {
        final Pair<String, String> obj1 = new Pair<String, String>("first", null);
        final Pair<String, String> obj2 = new Pair<String, String>("first", "second");

        assertFalse(obj1.equals(obj2));
    }

    @Test
    public void equals_sameObjectsDifferentFirstValue() {
        final Pair<String, String> obj1 = new Pair<String, String>("first", "second");
        final Pair<String, String> obj2 = new Pair<String, String>("third", "second");

        assertFalse(obj1.equals(obj2));
    }

    @Test
    public void equals_sameObjectsAllDifferentValues() {
        final Pair<String, String> obj1 = new Pair<String, String>("first", "second");
        final Pair<String, String> obj2 = new Pair<String, String>("second", "first");

        assertFalse(obj1.equals(obj2));
    }
}
