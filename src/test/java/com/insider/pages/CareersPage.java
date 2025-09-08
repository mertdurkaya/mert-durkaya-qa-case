package com.insider.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.insider.config.ConfigManager;
import com.insider.utils.Helper;

public class CareersPage extends BasePage {

    public CareersPage(WebDriver driver) {
        super(driver);
    }

    // --- Locators ---
    @FindBy(xpath = "//h3[normalize-space()='Our Locations']")
    private WebElement locationsBlock;

    @FindBy(id = "career-find-our-calling")
    private WebElement teamsBlock;

    @FindBy(xpath = "//h2[normalize-space()='Life at Insider']")
    private WebElement lifeAtInsiderBlock;

    @FindBy(xpath = "//a[normalize-space()='See all QA jobs']")
    private WebElement seeAllQAJobsButton;

    // --- Page Actions ---
    public boolean isLocationsBlockDisplayed() {
        // We'll use this method for our assertion later
        return Helper.waitForVisibility(locationsBlock, ConfigManager.getDefaultTimeout()).isDisplayed();
    }

    public boolean isTeamsBlockDisplayed() {
        return teamsBlock.isDisplayed();
    }

    public boolean isLifeAtInsiderBlockDisplayed() {
        return lifeAtInsiderBlock.isDisplayed();
    }

    public void clickSeeAllQAJobsButton() {
        Helper.safeClick(seeAllQAJobsButton, ConfigManager.getExtendedTimeout());
        // wait here until filter is applied and jobs are loaded
        Helper.waitForPageLoad();
    }
}