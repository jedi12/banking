package ru.demo.banking.utils;

import java.math.BigDecimal;

public class ConvertUtils {

    public static Long getLong(String value) {
        try {
            return Long.parseLong(value);
        } catch (Exception e) {
            throw new IllegalArgumentException("Введенное значение должно быть целым числом. Например: 1570");
        }
    }

    public static BigDecimal getBigDecimal(String value) {
        try {
            return new BigDecimal(value);
        } catch (Exception e) {
            throw new IllegalArgumentException("Введенное значение должно быть целым или дробным числом. Например: 177.15");
        }
    }
}
