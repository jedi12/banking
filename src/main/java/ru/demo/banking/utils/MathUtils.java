package ru.demo.banking.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MathUtils {

    public static BigDecimal sum(BigDecimal value1, BigDecimal value2) {
        if (value1 == null || value2 == null) return null;
        return value1.add(value2);
    }

    public static BigDecimal multiply(BigDecimal value, double value2, int scale) {
        return value == null ? null : BigDecimal.valueOf(value2).multiply(value).setScale(scale, RoundingMode.HALF_UP);
    }

    public static BigDecimal roundWithScale(BigDecimal value, int scale) {
        return value == null ? null : value.setScale(scale, RoundingMode.HALF_UP);
    }
}
