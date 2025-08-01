package mariomedhatGo.pageopjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class OrderPayment extends BasePage {

	// Constructor
	public OrderPayment(WebDriver driver) {
		super(driver); // Call BasePage constructor
		PageFactory.initElements(driver, this);
	}

	// ===== Page Elements =====

	// Payment form elements
	@FindBy(name = "name_on_card")
	private WebElement nameOnCard;

	@FindBy(name = "card_number")
	private WebElement cardNumber;

	@FindBy(name = "cvc")
	private WebElement cvc;

	@FindBy(name = "expiry_month")
	private WebElement expiryMonth;

	@FindBy(name = "expiry_year")
	private WebElement expiryYear;

	@FindBy(id = "submit")
	private WebElement submitBtn;

	// Success/Navigation elements
	@FindBy(xpath = "//a[@class='btn btn-primary']")
	private WebElement continueBtn;

	@FindBy(xpath = "//div[@class='col-sm-9 col-sm-offset-1']//p[text()='Congratulations! Your order has been confirmed!']")
	private WebElement orderSuccessMsg;

	@FindBy(xpath = "//p[contains(text(),'Your order has been placed')]")
	private WebElement orderConfirmationMsg;

	// Error elements
	@FindBy(xpath = "//div[contains(@class,'alert-danger')]")
	private WebElement paymentErrorMsg;

	@FindBy(xpath = "//p[contains(text(),'Payment failed')]")
	private WebElement paymentFailedMsg;

	// Order details elements
	@FindBy(css = "td.cart_description h4 a")
	private WebElement orderProductName;

	@FindBy(css = "td.cart_quantity")
	private WebElement orderQuantity;

	@FindBy(css = "td.cart_total")
	private WebElement orderTotal;

	// ===== Page Actions =====

	/**
	 * Fill payment information and submit order
	 */
	public void submitOrderWithPayment(String cardName, String cardNum, String cvcNum,
									   String expMonth, String expYear) {
		try {
			fillPaymentInfo(cardName, cardNum, cvcNum, expMonth, expYear);
			submitPayment();
			System.out.println("✅ Payment submitted successfully");
		} catch (Exception e) {
			System.err.println("❌ Error submitting payment: " + e.getMessage());
			throw e;
		}
	}

	/**
	 * Fill payment information only (without submitting)
	 */
	public void fillPaymentInfo(String cardName, String cardNum, String cvcNum,
								String expMonth, String expYear) {
		try {
			sendKeysToElement(nameOnCard, cardName);
			sendKeysToElement(cardNumber, cardNum);
			sendKeysToElement(cvc, cvcNum);
			sendKeysToElement(expiryMonth, expMonth);
			sendKeysToElement(expiryYear, expYear);

			System.out.println("✅ Payment information filled");
		} catch (Exception e) {
			System.err.println("❌ Error filling payment info: " + e.getMessage());
			throw e;
		}
	}

	/**
	 * Submit payment form
	 */
	public void submitPayment() {
		try {
			scrollToElement(submitBtn);
			clickElement(submitBtn);
			System.out.println("✅ Payment form submitted");
		} catch (Exception e) {
			System.err.println("❌ Error submitting payment form: " + e.getMessage());
			throw e;
		}
	}

	/**
	 * Click continue button after successful order
	 */
	public void clickContinue() {
		try {
			clickElement(continueBtn);
			System.out.println("✅ Continue button clicked after order");
		} catch (Exception e) {
			System.err.println("❌ Error clicking continue button: " + e.getMessage());
			throw e;
		}
	}

	/**
	 * Complete entire payment process with validation
	 */
	public boolean completePaymentProcess(String cardName, String cardNum, String cvcNum,
										  String expMonth, String expYear) {
		try {
			fillPaymentInfo(cardName, cardNum, cvcNum, expMonth, expYear);
			submitPayment();

			// Wait a bit for processing
			Thread.sleep(2000);

			if (isPaymentSuccessful()) {
				System.out.println("✅ Payment completed successfully");
				return true;
			} else {
				System.out.println("❌ Payment failed");
				return false;
			}
		} catch (Exception e) {
			System.err.println("❌ Error in payment process: " + e.getMessage());
			return false;
		}
	}

	// ===== Validation Methods =====

	/**
	 * Check if payment was successful
	 */
	public boolean isPaymentSuccessful() {
		return isElementDisplayed(orderSuccessMsg) || isElementDisplayed(orderConfirmationMsg);
	}

	/**
	 * Check if payment failed
	 */
	public boolean isPaymentFailed() {
		return isElementDisplayed(paymentErrorMsg) || isElementDisplayed(paymentFailedMsg);
	}

	/**
	 * Get order success message
	 */
	public String getOrderSuccessMessage() {
		if (isElementDisplayed(orderSuccessMsg)) {
			return getElementText(orderSuccessMsg);
		} else if (isElementDisplayed(orderConfirmationMsg)) {
			return getElementText(orderConfirmationMsg);
		}
		return "";
	}

	/**
	 * Get payment error message
	 */
	public String getPaymentErrorMessage() {
		if (isElementDisplayed(paymentErrorMsg)) {
			return getElementText(paymentErrorMsg);
		} else if (isElementDisplayed(paymentFailedMsg)) {
			return getElementText(paymentFailedMsg);
		}
		return "";
	}

	/**
	 * Check if we're on payment page
	 */
	public boolean isOnPaymentPage() {
		return isElementDisplayed(nameOnCard) && isElementDisplayed(cardNumber);
	}

	// ===== Order Details Methods =====

	/**
	 * Get product name from order
	 */
	public String getOrderProductName() {
		return getElementText(orderProductName);
	}

	/**
	 * Get order quantity
	 */
	public String getOrderQuantity() {
		return getElementText(orderQuantity);
	}

	/**
	 * Get order total amount
	 */
	public String getOrderTotal() {
		return getElementText(orderTotal);
	}

	// ===== Helper Methods =====

	/**
	 * Clear all payment fields
	 */
	public void clearPaymentFields() {
		try {
			nameOnCard.clear();
			cardNumber.clear();
			cvc.clear();
			expiryMonth.clear();
			expiryYear.clear();
			System.out.println("✅ Payment fields cleared");
		} catch (Exception e) {
			System.err.println("❌ Error clearing payment fields: " + e.getMessage());
		}
	}

	/**
	 * Validate card number format (basic validation)
	 */
	public boolean isValidCardNumber(String cardNum) {
		// Remove spaces and check if it's numeric and has valid length
		String cleanCardNum = cardNum.replaceAll("\\s+", "");
		return cleanCardNum.matches("\\d{13,19}");
	}

	/**
	 * Validate expiry month
	 */
	public boolean isValidExpiryMonth(String month) {
		try {
			int monthNum = Integer.parseInt(month);
			return monthNum >= 1 && monthNum <= 12;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	/**
	 * Validate expiry year
	 */
	public boolean isValidExpiryYear(String year) {
		try {
			int yearNum = Integer.parseInt(year);
			int currentYear = java.time.Year.now().getValue();
			return yearNum >= currentYear && yearNum <= currentYear + 10;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	/**
	 * Validate CVC
	 */
	public boolean isValidCVC(String cvcCode) {
		return cvcCode.matches("\\d{3,4}");
	}
}