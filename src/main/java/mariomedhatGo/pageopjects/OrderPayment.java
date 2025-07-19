package mariomedhatGo.pageopjects;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class OrderPayment {
WebDriver driver;
	
	public OrderPayment(WebDriver driver) {
		//super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	@FindBy(name = "name_on_card")
	WebElement nameOnCard;
	
	@FindBy(name = "card_number")
	WebElement numberOnCard;
	
	@FindBy(name = "cvc")
	WebElement cvc;
	
	@FindBy(name = "expiry_month")
	WebElement monthExpire;
	
	@FindBy(name = "expiry_year")
	WebElement yearExpire;
	
	@FindBy(id = "submit")
	WebElement submitBtn;
	
	@FindBy(xpath = "//a[@class='btn btn-primary']")
	WebElement startPageBtn;
	
	public void submitOrder(String name , String number , String cvcNum , String monExpere , String yearEx) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
		wait.until(ExpectedConditions.visibilityOf(nameOnCard));
		nameOnCard.sendKeys(name);
		numberOnCard.sendKeys(number);
		cvc.sendKeys(cvcNum);
		monthExpire.sendKeys(monExpere);
		yearExpire.sendKeys(yearEx);
		wait.until(ExpectedConditions.elementToBeClickable(submitBtn));
		System.out.println("waiting....");
		submitBtn.click();
	}
	
	public void goToStartPage() {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.visibilityOf(startPageBtn));
		startPageBtn.click();
	}
}
