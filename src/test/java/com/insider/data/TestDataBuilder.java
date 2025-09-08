package com.insider.data;

public class TestDataBuilder {
    private String location = TestDataManager.getTestLocation();
    private String department = TestDataManager.getExpectedDepartment();
    private String expectedRedirectDomain = TestDataManager.getExpectedRedirectDomain();
    
    public TestDataBuilder withLocation(String location) {
        this.location = location;
        return this;
    }
    
    public TestDataBuilder withDepartment(String department) {
        this.department = department;
        return this;
    }
    
    public TestDataBuilder withExpectedRedirectDomain(String expectedRedirectDomain) {
        this.expectedRedirectDomain = expectedRedirectDomain;
        return this;
    }
    
    public TestData build() {
        return new TestData(location, department, expectedRedirectDomain);
    }
    
    // Convenience method for default test data
    public static TestData defaultTestData() {
        return new TestDataBuilder().build();
    }
    
    // Convenience method for QA-specific test data
    public static TestData qaTestData() {
        return new TestDataBuilder()
                .withDepartment("Quality Assurance")
                .build();
    }
}
