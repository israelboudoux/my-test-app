package com.triggerise.entity;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ProductPricingRule {
    private Product product;
    private Rule pricingRule;
}
