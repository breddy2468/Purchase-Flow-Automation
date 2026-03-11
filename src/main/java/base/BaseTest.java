package base;

import io.github.bonigarcia.wdm.WebDriverManager;
import utils.ExtentManager;
import utils.TestData;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import java.lang.reflect.Method;
import java.time.Duration;

public class BaseTest {

    protected WebDriver driver;
    protected WebDriverWait wait;
    protected static ExtentReports extent;
    protected static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    @BeforeSuite(alwaysRun = true)
    public void beforeSuite() {
        extent = ExtentManager.getInstance();
    }

    @Parameters({"browser"})
    @BeforeClass(alwaysRun = true)
    public void setup(@Optional("chrome") String browser) {
        launchBrowser(browser);
    }

    public void launchBrowser(String browser) {
        if (browser == null || browser.isEmpty()) {
            browser = "chrome";
            System.out.println("No browser specified, defaulting to Chrome.");
        }

        switch (browser.toLowerCase()) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver();
                break;

            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver();
                break;

            case "edge":
                WebDriverManager.edgedriver().setup();
                driver = new EdgeDriver();
                break;

            default:
                throw new IllegalArgumentException("Unsupported browser: " + browser);
        }

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get(TestData.URL);
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        PageFactory.initElements(driver, this);
    }

    @BeforeMethod(alwaysRun = true)
    public void startTest(Method method) {
        String testName = getClass().getSimpleName() + " :: " + method.getName();
        ExtentTest extentTest = extent.createTest(testName);
        test.set(extentTest);
    }

    @AfterMethod(alwaysRun = true)
    public void afterMethod(ITestResult result) {
        ExtentTest extentTest = test.get();
        if (extentTest != null) {
            switch (result.getStatus()) {
                case ITestResult.SUCCESS:
                    extentTest.pass("Test Passed: " + result.getName());
                    break;
                case ITestResult.FAILURE:
                    extentTest.fail("Test Failed: " + result.getName());
                    extentTest.fail(result.getThrowable());
                    break;
                case ITestResult.SKIP:
                    extentTest.skip("Test Skipped: " + result.getName());
                    break;
            }
        }
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @AfterSuite(alwaysRun = true)
    public void afterSuite() {
        if (extent != null) {
            extent.flush();
        }
    }
}
