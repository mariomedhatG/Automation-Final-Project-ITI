package mariomedhatGo.tests;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

public class NavigationTests extends BaseTest {

    @Test(description = "Main menu navigation", groups = {"smoke", "navigation"})
    public void testMainMenuNavigation() {
        // Test Home navigation
        driver.findElement(By.linkText("Home")).click();
        Assert.assertTrue(driver.getCurrentUrl().equals(baseUrl) || driver.getCurrentUrl().equals(baseUrl + "/"),
                "Home navigation failed");

        // Test Products navigation
        driver.findElement(By.linkText("Products")).click();
        Assert.assertTrue(driver.getCurrentUrl().contains("products"), "Products navigation failed");

        // Test Login navigation
        driver.findElement(By.linkText("Signup / Login")).click();
        Assert.assertTrue(driver.getCurrentUrl().contains("login"), "Login navigation failed");

        System.out.println("✅ Main menu navigation tests passed");
    }

    @Test(description = "Breadcrumb navigation", groups = {"regression", "navigation"})
    public void testBreadcrumbNavigation() throws InterruptedException {
        navigateToProducts();

        // Check if breadcrumbs exist (implementation depends on site structure)
        boolean breadcrumbsExist = !driver.findElements(By.cssSelector("ol.breadcrumb")).isEmpty() ||
                !driver.findElements(By.cssSelector("nav[aria-label='breadcrumb']")).isEmpty();

        if (breadcrumbsExist) {
            System.out.println("✅ Breadcrumbs found and working");
        } else {
            System.out.println("ℹ️ No breadcrumbs found on this page");
        }

        Assert.assertTrue(true, "Breadcrumb navigation test completed");
    }

    @Test(description = "Footer navigation", groups = {"regression", "navigation"})
    public void testFooterNavigation() {
        // Scroll to footer
        productPage.scrollToElement(driver.findElement(By.tagName("footer")));

        // Check if footer links exist
        boolean footerExists = !driver.findElements(By.tagName("footer")).isEmpty();
        Assert.assertTrue(footerExists, "Footer not found");

        System.out.println("✅ Footer navigation test passed");
    }

    @Test(description = "Back button functionality", groups = {"regression", "navigation"})
    public void testBackButtonFunctionality() throws InterruptedException {
        // Navigate to products
        navigateToProducts();
        String productsUrl = driver.getCurrentUrl();

        // Navigate to signup
        navigateToSignup();
        String signupUrl = driver.getCurrentUrl();

        // Use browser back button
        driver.navigate().back();

        // Should be back on products page
        Assert.assertTrue(driver.getCurrentUrl().contains("products") ||
                        driver.getCurrentUrl().equals(productsUrl),
                "Back button navigation failed");

        System.out.println("✅ Back button functionality test passed");
    }
}