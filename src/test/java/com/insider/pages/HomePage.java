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
        Helper.safeClick(acceptCookiesButton, ConfigManager.getDefaultTimeout());
    }

    public void clickCompanyMenu() {
        logger.info("Clicking company menu");
        Helper.safeClick(companyMenu, ConfigManager.getDefaultTimeout());
    }

    public void clickCareersLink() {
        logger.info("Clicking careers link");
        Helper.safeClick(careersLink, ConfigManager.getDefaultTimeout());
        Helper.waitForPageLoad();
    }
}