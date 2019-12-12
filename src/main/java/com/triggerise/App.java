package com.triggerise;

import com.triggerise.entity.Product;
import com.triggerise.repository.UserCartRepository;
import com.triggerise.exception.ApplicationException;
import com.triggerise.repository.ProductRepository;
import com.triggerise.service.UserCartService;
import com.triggerise.util.Formatter;

import java.math.BigDecimal;
import java.util.Scanner;
import java.util.stream.Collectors;

public class App {
    private static final int ADD_PRODUCT_COMMAND = 1;
    private static final int CHECKOUT_COMMAND = 2;
    private static final int EXIT_COMMAND = 0;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int optionRead = 0;
        while(true) {
            System.out.println("---------------------------------------------------------------");
            System.out.println(String.format("-> Your cart have %d products: %s", UserCartRepository.INSTANCE.getProducts().size(),
                    UserCartRepository.INSTANCE.getProducts().stream().map(Product::getCode).collect(Collectors.joining(", "))));
            System.out.println("-> Available products for buying: " + ProductRepository.INSTANCE.findAll().stream()
                    .map(p -> String.format("%s (%s)", p.getCode(), Formatter.formatCurrency(p.getPrice())))
                    .collect(Collectors.joining(", ")));
            System.out.println("-> Please, enter an option: 1- Add Product; 2- Checkout; 0- Exit");

            try {
                if ((optionRead = Integer.valueOf(scanner.next())) == EXIT_COMMAND)
                    break;
            } catch (NumberFormatException e) {}

            if(optionRead == ADD_PRODUCT_COMMAND) {
                addNewProduct(scanner);
            } else if(optionRead == CHECKOUT_COMMAND) {
                doCartCheckout();
            } else {
                System.err.println("Please, enter a valid option");
            }
        }

        System.out.println("Exiting the program");
    }

    private static void doCartCheckout() {
        try {
            BigDecimal totalCart = UserCartService.INSTANCE.checkout();
            System.out.println("==> Total: " + Formatter.formatCurrency(totalCart));
        } catch (ApplicationException e) {
            System.err.println(">>> Message: " + e.getMessage());
        }
    }

    private static void addNewProduct(Scanner scanner) {
        while(true) {
            System.out.println("-> Please, enter the code of product you wish: ");
            String productCode = scanner.next();

            if (productCode == null || productCode.trim().equals("")) {
                System.err.println("** No product added **");
                return;
            }

            try {
                UserCartService.INSTANCE.addProduct(productCode);
                System.out.println("Product added");
                return;
            } catch (ApplicationException e) {
                System.err.println(">>> Message: " + e.getMessage());
            }
        }
    }
}
