package com.triggerise.repository;

import com.triggerise.entity.Product;
import com.triggerise.util.CsvUtils;

import java.math.BigDecimal;
import java.util.*;

public enum ProductRepository {
    INSTANCE;

    private final List<Product> productList;

    private ProductRepository() {
        List<String> csvRecords = CsvUtils.readCsv("products.csv", true);
        productList = new ArrayList<>(csvRecords.size());
        csvRecords.stream().forEach(r -> {
            String[] productInfo = r.split(";");
            productList.add(Product.builder().code(productInfo[0].trim().toUpperCase())
                    .description(productInfo[1].trim())
                    .price(new BigDecimal(productInfo[2].trim())).build());
        });
    }

    public List<Product> findAll() {
        return Collections.unmodifiableList(productList);
    }

    public Optional<Product> findByCode(String code) {
        Objects.requireNonNull(code, "Invalid argument 'code'");

        String codeTmp = code.toUpperCase();
        return productList.stream().filter(p -> p.getCode().equals(codeTmp)).findFirst();
    }
}
