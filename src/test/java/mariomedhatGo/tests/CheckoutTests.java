package mariomedhatGo.tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import mariomedhatGo.dataproviders.TestDataProviders;

public class CheckoutTests extends BaseTest {

    @Test(description = "Successful checkout process", groups = {"smoke", "checkout"},
            dataProvider = "paymentData", dataProviderClass = TestDataProviders.class)
    public void testSuccessfulCheckout(String cardName, String cardNumber, String cvc,
                                       String month, String year) throws InterruptedException {
        // Register and add product
        setupUserAndProduct();

        // Complete checkout
        productPage.proceedToCheckout();
        productPage.clickPlaceOrder();

        boolean paymentSuccess = orderPayment.completePaymentProcess(cardName, cardNumber, cvc, month, year);

        Assert.assertTrue(paymentSuccess, "Payment was not successful");
        Assert.assertTrue(orderPayment.isPaymentSuccessful(), "Success message not displayed");

        System.out.println("✅ Successful checkout test passed with card: " + cardName);
    }

    @Test(description = "Checkout with invalid payment data", groups = {"regression"},
            dataProvider = "invalidPaymentData", dataProviderClass = TestDataProviders.class)
    public void testCheckoutWithInvalidPayment(String cardName, String cardNumber, String cvc,
                                               String month, String year) throws InterruptedException {
        // Register and add product
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

        System.out.println("✅ Invalid payment test passed for card: " + cardNumber);
    }

    @Test(description = "Guest checkout functionality", groups = {"regression"})
    public void testGuestCheckout() throws InterruptedException {
        // Don't register, just add product as guest
        navigateToProducts();
        productPage.addProductToCart("Blue Cotton Indie Mickey Dress");

        Assert.assertTrue(driver.getCurrentUrl().contains("view_cart"), "Not on cart page");
        Assert.assertTrue(productPage.isCartNotEmpty(), "Cart is empty for guest user");

        System.out.println("✅ Guest checkout test passed");
    }

    // Helper method for common setup
    private void setupUserAndProduct() throws InterruptedException {
        navigateToSignup();
        regPage.enterSignupInfo("Checkout Tester", email);
        regPage.completeRegistration("password123", "Checkout", "Tester", "TestCorp",
                "123 Checkout St", "Apt 1", "CA", "TestCity", "12345", "1234567890");
        regPage.clickContinue();

        navigateToProducts();
        productPage.addProductToCart("Blue Cotton Indie Mickey Dress");
    }
}