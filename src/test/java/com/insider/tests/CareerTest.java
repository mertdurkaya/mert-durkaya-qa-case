package com.insider.tests;

import com.insider.config.ConfigManager;
import com.insider.data.TestData;
import com.insider.data.TestDataBuilder;
import com.insider.data.TestDataManager;
import com.insider.driver.DriverManager;
import com.insider.pages.CareersPage;
import com.insider.pages.HomePage;
import com.insider.pages.JobsPage;
import com.insider.reporting.ReportManager;
import com.insider.utils.Helper;
import com.insider.utils.ScreenshotUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.util.List;

public class CareerTest {
    private static final Logger logger = LoggerFactory.getLogger(CareerTest.class);
    private TestData testData;

    @BeforeSuite
    public void initializeReport() {
        ReportManager.initializeReport();
    }

    @BeforeMethod
    public void setup() {
        logger.info("Setting up test environment");
        DriverManager.setup();
        testData = TestDataBuilder.defaultTestData();
        ReportManager.logInfo("Test data initialized: " + testData);
    }

    @Test(description = "Test the complete career application flow from home page to job application")
    public void testInsiderCareerApplicationFlow() {
        try {
            // Visit https://useinsider.com/ and check Insider home page is opened or not
            logger.info("Starting Insider career application flow test");
            ReportManager.logInfo("Starting Insider career application flow test");
            
            HomePage homePage = new HomePage(DriverManager.getDriver());
            homePage.goToPage(ConfigManager.getBaseUrl());
            
            String currentUrl = DriverManager.getDriver().getCurrentUrl();
            Assert.assertEquals(currentUrl, ConfigManager.getBaseUrl(), 
                "Home page URL is not correct");
            ReportManager.logPass("Successfully navigated to home page: " + currentUrl);
            
            logger.info("Accepting cookies and navigating to careers page");
            ReportManager.logInfo("Accepting cookies and navigating to careers page");
            
            // Wait for page to be fully loaded before accepting cookies
            // Helper.waitForPageLoad();
            // Helper.sleep(2); // Additional wait for dynamic content
            
            homePage.acceptCookies();
            
            // Wait for any overlays or popups to disappear
            // Helper.sleep(3);
            // Helper.waitForPageLoad();

            // Select the "Company" menu in the navigation bar, select "Careers" and check Career page, its Locations, Teams, and Life at Insider blocks are open or not
            ReportManager.logInfo("Navigating to careers page via company menu");
            homePage.clickCompanyMenu();
            
            // Wait for menu to expand
            // Helper.sleep(2);
            
            homePage.clickCareersLink();

            CareersPage careersPage = new CareersPage(DriverManager.getDriver());
            
            // Verify all required blocks are displayed
            Assert.assertTrue(careersPage.isLocationsBlockDisplayed(), 
                TestDataManager.getErrorLocationsBlockNotDisplayed());
            ReportManager.logPass("Locations block is displayed");
            
            Assert.assertTrue(careersPage.isTeamsBlockDisplayed(), 
                TestDataManager.getErrorTeamsBlockNotDisplayed());
            ReportManager.logPass("Teams block is displayed");
            
            Assert.assertTrue(careersPage.isLifeAtInsiderBlockDisplayed(), 
                TestDataManager.getErrorLifeAtInsiderNotDisplayed());
            ReportManager.logPass("Life at Insider block is displayed");

            // Go to https://useinsider.com/careers/quality-assurance/, click "See all QA jobs", filter jobs by Location: "Istanbul, Turkey", and Department: "Quality Assurance", check the presence of the jobs list
            ReportManager.logInfo("Navigating to QA careers page");
            homePage.goToPage(ConfigManager.getCareersUrl());
            careersPage.clickSeeAllQAJobsButton();

            JobsPage jobsPage = new JobsPage(DriverManager.getDriver());
            logger.info("Filtering jobs by location: {}", testData.getLocation());
            ReportManager.logInfo("Filtering jobs by location: " + testData.getLocation());
            jobsPage.filterByLocation(testData.getLocation());
            
            Assert.assertTrue(jobsPage.isJobListPresent(), 
                TestDataManager.getErrorJobsListNotPresent());
            ReportManager.logPass("Jobs list is present after filtering");

            List<WebElement> jobs = jobsPage.getAllJobs();
            Assert.assertTrue(jobs.size() > 0, 
                TestDataManager.getErrorNoJobsFound());
            ReportManager.logPass("Found " + jobs.size() + " jobs in the list");


            // Check that all jobs' Position contains "Quality Assurance", Department contains "Quality Assurance", and Location contains "Istanbul, Turkey"
            logger.info("Verifying job listings...");
            ReportManager.logInfo("Verifying job listings for correct department and location");
            
            for (int i = 0; i < jobs.size(); i++) {
                WebElement job = jobs.get(i);
                String position = job.findElement(By.className("position-title")).getText();
                String department = job.findElement(By.className("position-department")).getText();
                String location = job.findElement(By.className("position-location")).getText();

                Assert.assertTrue(position.contains(testData.getDepartment()), 
                    "Job " + (i+1) + " position does not contain '" + testData.getDepartment() + "'");
                Assert.assertTrue(department.contains(testData.getDepartment()), 
                    "Job " + (i+1) + " department does not contain '" + testData.getDepartment() + "'");
                Assert.assertTrue(location.contains(testData.getLocation()), 
                    "Job " + (i+1) + " location does not contain '" + testData.getLocation() + "'");
                
                ReportManager.logPass("Job " + (i+1) + " validation passed - Position: " + position + 
                    ", Department: " + department + ", Location: " + location);
            }

            // Click the "View Role" button and check that this action redirects us to the Lever Application form page
            logger.info("Clicking view role button and switching to new window");
            ReportManager.logInfo("Clicking view role button and switching to new window");
            jobsPage.clickFirstViewRoleButton();
            Helper.switchToLastWindow();

            String redirectedUrl = DriverManager.getDriver().getCurrentUrl();
            Assert.assertTrue(redirectedUrl.contains(testData.getExpectedRedirectDomain()), 
                TestDataManager.getErrorIncorrectRedirect() + " " + testData.getExpectedRedirectDomain());
            ReportManager.logPass("Successfully redirected to: " + redirectedUrl);
            
            logger.info("Test completed successfully");
            ReportManager.logPass("Test completed successfully");
            
        } catch (Exception e) {
            logger.error("Test failed with exception", e);
            ReportManager.logFail("Test failed: " + e.getMessage());
            ScreenshotUtils.captureScreenshotOnFailure("testInsiderCareerApplicationFlow", e);
            throw e;
        }
    }

    @AfterMethod
    public void teardown() {
        logger.info("Cleaning up test environment");
        ReportManager.logInfo("Cleaning up test environment");
        DriverManager.teardown();
    }
    
    @AfterSuite
    public void closeReport() {
        ReportManager.closeReport();
    }
}