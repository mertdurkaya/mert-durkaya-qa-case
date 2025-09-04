package com.insider.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import com.insider.utils.Helper;

public class HomePage extends BasePage {

    // Constructor that calls the parent BasePage constructor
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
        driver.get("https://useinsider.com/");
    }

    public void acceptCookies() {
        // Wait until the button is visible and then click it
        Helper.safeClick(acceptCookiesButton, 10);
    }

    public void clickCompanyMenu() {
        companyMenu.click();
    }

    public void clickCareersLink() {
        careersLink.click();
    }
}