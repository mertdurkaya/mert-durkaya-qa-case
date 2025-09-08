package com.insider.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigManager {
    private static Properties properties;
    
    static {
        loadProperties();
    }
    
    private static void loadProperties() {
        properties = new Properties();
        try (InputStream input = ConfigManager.class.getClassLoader()
                .getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new RuntimeException("config.properties file not found in classpath");
            }
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
    }
    
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
    
    public static String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
    
    public static int getIntProperty(String key, int defaultValue) {
        String value = properties.getProperty(key);
        if (value == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
    
    public static boolean getBooleanProperty(String key, boolean defaultValue) {
        String value = properties.getProperty(key);
        if (value == null) {
            return defaultValue;
        }
        return Boolean.parseBoolean(value);
    }
    
    // Convenience methods for common configurations
    public static String getBrowser() {
        return System.getProperty("browser", getProperty("browser", "chrome"));
    }
    
    public static boolean isHeadless() {
        return Boolean.parseBoolean(System.getProperty("headless", 
            getProperty("headless", "false")));
    }
    
    public static String getBaseUrl() {
        return getProperty("base.url");
    }
    
    public static String getCareersUrl() {
        return getProperty("careers.url");
    }
    
    public static int getDefaultTimeout() {
        return getIntProperty("default.timeout", 10);
    }
    
    public static int getExtendedTimeout() {
        return getIntProperty("extended.timeout", 30);
    }
    
    public static boolean isScreenshotOnFailure() {
        return getBooleanProperty("screenshot.on.failure", true);
    }
}
