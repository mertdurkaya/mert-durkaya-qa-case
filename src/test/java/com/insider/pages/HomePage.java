package com.insider.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.insider.utils.Helper;
import com.insider.utils.Constants;

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
        driver.get(Constants.BASE_URL);
        Helper.waitForPageLoad(Constants.DEFAULT_TIMEOUT);
    }

    public void goToPage(String path) {
        logger.info("Navigating to page: {}", path);
        driver.get(path);
        Helper.waitForPageLoad(Constants.DEFAULT_TIMEOUT);
    }

    public void acceptCookies() {
        logger.info("Accepting cookies");
        Helper.safeClick(acceptCookiesButton, Constants.DEFAULT_TIMEOUT);
    }

    public void clickCompanyMenu() {
        logger.info("Clicking company menu");
        Helper.safeClick(companyMenu, Constants.DEFAULT_TIMEOUT);
    }

    public void clickCareersLink() {
        logger.info("Clicking careers link");
        Helper.safeClick(careersLink, Constants.DEFAULT_TIMEOUT);
        Helper.waitForPageLoad(Constants.DEFAULT_TIMEOUT);
    }
}