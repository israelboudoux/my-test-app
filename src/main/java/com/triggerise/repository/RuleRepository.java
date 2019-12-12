/**
 * Copyright (c) 2019 WIT Software <info@wit-software.com>. All rights reserved.
 * <p>
 * WIT Software Confidential and Proprietary information. It is strictly forbidden for 3rd parties to modify, decompile,
 * disassemble, defeat, disable or circumvent any protection mechanism; to sell, license, lease, rent, redistribute or
 * make accessible to any third party, whether for profit or without charge.
 */
package com.triggerise.repository;

import com.triggerise.entity.Rule;
import com.triggerise.util.CsvUtils;

import java.util.*;

public enum RuleRepository {
    INSTANCE;

    private final List<Rule> pricingRuleList;

    private RuleRepository() {
        List<String> csvRecords = CsvUtils.readCsv("pricing-rules.csv", true);
        pricingRuleList = new ArrayList<>(csvRecords.size());
        csvRecords.stream().forEach(r -> {
            String[] ruleInfo = r.split(";");
            pricingRuleList.add(Rule.builder().code(ruleInfo[0].trim().toUpperCase())
                    .name(ruleInfo[1].trim())
                    .description(ruleInfo[2].trim())
                    .parameters(ruleInfo[3].trim())
                    .build());
        });
    }

    public List<Rule> findAll() {
        return Collections.unmodifiableList(pricingRuleList);
    }

    public Optional<Rule> findByCode(String code) {
        Objects.requireNonNull(code, "Invalid argument 'code'");

        String codeTmp = code.toUpperCase();
        return pricingRuleList.stream().filter(p -> p.getCode().equals(codeTmp)).findFirst();
    }
}
