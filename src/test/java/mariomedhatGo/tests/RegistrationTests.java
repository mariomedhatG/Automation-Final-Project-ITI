package mariomedhatGo.tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import mariomedhatGo.dataproviders.TestDataProviders;

public class RegistrationTests extends BaseTest {

    @Test(description = "Successful user registration", groups = {"smoke", "registration"})
    public void testSuccessfulRegistration() {
        navigateToSignup();
        regPage.enterSignupInfo("Test User", email);
        regPage.completeRegistration("password123", "Test", "User", "TestCorp",
                "123 Main St", "Apt 1", "CA", "TestCity", "12345", "1234567890");

        Assert.assertTrue(regPage.isAccountCreated(), "Account was not created");
        regPage.clickContinue();
        System.out.println("✅ Registration test passed");
    }

    @Test(description = "Registration with existing email", groups = {"regression"},
            dataProvider = "registrationData", dataProviderClass = TestDataProviders.class)
    public void testRegistrationWithExistingEmail(String name, String email, String password,
                                                  String firstName, String lastName, String company,
                                                  String address1, String address2, String state,
                                                  String city, String zip, String mobile) {
        // First registration
        navigateToSignup();
        regPage.enterSignupInfo(name, email);
        regPage.completeRegistration(password, firstName, lastName, company,
                address1, address2, state, city, zip, mobile);
        regPage.clickContinue();

        // Try second registration with same email
        navigateToSignup();
        regPage.enterSignupInfo(name, email);

        Assert.assertTrue(regPage.isEmailExistsErrorDisplayed(), "Email exists error not shown");
        System.out.println("✅ Existing email validation test passed");
    }

    @Test(description = "Registration with blank fields", groups = {"regression"})
    public void testRegistrationWithBlankFields() {
        navigateToSignup();
        // Try to submit without filling fields
        regPage.enterSignupInfo("", "");

        // Verify validation (implementation depends on site behavior)
        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("login"), "Validation not working properly");
        System.out.println("✅ Blank fields validation test passed");
    }
}