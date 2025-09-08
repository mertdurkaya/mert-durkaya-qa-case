package com.insider.driver;

import com.insider.config.ConfigManager;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manages WebDriver instances with thread safety for parallel test execution.
 * Provides centralized driver lifecycle management and browser configuration.
 */
public class DriverManager {
    private static final Logger logger = LoggerFactory.getLogger(DriverManager.class);
    
    // Using ThreadLocal for thread safety, which is a best practice for parallel execution
    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    private DriverManager() {
        // Private constructor to prevent instantiation
    }

    /**
     * Gets the current WebDriver instance for the current thread.
     * 
     * @return WebDriver instance for the current thread, or null if not initialized
     */
    public static WebDriver getDriver() {
        return driver.get();
    }

    /**
     * Initializes and configures a new WebDriver instance based on configuration.
     * Sets up browser options, timeouts, and other driver settings.
     */
    public static void setup() {
        String browser = ConfigManager.getBrowser();
        boolean headless = ConfigManager.isHeadless();
        
        logger.info("Setting up {} browser (headless: {})", browser, headless);
        
        WebDriver webDriver = createDriver(browser, headless);
        driver.set(webDriver);
        
        // Configure timeouts
        webDriver.manage().timeouts().implicitlyWait(
            java.time.Duration.ofSeconds(ConfigManager.getDefaultTimeout()));
        webDriver.manage().timeouts().pageLoadTimeout(
            java.time.Duration.ofSeconds(ConfigManager.getIntProperty("page.load.timeout", 30)));
        
        logger.info("Driver setup completed successfully");
    }
    
    private static WebDriver createDriver(String browser, boolean headless) {
        switch (browser.toLowerCase()) {
            case "chrome":
                return createChromeDriver(headless);
            case "firefox":
                return createFirefoxDriver(headless);
            case "edge":
                return createEdgeDriver(headless);
            default:
                throw new IllegalArgumentException("Unsupported browser: " + browser + 
                    ". Supported browsers: chrome, firefox, edge");
        }
    }
    
    private static WebDriver createChromeDriver(boolean headless) {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        
        if (headless) {
            options.addArguments("--headless=new");
        }
        
        if (ConfigManager.getBooleanProperty("window.maximize", true)) {
            options.addArguments("--start-maximized");
        }
        
        if (ConfigManager.getBooleanProperty("disable.notifications", true)) {
            options.addArguments("--disable-notifications");
        }
        
        if (ConfigManager.getBooleanProperty("disable.infobars", true)) {
            options.addArguments("--disable-infobars");
        }
        
        if (ConfigManager.getBooleanProperty("disable.extensions", true)) {
            options.addArguments("--disable-extensions");
        }
        
        // Additional stability options
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        
        return new ChromeDriver(options);
    }
    
    private static WebDriver createFirefoxDriver(boolean headless) {
        WebDriverManager.firefoxdriver().setup();
        FirefoxOptions options = new FirefoxOptions();
        
        if (headless) {
            options.addArguments("--headless");
        }
        
        return new FirefoxDriver(options);
    }
    
    private static WebDriver createEdgeDriver(boolean headless) {
        WebDriverManager.edgedriver().setup();
        EdgeOptions options = new EdgeOptions();
        
        if (headless) {
            options.addArguments("--headless");
        }
        
        return new EdgeDriver(options);
    }

    /**
     * Closes the current WebDriver instance and cleans up resources.
     * Safely handles any exceptions during teardown and removes the driver from ThreadLocal.
     */
    public static void teardown() {
        WebDriver currentDriver = driver.get();
        if (currentDriver != null) {
            logger.info("Tearing down driver");
            try {
                currentDriver.quit();
            } catch (Exception e) {
                logger.warn("Error occurred during driver teardown: {}", e.getMessage());
            } finally {
                driver.remove();
            }
        }
    }
}