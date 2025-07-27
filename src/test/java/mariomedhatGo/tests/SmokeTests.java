package mariomedhatGo.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

public class SmokeTests extends BaseTest {

    @Test(description = "Verify home page loads", groups = {"smoke"}, priority = 1)
    public void verifyHomePage() {
        String pageTitle = driver.getTitle();
        Assert.assertTrue(pageTitle.contains("Automation Exercise"), "Home page title incorrect");

        // Verify main navigation elements
        Assert.assertTrue(driver.findElement(org.openqa.selenium.By.linkText("Home")).isDisplayed(),
                "Home link not displayed");
        Assert.assertTrue(driver.findElement(org.openqa.selenium.By.linkText("Products")).isDisplayed(),
                "Products link not displayed");

        System.out.println("✅ Home page verification passed");
    }

    @Test(description = "Verify products page loads", groups = {"smoke"}, priority = 2)
    public void verifyProductsPage() {
        navigateToProducts();

        Assert.assertTrue(productPage.isOnProductsPage(), "Products page not loaded");
        Assert.assertTrue(productPage.getProductCount() > 0, "No products found");

        System.out.println("✅ Products page verification passed");
    }

    @Test(description = "Verify signup page loads", groups = {"smoke"}, priority = 3)
    public void verifySignupPage() {
        navigateToSignup();

        Assert.assertTrue(driver.getCurrentUrl().contains("login"), "Not on signup page");
        Assert.assertTrue(driver.getPageSource().contains("New User Signup!"),
                "Signup form not displayed");

        System.out.println("✅ Signup page verification passed");
    }
}