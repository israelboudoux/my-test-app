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
