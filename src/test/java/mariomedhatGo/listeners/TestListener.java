package mariomedhatGo.listeners;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import mariomedhatGo.tests.BaseTest;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {
        System.out.println("========================================");
        System.out.println("Test Started: " + result.getMethod().getMethodName());
        System.out.println("Description: " + result.getMethod().getDescription());
        System.out.println("========================================");
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        System.out.println("✅ Test PASSED: " + result.getMethod().getMethodName());
        System.out.println("Execution Time: " + (result.getEndMillis() - result.getStartMillis()) + "ms");
        System.out.println("========================================");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        System.out.println("Test FAILED: " + result.getMethod().getMethodName());
        System.out.println("Error: " + result.getThrowable().getMessage());

        // Take Screenshot on failure
        Object testClass = result.getInstance();
        WebDriver driver = ((BaseTest) testClass).getDriver();

        if (driver != null) {
            takeScreenshot(driver, result.getMethod().getMethodName());
        }

        System.out.println("========================================");
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        System.out.println("Test SKIPPED: " + result.getMethod().getMethodName());
        System.out.println("Reason: " + result.getThrowable().getMessage());
        System.out.println("========================================");
    }

    private void takeScreenshot(WebDriver driver, String testName) {
        try {
            TakesScreenshot screenshot = (TakesScreenshot) driver;
            File sourceFile = screenshot.getScreenshotAs(OutputType.FILE);

            // Create screenshots directory if it doesn't exist
            File screenshotDir = new File("screenshots");
            if (!screenshotDir.exists()) {
                screenshotDir.mkdirs();
            }

            // Generate timestamp for unique filename
            String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
            String fileName = testName + "_" + timestamp + ".png";

            File destFile = new File(screenshotDir, fileName);
            FileUtils.copyFile(sourceFile, destFile);

            System.out.println("Screenshot saved: " + destFile.getAbsolutePath());

        } catch (IOException e) {
            System.err.println("Failed to take screenshot: " + e.getMessage());
        }
    }
}