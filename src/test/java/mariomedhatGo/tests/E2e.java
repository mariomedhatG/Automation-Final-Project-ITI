package mariomedhatGo.tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import mariomedhatGo.dataproviders.TestDataProviders;

public class E2e extends BaseTest {

    @Test(description = "End-To-End User Valid Sceinario" , groups = {"sanity" , "smoke"}
            , dataProvider = "completeE2EScenario" , dataProviderClass = TestDataProviders.class)
    public void E2eValidScenario(String userName , String userEmail , String[] registData ,
                                 String productName, String cardName, String cardNumber,
                                 String cvc, String month, String year) throws InterruptedException {
        try {
            // Registration
            navigateToSignup();
            regPage.enterSignupInfo(userName, userEmail);
            regPage.completeRegistration(registData);
            regPage.clickContinue();

            // Shopping
            navigateToProducts();
            productPage.addProductToCart(productName);

            // Checkout
            productPage.proceedToCheckout();
            productPage.clickPlaceOrder();

            // Payment
            orderPayment.completePaymentProcess(cardName, cardNumber, cvc, month, year);
            Assert.assertTrue(orderPayment.isPaymentSuccessful());

            //Logout
            regPage.logout();

        } catch (Exception e){
            System.err.println("TC failed: " + e.getMessage());
            logCurrentState();
            throw e;
        }
    }

    @Test(description = "Multiple products checkout", groups = {"smoke", "sanity"},
            dataProvider = "multiProductE2EScenario", dataProviderClass = TestDataProviders.class)
    public void testMultipleProductsCheckout(String userName, String userEmail, String[] registData,
                                             String[] productNames, String cardName, String cardNumber,
                                             String cvc, String month, String year) throws InterruptedException {
        try {
            // Step 1: Register user
            navigateToSignup();
            regPage.enterSignupInfo(userName, userEmail);
            regPage.completeRegistration(registData);
            regPage.clickContinue();

            // Step 2: Add multiple products to cart
            navigateToProducts();
            productPage.addMultipleProductsToCart(productNames);

            // Step 3: Verify cart
            Assert.assertTrue(driver.getCurrentUrl().contains("view_cart"), "Not on cart page");
            Assert.assertTrue(productPage.isCartNotEmpty(), "Cart is empty");

            // Step 4: Complete checkout
            productPage.proceedToCheckout();
            productPage.clickPlaceOrder();

            // Step 5: Complete payment
            boolean paymentSuccess = orderPayment.completePaymentProcess(cardName, cardNumber, cvc, month, year);

            Assert.assertTrue(paymentSuccess, "Payment was not successful");
            Assert.assertTrue(orderPayment.isPaymentSuccessful(), "Success message not displayed");

            System.out.println("Multiple products checkout passed");

        } catch (Exception e) {
            System.err.println("Multiple products checkout failed: " + e.getMessage());
            throw e;
        }
    }
}