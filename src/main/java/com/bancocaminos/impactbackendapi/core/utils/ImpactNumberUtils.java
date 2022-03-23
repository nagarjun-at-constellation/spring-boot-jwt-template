package com.bancocaminos.impactbackendapi.core.utils;

public class ImpactNumberUtils {
    private ImpactNumberUtils() {
    }

    public static boolean isNotAValidLong(String code) {
        try {
            Long.parseLong(code);
        } catch (final NumberFormatException e) {
            return true;
        }
        return false;
    }
}
