package mariomedhatGo.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

public class CrossBrowserTests extends BaseTest {

    @Test(description = "Cross browser registration test", groups = {"crossbrowser"})
    public void testCrossBrowserRegistration() {
        navigateToSignup();
        regPage.enterSignupInfo("CrossBrowser Tester", email);
        regPage.completeRegistration("password123", "Cross", "Browser", "TestCorp",
                "123 Browser St", "Apt 1", "CA", "TestCity", "12345", "1234567890");

        Assert.assertTrue(regPage.isAccountCreated(), "Cross browser registration failed");
        System.out.println("✅ Cross browser registration test passed");
    }

    @Test(description = "Cross browser product test", groups = {"crossbrowser"})
    public void testCrossBrowserProduct() throws InterruptedException {
        navigateToProducts();

        Assert.assertTrue(productPage.isOnProductsPage(), "Products page failed in this browser");
        Assert.assertTrue(productPage.getProductCount() > 0, "Products not loaded in this browser");

        System.out.println("✅ Cross browser product test passed");
    }
}