package mariomedhatGo.tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import mariomedhatGo.dataproviders.TestDataProviders;

public class ValidationTests extends BaseTest {

    @Test(description = "Invalid registration data validation", groups = {"regression", "validation"},
            dataProvider = "invalidRegistrationData", dataProviderClass = TestDataProviders.class)
    public void testInvalidRegistrationData(String name, String email) {
        navigateToSignup();
        regPage.enterSignupInfo(name, email);

        // Should either show validation message or stay on same page
        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("login") || currentUrl.contains("signup"),
                "Invalid registration data handled incorrectly");

        System.out.println("✅ Invalid registration validation test passed for: " + name + ", " + email);
    }

    @Test(description = "Payment form validation", groups = {"regression", "validation"})
    public void testPaymentFormValidation() throws InterruptedException {
        // Setup cart first
        setupBasicUserAndCart();

        productPage.proceedToCheckout();
        productPage.clickPlaceOrder();

        // Test card number validation
        Assert.assertFalse(orderPayment.isValidCardNumber("123"), "Invalid card number accepted");
        Assert.assertTrue(orderPayment.isValidCardNumber("4111111111111111"), "Valid card number rejected");

        // Test CVC validation
        Assert.assertFalse(orderPayment.isValidCVC("12345"), "Invalid CVC accepted");
        Assert.assertTrue(orderPayment.isValidCVC("123"), "Valid CVC rejected");

        // Test expiry month validation
        Assert.assertFalse(orderPayment.isValidExpiryMonth("13"), "Invalid month accepted");
        Assert.assertTrue(orderPayment.isValidExpiryMonth("12"), "Valid month rejected");

        // Test expiry year validation
        Assert.assertFalse(orderPayment.isValidExpiryYear("2020"), "Past year accepted");
        Assert.assertTrue(orderPayment.isValidExpiryYear("2025"), "Valid year rejected");

        System.out.println("✅ Payment form validation tests passed");
    }

    @Test(description = "URL validation tests", groups = {"regression", "validation"})
    public void testUrlValidation() {
        // Test direct navigation to different pages
        driver.get(baseUrl + "products");
        Assert.assertTrue(driver.getCurrentUrl().contains("products"), "Products URL navigation failed");

        driver.get(baseUrl + "login");
        Assert.assertTrue(driver.getCurrentUrl().contains("login"), "Login URL navigation failed");

        driver.get(baseUrl + "contact_us");
        Assert.assertTrue(driver.getCurrentUrl().contains("contact"), "Contact URL navigation failed");

        System.out.println("✅ URL validation tests passed");
    }

    // Helper method
    private void setupBasicUserAndCart() throws InterruptedException {
        navigateToSignup();
        regPage.enterSignupInfo("Validation Tester", email);
        regPage.completeRegistration("password123", "Validation", "Tester", "TestCorp",
                "123 Test St", "Apt 1", "CA", "TestCity", "12345", "1234567890");
        regPage.clickContinue();

        navigateToProducts();
        productPage.addProductToCart("Blue Cotton Indie Mickey Dress");
    }
}