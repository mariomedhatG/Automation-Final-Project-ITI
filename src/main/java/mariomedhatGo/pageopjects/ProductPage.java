package mariomedhatGo.pageopjects;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ProductPage {

	WebDriver driver;

	public ProductPage(WebDriver driver) {
		// super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	@FindBy()

	By products = By.cssSelector("div.single-products");
	By productName = By.tagName("p");
	By addToCartBtn = By.cssSelector("a.add-to-cart");
	By viewCartBtn = By.xpath("//u[normalize-space()='View Cart']");
	
	@FindBy(xpath = "//a[@class='btn btn-default check_out']")
	WebElement chkoutBtn;

	@FindBy(xpath = "//a[@class='btn btn-default check_out']")
	WebElement placeOrderBtn;
	
	public void addProductToCart(String productWanted) {
		List<WebElement> allProducts = driver.findElements(products);

		for (WebElement product : allProducts) {
			String name = product.findElement(productName).getText();

			if (name.equalsIgnoreCase(productWanted)) {
				Actions actions = new Actions(driver);
				actions.moveToElement(product).perform();

				WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
				wait.until(ExpectedConditions.visibilityOfElementLocated(addToCartBtn)).click();
				System.out.println("Button is clicked");
				break;
			}
		}
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
		wait.until(ExpectedConditions.visibilityOfElementLocated(viewCartBtn)).click();
	}

	public void CheckoutButton() {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.visibilityOf(chkoutBtn));
		chkoutBtn.click();
	}

	public void scrollToElement(WebElement element) {
	    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
	}

	public void placeOrderButton() {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.visibilityOf(placeOrderBtn));
		scrollToElement(placeOrderBtn);
		placeOrderBtn.click();
	}
}
