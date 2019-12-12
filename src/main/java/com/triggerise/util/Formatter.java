/**
 * Copyright (c) 2019 WIT Software <info@wit-software.com>. All rights reserved.
 * <p>
 * WIT Software Confidential and Proprietary information. It is strictly forbidden for 3rd parties to modify, decompile,
 * disassemble, defeat, disable or circumvent any protection mechanism; to sell, license, lease, rent, redistribute or
 * make accessible to any third party, whether for profit or without charge.
 */
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
