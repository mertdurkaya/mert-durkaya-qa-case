package com.insider.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.insider.config.ConfigManager;
import com.insider.utils.Helper;

public class HomePage extends BasePage {
    private static final Logger logger = LoggerFactory.getLogger(HomePage.class);

    public HomePage(WebDriver driver) {
        super(driver);
    }

    // --- Locators using @FindBy ---

    @FindBy(id = "wt-cli-accept-all-btn")
    private WebElement acceptCookiesButton;

    // More robust selectors for Company menu
    @FindBy(xpath = "//a[contains(text(),'Company')]")
    private WebElement companyMenu;

    @FindBy(xpath = "//a[contains(text(),'Careers')]")
    private WebElement careersLink;
    


    // --- Page Actions ---

    public void goToHomePage() {
        logger.info("Navigating to home page");
        driver.get(ConfigManager.getBaseUrl());
        Helper.waitForPageLoad();
    }

    public void goToPage(String path) {
        logger.info("Navigating to page: {}", path);
        driver.get(path);
        Helper.waitForPageLoad();
    }

    public void acceptCookies() {
        logger.info("Accepting cookies");
        try {
            // Wait for cookie banner to be present and clickable
            Helper.waitForVisibility(acceptCookiesButton, ConfigManager.getExtendedTimeout());
            Helper.safeClick(acceptCookiesButton, ConfigManager.getExtendedTimeout());
            
            // Wait for cookie banner to disappear
            Helper.waitForElementToDisappear(acceptCookiesButton, ConfigManager.getDefaultTimeout());
            logger.info("Cookies accepted successfully");
        } catch (Exception e) {
            logger.warn("Cookie acceptance failed or not needed: {}", e.getMessage());
            // Continue execution as cookie banner might not be present
        }
    }

    public void clickCompanyMenu() {
        logger.info("Clicking company menu");
        
        // Define optimized locator strategies (most likely first)
        org.openqa.selenium.By[] companyLocators = {
            org.openqa.selenium.By.xpath("//a[contains(text(),'Company')]"),
            org.openqa.selenium.By.xpath("//nav//a[contains(text(),'Company')]"),
            org.openqa.selenium.By.xpath("//header//a[contains(text(),'Company')]")
        };
        
        try {
            // Try fast method first
            WebElement companyElement = Helper.findElementFast(companyLocators);
            
            Helper.safeClick(companyElement, ConfigManager.getDefaultTimeout());
            logger.info("Company menu clicked successfully");
            
        } catch (Exception e) {
            logger.warn("Fast method failed, trying with extended timeout: {}", e.getMessage());
            try {
                // Fallback to extended method if fast method fails
                WebElement companyElement = Helper.findElementWithMultipleStrategies(
                    companyLocators, 
                    ConfigManager.getDefaultTimeout()
                );
                
                Helper.safeClick(companyElement, ConfigManager.getDefaultTimeout());
                logger.info("Company menu clicked successfully with fallback method");
                
            } catch (Exception fallbackException) {
                logger.error("Failed to click company menu: {}", fallbackException.getMessage());
                throw new RuntimeException("Failed to click company menu: " + fallbackException.getMessage(), fallbackException);
            }
        }
    }

    public void clickCareersLink() {
        logger.info("Clicking careers link");
        
        // Define optimized locator strategies for careers link (most likely first)
        org.openqa.selenium.By[] careersLocators = {
            org.openqa.selenium.By.xpath("//a[contains(text(),'Careers')]"),
            org.openqa.selenium.By.xpath("//nav//a[contains(text(),'Careers')]"),
            org.openqa.selenium.By.xpath("//header//a[contains(text(),'Careers')]")
        };
        
        try {
            // Try fast method first
            WebElement careersElement = Helper.findElementFast(careersLocators);
            
            Helper.safeClick(careersElement, ConfigManager.getDefaultTimeout());
            Helper.waitForPageLoad();
            logger.info("Careers link clicked successfully");
            
        } catch (Exception e) {
            logger.warn("Fast method failed, trying with extended timeout: {}", e.getMessage());
            try {
                // Fallback to extended method if fast method fails
                WebElement careersElement = Helper.findElementWithMultipleStrategies(
                    careersLocators, 
                    ConfigManager.getDefaultTimeout()
                );
                
                Helper.safeClick(careersElement, ConfigManager.getDefaultTimeout());
                Helper.waitForPageLoad();
                logger.info("Careers link clicked successfully with fallback method");
                
            } catch (Exception fallbackException) {
                logger.error("Failed to click careers link: {}", fallbackException.getMessage());
                throw new RuntimeException("Failed to click careers link: " + fallbackException.getMessage(), fallbackException);
            }
        }
    }
}