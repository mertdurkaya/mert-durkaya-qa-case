package com.insider.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.insider.config.ConfigManager;
import com.insider.utils.Helper;
import com.insider.utils.WaitStrategy;

import java.util.List;

public class JobsPage extends BasePage {

    public JobsPage(WebDriver driver) {
        super(driver);
    }

    // --- Locators ---
    private By locationDropdownBy() {
        return By.xpath("//span[@id='select2-filter-by-location-container' and @role='textbox' and text()='All']");
    }

    private By departmentDropdownBy() {
        return By.xpath("//span[@id='select2-filter-by-department-container' and @role='textbox' and text()='Quality Assurance']");
    }

    private By locationOptionByText(String location) {
        return By.xpath("//li[contains(@id, 'select2-filter-by-location-result') and text()='" + location + "']");
    }

    @FindBy(id = "jobs-list")
    private WebElement jobsListContainer;

    @FindBy(className = "position-list-item")
    private List<WebElement> jobItems;

    // --- Page Actions ---
    public void filterByLocation(String location) {
        // Wait for the page to be ready and dropdown to be available
        WaitStrategy.waitForTextToBePresent(driver.findElement(departmentDropdownBy()), "Quality Assurance", ConfigManager.getDefaultTimeout());

        WebElement dropdown = WaitStrategy.waitForElementWithRetry(locationDropdownBy(), 3);
        Helper.safeClick(dropdown, ConfigManager.getDefaultTimeout());
        Helper.waitForPageLoad();
        // Wait for the dropdown options to be visible
        Helper.waitForVisibility(driver.findElement(By.className("select2-results__options")), ConfigManager.getDefaultTimeout());
        // Select the desired location
        WebElement locationOption = WaitStrategy.waitForElementWithRetry(locationOptionByText(location), 3);
        Helper.safeClick(locationOption, ConfigManager.getDefaultTimeout());
        // Wait for AJAX requests to complete and page to stabilize
        Helper.waitForAjaxToComplete();
        Helper.waitForPageLoad();
    }

    public boolean isJobListPresent() {
        return Helper.waitForVisibility(jobsListContainer, ConfigManager.getDefaultTimeout()).isDisplayed();
    }

    public List<WebElement> getAllJobs() {
        // Wait for the container first to ensure the list is populated
        Helper.waitForVisibility(jobsListContainer, ConfigManager.getDefaultTimeout());
        
        // Wait for at least one job item to be present
        WaitStrategy.waitForElementWithRetry(By.className("position-list-item"), 3);
        
        // Wait for AJAX to complete to ensure all jobs are loaded
        Helper.waitForAjaxToComplete();
        
        return jobItems;
    }

    public void clickFirstViewRoleButton() {
        // Get fresh job items after filtering (stale element issue)
        List<WebElement> freshJobItems = driver.findElements(By.className("position-list-item"));
        
        if (freshJobItems.isEmpty()) {
            throw new RuntimeException("No job items found after filtering");
        }
        
        // Find the first job item and scroll to it
        WebElement firstJobItem = freshJobItems.get(0);
        Helper.scrollToElement(firstJobItem);
        
        // Wait for the job item to be visible and hover to reveal the button
        Helper.waitForVisibility(firstJobItem, ConfigManager.getDefaultTimeout());
        Helper.hoverOverElement(firstJobItem, driver);
        
        // Use JavaScript to find and click the link directly
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String script = 
            "var jobItem = arguments[0];" +
            "var links = jobItem.querySelectorAll('a');" +
            "if (links.length > 0) {" +
            "  links[0].click();" +
            "  return true;" +
            "}" +
            "return false;";
        
        Boolean result = (Boolean) js.executeScript(script, firstJobItem);
        if (!result) {
            throw new RuntimeException("Could not find or click link in job item");
        }
    }
}