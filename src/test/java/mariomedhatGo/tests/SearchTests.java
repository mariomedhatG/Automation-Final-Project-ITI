package mariomedhatGo.tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import mariomedhatGo.dataproviders.TestDataProviders;

public class SearchTests extends BaseTest {

    @Test(description = "Product search with various keywords", groups = {"regression", "search"},
            dataProvider = "searchData", dataProviderClass = TestDataProviders.class)
    public void testProductSearch(String searchTerm) {
        navigateToProducts();
        productPage.searchForProduct(searchTerm);

        Assert.assertTrue(productPage.areSearchResultsDisplayed(),
                "Search results not displayed for: " + searchTerm);

        int resultCount = productPage.getProductCount();
        System.out.println("✅ Search for '" + searchTerm + "' returned " + resultCount + " results");
    }

    @Test(description = "Search with empty string", groups = {"regression", "search"})
    public void testEmptySearch() {
        navigateToProducts();
        productPage.searchForProduct("");

        // Should either show all products or show no results message
        boolean validResult = productPage.areSearchResultsDisplayed() || productPage.getProductCount() > 0;
        Assert.assertTrue(validResult, "Empty search didn't handle properly");

        System.out.println("✅ Empty search test passed");
    }

    @Test(description = "Search with special characters", groups = {"regression", "search"})
    public void testSpecialCharacterSearch() {
        navigateToProducts();
        productPage.searchForProduct("@#$%");

        // Should handle gracefully without errors
        Assert.assertTrue(driver.getCurrentUrl().contains("products"), "Search with special chars failed");

        System.out.println("✅ Special character search test passed");
    }

    @Test(description = "Case insensitive search", groups = {"regression", "search"})
    public void testCaseInsensitiveSearch() {
        navigateToProducts();

        // Search with lowercase
        productPage.searchForProduct("dress");
        int lowerCaseResults = productPage.getProductCount();

        // Navigate back to products page
        navigateToProducts();

        // Search with uppercase
        productPage.searchForProduct("DRESS");
        int upperCaseResults = productPage.getProductCount();

        Assert.assertEquals(lowerCaseResults, upperCaseResults,
                "Case insensitive search not working properly");

        System.out.println("✅ Case insensitive search test passed");
    }
}