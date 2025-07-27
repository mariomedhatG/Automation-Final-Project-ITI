package mariomedhatGo.listeners;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {
    private int count = 0;
    private static final int maxTry = 2; // Retry failed tests 2 times

    @Override
    public boolean retry(ITestResult iTestResult) {
        if (!iTestResult.isSuccess()) {
            if (count < maxTry) {
                count++;
                System.out.println("🔄 Retrying test: " + iTestResult.getName() +
                        " | Attempt: " + count + "/" + maxTry);
                return true;
            }
        }
        return false;
    }
}