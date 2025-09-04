package com.insider.tests;

import com.insider.driver.DriverManager;
import com.insider.pages.CareersPage;
import com.insider.pages.HomePage;
import com.insider.pages.JobsPage;
import com.insider.utils.Helper;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

public class CareerTest {

    @BeforeMethod
    public void setup() {
        // All setup logic is now handled by our DriverManager
        DriverManager.setup();
    }

    @Test
    public void testInsiderCareerApplicationFlow() {
        HomePage homePage = new HomePage(DriverManager.getDriver());
        homePage.goToHomePage();
        Assert.assertEquals(DriverManager.getDriver().getCurrentUrl(), "https://useinsider.com/", "Home page URL is not correct.");
        homePage.acceptCookies();

        homePage.clickCompanyMenu();
        homePage.clickCareersLink();

        CareersPage careersPage = new CareersPage(DriverManager.getDriver());
        Assert.assertTrue(careersPage.isLocationsBlockDisplayed(), "Locations block is not displayed.");
        Assert.assertTrue(careersPage.isTeamsBlockDisplayed(), "Teams block is not displayed.");
        Assert.assertTrue(careersPage.isLifeAtInsiderBlockDisplayed(), "Life at Insider block is not displayed.");

        careersPage.clickSeeAllQAJobsButton();

        JobsPage jobsPage = new JobsPage(DriverManager.getDriver());
        jobsPage.filterByLocation("Istanbul, Turkey");
        Assert.assertTrue(jobsPage.isJobListPresent(), "Jobs list is not present after filtering.");

        List<WebElement> jobs = jobsPage.getAllJobs();
        Assert.assertTrue(jobs.size() > 0, "No jobs were found in the list.");

        for (WebElement job : jobs) {
            String position = job.findElement(By.className("position-title")).getText();
            String department = job.findElement(By.className("position-department")).getText();
            String location = job.findElement(By.className("position-location")).getText();

            Assert.assertTrue(position.contains("Quality Assurance"), "Job position does not contain 'Quality Assurance'");
            Assert.assertTrue(department.contains("Quality Assurance"), "Job department does not contain 'Quality Assurance'");
            Assert.assertTrue(location.contains("Istanbul, Turkey"), "Job location does not contain 'Istanbul, Turkey'");
        }

        jobsPage.clickFirstViewRoleButton();

        // The complex window switching is now a simple helper method call
        Helper.switchToLastWindow();

        Assert.assertTrue(DriverManager.getDriver().getCurrentUrl().contains("lever.co"), "Redirected URL does not belong to Lever.");
    }

    @AfterMethod
    public void teardown() {
        // All teardown logic is now handled by our DriverManager
        DriverManager.teardown();
    }
}