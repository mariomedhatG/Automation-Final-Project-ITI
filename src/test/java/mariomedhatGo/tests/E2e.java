//package mariomedhatGo.tests;
//
//import org.openqa.selenium.By;
//import org.openqa.selenium.support.ui.ExpectedConditions;
//import org.testng.Assert;
//import org.testng.annotations.Test;
//import mariomedhatGo.dataproviders.TestDataProviders;
//
//public class E2e extends BaseTest {
//
//    private String registeredUserEmail;
//    private boolean isUserRegistered = false;
//
//    // ===== REGISTRATION TESTS =====
//
//    @Test(description = "E2e Valid Scenario", groups = {"smoke"},
//           dataProvider = "registrationData", dataProviderClass = TestDataProviders.class)
//    public void E2e(String[] registData) {
//        try {
//            // Navigate to signup page
//            navigateToSignup();
//
//            // Enter signup information
//            regPage.enterSignupInfo("Mario Medhat", email);
//
//            // Complete registration
//            regPage.completeRegistration(registData);
//
//            // Verify account creation
//            Assert.assertTrue(regPage.isAccountCreated(), "Account was not created successfully");
//
//            // Continue to main page
//            regPage.clickContinue();
//
//            // Verify user is logged in
//            wait.until(ExpectedConditions.presenceOfElementLocated(
//                    By.xpath("//a[contains(text(),'Logged in as')]")));
//
//            Assert.assertTrue(driver.findElement(By.xpath("//a[contains(text(),'Logged in as')]")).isDisplayed(),
//                    "User is not logged in");
//
//            // حفظ بيانات المستخدم المسجل
//            registeredUserEmail = email;
//            isUserRegistered = true;
//
//            System.out.println("✅ TC01: User registration completed successfully");
//
//        } catch (Exception e) {
//            System.err.println("❌ TC01 failed: " + e.getMessage());
//            logCurrentState();
//            throw e;
//        }
//    }
//
//    @Test(priority = 2, description = "TC02_RegisterWithExistingEmail",
//            groups = {"regression", "registration"}, dependsOnMethods = {"TC01_RegisterUserSuccessfully"})
//    public void TC02_RegisterWithExistingEmail() {
//        try {
//            // Navigate to signup page
//            navigateToSignup();
//
//            // Try to register with existing email
//            regPage.enterSignupInfo("Mario Medhat", registeredUserEmail);
//
//            // Verify error message
//            Assert.assertTrue(regPage.isEmailExistsErrorDisplayed(),
//                    "Email exists error is not displayed");
//
//            System.out.println("✅ TC02: Existing email validation working correctly");
//
//        } catch (Exception e) {
//            System.err.println("❌ TC02 failed: " + e.getMessage());
//            throw e;
//        }
//    }
//
//    @Test(priority = 3, description = "TC03_RegisterWithBlankFields",
//            groups = {"regression", "registration"})
//    public void TC03_RegisterWithBlankFields() {
//        try {
//            // Navigate to signup page
//            navigateToSignup();
//
//            // Try to register with blank fields
//            wait.until(ExpectedConditions.elementToBeClickable(
//                    By.cssSelector("button[data-qa='signup-button']")));
//            driver.findElement(By.cssSelector("button[data-qa='signup-button']")).click();
//
//            // Verify validation message (this might vary based on browser)
//            String pageSource = driver.getPageSource();
//            Assert.assertTrue(pageSource.contains("required") || pageSource.contains("Please fill"),
//                    "Required field validation not working");
//
//            System.out.println("✅ TC03: Blank fields validation working correctly");
//
//        } catch (Exception e) {
//            System.err.println("❌ TC03 failed: " + e.getMessage());
//            throw e;
//        }
//    }
//
//    // ===== PRODUCT TESTS =====
//
//    @Test(priority = 2, description = "TC02_AddSingleProductToCart",
//            groups = {"smoke", "products"}, dependsOnMethods = {"TC01_RegisterUserSuccessfully"})
//    public void TC04_AddSingleProductToCart() throws InterruptedException {
//        try {
//            // Navigate to products page
//            navigateToProducts();
//
//            // Verify we're on products page
//            Assert.assertTrue(productPage.isOnProductsPage(), "Not on products page");
//
//            // Add product to cart
//            productPage.addProductToCart("Blue Cotton Indie Mickey Dress");
//
//            // Verify we're on cart page
//            wait.until(ExpectedConditions.urlContains("view_cart"));
//            Assert.assertTrue(driver.getCurrentUrl().contains("view_cart"),
//                    "Not redirected to cart page. Current URL: " + driver.getCurrentUrl());
//
//            // Verify cart is not empty
//            Assert.assertTrue(productPage.isCartNotEmpty(), "Cart is empty after adding product");
//
//            System.out.println("✅ TC04: Product added to cart successfully");
//
//        } catch (Exception e) {
//            System.err.println("❌ TC04 failed: " + e.getMessage());
//            logCurrentState();
//            throw e;
//        }
//    }
//
//    @Test(priority = 3, description = "TC03_AddMultipleProductsToCart",
//            groups = {"regression", "products"}, dataProvider = "multipleProductsData",
//            dataProviderClass = TestDataProviders.class, dependsOnMethods = {"TC01_RegisterUserSuccessfully"})
//    public void TC05_AddMultipleProductsToCart(String[] productNames) throws InterruptedException {
//        try {
//            // Navigate to products page
//            navigateToProducts();
//
//            // Verify product exists
//            //Assert.assertTrue(productPage.isProductDisplayed(productName),"Product not found: " + productName);
//
//            // Add product to cart
//            productPage.addMultipleProductsToCart(productNames);
//
//            // Verify product added
//            Assert.assertTrue(productPage.isCartNotEmpty(), "Cart is empty after adding: ");
//
//            System.out.println("✅ TC03: Product added successfully: ");
//
//        } catch (Exception e) {
//            System.err.println("❌ TC03 failed for product " + ": " + e.getMessage());
//            throw e;
//        }
//    }
//
//    @Test(priority = 4, description = "TC04_SearchProduct",
//            groups = {"regression", "products"})
//    public void TC06_SearchProduct() throws InterruptedException {
//        try {
//            // Navigate to products page
//            navigateToProducts();
//
//            // Search for a product
//            productPage.searchForProduct("dress");
//
//            // Verify search results are displayed
//            Assert.assertTrue(productPage.areSearchResultsDisplayed(),
//                    "Search results are not displayed");
//
//            // Verify at least one product is found
//            Assert.assertTrue(productPage.getProductCount() > 0,
//                    "No products found in search results");
//
//            System.out.println("✅ TC04: Product search working correctly");
//
//        } catch (Exception e) {
//            System.err.println("❌ TC04 failed: " + e.getMessage());
//            throw e;
//        }
//    }
//
//    @Test(priority = 5, description = "TC05_VerifyProductsPage",
//            groups = {"smoke", "products"})
//    public void TC07_VerifyProductsPage() throws InterruptedException {
//        try {
//            // Navigate to products page
//            navigateToProducts();
//
//            // Verify we're on products page
//            Assert.assertTrue(productPage.isOnProductsPage(), "Not on products page");
//
//            // Verify products are displayed
//            int productCount = productPage.getProductCount();
//            Assert.assertTrue(productCount > 0, "No products displayed. Count: " + productCount);
//
//            System.out.println("✅ TC05: Products page verification successful. Products found: " + productCount);
//
//        } catch (Exception e) {
//            System.err.println("❌ TC05 failed: " + e.getMessage());
//            throw e;
//        }
//    }
//
//    // ===== CHECKOUT TESTS =====
//
//    @Test(priority = 6, description = "TC06_SuccessfulCheckout",
//            groups = {"smoke", "checkout"}, dataProvider = "paymentData",
//            dataProviderClass = TestDataProviders.class, dependsOnMethods = {"TC04_AddSingleProductToCart"})
//    public void TC08_SuccessfulCheckout(String cardName, String cardNumber, String cvc,
//                                        String month, String year) throws InterruptedException {
//        try {
//            // Navigate to products and add item to cart
//            navigateToProducts();
//            productPage.addProductToCart("Blue Cotton Indie Mickey Dress");
//
//            // Wait to ensure we're on cart page
//            wait.until(ExpectedConditions.urlContains("view_cart"));
//
//            // Proceed to checkout
//            productPage.proceedToCheckout();
//
//            // Wait for checkout page
//            waitFor(2);
//
//            productPage.clickPlaceOrder();
//
//            // Complete payment
//            boolean paymentSuccess = orderPayment.completePaymentProcess(cardName, cardNumber, cvc, month, year);
//
//            // Verify payment success
//            Assert.assertTrue(paymentSuccess, "Payment was not successful");
//            Assert.assertTrue(orderPayment.isPaymentSuccessful(), "Payment success message not displayed");
//
//            System.out.println("✅ TC08: Checkout completed successfully");
//
//        } catch (Exception e) {
//            System.err.println("❌ TC08 failed: " + e.getMessage());
//            logCurrentState();
//            throw e;
//        }
//    }
//
//    @Test(priority = 9, description = "TC09_CheckoutWithInvalidPayment",
//            groups = {"regression", "checkout"}, dataProvider = "invalidPaymentData",
//            dataProviderClass = TestDataProviders.class)
//    public void TC09_CheckoutWithInvalidPayment(String cardName, String cardNumber, String cvc,
//                                                String month, String year) throws InterruptedException {
//        try {
//            // Navigate to products and add item to cart
//            navigateToProducts();
//            productPage.addProductToCart("Blue Cotton Indie Mickey Dress");
//
//            // Wait to ensure we're on cart page
//            wait.until(ExpectedConditions.urlContains("view_cart"));
//
//            // Proceed to checkout
//            productPage.proceedToCheckout();
//            productPage.clickPlaceOrder();
//
//            // Try payment with invalid data
//            orderPayment.fillPaymentInfo(cardName, cardNumber, cvc, month, year);
//            orderPayment.submitPayment();
//
//            // Wait for processing
//            waitFor(3);
//
//            // Verify payment failed or validation occurred
//            boolean paymentFailed = orderPayment.isPaymentFailed() ||
//                    !orderPayment.isPaymentSuccessful();
//
//            Assert.assertTrue(paymentFailed, "Payment should have failed with invalid data");
//
//            System.out.println("✅ TC09: Invalid payment handling working correctly");
//
//        } catch (Exception e) {
//            System.err.println("❌ TC09 failed: " + e.getMessage());
//            throw e;
//        }
//    }
//
//    @Test(priority = 10, description = "TC10_GuestCheckout",
//            groups = {"regression", "checkout"})
//    public void TC10_GuestCheckout() throws InterruptedException {
//        try {
//            // Navigate to products without login
//            navigateToProducts();
//
//            // Add product to cart
//            productPage.addProductToCart("Blue Cotton Indie Mickey Dress");
//
//            // Verify cart page
//            wait.until(ExpectedConditions.urlContains("view_cart"));
//            Assert.assertTrue(driver.getCurrentUrl().contains("view_cart"),
//                    "Not on cart page");
//
//            // Verify product added successfully
//            Assert.assertTrue(productPage.isCartNotEmpty(), "Cart is empty");
//
//            System.out.println("✅ TC10: Guest checkout (add to cart) working correctly");
//
//        } catch (Exception e) {
//            System.err.println("❌ TC10 failed: " + e.getMessage());
//            throw e;
//        }
//    }
//
//    // ===== USER MANAGEMENT TESTS =====
//
//    @Test(priority = 11, description = "TC11_LoginLogoutFunctionality",
//            groups = {"smoke", "user"}, dependsOnMethods = {"TC01_RegisterUserSuccessfully"})
//    public void TC11_LoginLogoutFunctionality() {
//        try {
//            // Verify user is already logged in from registration
//            wait.until(ExpectedConditions.presenceOfElementLocated(
//                    By.xpath("//a[contains(text(),'Logged in as')]")));
//
//            // Logout
//            wait.until(ExpectedConditions.elementToBeClickable(
//                    By.xpath("//a[contains(text(),'Logout')]")));
//            driver.findElement(By.xpath("//a[contains(text(),'Logout')]")).click();
//
//            // Verify logout successful
//            wait.until(ExpectedConditions.urlContains("/login"));
//            Assert.assertTrue(driver.getCurrentUrl().contains("/login"),
//                    "User not redirected to login page after logout");
//
//            System.out.println("✅ TC11: Login/Logout functionality working correctly");
//
//        } catch (Exception e) {
//            System.err.println("❌ TC11 failed: " + e.getMessage());
//            throw e;
//        }
//    }
//
//    @Test(priority = 12, description = "TC12_VerifyHomePage",
//            groups = {"smoke", "navigation"})
//    public void TC12_VerifyHomePage() {
//        try {
//            // Navigate to home page
//            driver.get(baseUrl);
//
//            // Wait for page to load
//            wait.until(webDriver -> ((org.openqa.selenium.JavascriptExecutor) webDriver)
//                    .executeScript("return document.readyState").equals("complete"));
//
//            // Verify page title
//            String pageTitle = driver.getTitle();
//            Assert.assertTrue(pageTitle.contains("Automation Exercise"),
//                    "Page title incorrect: " + pageTitle);
//
//            // Verify main elements are present
//            wait.until(ExpectedConditions.presenceOfElementLocated(
//                    By.xpath("//a[contains(text(),'Home')]")));
//            Assert.assertTrue(driver.findElement(By.xpath("//a[contains(text(),'Home')]")).isDisplayed(),
//                    "Home link not displayed");
//
//            System.out.println("✅ TC12: Home page verification successful");
//
//        } catch (Exception e) {
//            System.err.println("❌ TC12 failed: " + e.getMessage());
//            throw e;
//        }
//    }
//
//    // ===== PERFORMANCE & EDGE CASE TESTS =====
//
//    @Test(priority = 13, description = "TC13_SlowConnectionHandling",
//            groups = {"performance", "regression"})
//    public void TC13_SlowConnectionHandling() throws InterruptedException {
//        try {
//            // Simulate slow connection
//            waitFor(3);
//
//            // Navigate to products
//            navigateToProducts();
//
//            // Add product to cart with extra patience
//            productPage.addProductToCart("Blue Cotton Indie Mickey Dress");
//
//            // Additional wait to simulate slow processing
//            waitFor(2);
//
//            // Proceed with checkout
//            productPage.proceedToCheckout();
//            productPage.clickPlaceOrder();
//
//            // Complete payment with extra patience
//            boolean paymentSuccess = orderPayment.completePaymentProcess(
//                    "Mario Medhat", "4111111111111111", "123", "12", "2025");
//
//            Assert.assertTrue(paymentSuccess, "Payment failed under slow conditions");
//
//            System.out.println("✅ TC13: Slow connection handling successful");
//
//        } catch (Exception e) {
//            System.err.println("❌ TC13 failed: " + e.getMessage());
//            throw e;
//        }
//    }
//
//    @Test(priority = 14, description = "TC14_NavigationTest",
//            groups = {"regression", "navigation"})
//    public void TC14_NavigationTest() throws InterruptedException {
//        try {
//            // Test navigation to different pages
//            navigateToProducts();
//            Assert.assertTrue(productPage.isOnProductsPage(), "Products page navigation failed");
//
//            navigateToSignup();
//            wait.until(ExpectedConditions.urlContains("login"));
//            Assert.assertTrue(driver.getCurrentUrl().contains("login"), "Signup page navigation failed");
//
//            // Navigate back to home
//            driver.get(baseUrl);
//            wait.until(webDriver -> ((org.openqa.selenium.JavascriptExecutor) webDriver)
//                    .executeScript("return document.readyState").equals("complete"));
//            Assert.assertTrue(driver.getTitle().contains("Automation Exercise"), "Home navigation failed");
//
//            System.out.println("✅ TC14: Navigation test successful");
//
//        } catch (Exception e) {
//            System.err.println("❌ TC14 failed: " + e.getMessage());
//            throw e;
//        }
//    }
//
//    @Test(priority = 15, description = "TC15_DataValidationTest",
//            groups = {"regression", "validation"}, dataProvider = "registrationData",
//            dataProviderClass = TestDataProviders.class)
//    public void TC15_DataValidationTest(String name, String email, String password,
//                                        String firstName, String lastName, String company,
//                                        String address1, String address2, String state,
//                                        String city, String zip, String mobile) {
//        try {
//            // Navigate to signup
//            navigateToSignup();
//
//            // Generate unique email for this test
//            String uniqueEmail = "test" + System.currentTimeMillis() + "@example.com";
//
//            // Enter signup info
//            regPage.enterSignupInfo(name, uniqueEmail);
//
//            // Fill registration form
//            regPage.completeRegistration(password, firstName, lastName, company,
//                    address1, address2, state, city, zip, mobile);
//
//            // Verify account creation
//            Assert.assertTrue(regPage.isAccountCreated(),
//                    "Account creation failed with data: " + name);
//
//            System.out.println("✅ TC15: Data validation test passed for: " + name);
//
//        } catch (Exception e) {
//            System.err.println("❌ TC15 failed for user " + name + ": " + e.getMessage());
//            throw e;
//        }
//    }
//
//    // ===== HELPER METHOD FOR DEBUGGING =====
//
//    private void debugCartState() {
//        try {
//            System.out.println("🔍 DEBUG: Current URL: " + driver.getCurrentUrl());
//            System.out.println("🔍 DEBUG: Page title: " + driver.getTitle());
//
//            // Check for cart elements
//            try {
//                boolean checkoutBtnExists = driver.findElements(
//                        By.xpath("//a[normalize-space()='Proceed To Checkout']")).size() > 0;
//                System.out.println("🔍 DEBUG: Checkout button exists: " + checkoutBtnExists);
//
//                int cartRows = driver.findElements(
//                        By.cssSelector("table#cart_info_table tbody tr")).size();
//                System.out.println("🔍 DEBUG: Cart rows count: " + cartRows);
//
//            } catch (Exception e) {
//                System.out.println("🔍 DEBUG: Error checking cart elements: " + e.getMessage());
//            }
//
//        } catch (Exception e) {
//            System.err.println("❌ Error in debug method: " + e.getMessage());
//        }
//    }
//}