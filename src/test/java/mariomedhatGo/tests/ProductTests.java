package mariomedhatGo.tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import mariomedhatGo.dataproviders.TestDataProviders;

public class ProductTests extends BaseTest {

    @Test(description = "Add single product to cart", groups = {"smoke" , "sanity"},
            dataProvider = "singleProductData", dataProviderClass = TestDataProviders.class)
    public void testAddSingleProductToCart(String productName) throws InterruptedException {
        // Register user first
        navigateToSignup();
        regPage.enterSignupInfo("Product Tester", email);
        regPage.completeRegistration(new String[]{"password123", "Product", "Tester", "TestCorp",
                "123 Test St", "Apt 1", "CA", "TestCity", "12345", "1234567890"});
        regPage.clickContinue();

        // Add product
        navigateToProducts();
        productPage.addProductToCart(productName);

        Assert.assertTrue(driver.getCurrentUrl().contains("view_cart"), "Not on cart page");
        Assert.assertTrue(productPage.isCartNotEmpty(), "Cart is empty");
        System.out.println("Add single product test passed for: " + productName);
    }

    @Test(description = "Add multiple products to cart", groups = {"smoke" , "sanity"},
            dataProvider = "multipleProductsData", dataProviderClass = TestDataProviders.class)
    public void testAddMultipleProductsToCart(String[] productNames) throws InterruptedException {
        try {
            // Register user first
            navigateToSignup();
            regPage.enterSignupInfo("Multi Product Tester", email);
            regPage.completeRegistration(new String[]{"password123", "Multi", "Tester", "TestCorp",
                    "123 Test St", "Apt 1", "CA", "TestCity", "12345", "1234567890"});
            regPage.clickContinue();

            // Navigate to products page
            navigateToProducts();

            // Add products to cart
            productPage.addMultipleProductsToCart(productNames);

            // Verify products added
            Assert.assertTrue(productPage.isCartNotEmpty(), "Cart is empty after adding products");

            System.out.println("Multiple products added successfully: " + String.join(", ", productNames));

        } catch (Exception e) {
            System.err.println("Multiple products test failed: " + e.getMessage());
            throw e;
        }
    }

    @Test(description = "Product search functionality", groups = {"smoke" , "sanity"},
            dataProvider = "searchData", dataProviderClass = TestDataProviders.class)
    public void testProductSearch(String searchTerm) throws InterruptedException {
        navigateToProducts();
        productPage.searchForProduct(searchTerm);

        Assert.assertTrue(productPage.areSearchResultsDisplayed(), "Search results not displayed for: " + searchTerm);

        int resultCount = productPage.getProductCount();
        System.out.println("Search for '" + searchTerm + "' returned " + resultCount + " results");
    }

    @Test(description = "Verify all products page", groups = {"smoke" , "sanity"})
    public void testVerifyAllProductsPage() throws InterruptedException {
        navigateToProducts();

        Assert.assertTrue(productPage.isOnProductsPage(), "Not on products page");
        Assert.assertTrue(productPage.getProductCount() > 0, "No products displayed");

        String[] allProducts = productPage.getAllProductNames();
        Assert.assertTrue(allProducts.length > 0, "Product names not retrieved");
        System.out.println("Products page verification passed - Found " + allProducts.length + " products");
    }

    @Test(description = "Verify specific product exists", groups = {"smoke" , "sanity"},
            dataProvider = "singleProductData", dataProviderClass = TestDataProviders.class)
    public void testVerifyProductExists(String productName) throws InterruptedException {
        navigateToProducts();

        boolean productExists = productPage.isProductDisplayed(productName);
        Assert.assertTrue(productExists, "Product not found: " + productName);

        System.out.println("Product verification passed for: " + productName);
    }

    @Test(description = "Add product without registration (guest)", groups = {"smoke" , "sanity"})
    public void testAddProductAsGuest() throws InterruptedException {
        // Don't register, just add product as guest
        navigateToProducts();
        productPage.addProductToCart("Blue Cotton Indie Mickey Dress");

        Assert.assertTrue(driver.getCurrentUrl().contains("view_cart"), "Not on cart page");
        Assert.assertTrue(productPage.isCartNotEmpty(), "Cart is empty for guest user");

        System.out.println("Guest add product test passed");
    }

    @Test(description = "Search with no results", groups = {"smoke" , "sanity"})
    public void testSearchWithNoResults() throws InterruptedException {
        navigateToProducts();
        productPage.searchForProduct("nonexistentproduct12345");

        // Should handle gracefully - either show "no results" or empty list
        Assert.assertTrue(driver.getCurrentUrl().contains("products"), "Search failed unexpectedly");

        System.out.println("No results search test passed");
    }

    @Test(description = "Multiple single products", groups = {"smoke" , "sanity"})
    public void testMultipleSingleProducts() throws InterruptedException {
        // Register user first
        navigateToSignup();
        regPage.enterSignupInfo("Sequential Tester", email);
        regPage.completeRegistration(new String[]{"password123", "Sequential", "Tester", "TestCorp",
                "123 Test St", "Apt 1", "CA", "TestCity", "12345", "1234567890"});
        regPage.clickContinue();

        String[] products = {"Blue Cotton Indie Mickey Dress", "Men Tshirt", "Sleeveless Dress"};

        for (String product : products) {
            navigateToProducts();
            productPage.addProductToCart(product);

            // Verify each addition
            Assert.assertTrue(productPage.isCartNotEmpty(), "Cart empty after adding: " + product);
            System.out.println("Added product: " + product);

            // Continue shopping if not the last product
            if (!product.equals(products[products.length - 1])) {
                productPage.clickContinueShopping();
            }
        }

        System.out.println("Multiple single products test completed");
    }
}