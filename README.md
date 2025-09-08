# Insider QA Test Automation Project

## Project Description

This is a comprehensive Selenium WebDriver test automation project that automates the complete career application flow on the Insider website (https://useinsider.com/). The test suite validates the user journey from the home page through the careers section to job application, ensuring all critical functionality works as expected.

## Test Scenario

The automated test performs the following steps:

1. **Home Page Validation**: Navigates to https://useinsider.com/ and verifies the page loads correctly
2. **Navigation to Careers**: Accepts cookies and navigates to the careers page via the Company menu
3. **Careers Page Validation**: Verifies that all required sections (Locations, Teams, Life at Insider) are displayed
4. **QA Jobs Navigation**: Goes to the Quality Assurance careers page and clicks "See all QA jobs"
5. **Job Filtering**: Filters jobs by location (Istanbul, Turkey) and department (Quality Assurance)
6. **Job List Validation**: Verifies that jobs are displayed and contain the correct information
7. **Job Application**: Clicks on a job's "View Role" button and validates redirection to the application form

## Tech Stack

- **Java 11**: Programming language
- **Selenium WebDriver 4.35.0**: Web automation framework
- **TestNG 7.11.0**: Testing framework with parallel execution support
- **Maven**: Build and dependency management
- **WebDriverManager 6.2.0**: Automatic driver management
- **ExtentReports 5.1.1**: Enhanced test reporting
- **SLF4J + Logback**: Logging framework
- **Apache Commons IO**: File operations

## Prerequisites

Before running the tests, ensure you have the following installed on your system:

- **Java 11 or higher**: [Download from Oracle](https://www.oracle.com/java/technologies/downloads/) or [OpenJDK](https://openjdk.org/)
- **Maven 3.6+**: [Download from Apache Maven](https://maven.apache.org/download.cgi)
- **Google Chrome**: Latest version recommended
- **Git**: For cloning the repository

### Verify Installation

```bash
java -version
mvn -version
```

## Setup Instructions

1. **Clone the repository**:
   ```bash
   git clone <repository-url>
   cd insider-qa-test
   ```

2. **Verify project structure**:
   The project should contain the following key directories:
   ```
   src/
   ├── main/java/com/insider/
   └── test/
       ├── java/com/insider/
       │   ├── config/          # Configuration management
       │   ├── data/            # Test data management
       │   ├── driver/          # WebDriver management
       │   ├── exceptions/      # Custom exceptions
       │   ├── listeners/       # TestNG listeners
       │   ├── pages/           # Page Object Model classes
       │   ├── reporting/       # Test reporting
       │   ├── tests/           # Test classes
       │   └── utils/           # Utility classes
       └── resources/
           ├── config.properties
           ├── test-data.properties
           └── logback-test.xml
   ```

3. **No additional setup required**: The project uses WebDriverManager to automatically download and manage browser drivers.

## How to Run Tests

### Basic Test Execution

Run all tests with default configuration (Chrome browser):
```bash
mvn clean test
```

### Browser-Specific Execution

Run tests with specific browsers:
```bash
# Chrome
mvn clean test -Pchrome

# Firefox
mvn clean test -Pfirefox

# Edge
mvn clean test -Pedge
```

### Headless Mode

Run tests in headless mode (useful for CI/CD):
```bash
mvn clean test -Pheadless
```

### Cross-Browser Testing

Run tests across multiple browsers:
```bash
mvn clean test -Pcross-browser
```

### Parallel Execution

The tests are configured to run in parallel by default (3 threads). You can modify the thread count in `pom.xml` if needed.

## Test Reports

After test execution, reports are generated in the following locations:

- **TestNG Reports**: `target/surefire-reports/`
- **ExtentReports**: `test-output/reports/`
- **Screenshots**: `test-output/screenshots/` (captured on test failures)

### Viewing Reports

1. **TestNG HTML Report**: Open `target/surefire-reports/index.html` in your browser
2. **ExtentReports**: Open `test-output/reports/ExtentReport.html` in your browser

## Configuration

### Browser Configuration

Edit `src/test/resources/config.properties` to modify:
- Browser type and headless mode
- Timeouts and wait configurations
- Window settings
- Environment URLs

### Test Data Configuration

Edit `src/test/resources/test-data.properties` to modify:
- Test locations and departments
- Expected validation messages
- UI element text values

## Project Architecture

### Page Object Model (POM)
- **BasePage**: Common functionality for all pages
- **HomePage**: Home page interactions
- **CareersPage**: Careers page interactions  
- **JobsPage**: Job listings and filtering

### Utility Classes
- **DriverManager**: WebDriver lifecycle management with thread safety
- **Helper**: Common utility methods for element interactions
- **WaitStrategy**: Advanced waiting strategies
- **ScreenshotUtils**: Screenshot capture on failures

### Configuration Management
- **ConfigManager**: Centralized configuration management
- **TestDataManager**: Test data management and validation

### Reporting
- **ReportManager**: ExtentReports integration
- **TestListener**: TestNG listener for enhanced reporting

## Troubleshooting

### Common Issues

1. **Browser Driver Issues**: WebDriverManager automatically handles driver downloads. If issues persist, try:
   ```bash
   mvn clean
   mvn test
   ```

2. **Timeout Issues**: Increase timeout values in `config.properties`:
   ```properties
   default.timeout=15
   page.load.timeout=45
   ```

3. **Element Not Found**: Check if the website structure has changed and update locators accordingly.

4. **Parallel Execution Issues**: Reduce thread count in `pom.xml`:
   ```xml
   <threadCount>1</threadCount>
   ```

### Logs

Check the console output and log files for detailed error information:
- Console output shows real-time test execution
- `test-execution.log` contains detailed logging information

## CI/CD Pipeline

This project includes comprehensive GitHub Actions workflows for continuous integration and deployment:

### 🚀 **Main CI/CD Pipeline** (`.github/workflows/ci.yml`)
- **Multi-browser testing**: Chrome, Firefox, Edge
- **Multi-Java version support**: Java 11 and 17
- **Parallel execution**: Tests run in parallel for faster feedback
- **Code quality checks**: SpotBugs, Checkstyle, JaCoCo coverage
- **Security scanning**: OWASP dependency check
- **Artifact management**: Test reports and screenshots uploaded
- **Build verification**: Ensures project builds successfully

### 📅 **Scheduled Testing** (`.github/workflows/scheduled-tests.yml`)
- **Daily smoke tests**: Automated daily testing at 2 AM UTC
- **Regression testing**: Comprehensive cross-browser validation
- **Automated monitoring**: Continuous health checks

### 🔧 **Pipeline Features**
- **Matrix strategy**: Tests across multiple browser/Java combinations
- **Caching**: Maven dependencies cached for faster builds
- **Artifact retention**: Test reports and screenshots preserved
- **Failure handling**: Screenshots captured on test failures
- **Notification system**: Pipeline status notifications

### 📊 **Quality Gates**
- All tests must pass across all browsers
- Code quality checks must pass (SpotBugs, Checkstyle)
- Security vulnerabilities must be below threshold
- Test coverage reports generated
- Build artifacts created successfully

## GitHub Integration

### 🐛 **Issue Templates**
- **Bug Report Template**: Structured bug reporting with environment details
- **Feature Request Template**: Standardized feature request process

### 🔄 **Pull Request Template**
- **Comprehensive checklist**: Ensures code quality and testing
- **Change categorization**: Clear classification of changes
- **Testing verification**: Confirms all tests pass

### 📈 **Workflow Status**
[![CI/CD Pipeline](https://github.com/username/insider-qa-test/workflows/CI/CD%20Pipeline/badge.svg)](https://github.com/username/insider-qa-test/actions)
[![Scheduled Tests](https://github.com/username/insider-qa-test/workflows/Scheduled%20Tests/badge.svg)](https://github.com/username/insider-qa-test/actions)

## Contributing

1. **Fork the repository** and create a feature branch
2. **Follow the existing code structure** and naming conventions
3. **Add appropriate logging and error handling**
4. **Update documentation** for any new features
5. **Ensure all tests pass** before submitting changes
6. **Use the pull request template** for structured reviews
7. **Follow the CI/CD pipeline** - all checks must pass

### Development Workflow
1. Create feature branch from `main`
2. Make changes and test locally
3. Push to your fork
4. Create pull request using the template
5. Address any CI/CD pipeline failures
6. Merge after approval and successful pipeline

## License

This project is created for demonstration purposes as part of a QA automation assessment.

---

**Note**: This test suite is designed to work with the current structure of the Insider website. If the website structure changes, the test locators may need to be updated accordingly.

## 🎯 **Project Highlights**

- ✅ **Professional CI/CD Pipeline** with multi-browser testing
- ✅ **Comprehensive Test Automation** with Page Object Model
- ✅ **Advanced Reporting** with ExtentReports and screenshots
- ✅ **Code Quality Assurance** with automated checks
- ✅ **Security Scanning** with OWASP dependency check
- ✅ **Cross-browser Support** (Chrome, Firefox, Edge)
- ✅ **Parallel Execution** for faster test runs
- ✅ **Thread-safe Architecture** for reliable parallel testing
- ✅ **Comprehensive Documentation** with setup instructions
- ✅ **GitHub Integration** with issue/PR templates