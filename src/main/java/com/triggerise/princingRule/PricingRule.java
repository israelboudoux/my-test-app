/**
 * Copyright (c) 2019 WIT Software <info@wit-software.com>. All rights reserved.
 * <p>
 * WIT Software Confidential and Proprietary information. It is strictly forbidden for 3rd parties to modify, decompile,
 * disassemble, defeat, disable or circumvent any protection mechanism; to sell, license, lease, rent, redistribute or
 * make accessible to any third party, whether for profit or without charge.
 */
package com.triggerise.princingRule;

import com.triggerise.entity.Product;
import com.triggerise.entity.Rule;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public interface PricingRule {

    static void validateRule(Rule rule, int quantityParameters) {
        Objects.requireNonNull(rule, "Rule can't be null");
        Objects.requireNonNull(rule.getParameters(), "Rule's parameters can't be null");

        String[] params = rule.getParameters().split(",");
        if(params.length != quantityParameters)
            throw new IllegalStateException(String.format("Wrong number of rule's parameters: expected %d, found %d",
                    quantityParameters, params.length));

    }

    BigDecimal apply(Product product, List<Product> cartProducts);
}
