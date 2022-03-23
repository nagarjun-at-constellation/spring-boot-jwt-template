package com.bancocaminos.impactbackendapi.core.utils;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class ImpactNumberUtilsTest {

    @Test
    public void isNotAValidLongTest() {
        boolean notAValidLong = ImpactNumberUtils.isNotAValidLong("code");
        assertTrue(notAValidLong);
    }

    @Test
    public void isAValidLongTest() {
        boolean notAValidLong = ImpactNumberUtils.isNotAValidLong("123");
        assertFalse(notAValidLong);
    }
}
