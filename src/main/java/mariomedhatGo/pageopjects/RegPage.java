package mariomedhatGo.pageopjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class RegPage extends BasePage {

	// Constructor
	public RegPage(WebDriver driver) {
		super(driver); // Call BasePage constructor
		PageFactory.initElements(driver, this);
	}

	// ===== Page Elements =====
	@FindBy(css = "input[placeholder='Name']")
	private WebElement userName;

	@FindBy(css = "input[data-qa='signup-email']")
	private WebElement userEmail;

	@FindBy(css = "button[data-qa='signup-button']")
	private WebElement regBtn;

	@FindBy(id = "id_gender1")
	private WebElement maleChkbox;

	@FindBy(id = "id_gender2")
	private WebElement femaleChkbox;

	@FindBy(id = "password")
	private WebElement password;

	@FindBy(id = "days")
	private WebElement daySelect;

	@FindBy(id = "months")
	private WebElement monthSelect;

	@FindBy(id = "years")
	private WebElement yearSelect;

	@FindBy(id = "newsletter")
	private WebElement newsLetter;

	@FindBy(id = "optin")
	private WebElement optinChkbox;

	@FindBy(id = "first_name")
	private WebElement userFname;

	@FindBy(id = "last_name")
	private WebElement userLname;

	@FindBy(id = "company")
	private WebElement companyName;

	@FindBy(id = "address1")
	private WebElement addressOne;

	@FindBy(id = "address2")
	private WebElement addressTwo;

	@FindBy(id = "country")
	private WebElement countrySelect;

	@FindBy(id = "state")
	private WebElement stateName;

	@FindBy(id = "city")
	private WebElement cityName;

	@FindBy(id = "zipcode")
	private WebElement zipNumber;

	@FindBy(id = "mobile_number")
	private WebElement mobNumber;

	@FindBy(css = "button[data-qa='create-account']")
	private WebElement createBtn;

	@FindBy(css = "a[data-qa='continue-button']")
	private WebElement continueBtn;

	// Success/Error message elements
//	@FindBy(xpath = "//h2[contains(text(),'Account Created!')]")
//	private WebElement accountCreatedMsg;

	@FindBy(xpath = "//form[@action='/signup']//p[text()='Email Address already exist!']")
	private WebElement emailExistsMsg;

	// ===== Page Actions =====

	/**
	 * Enter signup information (name and email)
	 */
	public void enterSignupInfo(String name, String email) {
		try {
			sendKeysToElement(userName, name);
			sendKeysToElement(userEmail, email);
			clickElement(regBtn);
			System.out.println("Signup info entered successfully");
		} catch (Exception e) {
			System.err.println("Error entering signup info: " + e.getMessage());
			throw e;
		}
	}

	/**
	 * Fill registration form with basic info
	 */
	public void fillBasicAccountInfo(String pwd, String day, String month, String year, String gender) {
		try {
			// Select gender
			if (gender.equalsIgnoreCase("male")) {
				clickElement(maleChkbox);
			} else {
				clickElement(femaleChkbox);
			}

			// Enter password
			sendKeysToElement(password, pwd);

			// Select birth date
			selectDropdownByValue(daySelect, day);
			selectDropdownByText(monthSelect, month);
			selectDropdownByValue(yearSelect, year);

			// Check newsletter and offers
			clickElement(newsLetter);
			clickElement(optinChkbox);

			System.out.println("✅ Basic account info filled successfully");
		} catch (Exception e) {
			System.err.println("❌ Error filling basic account info: " + e.getMessage());
			throw e;
		}
	}

	/**
	 * Fill address information
	 */
	public void fillAddressInfo(String firstName, String lastName, String company,
								String address1, String address2, String country,
								String state, String city, String zip, String mobile) {
		try {
			sendKeysToElement(userFname, firstName);
			sendKeysToElement(userLname, lastName);
			sendKeysToElement(companyName, company);
			sendKeysToElement(addressOne, address1);
			sendKeysToElement(addressTwo, address2);

			selectDropdownByText(countrySelect, country);

			sendKeysToElement(stateName, state);
			sendKeysToElement(cityName, city);
			sendKeysToElement(zipNumber, zip);
			sendKeysToElement(mobNumber, mobile);

			System.out.println("Address info filled successfully");
		} catch (Exception e) {
			System.err.println("Error filling address info: " + e.getMessage());
			throw e;
		}
	}

	/**
	 * Complete registration process
	 */
	public void completeRegistration(String[] registData) {

        password.sendKeys(registData[0]);
        userFname.sendKeys(registData[1]);
        userLname.sendKeys(registData[2]);
        companyName.sendKeys(registData[3]);
        addressOne.sendKeys(registData[4]);
        addressTwo.sendKeys(registData[5]);
        stateName.sendKeys(registData[6]);
        cityName.sendKeys(registData[7]);
        zipNumber.sendKeys(registData[8]);
        mobNumber.sendKeys(registData[9]);

		createBtn.click();
		System.out.println("Registration completed successfully");
	}

    public void completeRegistrationWithBlank (String[] blankData) {
            // Enter Data
            password.sendKeys(blankData[0]);
            userFname.sendKeys(blankData[1]);
            userLname.sendKeys(blankData[2]);
            companyName.sendKeys(blankData[3]);
            addressOne.sendKeys(blankData[4]);
            addressTwo.sendKeys(blankData[5]);
            stateName.sendKeys(blankData[6]);
            cityName.sendKeys(blankData[7]);
            zipNumber.sendKeys(blankData[8]);
            mobNumber.sendKeys(blankData[9]);

            // Try To Click Crate Button
            createBtn.click();
            System.out.println("Registration with Blank completed ");
	}

    public void completeRegistrationWithInvalidData(String[] invalidData){
        // Enter Data
        password.sendKeys(invalidData[0]);
        userFname.sendKeys(invalidData[1]);
        userLname.sendKeys(invalidData[2]);
        companyName.sendKeys(invalidData[3]);
        addressOne.sendKeys(invalidData[4]);
        addressTwo.sendKeys(invalidData[5]);
        stateName.sendKeys(invalidData[6]);
        cityName.sendKeys(invalidData[7]);
        zipNumber.sendKeys(invalidData[8]);
        mobNumber.sendKeys(invalidData[9]);

        // Try To Click Crate Button
        createBtn.click();
        System.out.println("Registration with Blank completed ");
    }

	/**
	 * Click continue button after successful registration
	 */
	public void clickContinue() {
		try {
			clickElement(continueBtn);
			System.out.println("Continue button clicked");
		} catch (Exception e) {
			System.err.println("Error clicking continue button: " + e.getMessage());
			throw e;
		}
	}

	// ===== Validation Methods =====

	/**
	 * Check if account was created successfully
	 */
	public boolean isAccountCreated() {
		return isElementDisplayed(continueBtn);
	}

	/**
	 * Check if email already exists error is displayed
	 */
	public boolean isEmailExistsErrorDisplayed() {
        return isElementDisplayed(emailExistsMsg);
    }

	/**
	 * Get email exists error message text
	 */
	public String getEmailExistsMessage() {
		return getElementText(emailExistsMsg);
	}

	/**
	 * Check if we're on the account information page
	 */
	public boolean isOnAccountInfoPage() {
		return isElementDisplayed(password);
	}

	// ===== Helper Methods =====

	/**
	 * Clear all form fields
	 */
	public void clearAllFields() {
		try {
			userName.clear();
			userEmail.clear();
			password.clear();
			userFname.clear();
			userLname.clear();
			companyName.clear();
			addressOne.clear();
			addressTwo.clear();
			stateName.clear();
			cityName.clear();
			zipNumber.clear();
			mobNumber.clear();
			System.out.println("All fields cleared");
		} catch (Exception e) {
			System.err.println("Error clearing fields: " + e.getMessage());
		}
	}
}