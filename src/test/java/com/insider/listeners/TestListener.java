package com.insider.listeners;

import com.insider.reporting.ReportManager;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener {
    
    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String description = result.getMethod().getDescription();
        
        if (description != null && !description.isEmpty()) {
            ReportManager.createTest(testName, description);
        } else {
            ReportManager.createTest(testName);
        }
        
        ReportManager.logInfo("Starting test: " + testName);
    }
    
    @Override
    public void onTestSuccess(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        ReportManager.logPass("Test passed: " + testName);
    }
    
    @Override
    public void onTestFailure(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String errorMessage = result.getThrowable() != null ? 
            result.getThrowable().getMessage() : "Unknown error";
        
        ReportManager.logFail("Test failed: " + testName + " - " + errorMessage);
    }
    
    @Override
    public void onTestSkipped(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String skipReason = result.getSkipCausedBy() != null ? 
            result.getSkipCausedBy().toString() : "Test was skipped";
        
        ReportManager.logSkip("Test skipped: " + testName + " - " + skipReason);
    }
    
    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        ReportManager.logWarning("Test failed but within success percentage: " + testName);
    }
}
