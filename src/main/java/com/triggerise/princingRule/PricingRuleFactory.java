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
import com.triggerise.repository.RuleRepository;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Factory class responsible to create instances of the available Pricing Rules configured.
 */
public class PricingRuleFactory {

    /**
     * Maps the Pricing Rule's Name to the factory method of the Pricing Rule
     */
    private static Map<String, Function<Rule, PricingRule>> factoryMapNamePricingRule;

    /**
     * Maps the Pricing Rule's Code to the respective instance
     */
    private static Map<String, PricingRule> mapCodePricingRules;

    static {
        factoryMapNamePricingRule = new HashMap<>(2);
        factoryMapNamePricingRule.put(BuyXGetYPricingRule.NAME, BuyXGetYPricingRule::of);
        factoryMapNamePricingRule.put(DynamicDiscountPricingRule.NAME, DynamicDiscountPricingRule::of);

        List<Rule> rules = RuleRepository.INSTANCE.findAll();
        mapCodePricingRules = new HashMap<>(rules.size());

        rules.stream().forEach(r -> {
            if(factoryMapNamePricingRule.get(r.getName()) == null)
                throw new IllegalStateException(String.format("The Pricing Rule '%s' was not found", r.getName()));

            mapCodePricingRules.put(r.getCode().toUpperCase(), factoryMapNamePricingRule.get(r.getName()).apply(r));
        });
    }

    public static Map<String, PricingRule> getMapCodePricingRules() {
        return Collections.unmodifiableMap(mapCodePricingRules);
    }

    public static PricingRule getDefaultPricingRule() {
        return new DefaultPricingRule();
    }

    protected static class DefaultPricingRule implements PricingRule {
        @Override
        public BigDecimal apply(Product product, List<Product> cartProducts) {
            int totalItensProduct = (int) cartProducts.stream().filter(p -> p.getCode().equals(product.getCode())).count();

            return product.getPrice().multiply(BigDecimal.valueOf(totalItensProduct));
        }
    }

    @ToString
    protected static class BuyXGetYPricingRule implements PricingRule {
        public static final String NAME = "BuyXGetYPricingRule";

        private final String code;
        private final String description;
        private final int howManyYouBuy;
        private final int howManyYouGet;

        public BuyXGetYPricingRule(String code, String description, int howManyYouBuy, int howManyYouGet) {
            if(howManyYouBuy < 1)
                throw new IllegalStateException("Paramter 'howManyYouBuy' must be greater than zero");

            if(howManyYouBuy >= howManyYouGet)
                throw new IllegalStateException("Paramter 'howManyYouGet' must be greater than 'howManyYouBuy'");

            this.code = code;
            this.description = description;
            this.howManyYouBuy = howManyYouBuy;
            this.howManyYouGet = howManyYouGet;
        }

        public static BuyXGetYPricingRule of(Rule rule) {
            PricingRule.validateRule(rule, 2);

            String[] params = rule.getParameters().split(",");
            return new BuyXGetYPricingRule(rule.getCode(), rule.getDescription(), Integer.parseInt(params[0].trim()),
                    Integer.parseInt(params[1].trim()));
        }

        @Override
        public BigDecimal apply(Product product, List<Product> cartProducts) {
            int totalItensProduct = (int) cartProducts.stream().filter(p -> p.getCode().equals(product.getCode())).count();

            int totalItemsToPay = totalItensProduct / this.howManyYouGet * this.howManyYouBuy;
            int remaining = totalItensProduct % this.howManyYouGet;
            totalItemsToPay += remaining > this.howManyYouBuy ? this.howManyYouBuy : remaining;

            return product.getPrice().multiply(BigDecimal.valueOf(totalItemsToPay));
        }
    }

    @ToString
    protected static class DynamicDiscountPricingRule implements PricingRule {
        public static final String NAME = "DynamicDiscountPricingRule";

        private final String code;
        private final String description;
        private final int minProductQuantity;
        private final BigDecimal discoutValue;

        public DynamicDiscountPricingRule(String code, String description, int productQuantityOperand, BigDecimal discoutValue) {
            if(productQuantityOperand < 1)
                throw new IllegalStateException("Paramter 'productQuantityOperand' must be greater than zero");

            if(discoutValue.compareTo(BigDecimal.ZERO) <= 0)
                throw new IllegalStateException("Paramter 'discoutValue' must be greater than zero");

            this.code = code;
            this.description = description;
            this.minProductQuantity = productQuantityOperand;
            this.discoutValue = discoutValue;
        }

        public static DynamicDiscountPricingRule of(Rule rule) {
            PricingRule.validateRule(rule, 2);

            String[] params = rule.getParameters().split(",");
            return new DynamicDiscountPricingRule(rule.getCode(), rule.getDescription(), Integer.parseInt(params[0].trim()),
                    new BigDecimal(params[1].trim()));
        }

        @Override
        public BigDecimal apply(Product product, List<Product> cartProducts) {
            if(product.getPrice().compareTo(this.discoutValue) <= 0)
                throw new IllegalStateException("The product's price must be greater than the 'discoutValue'");

            int totalItensProduct = (int) cartProducts.stream().filter(p -> p.getCode().equals(product.getCode())).count();
            Supplier<BigDecimal> subTotal = () -> product.getPrice().multiply(BigDecimal.valueOf(totalItensProduct));

            if(totalItensProduct < this.minProductQuantity) {
                return subTotal.get();
            }

            return subTotal.get().subtract(BigDecimal.valueOf(totalItensProduct).multiply(this.discoutValue));
        }
    }
}
