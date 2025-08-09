package mariomedhatGo.tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import mariomedhatGo.dataproviders.TestDataProviders;

public class ProductTests extends BaseTest {

    @Test(description = "Add single product to cart", groups = {"smoke", "products"})
    public void testAddSingleProductToCart() throws InterruptedException {
        // Register user first
        navigateToSignup();
        regPage.enterSignupInfo("Product Tester", email);
        regPage.completeRegistration("password123", "Product", "Tester", "TestCorp",
                "123 Test St", "Apt 1", "CA", "TestCity", "12345", "1234567890");
        regPage.clickContinue();

        // Add product
        navigateToProducts();
        productPage.addProductToCart("Blue Cotton Indie Mickey Dress");

        Assert.assertTrue(driver.getCurrentUrl().contains("view_cart"), "Not on cart page");
        Assert.assertTrue(productPage.isCartNotEmpty(), "Cart is empty");
        System.out.println("Add single product test passed");
    }

    @Test(description = "Add multiple products to cart", groups = {"regression"},
            dataProvider = "getMultipleProductsData", dataProviderClass = TestDataProviders.class)
    public void testAddMultipleProductsToCart(String[] productNames) throws InterruptedException {
    try {
        navigateToProducts();

        // Add product to cart
        productPage.addMultipleProductsToCart(productNames);

        // Verify product added
        Assert.assertTrue(productPage.isCartNotEmpty(), "Cart is empty after adding: ");

        System.out.println("TC03: Product added successfully: ");

    } catch (Exception e) {
        System.err.println("TC03 failed for product " + ": " + e.getMessage());
        throw e;
    }
    }

    @Test(description = "Product search functionality", groups = {"regression"})
    public void testProductSearch() throws InterruptedException {
        navigateToProducts();
        productPage.searchForProduct("dress");

        Assert.assertTrue(productPage.areSearchResultsDisplayed(), "Search results not displayed");
        Assert.assertTrue(productPage.getProductCount() > 0, "No search results found");
        System.out.println("Product search test passed");
    }

    @Test(description = "Verify all products page", groups = {"smoke"})
    public void testVerifyAllProductsPage() throws InterruptedException {
        navigateToProducts();

        Assert.assertTrue(productPage.isOnProductsPage(), "Not on products page");
        Assert.assertTrue(productPage.getProductCount() > 0, "No products displayed");

        String[] allProducts = productPage.getAllProductNames();
        Assert.assertTrue(allProducts.length > 0, "Product names not retrieved");
        System.out.println("Products page verification passed - Found " + allProducts.length + " products");
    }
}