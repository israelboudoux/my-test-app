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
