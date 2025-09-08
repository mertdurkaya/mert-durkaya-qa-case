package com.insider.utils;

import com.insider.config.ConfigManager;
import com.insider.driver.DriverManager;
import com.insider.exceptions.ElementNotFoundException;
import com.insider.exceptions.TestFrameworkException;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Set;

/**
 * Utility class providing common helper methods for Selenium WebDriver operations.
 * Includes element waiting strategies, scrolling, clicking, and window management.
 */
public class Helper {
    private static final Logger logger = LoggerFactory.getLogger(Helper.class);

    private Helper() {
        // Private constructor to prevent instantiation
    }

    /**
     * Waits for an element to become visible within the specified timeout.
     * 
     * @param element the WebElement to wait for
     * @param timeoutInSeconds maximum time to wait in seconds
     * @return the visible WebElement
     * @throws ElementNotFoundException if element doesn't become visible within timeout
     */
    public static WebElement waitForVisibility(WebElement element, int timeoutInSeconds) {
        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(timeoutInSeconds));
            logger.debug("Waiting for element visibility: {}", element);
            return wait.until(ExpectedConditions.visibilityOf(element));
        } catch (TimeoutException e) {
            logger.error("Element did not become visible within {} seconds", timeoutInSeconds);
            throw new ElementNotFoundException("Element did not become visible: " + element, e);
        }
    }

    /**
     * Waits for an element to become clickable within the specified timeout.
     * 
     * @param element the WebElement to wait for
     * @param timeoutInSeconds maximum time to wait in seconds
     * @return the clickable WebElement
     * @throws ElementNotFoundException if element doesn't become clickable within timeout
     */
    public static WebElement waitForClickability(WebElement element, int timeoutInSeconds) {
        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(timeoutInSeconds));
            logger.debug("Waiting for element to be clickable: {}", element);
            return wait.until(ExpectedConditions.elementToBeClickable(element));
        } catch (TimeoutException e) {
            logger.error("Element did not become clickable within {} seconds", timeoutInSeconds);
            throw new ElementNotFoundException("Element did not become clickable: " + element, e);
        }
    }

    /**
     * Scrolls to the specified element using smooth scrolling behavior.
     * 
     * @param element the WebElement to scroll to
     * @throws TestFrameworkException if scrolling fails
     */
    public static void scrollToElement(WebElement element) {
        try {
            logger.debug("Scrolling to element: {}", element);
            ((JavascriptExecutor) DriverManager.getDriver()).executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
            // Wait for scroll to complete using a more reliable method
            waitForScrollToComplete();
        } catch (Exception e) {
            logger.error("Failed to scroll to element: {}", element);
            throw new TestFrameworkException("Failed to scroll to element", e);
        }
    }
    
    private static void waitForScrollToComplete() {
        try {
            // Wait for scroll to complete by checking if the page is stable
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofMillis(500));
            wait.until(webDriver -> {
                Long scrollY = (Long) ((JavascriptExecutor) webDriver).executeScript("return window.scrollY;");
                try {
                    Thread.sleep(100); // Small delay to check if scroll position is stable
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return false;
                }
                Long newScrollY = (Long) ((JavascriptExecutor) webDriver).executeScript("return window.scrollY;");
                return scrollY.equals(newScrollY);
            });
        } catch (Exception e) {
            // If scroll detection fails, just continue - it's not critical
            logger.debug("Scroll completion detection failed, continuing...");
        }
    }

    /**
     * Safely clicks an element with retry logic and JavaScript fallback.
     * 
     * @param element the WebElement to click
     * @param timeoutInSeconds maximum time to wait for element to be clickable
     */
    public static void safeClick(WebElement element, int timeoutInSeconds) {
        try {
            logger.debug("Attempting to safely click element: {}", element);
            WebElement clickableElement = waitForClickability(element, timeoutInSeconds);
            scrollToElement(clickableElement);
            clickableElement.click();
            logger.debug("Successfully clicked element");
        } catch (ElementClickInterceptedException e) {
            logger.warn("Element click was intercepted, attempting JavaScript click");
            ((JavascriptExecutor) DriverManager.getDriver())
                .executeScript("arguments[0].click();", element);
        }
    }

    /**
     * Switches to the last opened window/tab.
     * 
     * @throws TestFrameworkException if window switching fails
     */
    public static void switchToLastWindow() {
        try {
            Set<String> windowHandles = DriverManager.getDriver().getWindowHandles();
            String lastWindow = new ArrayList<>(windowHandles).get(windowHandles.size() - 1);
            logger.debug("Switching to last window: {}", lastWindow);
            DriverManager.getDriver().switchTo().window(lastWindow);
        } catch (Exception e) {
            logger.error("Failed to switch to last window");
            throw new TestFrameworkException("Failed to switch to last window", e);
        }
    }

    /**
     * Waits for the page to fully load by checking document ready state.
     * 
     * @param timeoutInSeconds maximum time to wait for page load
     * @throws TestFrameworkException if page doesn't load within timeout
     */
    public static void waitForPageLoad(int timeoutInSeconds) {
        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(timeoutInSeconds));
            wait.until(webDriver -> ((JavascriptExecutor) webDriver)
                .executeScript("return document.readyState").equals("complete"));
            logger.debug("Page loaded successfully");
        } catch (TimeoutException e) {
            logger.error("Page did not load within {} seconds", timeoutInSeconds);
            throw new TestFrameworkException("Page did not load within timeout", e);
        }
    }

    public static void sleep(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Sleep was interrupted", e);
        }
    }

    public static void hoverOverElement(WebElement element, WebDriver driver) {
        try {
            logger.debug("Hovering over element: {}", element);
            // Use modern JavaScript approach for hover
            String hoverScript = "var element = arguments[0];" +
                "var event = new MouseEvent('mouseover', {" +
                "  view: window," +
                "  bubbles: true," +
                "  cancelable: true" +
                "});" +
                "element.dispatchEvent(event);";
            ((JavascriptExecutor) driver).executeScript(hoverScript, element);
            
            // Wait for hover effects to complete using a more reliable method
            waitForHoverEffects();
        } catch (Exception e) {
            logger.error("Failed to hover over element: {}", element);
            throw new TestFrameworkException("Failed to hover over element", e);
        }
    }
    
    private static void waitForHoverEffects() {
        try {
            // Wait for any CSS transitions or animations to complete
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofMillis(300));
            wait.until(webDriver -> {
                Boolean animationsComplete = (Boolean) ((JavascriptExecutor) webDriver)
                    .executeScript("return !document.querySelectorAll('*').length || " +
                        "Array.from(document.querySelectorAll('*')).every(el => " +
                        "getComputedStyle(el).transitionDuration === '0s' && " +
                        "getComputedStyle(el).animationDuration === '0s');");
                return animationsComplete;
            });
        } catch (Exception e) {
            // If animation detection fails, just continue - it's not critical
            logger.debug("Hover effect detection failed, continuing...");
        }
    }
    
    // Enhanced wait methods with retry logic - delegate to WaitStrategy
    public static WebElement waitForElementWithRetry(By locator, int maxRetries) {
        return WaitStrategy.waitForElementWithRetry(locator, maxRetries);
    }
    
    // Convenience method using default timeout
    public static WebElement waitForVisibility(WebElement element) {
        return waitForVisibility(element, ConfigManager.getDefaultTimeout());
    }
    
    public static WebElement waitForClickability(WebElement element) {
        return waitForClickability(element, ConfigManager.getDefaultTimeout());
    }
    
    public static void waitForPageLoad() {
        waitForPageLoad(ConfigManager.getIntProperty("page.load.timeout", 30));
    }
    
    // Wait for AJAX requests to complete
    public static void waitForAjaxToComplete() {
        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(10));
            wait.until(webDriver -> {
                Boolean ajaxComplete = (Boolean) ((JavascriptExecutor) webDriver)
                    .executeScript("return typeof jQuery !== 'undefined' ? jQuery.active === 0 : true;");
                return ajaxComplete;
            });
            logger.debug("AJAX requests completed");
        } catch (Exception e) {
            logger.debug("AJAX completion detection failed, continuing...");
        }
    }
    
    // Wait for specific element count to be stable (useful for dynamic lists)
    public static void waitForElementCountToBeStable(By locator, int expectedCount, int timeoutInSeconds) {
        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(timeoutInSeconds));
            wait.until(webDriver -> {
                int currentCount = webDriver.findElements(locator).size();
                if (currentCount == expectedCount) {
                    // Wait a bit more to ensure count is stable
                    try {
                        Thread.sleep(500);
                        int newCount = webDriver.findElements(locator).size();
                        return newCount == expectedCount;
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return false;
                    }
                }
                return false;
            });
            logger.debug("Element count stabilized at: {}", expectedCount);
        } catch (Exception e) {
            logger.warn("Element count stabilization failed: {}", e.getMessage());
        }
    }
}