package mariomedhatGo.pageopjects;

import java.time.Duration;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class ProductPage extends BasePage {

	// Constructor
	public ProductPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}

	// ===== Page Elements =====
	private By products = By.cssSelector("div.single-products");
	private By productName = By.tagName("p");
	private By addToCartBtn = By.xpath("//div[@class='overlay-content']//a[@data-product-id='21']");

	// إصلاح المشكلة: استخدام FindBy بدلاً من By
	@FindBy(xpath = "//u[normalize-space()='View Cart']")
	private WebElement viewCartBtn;

	@FindBy(xpath = "//button[contains(text(),'Continue Shopping')]")
	private WebElement continueShoppingBtn;

	@FindBy(xpath = "//a[@class='btn btn-default check_out']")
	private WebElement checkoutBtn;

	@FindBy(css = "input#search_product")
	private WebElement searchBox;

	@FindBy(css = "button#submit_search")
	private WebElement searchBtn;

	@FindBy(xpath = "//h2[contains(text(),'All Products')]")
	private WebElement allProductsTitle;

	@FindBy(xpath = "//h2[contains(text(),'Searched Products')]")
	private WebElement searchResultsTitle;

	@FindBy(xpath = "//a[normalize-space()='Proceed To Checkout']")
	private WebElement proccedToCheckout;

	@FindBy(css = "td.cart_quantity button")
	private WebElement quantityInCart;

	@FindBy(css = "td.cart_total p")
	private WebElement itemTotalPrice;

	@FindBy(xpath = "//a[normalize-space()='Place Order']")
	private WebElement placeOrderBtn;

	// ===== Page Actions =====

	/**
	 * Add a specific product to cart by name
	 */
	public void addProductToCart(String productWanted) {
		try {
			List<WebElement> allProducts = driver.findElements(products);
			boolean productFound = false;

			for (WebElement product : allProducts) {
				String name = product.findElement(productName).getText().trim();

				if (name.equalsIgnoreCase(productWanted.trim())) {
					// Scroll to product first
					scrollToElement(product);

					// Hover over product to show Add to Cart button
					Actions actions = new Actions(driver);
					actions.moveToElement(product).perform();

					// Wait a bit for hover effect
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
					}

					// Find and click Add to Cart button
					WebElement addBtn = product.findElement(addToCartBtn);
					waitForElementToBeClickable(addBtn);
					clickElement(addBtn);

					productFound = true;
					System.out.println("✅ Product added to cart: " + productWanted);
					break;
				}
			}

			if (!productFound) {
				throw new RuntimeException("Product not found: " + productWanted);
			}

			// إصلاح المشكلة الكبيرة هنا
			// استخدام wait مناسب للـ modal أو overlay
			wait.until(ExpectedConditions.or(
					ExpectedConditions.elementToBeClickable(viewCartBtn),
					ExpectedConditions.elementToBeClickable(continueShoppingBtn)
			));

			// Click View Cart
			clickElement(viewCartBtn);
			System.out.println("➡️ Current URL after clicking view cart: " + driver.getCurrentUrl());

		} catch (Exception e) {
			System.err.println("❌ Error adding product to cart: " + e.getMessage());
			e.printStackTrace();
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
					// Wait for page to reload
					wait.until(ExpectedConditions.visibilityOfElementLocated(products));
				}
			}

			// View cart after adding all products
			clickElement(viewCartBtn);
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
		try {
			List<WebElement> allProducts = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(products));

			for (WebElement product : allProducts) {
				String name = product.findElement(this.productName).getText().trim();

				if (name.equalsIgnoreCase(productName.trim())) {
					scrollToElement(product);

					Actions actions = new Actions(driver);
					actions.moveToElement(product).perform();

					// Wait for hover effect
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
					}

					WebElement addBtn = product.findElement(By.xpath(".//a[contains(@class, 'add-to-cart')]"));
					System.out.println("🎯 Found Add to Cart button for: " + productName);
					waitForElementToBeClickable(addBtn);
					System.out.println("🔍 Is displayed: " + addBtn.isDisplayed());
					System.out.println("🔍 Is enabled: " + addBtn.isEnabled());
					//clickElement(addBtn);
					actions.moveToElement(addBtn).pause(Duration.ofMillis(500)).click().perform();


					System.out.println("✅ Single product added: " + productName);
					break;
				}
			}
		} catch (Exception e) {
			System.err.println("❌ Error adding single product: " + e.getMessage());
			throw e;
		}
	}

	/**
	 * Click continue shopping button
	 */
	public void clickContinueShopping() {
		try {
			wait.until(ExpectedConditions.elementToBeClickable(continueShoppingBtn));
			clickElement(continueShoppingBtn);
			System.out.println("✅ Continue shopping clicked");

			// Wait for the modal to disappear
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}

		} catch (Exception e) {
			System.err.println("❌ Error clicking continue shopping: " + e.getMessage());
			throw e;
		}
	}

	/**
	 * Search for products
	 */
	public void searchForProduct(String searchTerm) {
		try {
			waitForElementToBeVisible(searchBox);
			sendKeysToElement(searchBox, searchTerm);
			clickElement(searchBtn);

			// Wait for search results to load
			wait.until(ExpectedConditions.or(
					ExpectedConditions.visibilityOf(searchResultsTitle),
					ExpectedConditions.presenceOfElementLocated(products)
			));

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
			waitForElementToBeClickable(checkoutBtn);
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
		try {
			return wait.until(ExpectedConditions.visibilityOf(allProductsTitle)).isDisplayed();
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Check if search results are displayed
	 */
	public boolean areSearchResultsDisplayed() {
		try {
			return wait.until(ExpectedConditions.visibilityOf(searchResultsTitle)).isDisplayed();
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Get number of products displayed
	 */
	public int getProductCount() {
		try {
			List<WebElement> productsList = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(products));
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
			List<WebElement> allProducts = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(products));

			for (WebElement product : allProducts) {
				String name = product.findElement(this.productName).getText().trim();
				if (name.equalsIgnoreCase(productName.trim())) {
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
		try {
			// Wait longer for cart page to load completely
			wait.until(ExpectedConditions.urlContains("view_cart"));

			// Then check for checkout button
			wait.until(ExpectedConditions.visibilityOf(proccedToCheckout));
			System.out.println("DEBUG: 'Proceed To Checkout' button is visible. Cart is not empty.");
			return true;

		} catch (Exception e) {
			System.err.println("❌ Error: Cart validation failed. Error: " + e.getMessage());

			// تشيك إضافي للتأكد
			try {
				// Check if we're actually on cart page
				if (!driver.getCurrentUrl().contains("view_cart")) {
					System.err.println("❌ Not on cart page. Current URL: " + driver.getCurrentUrl());
					return false;
				}

				// Check for any products in cart table
				List<WebElement> cartItems = driver.findElements(By.cssSelector("table#cart_info_table tbody tr"));
				boolean hasItems = cartItems.size() > 0;
				System.out.println("DEBUG: Cart items count: " + cartItems.size());
				return hasItems;

			} catch (Exception ex) {
				System.err.println("❌ Secondary cart check failed: " + ex.getMessage());
				return false;
			}
		}
	}

	/**
	 * Get quantity of first item in cart
	 */
	public String getCartItemQuantity() {
		try {
			waitForElementToBeVisible(quantityInCart);
			return getElementText(quantityInCart);
		} catch (Exception e) {
			System.err.println("❌ Error getting cart quantity: " + e.getMessage());
			return "0";
		}
	}

	/**
	 * Get total price of item in cart
	 */
	public String getCartItemTotalPrice() {
		try {
			waitForElementToBeVisible(itemTotalPrice);
			return getElementText(itemTotalPrice);
		} catch (Exception e) {
			System.err.println("❌ Error getting cart total price: " + e.getMessage());
			return "0";
		}
	}
}