package com.insider.utils;

import com.insider.config.ConfigManager;
import com.insider.driver.DriverManager;
import com.insider.exceptions.TestFrameworkException;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ScreenshotUtils {
    private static final Logger logger = LoggerFactory.getLogger(ScreenshotUtils.class);
    private static final String SCREENSHOT_DIR = "test-output/screenshots/";
    
    public static String captureScreenshot(String testName) {
        if (!ConfigManager.isScreenshotOnFailure() || DriverManager.getDriver() == null) {
            return null;
        }
        
        try {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS"));
            String fileName = testName + "_" + timestamp + ".png";
            String filePath = SCREENSHOT_DIR + fileName;
            
            TakesScreenshot screenshot = (TakesScreenshot) DriverManager.getDriver();
            File sourceFile = screenshot.getScreenshotAs(OutputType.FILE);
            File destFile = new File(filePath);
            
            // Ensure directory exists
            FileUtils.forceMkdirParent(destFile);
            FileUtils.copyFile(sourceFile, destFile);
            
            logger.info("Screenshot captured: {}", filePath);
            return filePath;
        } catch (IOException e) {
            logger.error("Failed to capture screenshot", e);
            throw new TestFrameworkException("Failed to capture screenshot", e);
        }
    }
    
    public static String captureScreenshot() {
        return captureScreenshot("screenshot");
    }
    
    public static void captureScreenshotOnFailure(String testName, Throwable throwable) {
        try {
            String screenshotPath = captureScreenshot(testName + "_FAILURE");
            if (screenshotPath != null) {
                logger.error("Test failed. Screenshot saved at: {}", screenshotPath);
            }
        } catch (Exception e) {
            logger.error("Failed to capture screenshot on test failure", e);
        }
    }
}
