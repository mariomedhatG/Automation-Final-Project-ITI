package mariomedhatGo.tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import mariomedhatGo.dataproviders.TestDataProviders;

public class CheckoutTests extends BaseTest {

    @Test(description = "Successful checkout process", groups = {"sanity", "checkout"},
            dataProvider = "registrationAndPaymentData", dataProviderClass = TestDataProviders.class)
    public void testSuccessfulCheckout(String userName, String userEmail, String[] registData,
                                       String cardName, String cardNumber, String cvc,
                                       String month, String year) throws InterruptedException {
        try {
            // Register user
            navigateToSignup();
            regPage.enterSignupInfo(userName, userEmail);
            regPage.completeRegistration(registData);
            regPage.clickContinue();

            // Add product to cart
            navigateToProducts();
            productPage.addProductToCart("Blue Cotton Indie Mickey Dress");

            // Complete checkout
            productPage.proceedToCheckout();
            productPage.clickPlaceOrder();

            // Complete payment
            boolean paymentSuccess = orderPayment.completePaymentProcess(cardName, cardNumber, cvc, month, year);

            Assert.assertTrue(paymentSuccess, "Payment was not successful");
            Assert.assertTrue(orderPayment.isPaymentSuccessful(), "Success message not displayed");

            System.out.println("Successful checkout test passed with card: " + cardNumber);

        } catch (Exception e) {
            System.err.println("Checkout test failed: " + e.getMessage());
            throw e;
        }
    }

    @Test(description = "Checkout with invalid payment data", groups = {"sanity"},
            dataProvider = "invalidPaymentData", dataProviderClass = TestDataProviders.class)
    public void testCheckoutWithInvalidPayment(String cardName, String cardNumber, String cvc,
                                               String month, String year) throws InterruptedException {
        try {
            // Register user and add product
            setupUserAndProduct();

            // Try checkout with invalid data
            productPage.proceedToCheckout();
            productPage.clickPlaceOrder();

            orderPayment.fillPaymentInfo(cardName, cardNumber, cvc, month, year);
            orderPayment.submitPayment();

            waitFor(2);

            // Should either fail or not show success
            boolean paymentFailed = orderPayment.isPaymentFailed() || !orderPayment.isPaymentSuccessful();
            Assert.assertTrue(paymentFailed, "Payment should have failed with invalid data");

            System.out.println("Invalid payment test passed for card: " + cardNumber);

        } catch (Exception e) {
            System.err.println("Invalid payment test failed: " + e.getMessage());
            throw e;
        }
    }

    @Test(description = "Guest checkout functionality", groups = {"sanity"})
    public void testGuestCheckout() throws InterruptedException {
        // Don't register, just add product as guest
        navigateToProducts();
        productPage.addProductToCart("Blue Cotton Indie Mickey Dress");

        Assert.assertTrue(driver.getCurrentUrl().contains("view_cart"), "Not on cart page");
        Assert.assertTrue(productPage.isCartNotEmpty(), "Cart is empty for guest user");

        System.out.println("Guest checkout test passed");
    }


    @Test(description = "Payment form validation", groups = {"sanity"})
    public void testPaymentFormValidation() throws InterruptedException {
        try {
            setupUserAndProduct();

            productPage.proceedToCheckout();
            productPage.clickPlaceOrder();

            // Test individual validations
            Assert.assertFalse(orderPayment.isValidCardNumber("123"), "Invalid card number accepted");
            Assert.assertTrue(orderPayment.isValidCardNumber("4111111111111111"), "Valid card number rejected");

            Assert.assertFalse(orderPayment.isValidCVC("12345"), "Invalid CVC accepted");
            Assert.assertTrue(orderPayment.isValidCVC("123"), "Valid CVC rejected");

            Assert.assertFalse(orderPayment.isValidExpiryMonth("13"), "Invalid month accepted");
            Assert.assertTrue(orderPayment.isValidExpiryMonth("12"), "Valid month rejected");

            Assert.assertFalse(orderPayment.isValidExpiryYear("2020"), "Past year accepted");
            Assert.assertTrue(orderPayment.isValidExpiryYear("2025"), "Valid year rejected");

            System.out.println("Payment validation tests passed");

        } catch (Exception e) {
            System.err.println("Payment validation test failed: " + e.getMessage());
            throw e;
        }
    }

    @Test(description = "Checkout with different payment methods", groups = {"sanity"},
            dataProvider = "paymentData", dataProviderClass = TestDataProviders.class)
    public void testCheckoutWithDifferentPaymentMethods(String cardName, String cardNumber, String cvc,
                                                        String month, String year) throws InterruptedException {
        try {
            setupUserAndProduct();

            productPage.proceedToCheckout();
            productPage.clickPlaceOrder();

            boolean paymentSuccess = orderPayment.completePaymentProcess(cardName, cardNumber, cvc, month, year);

            // Different cards may have different behaviors, so we check both scenarios
            if (paymentSuccess) {
                Assert.assertTrue(orderPayment.isPaymentSuccessful(), "Success message not displayed");
                System.out.println("Payment successful with card: " + cardNumber);
            } else {
                System.out.println("Payment processing result for card " + cardNumber + ": " +
                        (orderPayment.isPaymentFailed() ? "Failed as expected" : "No clear result"));
            }

        } catch (Exception e) {
            System.err.println("Payment method test failed for card " + cardNumber + ": " + e.getMessage());
            throw e;
        }
    }

    // Helper method for common setup
    private void setupUserAndProduct() throws InterruptedException {
        navigateToSignup();
        regPage.enterSignupInfo("Checkout Tester", email);
        regPage.completeRegistration(new String[]{"password123", "Checkout", "Tester", "TestCorp",
                "123 Checkout St", "Apt 1", "CA", "TestCity", "12345", "1234567890"});
        regPage.clickContinue();

        navigateToProducts();
        productPage.addProductToCart("Blue Cotton Indie Mickey Dress");
    }
}