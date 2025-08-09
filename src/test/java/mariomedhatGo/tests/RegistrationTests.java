package mariomedhatGo.tests;

import mariomedhatGo.dataproviders.TestDataProviders;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

public class RegistrationTests extends BaseTest {

    private String registeredUserEmail;
    private boolean isUserRegistered = false;

    @Test(description = "Successful user registration", groups = {"smoke", "registration"}
    , dataProvider = "registrationData" , dataProviderClass = TestDataProviders.class)
    public void testSuccessfulRegistration(String[] registData) {
        try {
            // Navigate to signup page
            navigateToSignup();

            // Enter signup information
            regPage.enterSignupInfo("Mario Medhat", email);

            // Complete registration
            regPage.completeRegistration(registData);

            // Verify account creation
            Assert.assertTrue(regPage.isAccountCreated(), "Account was not created successfully");

            // Continue to main page
            regPage.clickContinue();

            // Verify user is logged in
            wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//a[contains(text(),'Logged in as')]")));

            Assert.assertTrue(driver.findElement(By.xpath("//a[contains(text(),'Logged in as')]")).isDisplayed(),
                    "User is not logged in");

            // Save User Data
            registeredUserEmail = email;
            isUserRegistered = true;

            System.out.println("TC01: User registration completed successfully");

        } catch (Exception e) {
            System.err.println("TC01 failed: " + e.getMessage());
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

            System.out.println("TC02: Existing email validation working correctly");

        } catch (Exception e) {
            System.err.println("TC02 failed: " + e.getMessage());
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

            // Verify validation message
            String pageSource = driver.getPageSource();
            Assert.assertTrue(pageSource.contains("required"),
                    "Required field validation not working");

            System.out.println("TC03: Blank fields validation working correctly");

        } catch (Exception e) {
            System.err.println("TC03 failed: " + e.getMessage());
            throw e;
        }
}

    @Test(description = "Registrtion with blank fields in sign up form" , groups = {"smoke"},
    dataProvider = "registrationDataBlankFields" , dataProviderClass = TestDataProviders.class)
    public void testSignupFieldsWithBlankFields(String[] blankData) {
        try{
            // Navigate to signup page
            navigateToSignup();

            // Enter signup information
            regPage.enterSignupInfo("Mario Medhat", email);

            // Complete registration
            regPage.completeRegistrationWithBlank(blankData);

            // Verify account creation
            String pageSource = driver.getPageSource();
            Assert.assertTrue(pageSource.contains("required"),
                    "Required field validation not working");

            System.out.println("TC04: Registrtion with blank fields in sign up form completed successfully");

        } catch (Exception e) {
            System.err.println("TC04 failed: " + e.getMessage());
            logCurrentState();
            throw e;
        }
    }

    @Test(description = "Registrtion with Invalid data in sign up form" , groups = {"smoke"},
    dataProvider = "registrationInvalidDataBlankFields" , dataProviderClass = TestDataProviders.class)
    public void testSignupFieldsWithInvalidData (String[] invalidData) {
        try{
            // Navigate to signup page
            navigateToSignup();

            // Enter signup information
            regPage.enterSignupInfo("Mario Medhat", email);

            // Complete registration
            regPage.completeRegistrationWithBlank(invalidData);

            // Verify account creation
            String pageSource = driver.getPageSource();
            Assert.assertTrue(pageSource.contains("required"),
                    "Required field validation not working");

            System.out.println("TC05: Registrtion with blank fields in sign up form completed successfully");

        } catch (Exception e) {
            System.err.println("TC05 failed: " + e.getMessage());
            logCurrentState();
            throw e;
        }
    }
}
