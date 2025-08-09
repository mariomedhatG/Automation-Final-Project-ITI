package mariomedhatGo.tests;

import java.time.Duration;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import io.github.bonigarcia.wdm.WebDriverManager;
import mariomedhatGo.pageopjects.OrderPayment;
import mariomedhatGo.pageopjects.ProductPage;
import mariomedhatGo.pageopjects.RegPage;

public class BaseTest {

    protected WebDriver driver;
    protected WebDriverWait wait;
    protected RegPage regPage;
    protected ProductPage productPage;
    protected OrderPayment orderPayment;
    protected Properties config;
    protected String baseUrl;
    protected String email;

    // Getter for driver (needed for listeners)
    public WebDriver getDriver() {
        return driver;
    }

    @BeforeMethod
    @Parameters({"browser"})
    public void setup(@Optional String browser) {
        loadConfiguration();
        initializeDriver(browser);
        setupBrowser();
        navigateToApplication();
        initializePageObjects();
        generateTestData();
    }

    private void loadConfiguration() {
        config = new Properties();
        try {
            FileInputStream fis = new FileInputStream("src/test/resources/config.properties");
            config.load(fis);
            baseUrl = config.getProperty("base.url", "https://automationexercise.com/");
        } catch (IOException e) {
            System.err.println("⚠️ Config file not found, using default values");
            baseUrl = "https://automationexercise.com/";
        }
    }

    private void initializeDriver(String browser) {
        if (browser == null) {
            browser = "chrome"; // default browser
        }

        switch (browser.toLowerCase()) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--disable-notifications");
                chromeOptions.addArguments("--disable-popup-blocking");
                chromeOptions.addArguments("--disable-features=AutofillAddressProfile");
                chromeOptions.addArguments("--disable-features=PasswordAutosave,AutoFill,CredentialsManagement");
                chromeOptions.addArguments("--disable-blink-features=AutomationControlled");
                chromeOptions.addArguments("--no-sandbox");
                chromeOptions.addArguments("--disable-dev-shm-usage");
                chromeOptions.addArguments("--disable-gpu");
                chromeOptions.addArguments("--remote-allow-origins=*");
                chromeOptions.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
                chromeOptions.setExperimentalOption("useAutomationExtension", false);

                driver = new ChromeDriver(chromeOptions);
                break;

            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver();
                break;

            case "edge":
                WebDriverManager.edgedriver().setup();
                driver = new EdgeDriver();
                break;

            default:
                throw new IllegalArgumentException("Browser not supported: " + browser);
        }
    }

    private void setupBrowser() {
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(45));
        driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(30));

        // إضافة WebDriverWait
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    private void navigateToApplication() {
        try {
            driver.get(baseUrl);

            wait.until(webDriver -> ((org.openqa.selenium.JavascriptExecutor) webDriver)
                    .executeScript("return document.readyState").equals("complete"));

            System.out.println("✅ Application loaded successfully: " + baseUrl);

        } catch (Exception e) {
            System.err.println("❌ Error navigating to application: " + e.getMessage());
            throw e;
        }
    }

    private void initializePageObjects() {
        regPage = new RegPage(driver);
        productPage = new ProductPage(driver);
        orderPayment = new OrderPayment(driver);
    }

    private void generateTestData() {
        email = "mario" + System.currentTimeMillis() + "@gmail.com";
        System.out.println(" Generated test email: " + email);
    }

    // إصلاح Common helper methods for tests
    protected void navigateToSignup() {
        try {
            String signupUrl = baseUrl + "login";
            driver.get(signupUrl);

            // Wait for page to load completely
            wait.until(webDriver -> ((org.openqa.selenium.JavascriptExecutor) webDriver)
                    .executeScript("return document.readyState").equals("complete"));

            System.out.println("✅ Navigated to signup page: " + signupUrl);

        } catch (Exception e) {
            System.err.println("❌ Error navigating to signup: " + e.getMessage());
            throw e;
        }
    }

    protected void navigateToProducts() throws InterruptedException {
        try {
            String productsUrl = baseUrl + "products";
            driver.get(productsUrl);

            // Wait for page to load completely
            wait.until(webDriver -> ((org.openqa.selenium.JavascriptExecutor) webDriver)
                    .executeScript("return document.readyState").equals("complete"));

            // Additional wait for products to load
            Thread.sleep(2000);

            System.out.println("Navigated to products page: " + productsUrl);

        } catch (Exception e) {
            System.err.println("Error navigating to products: " + e.getMessage());
            throw e;
        }
    }

    protected void waitFor(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Wait interrupted: " + e.getMessage());
        }
    }

    protected void logCurrentState() {
        try {
            System.out.println("Current URL: " + driver.getCurrentUrl());
            System.out.println("Page Title: " + driver.getTitle());
            System.out.println("Window Handle: " + driver.getWindowHandle());
        } catch (Exception e) {
            System.err.println("❌ Error logging current state: " + e.getMessage());
        }
    }

    protected void handleAlertIfPresent() {
        try {
            org.openqa.selenium.Alert alert = wait.until(
                    org.openqa.selenium.support.ui.ExpectedConditions.alertIsPresent()
            );
            String alertText = alert.getText();
            System.out.println("⚠️ Alert detected: " + alertText);
            alert.accept();
        } catch (Exception e) {
            // No alert present, continue
        }
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            try {
                // Log final state for debugging
                System.out.println("🏁 Test completed. Final URL: " + driver.getCurrentUrl());

                // Handle any remaining alerts
                handleAlertIfPresent();

                driver.quit();
                System.out.println("Browser closed successfully");

            } catch (Exception e) {
                System.err.println("Error during teardown: " + e.getMessage());

                // Force close if normal quit fails
                try {
                    if (driver != null) {
                        driver.quit();
                    }
                } catch (Exception ex) {
                    System.err.println("Force close also failed: " + ex.getMessage());
                }
            }
        }
    }
}