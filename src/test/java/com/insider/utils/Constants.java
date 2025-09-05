package com.insider.utils;

public class Constants {
    // URLs
    public static final String BASE_URL = "https://useinsider.com/";
    public static final String CAREERS_URL = BASE_URL + "careers/quality-assurance/";
    public static final String EXPECTED_REDIRECT_DOMAIN = "lever.co";
    
    // Timeouts
    public static final int DEFAULT_TIMEOUT = 10;
    public static final int EXTENDED_TIMEOUT = 15;
    
    // Test Data
    public static final String TEST_LOCATION = "Istanbul, Turkiye";
    public static final String EXPECTED_DEPARTMENT = "Quality Assurance";
    
    // UI Elements Text
    public static final String VIEW_ROLE_BUTTON_TEXT = "View Role";
    
    private Constants() {
        // Private constructor to prevent instantiation
    }
}
