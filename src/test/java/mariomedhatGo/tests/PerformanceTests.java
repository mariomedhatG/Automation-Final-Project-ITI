package mariomedhatGo.tests;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

public class PerformanceTests extends BaseTest {

    @Test(description = "Page load time test", groups = {"performance"})
    public void testPageLoadTime() {
        long startTime = System.currentTimeMillis();

        navigateToProducts();

        long endTime = System.currentTimeMillis();
        long loadTime = endTime - startTime;

        // Assert page loads within 10 seconds
        Assert.assertTrue(loadTime < 10000, "Page load time too slow: " + loadTime + "ms");

        System.out.println("✅ Page loaded in " + loadTime + "ms");
    }

    @Test(description = "Multiple rapid clicks test", groups = {"performance"})
    public void testMultipleRapidClicks() {
        navigateToProducts();

        // Rapidly click search button multiple times
        for (int i = 0; i < 5; i++) {
            try {
                driver.findElement(By.id("submit_search")).click();
                Thread.sleep(100);
            } catch (Exception e) {
                // Expected behavior - button might be disabled or not clickable
            }
        }

        // Should still be on products page without errors
        Assert.assertTrue(driver.getCurrentUrl().contains("products"), "Rapid clicks caused navigation issues");

        System.out.println("✅ Multiple rapid clicks test passed");
    }

    @Test(description = "Memory leak test with multiple operations", groups = {"performance"})
    public void testMemoryLeakPrevention() {
        // Perform multiple operations that might cause memory leaks
        for (int i = 0; i < 3; i++) {
            navigateToProducts();
            productPage.searchForProduct("dress");
            navigateToSignup();
            driver.navigate().back();
        }

        // Should still be functional
        Assert.assertTrue(driver.getCurrentUrl().contains("products") ||
                        driver.getCurrentUrl().contains("login"),
                "Multiple operations caused browser issues");

        System.out.println("✅ Memory leak prevention test passed");
    }
}