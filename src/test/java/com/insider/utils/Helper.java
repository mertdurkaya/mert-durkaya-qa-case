package com.insider.utils;

import com.insider.driver.DriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Set;

public class Helper {
    private static final Logger logger = LoggerFactory.getLogger(Helper.class);

    private Helper() {
        // Private constructor to prevent instantiation
    }

    public static WebElement waitForVisibility(WebElement element, int timeoutInSeconds) {
        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(timeoutInSeconds));
            logger.debug("Waiting for element visibility: {}", element);
            return wait.until(ExpectedConditions.visibilityOf(element));
        } catch (TimeoutException e) {
            logger.error("Element did not become visible within {} seconds", timeoutInSeconds);
            throw new RuntimeException("Element did not become visible: " + element, e);
        }
    }

    public static WebElement waitForClickability(WebElement element, int timeoutInSeconds) {
        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(timeoutInSeconds));
            logger.debug("Waiting for element to be clickable: {}", element);
            return wait.until(ExpectedConditions.elementToBeClickable(element));
        } catch (TimeoutException e) {
            logger.error("Element did not become clickable within {} seconds", timeoutInSeconds);
            throw new RuntimeException("Element did not become clickable: " + element, e);
        }
    }

    public static void scrollToElement(WebElement element) {
        try {
            logger.debug("Scrolling to element: {}", element);
            ((JavascriptExecutor) DriverManager.getDriver()).executeScript("arguments[0].scrollIntoView(true);", element);
            // Add a small pause to allow the scroll to complete
            // Helper.waitForVisibility(element, 5);
            Thread.sleep(500);
        } catch (Exception e) {
            logger.error("Failed to scroll to element: {}", element);
            throw new RuntimeException("Failed to scroll to element", e);
        }
    }

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

    public static void switchToLastWindow() {
        try {
            Set<String> windowHandles = DriverManager.getDriver().getWindowHandles();
            String lastWindow = new ArrayList<>(windowHandles).get(windowHandles.size() - 1);
            logger.debug("Switching to last window: {}", lastWindow);
            DriverManager.getDriver().switchTo().window(lastWindow);
        } catch (Exception e) {
            logger.error("Failed to switch to last window");
            throw new RuntimeException("Failed to switch to last window", e);
        }
    }

    public static void waitForPageLoad(int timeoutInSeconds) {
        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(timeoutInSeconds));
            wait.until(webDriver -> ((JavascriptExecutor) webDriver)
                .executeScript("return document.readyState").equals("complete"));
            logger.debug("Page loaded successfully");
        } catch (TimeoutException e) {
            logger.error("Page did not load within {} seconds", timeoutInSeconds);
            throw new RuntimeException("Page did not load within timeout", e);
        }
    }

    public static void hoverOverElement(WebElement element, WebDriver driver) {
        try {
            logger.debug("Hovering over element: {}", element);
            String mouseOverScript = "var evObj = document.createEvent('MouseEvents');" +
                "evObj.initMouseEvent('mouseover', true, true, window, 1, 0, 0, 0, 0, false, false, false, false, 0, null);" +
                "arguments[0].dispatchEvent(evObj);";
            ((JavascriptExecutor) driver).executeScript(mouseOverScript, element);
            // Add a small pause to allow any hover effects to take place
            Thread.sleep(500);
        } catch (Exception e) {
            logger.error("Failed to hover over element: {}", element);
            throw new RuntimeException("Failed to hover over element", e);
        }
    }
}