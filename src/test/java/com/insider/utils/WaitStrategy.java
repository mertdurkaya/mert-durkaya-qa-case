package com.insider.utils;

import com.insider.driver.DriverManager;
import com.insider.exceptions.ElementNotFoundException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;

/**
 * Advanced wait strategies for better test reliability
 */
public class WaitStrategy {
    private static final Logger logger = LoggerFactory.getLogger(WaitStrategy.class);
    
    private WaitStrategy() {
        // Private constructor to prevent instantiation
    }
    
    /**
     * Wait for element to be present and visible with retry logic
     */
    public static WebElement waitForElementWithRetry(By locator, int maxRetries) {
        for (int i = 0; i < maxRetries; i++) {
            try {
                WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(5));
                return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            } catch (Exception e) {
                if (i == maxRetries - 1) {
                    throw new ElementNotFoundException("Element not found after " + maxRetries + " retries: " + locator, e);
                }
                Helper.sleep(1);
            }
        }
        return null;
    }
    
    /**
     * Wait for element to be clickable with retry logic
     */
    public static WebElement waitForClickableWithRetry(By locator, int maxRetries) {
        for (int i = 0; i < maxRetries; i++) {
            try {
                WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(5));
                return wait.until(ExpectedConditions.elementToBeClickable(locator));
            } catch (Exception e) {
                if (i == maxRetries - 1) {
                    throw new ElementNotFoundException("Element not clickable after " + maxRetries + " retries: " + locator, e);
                }
                Helper.sleep(1);
            }
        }
        return null;
    }
    
    /**
     * Wait for text to be present in element
     */
    public static boolean waitForTextToBePresent(WebElement element, String text, int timeoutInSeconds) {
        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(timeoutInSeconds));
            return wait.until(ExpectedConditions.textToBePresentInElement(element, text));
        } catch (Exception e) {
            logger.warn("Text '{}' not found in element within {} seconds", text, timeoutInSeconds);
            return false;
        }
    }
    
    /**
     * Wait for element to be invisible
     */
    public static boolean waitForElementToBeInvisible(By locator, int timeoutInSeconds) {
        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(timeoutInSeconds));
            return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
        } catch (Exception e) {
            logger.warn("Element still visible after {} seconds", timeoutInSeconds);
            return false;
        }
    }
    
    /**
     * Wait for number of elements to be present
     */
    public static List<WebElement> waitForNumberOfElementsToBe(By locator, int expectedCount, int timeoutInSeconds) {
        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(timeoutInSeconds));
            return wait.until(ExpectedConditions.numberOfElementsToBe(locator, expectedCount));
        } catch (Exception e) {
            logger.warn("Expected {} elements not found within {} seconds", expectedCount, timeoutInSeconds);
            return DriverManager.getDriver().findElements(locator);
        }
    }
    
    /**
     * Wait for element to have specific attribute value
     */
    public static boolean waitForAttributeToBe(WebElement element, String attribute, String value, int timeoutInSeconds) {
        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(timeoutInSeconds));
            return wait.until(ExpectedConditions.attributeToBe(element, attribute, value));
        } catch (Exception e) {
            logger.warn("Attribute '{}' did not become '{}' within {} seconds", attribute, value, timeoutInSeconds);
            return false;
        }
    }
    
    /**
     * Wait for element to be selected
     */
    public static boolean waitForElementToBeSelected(WebElement element, int timeoutInSeconds) {
        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(timeoutInSeconds));
            return wait.until(ExpectedConditions.elementToBeSelected(element));
        } catch (Exception e) {
            logger.warn("Element not selected within {} seconds", timeoutInSeconds);
            return false;
        }
    }
    
    /**
     * Wait for alert to be present
     */
    public static boolean waitForAlertToBePresent(int timeoutInSeconds) {
        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(timeoutInSeconds));
            wait.until(ExpectedConditions.alertIsPresent());
            return true;
        } catch (Exception e) {
            logger.warn("Alert not present within {} seconds", timeoutInSeconds);
            return false;
        }
    }
    
    /**
     * Wait for frame to be available and switch to it
     */
    public static boolean waitForFrameToBeAvailableAndSwitchToIt(By frameLocator, int timeoutInSeconds) {
        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(timeoutInSeconds));
            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameLocator));
            return true;
        } catch (Exception e) {
            logger.warn("Frame not available within {} seconds", timeoutInSeconds);
            return false;
        }
    }
    
    /**
     * Wait for page title to contain specific text
     */
    public static boolean waitForTitleToContain(String title, int timeoutInSeconds) {
        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(timeoutInSeconds));
            return wait.until(ExpectedConditions.titleContains(title));
        } catch (Exception e) {
            logger.warn("Title did not contain '{}' within {} seconds", title, timeoutInSeconds);
            return false;
        }
    }
    
    /**
     * Wait for URL to contain specific text
     */
    public static boolean waitForUrlToContain(String url, int timeoutInSeconds) {
        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(timeoutInSeconds));
            return wait.until(ExpectedConditions.urlContains(url));
        } catch (Exception e) {
            logger.warn("URL did not contain '{}' within {} seconds", url, timeoutInSeconds);
            return false;
        }
    }
}
