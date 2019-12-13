package com.triggerise.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class Formatter {
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("###,###,##0.00'€'");

    private Formatter() {}

    public static String formatCurrency(BigDecimal value) {
        return DECIMAL_FORMAT.format(value);
    }
}
