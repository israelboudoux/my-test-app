/**
 * Copyright (c) 2019 WIT Software <info@wit-software.com>. All rights reserved.
 * <p>
 * WIT Software Confidential and Proprietary information. It is strictly forbidden for 3rd parties to modify, decompile,
 * disassemble, defeat, disable or circumvent any protection mechanism; to sell, license, lease, rent, redistribute or
 * make accessible to any third party, whether for profit or without charge.
 */
package com.triggerise.repository;

import com.triggerise.entity.Product;

import java.util.ArrayList;
import java.util.List;

public enum UserCartRepository {
    INSTANCE;

    private List<Product> products = new ArrayList<>();

    public void addProduct(Product newProduct) {
        this.products.add(newProduct);
    }

    public List<Product> getProducts() {
        return this.products;
    }

    public void cleanUp() {
        this.products.clear();
    }

}
