package com.insider.reporting;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.insider.config.ConfigManager;
import com.insider.driver.DriverManager;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ReportManager {
    private static final Logger logger = LoggerFactory.getLogger(ReportManager.class);
    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();
    private static final String SCREENSHOT_DIR = "test-output/screenshots/";
    private static final String REPORT_DIR = "test-output/reports/";
    
    static {
        createDirectories();
    }
    
    private static void createDirectories() {
        try {
            FileUtils.forceMkdir(new File(SCREENSHOT_DIR));
            FileUtils.forceMkdir(new File(REPORT_DIR));
        } catch (IOException e) {
            logger.error("Failed to create report directories", e);
        }
    }
    
    public static void initializeReport() {
        if (extent == null) {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String reportPath = REPORT_DIR + "ExtentReport_" + timestamp + ".html";
            
            ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
            spark.config().setTheme(Theme.STANDARD);
            spark.config().setDocumentTitle("Insider QA Test Report");
            spark.config().setReportName("Career Application Flow Test Report");
            spark.config().setTimeStampFormat("MMM dd, yyyy HH:mm:ss");
            
            extent = new ExtentReports();
            extent.attachReporter(spark);
            
            // Add system information
            extent.setSystemInfo("OS", System.getProperty("os.name"));
            extent.setSystemInfo("Java Version", System.getProperty("java.version"));
            extent.setSystemInfo("Browser", ConfigManager.getBrowser());
            extent.setSystemInfo("Headless Mode", String.valueOf(ConfigManager.isHeadless()));
            extent.setSystemInfo("Environment", ConfigManager.getProperty("environment", "dev"));
            
            logger.info("ExtentReports initialized successfully. Report will be generated at: {}", reportPath);
        }
    }
    
    public static void createTest(String testName) {
        if (extent != null) {
            test.set(extent.createTest(testName));
            logger.info("Created test: {}", testName);
        }
    }
    
    public static void createTest(String testName, String description) {
        if (extent != null) {
            test.set(extent.createTest(testName, description));
            logger.info("Created test: {} - {}", testName, description);
        }
    }
    
    public static void logInfo(String message) {
        if (test.get() != null) {
            test.get().info(message);
        }
        logger.info(message);
    }
    
    public static void logPass(String message) {
        if (test.get() != null) {
            test.get().pass(message);
        }
        logger.info("PASS: {}", message);
    }
    
    public static void logFail(String message) {
        if (test.get() != null) {
            test.get().fail(message);
            captureScreenshot("FAILURE");
        }
        logger.error("FAIL: {}", message);
    }
    
    public static void logSkip(String message) {
        if (test.get() != null) {
            test.get().skip(message);
        }
        logger.warn("SKIP: {}", message);
    }
    
    public static void logWarning(String message) {
        if (test.get() != null) {
            test.get().warning(message);
        }
        logger.warn("WARNING: {}", message);
    }
    
    public static void captureScreenshot(String screenshotName) {
        if (ConfigManager.isScreenshotOnFailure() && DriverManager.getDriver() != null) {
            try {
                String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS"));
                String fileName = screenshotName + "_" + timestamp + ".png";
                String filePath = SCREENSHOT_DIR + fileName;
                
                TakesScreenshot screenshot = (TakesScreenshot) DriverManager.getDriver();
                File sourceFile = screenshot.getScreenshotAs(OutputType.FILE);
                File destFile = new File(filePath);
                
                FileUtils.copyFile(sourceFile, destFile);
                
                if (test.get() != null) {
                    test.get().addScreenCaptureFromPath(filePath);
                }
                
                logger.info("Screenshot captured: {}", filePath);
            } catch (Exception e) {
                logger.error("Failed to capture screenshot", e);
            }
        }
    }
    
    public static void flushReport() {
        if (extent != null) {
            extent.flush();
            logger.info("ExtentReports flushed successfully");
        }
    }
    
    public static void closeReport() {
        if (extent != null) {
            extent.flush();
            extent = null;
            logger.info("ExtentReports closed successfully");
        }
    }
    
    public static ExtentTest getCurrentTest() {
        return test.get();
    }
}
