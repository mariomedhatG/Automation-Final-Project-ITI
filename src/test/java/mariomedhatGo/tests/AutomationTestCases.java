package mariomedhatGo.tests;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;
import mariomedhatGo.dataproviders.TestDataProviders;

public class AutomationTestCases extends BaseTest {

    // ===== REGISTRATION TESTS =====

    @Test(priority = 1, description = "TC01_RegisterUserSuccessfully",
            groups = {"smoke", "registration"})
    public void TC01_RegisterUserSuccessfully() {
        // Navigate to signup page
        navigateToSignup();

        // Enter signup information
        regPage.enterSignupInfo("Mario Medhat", email);

        // Complete registration
        regPage.completeRegistration("marioM123/", "Mario", "Medhat", "Mario Co",
                "13 Ali st", "14 Mohamed st", "NY", "Badr", "12345", "0123456789");

        // Verify account creation
        Assert.assertTrue(regPage.isAccountCreated(), "Account was not created successfully");

        // Continue to main page
        regPage.clickContinue();

        // Verify user is logged in
        Assert.assertTrue(driver.findElement(By.xpath("//a[contains(text(),'Logged in as')]")).isDisplayed(),
                "User is not logged in");

        System.out.println("✅ TC01: User registration completed successfully");
    }

    @Test(priority = 2, description = "TC02_RegisterWithExistingEmail",
            groups = {"regression", "registration"}, dependsOnMethods = {"TC01_RegisterUserSuccessfully"})
    public void TC02_RegisterWithExistingEmail() {
        // Navigate to signup page
        navigateToSignup();

        // Try to register with existing email
        regPage.enterSignupInfo("Mario Medhat", email);

        // Verify error message
        Assert.assertTrue(regPage.isEmailExistsErrorDisplayed(),
                "Email exists error is not displayed");

        System.out.println("✅ TC02: Existing email validation working correctly");
    }

    @Test(priority = 3, description = "TC03_RegisterWithBlankFields",
            groups = {"regression", "registration"})
    public void TC03_RegisterWithBlankFields() {
        // Navigate to signup page
        navigateToSignup();

        // Try to register with blank fields
        driver.findElement(By.cssSelector("button[data-qa='signup-button']")).click();

        // Verify validation message (this might vary based on browser)
        String pageSource = driver.getPageSource();
        Assert.assertTrue(pageSource.contains("required") || pageSource.contains("Please fill"),
                "Required field validation not working");

        System.out.println("✅ TC03: Blank fields validation working correctly");
    }

    // ===== PRODUCT TESTS =====

    @Test(priority = 4, description = "TC04_AddSingleProductToCart",
            groups = {"smoke", "products"})
    public void TC04_AddSingleProductToCart() {
        // First register user
        TC01_RegisterUserSuccessfully();

        // Navigate to products page
        navigateToProducts();

        // Add product to cart
        productPage.addProductToCart("Blue Cotton Indie Mickey Dress");

        // Verify we're on cart page
        Assert.assertTrue(driver.getCurrentUrl().contains("view_cart"),
                "Not redirected to cart page");

        // Verify cart is not empty
        Assert.assertTrue(productPage.isCartNotEmpty(), "Cart is empty after adding product");

        System.out.println("✅ TC04: Product added to cart successfully");
    }

    @Test(priority = 5, description = "TC05_AddMultipleProductsToCart",
            groups = {"regression", "products"}, dataProvider = "productData",
            dataProviderClass = TestDataProviders.class)
    public void TC05_AddMultipleProductsToCart(String productName) {
        // Register user first
        TC01_RegisterUserSuccessfully();

        // Navigate to products page
        navigateToProducts();

        // Verify product exists
        Assert.assertTrue(productPage.isProductDisplayed(productName),
                "Product not found: " + productName);

        // Add product to cart
        productPage.addProductToCart(productName);

        // Verify product added
        Assert.assertTrue(productPage.isCartNotEmpty(), "Cart is empty after adding: " + productName);

        System.out.println("✅ TC05: Product added successfully: " + productName);
    }

    @Test(priority = 6, description = "TC06_SearchProduct",
            groups = {"regression", "products"})
    public void TC06_SearchProduct() {
        // Navigate to products page
        navigateToProducts();

        // Search for a product
        productPage.searchForProduct("dress");

        // Verify search results are displayed
        Assert.assertTrue(productPage.areSearchResultsDisplayed(),
                "Search results are not displayed");

        // Verify at least one product is found
        Assert.assertTrue(productPage.getProductCount() > 0,
                "No products found in search results");

        System.out.println("✅ TC06: Product search working correctly");
    }

    @Test(priority = 7, description = "TC07_VerifyProductsPage",
            groups = {"smoke", "products"})
    public void TC07_VerifyProductsPage() {
        // Navigate to products page
        navigateToProducts();

        // Verify we're on products page
        Assert.assertTrue(productPage.isOnProductsPage(), "Not on products page");

        // Verify products are displayed
        Assert.assertTrue(productPage.getProductCount() > 0, "No products displayed");

        System.out.println("✅ TC07: Products page verification successful");
    }

    // ===== CHECKOUT TESTS =====

    @Test(priority = 8, description = "TC08_SuccessfulCheckout",
            groups = {"smoke", "checkout"}, dataProvider = "paymentData",
            dataProviderClass = TestDataProviders.class)
    public void TC08_SuccessfulCheckout(String cardName, String cardNumber, String cvc,
                                        String month, String year) {
        // Add product to cart first
        TC04_AddSingleProductToCart();

        // Proceed to checkout
        productPage.proceedToCheckout();
        productPage.clickPlaceOrder();

        // Complete payment
        boolean paymentSuccess = orderPayment.completePaymentProcess(cardName, cardNumber, cvc, month, year);

        // Verify payment success
        Assert.assertTrue(paymentSuccess, "Payment was not successful");
        Assert.assertTrue(orderPayment.isPaymentSuccessful(), "Payment success message not displayed");

        System.out.println("✅ TC08: Checkout completed successfully");
    }

    @Test(priority = 9, description = "TC09_CheckoutWithInvalidPayment",
            groups = {"regression", "checkout"}, dataProvider = "invalidPaymentData",
            dataProviderClass = TestDataProviders.class)
    public void TC09_CheckoutWithInvalidPayment(String cardName, String cardNumber, String cvc,
                                                String month, String year) {
        // Add product to cart first
        TC04_AddSingleProductToCart();

        // Proceed to checkout
        productPage.proceedToCheckout();
        productPage.clickPlaceOrder();

        // Try payment with invalid data
        orderPayment.fillPaymentInfo(cardName, cardNumber, cvc, month, year);
        orderPayment.submitPayment();

        // Wait for processing
        waitFor(3);

        // Verify payment failed or validation occurred
        boolean paymentFailed = orderPayment.isPaymentFailed() ||
                !orderPayment.isPaymentSuccessful();

        Assert.assertTrue(paymentFailed, "Payment should have failed with invalid data");

        System.out.println("✅ TC09: Invalid payment handling working correctly");
    }

    @Test(priority = 10, description = "TC10_GuestCheckout",
            groups = {"regression", "checkout"})
    public void TC10_GuestCheckout() {
        // Navigate to products without login
        navigateToProducts();

        // Add product to cart
        productPage.addProductToCart("Blue Cotton Indie Mickey Dress");

        // Verify cart page
        Assert.assertTrue(driver.getCurrentUrl().contains("view_cart"),
                "Not on cart page");

        // Verify product added successfully
        Assert.assertTrue(productPage.isCartNotEmpty(), "Cart is empty");

        System.out.println("✅ TC10: Guest checkout (add to cart) working correctly");
    }

    // ===== USER MANAGEMENT TESTS =====

    @Test(priority = 11, description = "TC11_LoginLogoutFunctionality",
            groups = {"smoke", "user"})
    public void TC11_LoginLogoutFunctionality() {
        // Register user first
        TC01_RegisterUserSuccessfully();

        // Logout
        driver.findElement(By.xpath("//a[contains(text(),'Logout')]")).click();

        // Verify logout successful
        Assert.assertTrue(driver.getCurrentUrl().contains("/login"),
                "User not redirected to login page after logout");

        System.out.println("✅ TC11: Login/Logout functionality working correctly");
    }

    @Test(priority = 12, description = "TC12_VerifyHomePage",
            groups = {"smoke", "navigation"})
    public void TC12_VerifyHomePage() {
        // Verify page title
        String pageTitle = driver.getTitle();
        Assert.assertTrue(pageTitle.contains("Automation Exercise"),
                "Page title incorrect: " + pageTitle);

        // Verify main elements are present
        Assert.assertTrue(driver.findElement(By.xpath("//a[contains(text(),'Home')]")).isDisplayed(),
                "Home link not displayed");

        System.out.println("✅ TC12: Home page verification successful");
    }

    // ===== PERFORMANCE & EDGE CASE TESTS =====

    @Test(priority = 13, description = "TC13_SlowConnectionHandling",
            groups = {"performance", "regression"})
    public void TC13_SlowConnectionHandling() {
        try {
            // Simulate slow connection
            Thread.sleep(3000);

            // Add product to cart
            TC04_AddSingleProductToCart();

            // Additional wait to simulate slow processing
            Thread.sleep(2000);

            // Proceed with checkout
            productPage.proceedToCheckout();
            productPage.clickPlaceOrder();

            // Complete payment with extra patience
            boolean paymentSuccess = orderPayment.completePaymentProcess(
                    "Mario Medhat", "4111111111111111", "123", "12", "2025");

            Assert.assertTrue(paymentSuccess, "Payment failed under slow conditions");

            System.out.println("✅ TC13: Slow connection handling successful");

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            Assert.fail("Test interrupted: " + e.getMessage());
        }
    }

    @Test(priority = 14, description = "TC14_NavigationTest",
            groups = {"regression", "navigation"})
    public void TC14_NavigationTest() {
        // Test navigation to different pages
        navigateToProducts();
        Assert.assertTrue(productPage.isOnProductsPage(), "Products page navigation failed");

        navigateToSignup();
        Assert.assertTrue(driver.getCurrentUrl().contains("login"), "Signup page navigation failed");

        // Navigate back to home
        driver.get(baseUrl);
        Assert.assertTrue(driver.getTitle().contains("Automation Exercise"), "Home navigation failed");

        System.out.println("✅ TC14: Navigation test successful");
    }

    @Test(priority = 15, description = "TC15_DataValidationTest",
            groups = {"regression", "validation"}, dataProvider = "registrationData",
            dataProviderClass = TestDataProviders.class)
    public void TC15_DataValidationTest(String name, String email, String password,
                                        String firstName, String lastName, String company,
                                        String address1, String address2, String state,
                                        String city, String zip, String mobile) {
        // Navigate to signup
        navigateToSignup();

        // Generate unique email for this test
        String uniqueEmail = "test" + System.currentTimeMillis() + "@example.com";

        // Enter signup info
        regPage.enterSignupInfo(name, uniqueEmail);

        // Fill registration form
        regPage.completeRegistration(password, firstName, lastName, company,
                address1, address2, state, city, zip, mobile);

        // Verify account creation
        Assert.assertTrue(regPage.isAccountCreated(),
                "Account creation failed with data: " + name);

        System.out.println("✅ TC15: Data validation test passed for: " + name);
    }
}