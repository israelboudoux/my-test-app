/**
 * Copyright (c) 2019 WIT Software <info@wit-software.com>. All rights reserved.
 * <p>
 * WIT Software Confidential and Proprietary information. It is strictly forbidden for 3rd parties to modify, decompile,
 * disassemble, defeat, disable or circumvent any protection mechanism; to sell, license, lease, rent, redistribute or
 * make accessible to any third party, whether for profit or without charge.
 */
package com.triggerise.entity;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class Product {
    private String code;
    private String description;
    private BigDecimal price;
}
