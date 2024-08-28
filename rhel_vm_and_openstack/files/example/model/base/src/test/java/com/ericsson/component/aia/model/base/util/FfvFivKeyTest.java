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

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class FfvFivKeyTest {
    @Test
    public void equalsHashCode_equalObjects_sameHashcode() {
        final FfvFivKey obj1 = new FfvFivKey("101/1022-HRB105500", "T", "AA11");
        final FfvFivKey obj2 = new FfvFivKey("101/1022-HRB105500", "T", "AA11");

        assertEquals(obj1, obj2);
        assertEquals(obj1.hashCode(), obj2.hashCode());
    }

    @Test
    public void equalsHashCode_equalObjectsNullFirstValue_sameHashcode() {
        final FfvFivKey obj1 = new FfvFivKey(null, "T", "AA11");
        final FfvFivKey obj2 = new FfvFivKey("101/1022-HRB105500", "T", "AA11");
        assertEquals(obj1, obj2);
        assertEquals(obj1.hashCode(), obj2.hashCode());
    }

    @Test
    public void equalsHashCode_equalSecondObjectiFirstValueisNull() {
        final FfvFivKey obj1 = new FfvFivKey("101/1022-HRB105500", "T", "AA11");
        final FfvFivKey obj2 = new FfvFivKey(null, "T", "AA11");
        assertEquals(obj1, obj2);
        assertEquals(obj1.hashCode(), obj2.hashCode());
    }

    @Test
    public void equalsHashCode_BothObjectFirstValueIsNull() {
        final FfvFivKey obj1 = new FfvFivKey(null, "T", "AA11");
        final FfvFivKey obj2 = new FfvFivKey(null, "T", "AA11");
        assertEquals(obj1, obj2);
        assertEquals(obj1.hashCode(), obj2.hashCode());
    }

    @Test
    public void equalsHashCode_FirstObjectSecondValueIsNull() {
        final FfvFivKey obj1 = new FfvFivKey("101/1022-HRB105500", null, "AA11");
        final FfvFivKey obj2 = new FfvFivKey("101/1022-HRB105500", "T", "AA11");
        assertNotSame(obj1, obj2);
        assertNotSame(obj1.hashCode(), obj2.hashCode());
    }

    @Test
    public void equalsHashCode_SecondObjectSecondValueIsNull() {
        final FfvFivKey obj1 = new FfvFivKey("101/1022-HRB105500", "T", "AA11");
        final FfvFivKey obj2 = new FfvFivKey("101/1022-HRB105500", null, "AA11");
        assertNotSame(obj1, obj2);
        assertNotSame(obj1.hashCode(), obj2.hashCode());
    }

    @Test
    public void equals_sameObjectsSamleValues() {
        final FfvFivKey obj1 = new FfvFivKey("101/1022-HRB105500", "T", "AA11");
        final FfvFivKey obj2 = new FfvFivKey("101/1022-HRB105500", "T", "AA11");

        assertTrue(obj1.equals(obj2));
    }

    @Test
    public void equals_sameObjectsFirstValueIsNull() {
        final FfvFivKey obj1 = new FfvFivKey("101/1022-HRB105500", "T", "AA11");
        final FfvFivKey obj2 = new FfvFivKey(null, "T", "AA11");

        assertTrue(obj1.equals(obj2));
    }

    @Test
    public void equals_sameObjectsFirstValueOfFirstObjectIsNull() {
        final FfvFivKey obj1 = new FfvFivKey(null, "T", "AA11");
        final FfvFivKey obj2 = new FfvFivKey("101/1022-HRB105500", "T", "AA11");

        assertTrue(obj1.equals(obj2));
    }

    @Test
    public void equals_sameObjectsBothFirstObjectIsNull() {
        final FfvFivKey obj1 = new FfvFivKey(null, "T", "AA11");
        final FfvFivKey obj2 = new FfvFivKey(null, "T", "AA11");

        assertTrue(obj1.equals(obj2));
    }

    @Test
    public void equals_sameObjectsSecondObjectIsNull() {
        final FfvFivKey obj1 = new FfvFivKey("101/1022-HRB105500", null, "AA11");
        final FfvFivKey obj2 = new FfvFivKey("101/1022-HRB105500", "T", "AA11");

        assertFalse(obj1.equals(obj2));
    }

    @Test
    public void equals_sameObjectsSecondObjectSecondValueIsNull() {
        final FfvFivKey obj1 = new FfvFivKey("101/1022-HRB105500", "T", "AA11");
        final FfvFivKey obj2 = new FfvFivKey("101/1022-HRB105500", null, "AA11");

        assertFalse(obj1.equals(obj2));
    }

    @Test
    public void equals_sameObjectsBothObjectSecondValueIsNull() {
        final FfvFivKey obj1 = new FfvFivKey("101/1022-HRB105500", null, "AA11");
        final FfvFivKey obj2 = new FfvFivKey("101/1022-HRB105500", null, "AA11");

        assertTrue(obj1.equals(obj2));
    }

    @Test
    public void equals_sameObjectsThirdObjectIsNull() {
        final FfvFivKey obj1 = new FfvFivKey("101/1022-HRB105500", "T", null);
        final FfvFivKey obj2 = new FfvFivKey("101/1022-HRB105500", "T", "AA11");

        assertFalse(obj1.equals(obj2));
    }

    @Test
    public void equals_sameObjectsSecondObjectThirdValueIsNull() {
        final FfvFivKey obj1 = new FfvFivKey("101/1022-HRB105500", "T", "AA11");
        final FfvFivKey obj2 = new FfvFivKey("101/1022-HRB105500", "T", null);

        assertFalse(obj1.equals(obj2));
    }

    @Test
    public void equals_sameObjectsBothObjectThirdValueIsNull() {
        final FfvFivKey obj1 = new FfvFivKey("101/1022-HRB105500", "T", null);
        final FfvFivKey obj2 = new FfvFivKey("101/1022-HRB105500", "T", null);

        assertTrue(obj1.equals(obj2));
    }

    @Test
    public void equals_sameObjectsAllDifferentValues() {
        final FfvFivKey obj1 = new FfvFivKey("CXP9018505/21", "T", "RH5");
        final FfvFivKey obj2 = new FfvFivKey("101/1022-HRB105500", "T", "AA11");

        assertFalse(obj1.equals(obj2));
    }

    @Test
    public void equals_sameObjectsAllNullValues() {
        final FfvFivKey obj1 = new FfvFivKey(null, null, null);
        final FfvFivKey obj2 = new FfvFivKey(null, null, null);

        assertTrue(obj1.equals(obj2));
    }

    @Test
    public void hascodeAndEqualWorksInHashMapifFirstValueIsNull() {
        final FfvFivKey obj = new FfvFivKey("101/1022-HRB105500", "T", "AA11");
        final FfvFivKey obj1 = new FfvFivKey(null, "T", "AA11");

        final Map<FfvFivKey, String> hasMap = new HashMap<FfvFivKey, String>();
        hasMap.put(obj, "Object");

        assertEquals("Object", hasMap.get(obj1));

    }

    @Test
    public void hascodeAndEqualWorksInHashMapifSearchByObjectWhoseFirstValueIsNull() {
        final FfvFivKey obj = new FfvFivKey(null, "T", "AA11");
        final FfvFivKey obj1 = new FfvFivKey("101/1022-HRB105500", "T", "AA11");

        final Map<FfvFivKey, String> hasMap = new HashMap<FfvFivKey, String>();
        hasMap.put(obj, "Object");

        assertEquals("Object", hasMap.get(obj1));

    }

    @Test
    public void hascodeAndEqualWorksInHashMapifSearchByObjectWhoseBothFirstValueIsNull() {
        final FfvFivKey obj = new FfvFivKey(null, "T", "AA11");
        final FfvFivKey obj1 = new FfvFivKey(null, "T", "AA11");

        final Map<FfvFivKey, String> hasMap = new HashMap<FfvFivKey, String>();
        hasMap.put(obj, "Object");

        assertEquals("Object", hasMap.get(obj1));

    }

    @Test
    public void campareTo_equalObjects() {
        final FfvFivKey obj1 = new FfvFivKey("101/1022-HRB105500", "T", "AA11");
        final FfvFivKey obj2 = new FfvFivKey("101/1022-HRB105500", "T", "AA11");

        assertEquals(obj1, obj2);
        assertEquals(0, obj1.compareTo(obj2));
    }

    @Test
    public void campareTo_DifferentObjects() {
        final FfvFivKey obj1 = new FfvFivKey("CXP9018505/21", "T", "AA11");
        final FfvFivKey obj2 = new FfvFivKey("CXP9018505/20", "T", "AA11");

        assertNotSame(obj1, obj2);
        assertEquals(1, obj1.compareTo(obj2));
    }

    @Test
    public void campareTo_FirstObjectFirstvalueIsNull() {
        final FfvFivKey obj1 = new FfvFivKey(null, "T", "AA11");
        final FfvFivKey obj2 = new FfvFivKey("101/1022-HRB105500", "T", "AA11");

        assertEquals(obj1, obj2);
        assertEquals(0, obj1.compareTo(obj2));
    }

    @Test
    public void campareTo_SecondObjectFirstvalueIsNull() {
        final FfvFivKey obj1 = new FfvFivKey("101/1022-HRB105500", "T", "AA11");
        final FfvFivKey obj2 = new FfvFivKey(null, "T", "AA11");

        assertEquals(obj1, obj2);
        assertEquals(0, obj1.compareTo(obj2));
    }

    @Test
    public void campareTo_BothObjectFirstvalueIsNull() {
        final FfvFivKey obj1 = new FfvFivKey(null, "T", "AA11");
        final FfvFivKey obj2 = new FfvFivKey(null, "T", "AA11");

        assertEquals(0, obj1.compareTo(obj2));
    }
    
    @Test
    public void campareTo_BothObjectFirstvalueIsOldTypeAndSecondIsNewType() {
        final FfvFivKey obj1 = new FfvFivKey("101/1022-HRB105500", "T", "AA11");
        final FfvFivKey obj2 = new FfvFivKey("CXP9018505/21", "T", "AA11");

        assertEquals(-1, obj1.compareTo(obj2));
        assertEquals(1, obj2.compareTo(obj1));
    }
    
    @Test
    public void campareTo_BothObjectareNewTypes() {
        final FfvFivKey obj1 = new FfvFivKey("CXP9018505/9", "T", "AA11");
        final FfvFivKey obj2 = new FfvFivKey("CXP9018505/10", "T", "AA11");

        assertEquals(-1, obj1.compareTo(obj2));
        assertEquals(1, obj2.compareTo(obj1));
    }


    @Test
    public void campareTo_FirstObjectSecondvalueIsNull() {
        final FfvFivKey obj1 = new FfvFivKey("101/1022-HRB105500", null, "AA11");
        final FfvFivKey obj2 = new FfvFivKey("101/1022-HRB105500", "T", "AA11");

        assertEquals(-1, obj1.compareTo(obj2));
    }

    @Test
    public void campareTo_SecondObjectSecondvalueIsNull() {
        final FfvFivKey obj1 = new FfvFivKey("101/1022-HRB105500", "T", "AA11");
        final FfvFivKey obj2 = new FfvFivKey("101/1022-HRB105500", null, "AA11");

        assertEquals(1, obj1.compareTo(obj2));
    }

    @Test
    public void campareTo_BothObjectSecondvalueIsNull() {
        final FfvFivKey obj1 = new FfvFivKey("101/1022-HRB105500", null, "AA11");
        final FfvFivKey obj2 = new FfvFivKey("101/1022-HRB105500", null, "AA11");

        assertEquals(0, obj1.compareTo(obj2));
    }

    @Test
    public void campareTo_FirstObjectThirdvalueIsNull() {
        final FfvFivKey obj1 = new FfvFivKey("101/1022-HRB105500", "T", null);
        final FfvFivKey obj2 = new FfvFivKey("101/1022-HRB105500", "T", "AA11");

        assertEquals(-1, obj1.compareTo(obj2));
    }

    @Test
    public void campareTo_SecondObjectThirdvalueIsNull() {
        final FfvFivKey obj1 = new FfvFivKey("101/1022-HRB105500", "T", "AA11");
        final FfvFivKey obj2 = new FfvFivKey("101/1022-HRB105500", "T", null);

        assertEquals(1, obj1.compareTo(obj2));
    }

    @Test
    public void campareTo_BothObjectThirdvalueIsNull() {
        final FfvFivKey obj1 = new FfvFivKey("101/1022-HRB105500", "T", null);
        final FfvFivKey obj2 = new FfvFivKey("101/1022-HRB105500", "T", null);

        assertEquals(0, obj1.compareTo(obj2));
    }

    @Test
    public void testGetIterationFromDocumentNo() {
        assertEquals("21", new FfvFivKey("CXP9018505/21", "T", "R5H").getIteration());
    }
}
