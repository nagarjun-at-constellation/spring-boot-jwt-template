package com.bancocaminos.impactbackendapi.core.utils;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class ImpactStringUtilsTest {

    @Test
    public void notEqualsTrueTest() {
        boolean notEquals = HydraStringUtils.notEquals("cs1", "cs2");
        assertTrue(notEquals);
    }

    @Test
    public void notEqualsFalseTest() {
        boolean notEquals = HydraStringUtils.notEquals("cs1", "cs1");
        assertFalse(notEquals);
    }

    @Test
    public void isNotBlankFalseTest() {
        boolean notBlank = HydraStringUtils.isNotBlank("");
        assertFalse(notBlank);
    }

    @Test
    public void isNotBlankTrueTest() {
        boolean notBlank = HydraStringUtils.isNotBlank("not_blank");
        assertTrue(notBlank);
    }

    @Test
    public void notContainsTrueTest() {
        boolean notContains = HydraStringUtils.notContains("cs1 cs0 cs3", "cs2");
        assertTrue(notContains);
    }

    @Test
    public void notContainsFalseTest() {
        boolean notContains = HydraStringUtils.notContains("cs0 cs2 cs1 cs3", "cs1");
        assertFalse(notContains);
    }
}
