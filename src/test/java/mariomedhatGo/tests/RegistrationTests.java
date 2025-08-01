package mariomedhatGo.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

public class RegistrationTests extends BaseTest {

    private String registeredUserEmail;
    private boolean isUserRegistered = false;

    @Test(description = "Successful user registration", groups = {"smoke", "registration"})
    public void testSuccessfulRegistration() {
        try {
            // Navigate to signup page
            navigateToSignup();

            // Enter signup information
            regPage.enterSignupInfo("Mario Medhat", email);

            // Complete registration
            regPage.completeRegistration("marioM123/", "Mario", "Medhat", "Mario Co",
                    "13 Ali st", "14 Mohamed st", "NY", "Badr", "12345", "0123456789");

            // Verify account creation
            Assert.assertTrue(regPage.isAccountCreated(), "Account was not created successfully");

            // Continue to main page
            regPage.clickContinue();

            // Verify user is logged in
            wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//a[contains(text(),'Logged in as')]")));

            Assert.assertTrue(driver.findElement(By.xpath("//a[contains(text(),'Logged in as')]")).isDisplayed(),
                    "User is not logged in");

            // حفظ بيانات المستخدم المسجل
            registeredUserEmail = email;
            isUserRegistered = true;

            System.out.println("✅ TC01: User registration completed successfully");

        } catch (Exception e) {
            System.err.println("❌ TC01 failed: " + e.getMessage());
            logCurrentState();
            throw e;
        }
    }

    @Test(description = "Registration with existing email", groups = {"regression"},
            dependsOnMethods = {"testSuccessfulRegistration"})
    public void testRegistrationWithExistingEmail() {
        try {
            // Navigate to signup page
            navigateToSignup();

            // Try to register with existing email
            regPage.enterSignupInfo("Mario Medhat", registeredUserEmail);

            // Verify error message
            Assert.assertTrue(regPage.isEmailExistsErrorDisplayed(),
                    "Email exists error is not displayed");

            System.out.println("✅ TC02: Existing email validation working correctly");

        } catch (Exception e) {
            System.err.println("❌ TC02 failed: " + e.getMessage());
            throw e;
        }
    }

    @Test(description = "Registration with blank fields", groups = {"regression"})
    public void testRegistrationWithBlankFields() {
        try {
            // Navigate to signup page
            navigateToSignup();

            // Try to register with blank fields
            wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector("button[data-qa='signup-button']")));
            driver.findElement(By.cssSelector("button[data-qa='signup-button']")).click();

            // Verify validation message (this might vary based on browser)
            String pageSource = driver.getPageSource();
            Assert.assertTrue(pageSource.contains("required") || pageSource.contains("Please fill"),
                    "Required field validation not working");

            System.out.println("✅ TC03: Blank fields validation working correctly");

        } catch (Exception e) {
            System.err.println("❌ TC03 failed: " + e.getMessage());
            throw e;
        }
}
}