package com.bancocaminos.impactbackendapi.core.utils;

import org.apache.commons.lang3.StringUtils;

public class HydraStringUtils extends StringUtils {

    public static boolean notEquals(final CharSequence cs1, final CharSequence cs2) {
        return !equals(cs1, cs2);
    }

    public static boolean isNotBlank(final CharSequence cs) {
        return !isBlank(cs);
    }

    public static boolean notContains(final CharSequence cs1, final CharSequence cs2) {
        return !contains(cs1, cs2);
    }
}
