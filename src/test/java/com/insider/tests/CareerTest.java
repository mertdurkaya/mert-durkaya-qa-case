package com.insider.tests;

import com.insider.driver.DriverManager;
import com.insider.pages.CareersPage;
import com.insider.pages.HomePage;
import com.insider.pages.JobsPage;
import com.insider.utils.Constants;
import com.insider.utils.Helper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

public class CareerTest {
    private static final Logger logger = LoggerFactory.getLogger(CareerTest.class);

    @BeforeMethod
    public void setup() {
        logger.info("Setting up test environment");
        DriverManager.setup();
    }

    @Test
    public void testInsiderCareerApplicationFlow() {

        // Visit https://useinsider.com/ and check Insider home page is opened or not
        logger.info("Starting Insider career application flow test");
        
        HomePage homePage = new HomePage(DriverManager.getDriver());
        homePage.goToPage(Constants.BASE_URL);
        Assert.assertEquals(DriverManager.getDriver().getCurrentUrl(), Constants.BASE_URL, 
            "Home page URL is not correct");
        
        logger.info("Accepting cookies and navigating to careers page");
        homePage.acceptCookies();

        // Select the “Company” menu in the navigation bar, select “Careers” and check Career page, its Locations, Teams, and Life at Insider blocks are open or not
        homePage.clickCompanyMenu();
        homePage.clickCareersLink();

        CareersPage careersPage = new CareersPage(DriverManager.getDriver());
        Assert.assertTrue(careersPage.isLocationsBlockDisplayed(), "Locations block is not displayed.");
        Assert.assertTrue(careersPage.isTeamsBlockDisplayed(), "Teams block is not displayed.");
        Assert.assertTrue(careersPage.isLifeAtInsiderBlockDisplayed(), "Life at Insider block is not displayed.");


        // Go to https://useinsider.com/careers/quality-assurance/, click “See all QA jobs”, filter jobs by Location: “Istanbul, Turkey”, and Department: “Quality Assurance”, check the presence of the jobs list
        homePage.goToPage(Constants.CAREERS_URL);
        careersPage.clickSeeAllQAJobsButton();

        JobsPage jobsPage = new JobsPage(DriverManager.getDriver());
        logger.info("Filtering jobs by location: {}", Constants.TEST_LOCATION);
        jobsPage.filterByLocation(Constants.TEST_LOCATION);
        Assert.assertTrue(jobsPage.isJobListPresent(), "Jobs list is not present after filtering.");

        List<WebElement> jobs = jobsPage.getAllJobs();
        Assert.assertTrue(jobs.size() > 0, "No jobs were found in the list.");


        // Check that all jobs’ Position contains “Quality Assurance”, Department contains “Quality Assurance”, and Location contains “Istanbul, Turkey”
        logger.info("Verifying job listings...");
        for (WebElement job : jobs) {
            String position = job.findElement(By.className("position-title")).getText();
            String department = job.findElement(By.className("position-department")).getText();
            String location = job.findElement(By.className("position-location")).getText();

            Assert.assertTrue(position.contains(Constants.EXPECTED_DEPARTMENT), 
                "Job position does not contain '" + Constants.EXPECTED_DEPARTMENT + "'");
            Assert.assertTrue(department.contains(Constants.EXPECTED_DEPARTMENT), 
                "Job department does not contain '" + Constants.EXPECTED_DEPARTMENT + "'");
            Assert.assertTrue(location.contains(Constants.TEST_LOCATION), 
                "Job location does not contain '" + Constants.TEST_LOCATION + "'");
        }

        // Click the “View Role” button and check that this action redirects us to the Lever Application form page
        logger.info("Clicking view role button and switching to new window");
        jobsPage.clickFirstViewRoleButton();
        Helper.switchToLastWindow();

        String currentUrl = DriverManager.getDriver().getCurrentUrl();
        Assert.assertTrue(currentUrl.contains(Constants.EXPECTED_REDIRECT_DOMAIN), 
            "Redirected URL does not belong to " + Constants.EXPECTED_REDIRECT_DOMAIN);
        
        logger.info("Test completed successfully");
    }

    @AfterMethod
    public void teardown() {
        logger.info("Cleaning up test environment");
        DriverManager.teardown();
    }
}