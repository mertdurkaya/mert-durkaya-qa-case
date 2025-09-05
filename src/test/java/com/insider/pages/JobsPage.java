package com.insider.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.insider.utils.Helper;

import java.util.List;

public class JobsPage extends BasePage {

    public JobsPage(WebDriver driver) {
        super(driver);
    }

    // --- Locators ---
    @FindBy(id = "select2-filter-by-location-container")
    private WebElement locationFilter;

    // This locator finds the specific option after the dropdown is opened
    private By locationOptionByText(String location) {
        // return By.id("select2-filter-by-location-result-kpap-" + location);
        // #select2-filter-by-location-result-kpap-Istanbul\,\ Turkiye
        return By.xpath("//li[contains(@id, 'select2-filter-by-location-result') and text()='" + location + "']");
    }

    @FindBy(id = "jobs-list")
    private WebElement jobsListContainer;

    @FindBy(className = "position-list-item")
    private List<WebElement> jobItems;

    // --- Page Actions ---
    public void filterByLocation(String location) {
        Helper.safeClick(locationFilter, 10);
        WebElement locationOption = driver.findElement(locationOptionByText(location));
        Helper.safeClick(locationOption, 10);
    }

    public boolean isJobListPresent() {
        return Helper.waitForVisibility(jobsListContainer, 10).isDisplayed();
    }

    public List<WebElement> getAllJobs() {
        // Wait for the container first to ensure the list is populated
        Helper.waitForVisibility(jobsListContainer, 10);
        return jobItems;
    }

    public void clickFirstViewRoleButton() {
        // Find the first "View Role" button in the list and click it
        WebElement firstJobItem = jobItems.get(0);
        Helper.hoverOverElement(firstJobItem, driver); // Ensure hover to reveal the button
        WebElement firstViewRoleButton = Helper.waitForClickability(firstJobItem.findElement(By.linkText("View Role")), 10);
        Helper.safeClick(firstViewRoleButton, 10);
    }
}