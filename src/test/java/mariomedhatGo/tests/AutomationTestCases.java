package mariomedhatGo.tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import mariomedhatGo.pageopjects.OrderPayment;
import mariomedhatGo.pageopjects.ProductPage;
import mariomedhatGo.pageopjects.RegPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;

public class AutomationTestCases {

    WebDriver driver;
    RegPage regPage;
    ProductPage productPage;
    OrderPayment orderPayment;
    String email;

    @BeforeMethod
    public void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://automationexercise.com/");
        email = "mario" + System.currentTimeMillis() + "@gmail.com";
    }

    @Test(priority = 1, description = "TC01_RegisterUserSuccessfully")
    public void TC01_RegisterUserSuccessfully() {
        driver.findElement(By.cssSelector("a[href='/login']")).click();
        regPage = new RegPage(driver);
        regPage.RegisterWeb("Mario Medhat", email);
        regPage.RegisterWebComplete("marioM123/", "Mario", "Medhat", "Mario Co", "13 Ali st", "14 Mohamed st", "NY", "Badr", "12345", "0123456789");
        regPage.continueButton();
        Assert.assertTrue(driver.findElement(By.xpath("//a[contains(text(),'Logged in as')]")) != null);
    }

    @Test(priority = 2, description = "TC02_AddProductToCart")
    public void TC02_AddProductToCart() {
        TC01_RegisterUserSuccessfully();
        driver.findElement(By.xpath("//a[contains(text(),'Products')]")).click();
        productPage = new ProductPage(driver);
        productPage.addProductToCart("Blue Cotton Indie Mickey Dress");
        Assert.assertTrue(driver.getCurrentUrl().contains("view_cart"));
    }

    @Test(priority = 3, description = "TC03_CheckoutAndPlaceOrder")
    public void TC03_CheckoutAndPlaceOrder() {
        TC02_AddProductToCart();
        productPage.CheckoutButton();
        productPage.placeOrderButton();
        orderPayment = new OrderPayment(driver);
        orderPayment.submitOrder("Mario Medhat", "12345678910123", "456", "03", "2028");
        Assert.assertTrue(driver.getPageSource().contains("Your order has been placed"));
    }

    @Test(priority = 4, description = "TC04_NavigateToProductPageAndVerifyProduct")
    public void TC04_NavigateToProductPageAndVerifyProduct() {
        driver.findElement(By.xpath("//a[contains(text(),'Products')]")).click();
        Assert.assertTrue(driver.getPageSource().contains("All Products"));
    }

    @Test(priority = 5, description = "TC05_LogoutFunctionality")
    public void TC05_LogoutFunctionality() {
        TC01_RegisterUserSuccessfully();
        driver.findElement(By.xpath("//a[contains(text(),'Logout')]")).click();
        Assert.assertTrue(driver.getCurrentUrl().contains("/login"));
    }

    @Test(priority = 6, description = "TC06_RegisterWithExistingEmail")
    public void TC06_RegisterWithExistingEmail() {
        TC01_RegisterUserSuccessfully();
        driver.findElement(By.cssSelector("a[href='/login']")).click();
        regPage.RegisterWeb("Mario Medhat", email);
        Assert.assertTrue(driver.getPageSource().contains("Email Address already exist!"));
    }

    @Test(priority = 7, description = "TC07_LeaveMandatoryFieldsBlank")
    public void TC07_LeaveMandatoryFieldsBlank() {
        driver.findElement(By.cssSelector("a[href='/login']")).click();
        driver.findElement(By.cssSelector("button[data-qa='signup-button']")).click();
        Assert.assertTrue(driver.getPageSource().contains("Name is required"));
    }

    @Test(priority = 8, description = "TC08_InvalidCardData")
    public void TC08_InvalidCardData() {
        TC02_AddProductToCart();
        productPage.CheckoutButton();
        productPage.placeOrderButton();
        orderPayment = new OrderPayment(driver);
        orderPayment.submitOrder("Test", "1111", "12", "01", "2000");
        Assert.assertTrue(driver.getPageSource().contains("Payment failed"));
    }

    @Test(priority = 9, description = "TC09_AddProductWithoutLogin")
    public void TC09_AddProductWithoutLogin() {
        driver.findElement(By.xpath("//a[contains(text(),'Products')]")).click();
        productPage = new ProductPage(driver);
        productPage.addProductToCart("Blue Cotton Indie Mickey Dress");
        Assert.assertTrue(driver.getCurrentUrl().contains("view_cart"));
    }

    @Test(priority = 10, description = "TC10_SlowInternetHandling")
    public void TC10_SlowInternetHandling() throws InterruptedException {
        TC02_AddProductToCart();
        Thread.sleep(5000); // simulate slow loading
        productPage.CheckoutButton();
        productPage.placeOrderButton();
        orderPayment = new OrderPayment(driver);
        orderPayment.submitOrder("Mario Medhat", "12345678910123", "456", "03", "2028");
        Assert.assertTrue(driver.getPageSource().contains("Your order has been placed"));
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
