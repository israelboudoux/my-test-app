package com.triggerise.service;

import com.triggerise.entity.Product;
import com.triggerise.repository.UserCartRepository;
import com.triggerise.exception.ApplicationException;
import com.triggerise.princingRule.PricingRuleEngine;
import com.triggerise.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.Optional;

public enum UserCartService {
    INSTANCE;

    /**
     * Adds a new Product to the User Cart
     *
     * @param productCode
     * @throws ApplicationException
     */
    public void addProduct(String productCode) throws ApplicationException {
        Optional<Product> productOpt = ProductRepository.INSTANCE.findByCode(productCode);
        if(! productOpt.isPresent())
            throw new ApplicationException(String.format("Product '%s' not found. Please, enter an existing product", productCode));

        UserCartRepository.INSTANCE.addProduct(productOpt.get());
    }

    /**
     * Executes the checkout of the User Cart and cleans it when checkout is done.
     *
     * @return the total to pay
     * @throws ApplicationException if the User Cart is empty
     */
    public BigDecimal checkout() throws ApplicationException {
        if(UserCartRepository.INSTANCE.getProducts().isEmpty())
            throw new ApplicationException("The cart is empty. Please, enter at least one product to proceed to checkout");

        BigDecimal total = PricingRuleEngine.INSTANCE.applyRules(UserCartRepository.INSTANCE.getProducts());

        UserCartRepository.INSTANCE.cleanUp();

        return total;
    }
}
