package mariomedhatGo.pageopjects;

import java.time.Duration;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    protected void waitForElementToBeVisible(WebElement element) {
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    protected void waitForElementToBeClickable(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    protected void sendKeysToElement(WebElement element, String text) {
        try {
            waitForElementToBeVisible(element);
            element.clear();
            element.sendKeys(text);
        } catch (Exception e) {
            System.err.println("Error sending keys to element: " + e.getMessage());
            throw e;
        }
    }

    protected void clickElement(WebElement element) {
        try {
            waitForElementToBeClickable(element);
            element.click();
        } catch (Exception e) {
            System.err.println("Error clicking element: " + e.getMessage());
            throw e;
        }
    }

    protected void selectDropdownByValue(WebElement dropdown, String value) {
        try {
            waitForElementToBeVisible(dropdown);
            Select select = new Select(dropdown);
            select.selectByValue(value);
        } catch (Exception e) {
            System.err.println("Error selecting dropdown by value: " + e.getMessage());
            throw e;
        }
    }

    protected void selectDropdownByText(WebElement dropdown, String text) {
        try {
            waitForElementToBeVisible(dropdown);
            Select select = new Select(dropdown);
            select.selectByVisibleText(text);
        } catch (Exception e) {
            System.err.println("Error selecting dropdown by text: " + e.getMessage());
            throw e;
        }
    }

    public void scrollToElement(WebElement element) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
            Thread.sleep(1000); // Small delay for smooth scrolling
        } catch (Exception e) {
            System.err.println("Error scrolling to element: " + e.getMessage());
        }
    }

    protected boolean isElementDisplayed(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    protected String getElementText(WebElement element) {
        try {
            waitForElementToBeVisible(element);
            return element.getText();
        } catch (Exception e) {
            System.err.println("Error getting element text: " + e.getMessage());
            return "";
        }
    }
}
