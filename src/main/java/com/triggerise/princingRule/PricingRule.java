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
