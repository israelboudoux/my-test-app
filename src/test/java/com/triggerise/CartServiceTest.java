package com.triggerise;

import com.triggerise.entity.Product;
import com.triggerise.exception.ApplicationException;
import com.triggerise.repository.UserCartRepository;
import com.triggerise.service.UserCartService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.math.BigDecimal;

public class CartServiceTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void cleanUp() {
        UserCartRepository.INSTANCE.cleanUp();
    }

    private Product mockProduct(String code, BigDecimal price, String description) {
        return Product.builder().code(code).description(description).price(price).build();
    }

    @Test
    public void addProductToCart_Success() throws Exception {
        UserCartService.INSTANCE.addProduct("TICKET");
        UserCartService.INSTANCE.addProduct("ticket");

        Assert.assertEquals(2, UserCartRepository.INSTANCE.getProducts().size());
    }

    @Test
    public void addProductToCart_Fail() throws Exception {
        String productCode = "qwfakldfjass";

        expectedException.expect(ApplicationException.class);
        expectedException.expectMessage(String.format("Product '%s' not found. Please, enter an existing product", productCode));

        UserCartService.INSTANCE.addProduct("qwfakldfjass");
    }

    @Test
    public void checkoutEmptyCart() throws ApplicationException {
        expectedException.expect(ApplicationException.class);
        expectedException.expectMessage("The cart is empty. Please, enter at least one product to proceed to checkout");

        UserCartService.INSTANCE.checkout();
    }

    @Test
    public void checkoutTestPricingRuleBuyXGetY() throws ApplicationException {
        // 2_for_1 pricing rule
        UserCartService.INSTANCE.addProduct("TICKET");
        UserCartService.INSTANCE.addProduct("TICKET");
        UserCartService.INSTANCE.addProduct("TICKET");
        Assert.assertEquals(new BigDecimal("10.00"), UserCartService.INSTANCE.checkout());

        // new test - 2_for_1 pricing rule
        UserCartService.INSTANCE.addProduct("TICKET");
        Assert.assertEquals(new BigDecimal("5.00"), UserCartService.INSTANCE.checkout());

        // new test - 2_for_1 pricing rule
        UserCartService.INSTANCE.addProduct("TICKET");
        UserCartService.INSTANCE.addProduct("TICKET");
        Assert.assertEquals(new BigDecimal("5.00"), UserCartService.INSTANCE.checkout());

        // new test - 4_for_2 pricing rule
        UserCartService.INSTANCE.addProduct("BALL");
        Assert.assertEquals(new BigDecimal("20.00"), UserCartService.INSTANCE.checkout());

        // new test - 4_for_2 pricing rule
        UserCartService.INSTANCE.addProduct("BALL");
        UserCartService.INSTANCE.addProduct("BALL");
        UserCartService.INSTANCE.addProduct("BALL");
        UserCartService.INSTANCE.addProduct("BALL");

        Assert.assertEquals(new BigDecimal("40.00"), UserCartService.INSTANCE.checkout());

        // new test - 2_for_1 + 4_for_2 pricing rule
        UserCartService.INSTANCE.addProduct("BALL");
        UserCartService.INSTANCE.addProduct("BALL");
        UserCartService.INSTANCE.addProduct("BALL");
        UserCartService.INSTANCE.addProduct("BALL");
        UserCartService.INSTANCE.addProduct("TICKET");
        UserCartService.INSTANCE.addProduct("TICKET");

        Assert.assertEquals(new BigDecimal("45.00"), UserCartService.INSTANCE.checkout());
    }

    @Test
    public void checkoutTestPricingRuleDiscountOnPrice() throws ApplicationException {
        // get_1_in_each_item_when_qty_gte_3 pricing rule
        UserCartService.INSTANCE.addProduct("HOODIE");
        UserCartService.INSTANCE.addProduct("HOODIE");
        UserCartService.INSTANCE.addProduct("HOODIE");
        UserCartService.INSTANCE.addProduct("HOODIE");
        UserCartService.INSTANCE.addProduct("HOODIE");
        UserCartService.INSTANCE.addProduct("HOODIE");
        UserCartService.INSTANCE.addProduct("HOODIE");

        Assert.assertEquals(new BigDecimal("133.00"), UserCartService.INSTANCE.checkout());

        // new test - get_1_in_each_item_when_qty_gte_3 pricing rule
        UserCartService.INSTANCE.addProduct("HOODIE");
        UserCartService.INSTANCE.addProduct("HOODIE");

        Assert.assertEquals(new BigDecimal("40.00"), UserCartService.INSTANCE.checkout());

        // new test - get_50_in_each_item_when_qty_gte_2 pricing rule
        UserCartService.INSTANCE.addProduct("IPHONE11");
        UserCartService.INSTANCE.addProduct("IPHONE11");

        Assert.assertEquals(new BigDecimal("699.98"), UserCartService.INSTANCE.checkout());

        // new test - get_1_in_each_item_when_qty_gte_3 + get_50_in_each_item_when_qty_gte_2 pricing rule
        UserCartService.INSTANCE.addProduct("IPHONE11");
        UserCartService.INSTANCE.addProduct("IPHONE11");
        UserCartService.INSTANCE.addProduct("HOODIE");
        UserCartService.INSTANCE.addProduct("HOODIE");

        Assert.assertEquals(new BigDecimal("739.98"), UserCartService.INSTANCE.checkout());
    }

    @Test
    public void checkoutTestManyProductsAndPricingRules() throws ApplicationException {
        // new test -
        // get_1_in_each_item_when_qty_gte_3 + get_50_in_each_item_when_qty_gte_2 pricing rule
        UserCartService.INSTANCE.addProduct("IPHONE11");
        UserCartService.INSTANCE.addProduct("IPHONE11");
        UserCartService.INSTANCE.addProduct("HOODIE");
        UserCartService.INSTANCE.addProduct("HOODIE");

        // 2_for_1 + 4_for_2 pricing rule
        UserCartService.INSTANCE.addProduct("BALL");
        UserCartService.INSTANCE.addProduct("BALL");
        UserCartService.INSTANCE.addProduct("BALL");
        UserCartService.INSTANCE.addProduct("BALL");
        UserCartService.INSTANCE.addProduct("TICKET");
        UserCartService.INSTANCE.addProduct("TICKET");

        // no pricing rule
        UserCartService.INSTANCE.addProduct("HAT");
        UserCartService.INSTANCE.addProduct("HAT");
        UserCartService.INSTANCE.addProduct("ASUS_CELLPHONE");

        Assert.assertEquals(new BigDecimal("1049.98"), UserCartService.INSTANCE.checkout());
    }
}
