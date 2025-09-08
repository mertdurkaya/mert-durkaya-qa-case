package com.insider.data;

public class TestData {
    private final String location;
    private final String department;
    private final String expectedRedirectDomain;
    
    public TestData(String location, String department, String expectedRedirectDomain) {
        this.location = location;
        this.department = department;
        this.expectedRedirectDomain = expectedRedirectDomain;
    }
    
    public TestData(String location, String department) {
        this(location, department, TestDataManager.getExpectedRedirectDomain());
    }
    
    public String getLocation() {
        return location;
    }
    
    public String getDepartment() {
        return department;
    }
    
    public String getExpectedRedirectDomain() {
        return expectedRedirectDomain;
    }
    
    @Override
    public String toString() {
        return "TestData{" +
                "location='" + location + '\'' +
                ", department='" + department + '\'' +
                ", expectedRedirectDomain='" + expectedRedirectDomain + '\'' +
                '}';
    }
}
