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
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
    }

    private void navigateToApplication() {
        driver.get(baseUrl);
    }

    private void initializePageObjects() {
        regPage = new RegPage(driver);
        productPage = new ProductPage(driver);
        orderPayment = new OrderPayment(driver);
    }

    private void generateTestData() {
        email = "mario" + System.currentTimeMillis() + "@gmail.com";
    }

    // Common helper methods for tests
    protected void navigateToSignup() {
        driver.get(baseUrl + "login");
    }

    protected void navigateToProducts() {
        driver.get(baseUrl + "products");
    }

    protected void waitFor(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            try {
                driver.quit();
                System.out.println("✅ Browser closed successfully");
            } catch (Exception e) {
                System.err.println("❌ Error closing browser: " + e.getMessage());
            }
        }
    }
}