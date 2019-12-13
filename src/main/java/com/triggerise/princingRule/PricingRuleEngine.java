package com.triggerise.princingRule;

import com.triggerise.entity.Product;
import com.triggerise.entity.ProductPricingRule;
import com.triggerise.repository.ProductPricingRuleRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Manages the application of Pricing Rules on products.
 */
public enum PricingRuleEngine {
    INSTANCE;

    /**
     * Applies Pricing Rules to products.
     *
     * @param productList
     * @return
     */
    public BigDecimal applyRules(List<Product> productList) {
        if(productList == null)
            throw new IllegalStateException("Product list can't be null");

        if(productList.isEmpty())
            return BigDecimal.ZERO;

        List<Product> distinctList = productList.stream().distinct().collect(Collectors.toList());
        Map<String, ProductPricingRule> mapProductPricingRule = ProductPricingRuleRepository.INSTANCE.getProductPricingRuleMap();
        BigDecimal total = BigDecimal.ZERO;
        ProductPricingRule pricingRuleFound = null;

        for(Product product: distinctList) {
            if((pricingRuleFound = mapProductPricingRule.get(product.getCode())) != null) {
                total = total.add(PricingRuleFactory.getMapCodePricingRules().get(pricingRuleFound.getPricingRule()
                        .getCode())
                        .apply(product, productList));
            } else {
                // in case of the product doesn't have an associated Pricing Rule, applies the default pricing rule
                total = total.add(PricingRuleFactory.getDefaultPricingRule().apply(product, productList));
            }
        }

        return total;
    }
}
