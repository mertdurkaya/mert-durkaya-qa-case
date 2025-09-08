package com.insider.data;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class TestDataManager {
    private static Properties properties;
    
    static {
        loadProperties();
    }
    
    private static void loadProperties() {
        properties = new Properties();
        try (InputStream input = TestDataManager.class.getClassLoader()
                .getResourceAsStream("test-data.properties")) {
            if (input == null) {
                throw new RuntimeException("test-data.properties file not found in classpath");
            }
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load test-data.properties", e);
        }
    }
    
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
    
    public static String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
    
    // Test Data Getters
    public static String getTestLocation() {
        return getProperty("test.location");
    }
    
    public static String getExpectedDepartment() {
        return getProperty("expected.department");
    }
    
    public static String getExpectedRedirectDomain() {
        return getProperty("expected.redirect.domain");
    }
    
    public static String getViewRoleButtonText() {
        return getProperty("view.role.button.text");
    }
    
    public static String getAcceptCookiesButtonText() {
        return getProperty("accept.cookies.button.text");
    }
    
    public static String getCompanyMenuText() {
        return getProperty("company.menu.text");
    }
    
    public static String getCareersLinkText() {
        return getProperty("careers.link.text");
    }
    
    public static String getDefaultLocationFilter() {
        return getProperty("default.location.filter");
    }
    
    public static String getDefaultDepartmentFilter() {
        return getProperty("default.department.filter");
    }
    
    // Error Messages
    public static String getErrorLocationsBlockNotDisplayed() {
        return getProperty("error.locations.block.not.displayed");
    }
    
    public static String getErrorTeamsBlockNotDisplayed() {
        return getProperty("error.teams.block.not.displayed");
    }
    
    public static String getErrorLifeAtInsiderNotDisplayed() {
        return getProperty("error.life.at.insider.not.displayed");
    }
    
    public static String getErrorJobsListNotPresent() {
        return getProperty("error.jobs.list.not.present");
    }
    
    public static String getErrorNoJobsFound() {
        return getProperty("error.no.jobs.found");
    }
    
    public static String getErrorIncorrectRedirect() {
        return getProperty("error.incorrect.redirect");
    }
    
    // Data-driven testing support
    public static List<String> getTestLocations() {
        String locations = getProperty("test.locations");
        return Arrays.asList(locations.split("\\|"));
    }
    
    public static List<String> getTestDepartments() {
        String departments = getProperty("test.departments");
        return Arrays.asList(departments.split("\\|"));
    }
}
