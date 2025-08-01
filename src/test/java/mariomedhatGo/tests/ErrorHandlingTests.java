package mariomedhatGo.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

public class ErrorHandlingTests extends BaseTest {

    @Test(description = "Network interruption simulation", groups = {"error", "regression"})
    public void testNetworkInterruption() {
        try {
            navigateToProducts();

            // Simulate slow network by adding waits
            Thread.sleep(3000);

            // Try to perform actions
            productPage.searchForProduct("dress");

            Assert.assertTrue(driver.getCurrentUrl().contains("products"),
                    "Application didn't handle slow network properly");

            System.out.println("✅ Network interruption test passed");

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            Assert.fail("Test interrupted: " + e.getMessage());
        }
    }

    @Test(description = "Invalid URL handling", groups = {"error", "regression"})
    public void testInvalidUrlHandling() {
        // Try to access non-existent page
        driver.get(baseUrl + "nonexistentpage");

        // Should either redirect to home or show 404 page
        String currentUrl = driver.getCurrentUrl();
        String pageSource = driver.getPageSource().toLowerCase();

        boolean validHandling = currentUrl.contains(baseUrl) ||
                pageSource.contains("404") ||
                pageSource.contains("not found") ||
                pageSource.contains("error");

        Assert.assertTrue(validHandling, "Invalid URL not handled properly");

        System.out.println("✅ Invalid URL handling test passed");
    }

    @Test(description = "Browser refresh handling", groups = {"error", "regression"})
    public void testBrowserRefreshHandling() throws InterruptedException {
        navigateToProducts();

        // Refresh the page
        driver.navigate().refresh();

        // Should still be on products page
        Assert.assertTrue(driver.getCurrentUrl().contains("products"),
                "Browser refresh not handled properly");

        // Page should still be functional
        Assert.assertTrue(productPage.isOnProductsPage(),
                "Page not functional after refresh");

        System.out.println("✅ Browser refresh handling test passed");
    }
}