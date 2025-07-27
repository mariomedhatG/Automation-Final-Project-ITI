package mariomedhatGo.pageopjects;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class ProductPage extends BasePage {

	// Constructor
	public ProductPage(WebDriver driver) {
		super(driver); // Call BasePage constructor
		PageFactory.initElements(driver, this);
	}

	// ===== Page Elements =====

	// Product-related locators
	private By products = By.cssSelector("div.single-products");
	private By productName = By.tagName("p");
	private By addToCartBtn = By.cssSelector("a.add-to-cart");
	private By viewCartBtn = By.xpath("//u[normalize-space()='View Cart']");
	private By continueShoppingBtn = By.xpath("//button[contains(text(),'Continue Shopping')]");

	@FindBy(xpath = "//a[@class='btn btn-default check_out']")
	private WebElement checkoutBtn;

	@FindBy(xpath = "//a[@class='btn btn-default check_out']")
	private WebElement placeOrderBtn;

	@FindBy(css = "input#search_product")
	private WebElement searchBox;

	@FindBy(css = "button#submit_search")
	private WebElement searchBtn;

	@FindBy(xpath = "//h2[contains(text(),'All Products')]")
	private WebElement allProductsTitle;

	@FindBy(xpath = "//h2[contains(text(),'Searched Products')]")
	private WebElement searchResultsTitle;

	// Cart-related elements
	@FindBy(xpath = "//tr[@id='product-1']")
	private WebElement firstCartItem;

	@FindBy(css = "td.cart_quantity button")
	private WebElement quantityInCart;

	@FindBy(css = "td.cart_total p")
	private WebElement itemTotalPrice;

	// ===== Page Actions =====

	/**
	 * Add a specific product to cart by name
	 */
	public void addProductToCart(String productWanted) {
		try {
			List<WebElement> allProducts = driver.findElements(products);
			boolean productFound = false;

			for (WebElement product : allProducts) {
				String name = product.findElement(productName).getText();

				if (name.equalsIgnoreCase(productWanted)) {
					// Hover over product to show Add to Cart button
					Actions actions = new Actions(driver);
					actions.moveToElement(product).perform();

					// Click Add to Cart
					WebElement addBtn = product.findElement(addToCartBtn);
					clickElement(addBtn);

					productFound = true;
					System.out.println("✅ Product added to cart: " + productWanted);
					break;
				}
			}

			if (!productFound) {
				throw new RuntimeException("Product not found: " + productWanted);
			}

			// Click View Cart
			waitForElementToBeClickable(driver.findElement(viewCartBtn));
			clickElement(driver.findElement(viewCartBtn));

		} catch (Exception e) {
			System.err.println("❌ Error adding product to cart: " + e.getMessage());
			throw e;
		}
	}

	/**
	 * Add multiple products to cart
	 */
	public void addMultipleProductsToCart(String[] productNames) {
		try {
			for (int i = 0; i < productNames.length; i++) {
				addSingleProductToCart(productNames[i]);

				// If not the last product, continue shopping
				if (i < productNames.length - 1) {
					clickContinueShopping();
				}
			}

			// View cart after adding all products
			clickElement(driver.findElement(viewCartBtn));
			System.out.println("✅ Multiple products added to cart successfully");

		} catch (Exception e) {
			System.err.println("❌ Error adding multiple products: " + e.getMessage());
			throw e;
		}
	}

	/**
	 * Add single product without viewing cart
	 */
	private void addSingleProductToCart(String productName) {
		List<WebElement> allProducts = driver.findElements(products);

		for (WebElement product : allProducts) {
			String name = product.findElement(this.productName).getText();

			if (name.equalsIgnoreCase(productName)) {
				Actions actions = new Actions(driver);
				actions.moveToElement(product).perform();

				WebElement addBtn = product.findElement(addToCartBtn);
				clickElement(addBtn);
				break;
			}
		}
	}

	/**
	 * Click continue shopping button
	 */
	public void clickContinueShopping() {
		try {
			clickElement(driver.findElement(continueShoppingBtn));
			System.out.println("✅ Continue shopping clicked");
		} catch (Exception e) {
			System.err.println("❌ Error clicking continue shopping: " + e.getMessage());
		}
	}

	/**
	 * Search for products
	 */
	public void searchForProduct(String searchTerm) {
		try {
			sendKeysToElement(searchBox, searchTerm);
			clickElement(searchBtn);
			System.out.println("✅ Product search completed for: " + searchTerm);
		} catch (Exception e) {
			System.err.println("❌ Error searching for product: " + e.getMessage());
			throw e;
		}
	}

	/**
	 * Proceed to checkout
	 */
	public void proceedToCheckout() {
		try {
			scrollToElement(checkoutBtn);
			clickElement(checkoutBtn);
			System.out.println("✅ Proceeded to checkout");
		} catch (Exception e) {
			System.err.println("❌ Error proceeding to checkout: " + e.getMessage());
			throw e;
		}
	}

	/**
	 * Click place order button
	 */
	public void clickPlaceOrder() {
		try {
			scrollToElement(placeOrderBtn);
			clickElement(placeOrderBtn);
			System.out.println("✅ Place order button clicked");
		} catch (Exception e) {
			System.err.println("❌ Error clicking place order: " + e.getMessage());
			throw e;
		}
	}

	// ===== Validation Methods =====

	/**
	 * Check if we're on products page
	 */
	public boolean isOnProductsPage() {
		return isElementDisplayed(allProductsTitle);
	}

	/**
	 * Check if search results are displayed
	 */
	public boolean areSearchResultsDisplayed() {
		return isElementDisplayed(searchResultsTitle);
	}

	/**
	 * Get number of products displayed
	 */
	public int getProductCount() {
		try {
			List<WebElement> productsList = driver.findElements(products);
			return productsList.size();
		} catch (Exception e) {
			System.err.println("❌ Error getting product count: " + e.getMessage());
			return 0;
		}
	}

	/**
	 * Check if specific product exists
	 */
	public boolean isProductDisplayed(String productName) {
		try {
			List<WebElement> allProducts = driver.findElements(products);

			for (WebElement product : allProducts) {
				String name = product.findElement(this.productName).getText();
				if (name.equalsIgnoreCase(productName)) {
					return true;
				}
			}
			return false;
		} catch (Exception e) {
			System.err.println("❌ Error checking if product is displayed: " + e.getMessage());
			return false;
		}
	}

	/**
	 * Get all product names on the page
	 */
	public String[] getAllProductNames() {
		try {
			List<WebElement> allProducts = driver.findElements(products);
			String[] productNames = new String[allProducts.size()];

			for (int i = 0; i < allProducts.size(); i++) {
				productNames[i] = allProducts.get(i).findElement(productName).getText();
			}

			return productNames;
		} catch (Exception e) {
			System.err.println("❌ Error getting all product names: " + e.getMessage());
			return new String[0];
		}
	}

	/**
	 * Check if cart has items
	 */
	public boolean isCartNotEmpty() {
		return isElementDisplayed(firstCartItem);
	}

	/**
	 * Get quantity of first item in cart
	 */
	public String getCartItemQuantity() {
		return getElementText(quantityInCart);
	}

	/**
	 * Get total price of item in cart
	 */
	public String getCartItemTotalPrice() {
		return getElementText(itemTotalPrice);
	}
}