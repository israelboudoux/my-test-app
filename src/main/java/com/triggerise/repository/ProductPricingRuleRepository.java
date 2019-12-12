/**
 * Copyright (c) 2019 WIT Software <info@wit-software.com>. All rights reserved.
 * <p>
 * WIT Software Confidential and Proprietary information. It is strictly forbidden for 3rd parties to modify, decompile,
 * disassemble, defeat, disable or circumvent any protection mechanism; to sell, license, lease, rent, redistribute or
 * make accessible to any third party, whether for profit or without charge.
 */
package com.triggerise.repository;

import com.triggerise.entity.Product;
import com.triggerise.entity.ProductPricingRule;
import com.triggerise.entity.Rule;
import com.triggerise.util.CsvUtils;

import java.util.*;

public enum ProductPricingRuleRepository {
    INSTANCE;

    private final Map<String, ProductPricingRule> productPricingRuleMap;

    private ProductPricingRuleRepository() {
        List<String> csvRecords = CsvUtils.readCsv("product-princingRule.csv", true);
        productPricingRuleMap = new HashMap<>(csvRecords.size());
        csvRecords.stream().forEach(r -> {
            String[] productPricingRuleInfo = r.split(";");

            if(productPricingRuleInfo.length != 2)
                throw new IllegalStateException("Found an invalid format for the product-pricingRule");

            Optional<Product> productFound = ProductRepository.INSTANCE.findByCode(productPricingRuleInfo[0].trim());
            if(! productFound.isPresent())
                throw new IllegalStateException(String.format("Product '%s' not found", productPricingRuleInfo[0].trim().toUpperCase()));

            Optional<Rule> ruleFound = RuleRepository.INSTANCE.findByCode(productPricingRuleInfo[1].trim());
            if(! ruleFound.isPresent())
                throw new IllegalStateException(String.format("Rule '%s' not found", productPricingRuleInfo[1].trim()));

            productPricingRuleMap.put(productFound.get().getCode(), ProductPricingRule.builder().product(productFound.get())
                    .pricingRule(ruleFound.get()).build());
        });
    }

    public Map<String, ProductPricingRule> getProductPricingRuleMap() {
        return Collections.unmodifiableMap(this.productPricingRuleMap);
    }
}
