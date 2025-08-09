package mariomedhatGo.dataproviders;

import mariomedhatGo.utils.ExcelUtils;
import org.testng.annotations.DataProvider;

public class TestDataProviders {
    @DataProvider(name = "registrationData")
    public Object[][] getRegistrationData() {
        return new Object[][] {
                {"Mario Medhat", "mario1@test.com", "password123", "Mario", "Medhat", "TechCorp",
                        "123 Main St", "Apt 4B", "California", "Los Angeles", "90210", "1234567890"},
                {"Ahmed Ali", "ahmed2@test.com", "ahmed456", "Ahmed", "Ali", "DevCorp",
                        "456 Oak Ave", "Suite 200", "Texas", "Austin", "73301", "9876543210"},
                {"Sara Mohamed", "sara3@test.com", "sara789", "Sara", "Mohamed", "TestCorp",
                        "789 Pine Rd", "Floor 3", "New York", "Albany", "12084", "5555551234"},
                {"John Smith", "john4@test.com", "john123", "John", "Smith", "SmithCorp",
                        "321 Elm St", "Unit 5", "Florida", "Miami", "33101", "3051234567"},
                {"Lisa Johnson", "lisa5@test.com", "lisa456", "Lisa", "Johnson", "JohnsonInc",
                        "654 Maple Ave", "Floor 2", "Illinois", "Chicago", "60601", "3122345678"},
                {"David Wilson", "david6@test.com", "david789", "David", "Wilson", "WilsonLLC",
                        "987 Oak Rd", "Suite 10", "Nevada", "Las Vegas", "89101", "7023456789"}
        };
    }

    @DataProvider(name = "registrationInvalidData")
    public Object[][] getRegistrationInvalidData() {
        return new Object[][] {
                {new String[] {"ahmed456", "123456", "Ali", "DevCorp",
                        "456 Oak Ave", "Suite 200", "Texas", "Austin", "73301", "9876543210"}},
                {new String[] {"sara789", "Sara", "Mohamed", "TestCorp",
                        "789 Pine Rd", "Floor 3", "123456", "Albany", "12084", "5555551234"}},
                {new String[] {"john123", "John", "Smith", "SmithCorp",
                        "321 Elm St", "Unit 5", "Florida", "123456", "33101", "3051234567"}},
                {new String[] {"lisa456", "Lisa", "Johnson", "JohnsonInc",
                        "654 Maple Ave", "Floor 2", "Illinois", "Chicago", "mhdjdjdj", "3122345678"}},
                {new String[] {"david789", "David", "Wilson", "WilsonLLC",
                        "987 Oak Rd", "Suite 10", "Nevada", "Las Vegas", "89101", "socosdocsk"}}
        };
    }

    @DataProvider(name = "registrationDataBlankFields")
    public Object[][] getRegistrationDataWithBlankFields() {
        return new Object[][] {
                {new String[] {"", "Mario", "Medhat", "TechCorp",
                        "123 Main St", "Apt 4B", "California", "Los Angeles", "90210", "1234567890"}},
                {new String[] {"ahmed456", "", "Ali", "DevCorp",
                        "456 Oak Ave", "Suite 200", "Texas", "Austin", "73301", "9876543210"}},
                {new String[] {"sara789", "Sara", "Mohamed", "TestCorp",
                        "789 Pine Rd", "Floor 3", "", "Albany", "12084", "5555551234"}},
                {new String[] {"john123", "John", "Smith", "SmithCorp",
                        "321 Elm St", "Unit 5", "Florida", "", "33101", "3051234567"}},
                {new String[] {"lisa456", "Lisa", "Johnson", "JohnsonInc",
                        "654 Maple Ave", "Floor 2", "Illinois", "Chicago", "", "3122345678"}},
                {new String[] {"david789", "David", "Wilson", "WilsonLLC",
                        "987 Oak Rd", "Suite 10", "Nevada", "Las Vegas", "89101", ""}}
        };
    }

    @DataProvider(name = "loginData")
    public Object[][] getLoginData() {
        return new Object[][] {
                {"valid@email.com", "validPassword"},
                {"invalid@email.com", "wrongPassword"},
                {"", "password123"},
                {"test@email.com", ""}
        };
    }

    @DataProvider(name = "productData")
    public Object[][] getProductData() {
        return new Object[][] {
                {"Blue Cotton Indie Mickey Dress"},
                {"Men Tshirt"},
                {"Sleeveless Dress"},
                {"Stylish Dress"},
                {"Winter Top"},
                {"Summer White Top"},
                {"Madame Top For Women"},
                {"Fancy Green Top"},
                {"Regular Fit Straight Jeans"},
                {"Soft Stretch Jeans"}
        };
    }

    @DataProvider(name = "multipleProductsData")
    public Object[][] getMultipleProductsData() {
        return new Object[][] {
                { new String[] {
                        "Blue Cotton Indie Mickey Dress",
                        "Men Tshirt",
                        "Sleeveless Dress"
                }}
        };
    }


    @DataProvider(name = "paymentData")
    public Object[][] getPaymentData() {
        return new Object[][] {
                {"Mario Medhat", "4111111111111111", "123", "12", "2025"},
                {"Test User", "5555555555554444", "456", "06", "2026"},
                {"John Doe", "378282246310005", "789", "03", "2027"},
                {"Jane Smith", "4000000000000002", "321", "09", "2025"},
                {"Bob Wilson", "5105105105105100", "654", "11", "2026"}
        };
    }

@DataProvider(name = "invalidPaymentData")
public Object[][] getInvalidPaymentData() {
    return new Object[][] {
            {"Test Name", "1234", "12", "01", "2020"},        // Invalid card number
            {"", "4111111111111111", "123", "12", "2025"},    // Empty name
            {"Test Name", "4111111111111111", "12", "13", "2025"}, // Invalid month
            {"Test Name", "4111111111111111", "1234", "12", "2025"}, // Invalid CVC
            {"Test Name", "4111111111111111", "123", "12", "2020"}, // Expired year
            {"Test Name", "invalidcardnumber", "123", "12", "2025"}, // Non-numeric card
            {"Test Name", "4111111111111111", "abc", "12", "2025"}, // Non-numeric CVC
            {"Test Name", "4111111111111111", "123", "invalid", "2025"} // Invalid month format
    };
}

@DataProvider(name = "registAndPayment")
public Object[][] getRegistAndPayment(){
        Object[][] registData = {{"Mario Medhat", "mario1@test.com", "password123", "Mario", "Medhat", "TechCorp",
                "123 Main St", "Apt 4B", "California", "Los Angeles", "90210", "1234567890"}
        };

        Object[][] paymentData = {
                {"Mario Medhat", "4111111111111111", "123", "12", "2025"},
        };

    Object[][] combinedData = new Object[registData.length][17];
    for (int i = 0; i < registData.length; i++) {
        combinedData[0] = registData[0];
        combinedData[1] = registData[1];
        combinedData[2] = registData[2];
        combinedData[3] = registData[3];
        combinedData[4] = registData[4];
        combinedData[5] = registData[5];
        combinedData[6] = registData[6];
        combinedData[7] = registData[7];
        combinedData[8] = registData[8];
        combinedData[9] = registData[9];
        combinedData[10] = registData[10];
        combinedData[11] = registData[11];
        combinedData[12] = paymentData[0];
        combinedData[13] = paymentData[1];
        combinedData[14] = paymentData[2];
        combinedData[15] = paymentData[3];
        combinedData[17] = paymentData[4];
    }

    return combinedData;
}

// New data providers for additional test scenarios
@DataProvider(name = "searchData")
public Object[][] getSearchData() {
    return new Object[][] {
            {"dress"},
            {"shirt"},
            {"jeans"},
            {"top"},
            {"cotton"},
            {"blue"},
            {"women"},
            {"men"}
    };
}

@DataProvider(name = "invalidLoginData")
public Object[][] getInvalidLoginData() {
    return new Object[][] {
            {"invalid@email.com", "wrongPassword"},
            {"", "password123"},
            {"test@email.com", ""},
            {"notanemail", "password123"},
            {"test@", "password123"},
            {"@domain.com", "password123"}
    };
}

@DataProvider(name = "browserData")
public Object[][] getBrowserData() {
    return new Object[][] {
            {"chrome"},
            {"firefox"},
            {"edge"}
    };
}

@DataProvider(name = "invalidRegistrationData")
public Object[][] getInvalidRegistrationData() {
    return new Object[][] {
            {"", "test@email.com"},                    // Empty name
            {"Test User", ""},                         // Empty email
            {"Test User", "invalidemail"},             // Invalid email format
            {"Test User", "test@"},                    // Incomplete email
            {"", ""},                                  // Both empty
            {"Test User", "test@domain"},              // Missing TLD
            {"Test User", "test.domain.com"}           // Missing @
    };
}

// Excel-based data provider (if you want to use Excel files)
@DataProvider(name = "excelRegistrationData")
public Object[][] getExcelRegistrationData() {
    String filePath = "src/test/resources/testdata/RegistrationData.xlsx";
    return ExcelUtils.getTestData(filePath, "Registration");
}

@DataProvider(name = "excelProductData")
public Object[][] getExcelProductData() {
    String filePath = "src/test/resources/testdata/ProductData.xlsx";
    return ExcelUtils.getTestData(filePath, "Products");
}

    @DataProvider(name = "yourNewData")
    public Object[][] getYourNewData() {
        return new Object[][] {
                {"data1", "data2"},
                {"data3", "data4"}
        };
    }
}
