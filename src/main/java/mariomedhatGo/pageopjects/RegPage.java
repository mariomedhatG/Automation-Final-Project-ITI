package mariomedhatGo.pageopjects;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class RegPage {

	WebDriver driver;
	
	public RegPage(WebDriver driver) {
		//super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	@FindBy(css = "input[placeholder='Name']")
	WebElement userName;
	
	@FindBy(css = "input[data-qa='signup-email']")
	WebElement userEmail;
	
	@FindBy(css = "button[data-qa='signup-button']")
	WebElement regBtn;
	
	@FindBy(id = "id_gender1")
	WebElement maleChkbox;
	
	@FindBy(id = "password")
	WebElement password;
	
	@FindBy(id = "days")
	WebElement daySelect;
	
	@FindBy(id = "months")
	WebElement monthSelect;
	
	@FindBy(id = "years")
	WebElement yearSelect;
	
	@FindBy(id = "newsletter")
	WebElement newsLetter;
	
	@FindBy(id = "optin")
	WebElement optinChkbox;
	
	@FindBy(id = "first_name")
	WebElement userFname;
	
	@FindBy(id = "last_name")
	WebElement userLname;
	
	@FindBy(id = "company")
	WebElement companyName;
	
	@FindBy(id = "address1")
	WebElement addressOne;
	
	@FindBy(id = "address2")
	WebElement addressTwo;
	
	@FindBy(id = "country")
	WebElement countrySelect;
	
	@FindBy(id = "state")
	WebElement stateName;
	
	@FindBy(id = "city")
	WebElement cityName;
	
	@FindBy(id = "zipcode")
	WebElement zipNumber;
	
	@FindBy(id = "mobile_number")
	WebElement mobNumber;
	
	@FindBy(css = "button[data-qa='create-account']")
	WebElement createBtn;
	
	@FindBy(css = "a[data-qa='continue-button']")
	WebElement continueBtn;
	
	public void RegisterWeb(String fname ,String email) {
		userName.sendKeys(fname);
		userEmail.sendKeys(email);
		regBtn.click();
	}
	
	public void RegisterWebComplete(String pwd , String Fname , String Lname , String compName ,String addOne , String addTwo , String stat , String city , String zip , String mobile) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.visibilityOf(maleChkbox));
		
		maleChkbox.click();
		password.sendKeys(pwd);
		Select day = new Select(daySelect);
		day.selectByValue("2");
		Select month = new Select(monthSelect);
		month.selectByVisibleText("May");
		Select year = new Select(yearSelect);
	    year.selectByValue("2000");
	    newsLetter.click();
	    optinChkbox.click();
	    userFname.sendKeys(Fname);
	    userLname.sendKeys(Lname);
	    companyName.sendKeys(compName);
	    addressOne.sendKeys(addOne);
	    addressTwo.sendKeys(addTwo);
	    System.out.println("Address 2 done");
	    Select contry = new Select(countrySelect);
	    contry.selectByVisibleText("United States");
	    System.out.println("Country done");
	    stateName.sendKeys(stat);
	    cityName.sendKeys(city);
	    zipNumber.sendKeys(zip);
	    mobNumber.sendKeys(mobile);
	    createBtn.click();
	}
	
	public void continueButton() {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.visibilityOf(continueBtn));
		continueBtn.click();
	}
}
