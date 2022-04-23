package numbereight;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import io.github.bonigarcia.wdm.WebDriverManager;

public class RegistrationPage {

	
	public WebDriver driver;
	public ExtentHtmlReporter htmlReporter;
	public ExtentReports extent;
	public ExtentTest test;

	public Actions action;

//this one will run before test
	@BeforeTest
	public void setExtent() {
		htmlReporter = new ExtentHtmlReporter(System.getProperty("user.dir") + "/test-output/myReport.html");
		htmlReporter.config().setDocumentTitle("Automation Report");

		htmlReporter.config().setTheme(Theme.STANDARD);

		extent = new ExtentReports();
		extent.attachReporter(htmlReporter);

		// Passing General information

		extent.setSystemInfo("Tester Name", "Anamul Haque");

	}

	@AfterTest
	public void endReport() {
		extent.flush();
	}

//before test execution
	@BeforeTest
	public void setup() {
		WebDriverManager.firefoxdriver().setup();
		driver = new FirefoxDriver();

		action = new Actions(driver);

		driver.manage().window().maximize();

		driver.get("https://portal.eu.numbereight.ai/auth/register");
	}

	@Test(priority = 0)
	public void emailPasswordSetup() {
		test = extent.createTest("Email & Password");
		// email
		driver.findElement(By.id("input-23")).sendKeys("test1@test.com");
		// password
		driver.findElement(By.id("input-27")).sendKeys("test1234");
		// confirm-password
		driver.findElement(By.id("input-31")).sendKeys("test1234");
		// next step
		driver.findElement(By.xpath("//span[text() = \"Next Step\"]")).click();

	}

	@Test(priority = 1)
	public void personalInformationSetup() throws InterruptedException {
		test = extent.createTest("Personal Information");
		// name
		driver.findElement(By.id("input-41")).sendKeys("tester xyz");

		// hear bout NE
		driver.findElement(By.id("input-45")).click();
		driver.findElement(By.xpath("//div[@id=\"list-item-78-0\"]/div/div")).click();

		// select android
		WebElement android = driver.findElement(By.id("input-52"));
		action.moveToElement(android).click().perform();

		// select email notification
//		Thread.sleep(2000); 
		WebElement emailNotification = driver.findElement(By.id("input-56"));
		action.moveToElement(emailNotification).click().perform();

		// select terms and condition
		WebElement termsAndCond = driver.findElement(By.id("input-58"));
		action.moveToElement(termsAndCond).click().perform();

		// next step

		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
		Thread.sleep(2000);


		driver.findElement(By.xpath("//div/button[2]/span")).click();

	}

	@Test(priority = 2)
	public void softwareLicense() throws InterruptedException {
		test = extent.createTest("Software License");
		
		Thread.sleep(1000);
		// select license and service agreement
		WebElement licenseAndServie = driver.findElement(By.id("input-67"));
		action.moveToElement(licenseAndServie).click().perform();
		
		//register
		driver.findElement(By.xpath("//span[text()='Register']")).click(); 
		
		
	}
	
	@Test(priority = 3)
	public void registrationVerification() throws InterruptedException {
		
		test = extent.createTest("Register");
		Thread.sleep(2000);
		String actual = driver.findElement(By.xpath("//h3[text()='Dashboard']")).getText(); 
		String expected = "Dashboard"; 
		Assert.assertEquals(actual, expected); 
		System.out.println("Registered Succesfully!"); 
		test.log(Status.PASS, "Registered Succesfully!"); 
		
	}

	@AfterMethod
	public void tearDown(ITestResult result) throws IOException {
		if (result.getStatus() == ITestResult.FAILURE) {
			test.log(Status.FAIL, "TEST CASE FAILED IS " + result.getName()); // to add name in extent report
			test.log(Status.FAIL, "TEST CASE FAILED IS " + result.getThrowable()); // to add error/exception in extent
																					// report
			String screenshotPath = RegistrationPage.getScreenshot(driver, result.getName());
			test.addScreenCaptureFromPath(screenshotPath);// adding screen shot
		} else if (result.getStatus() == ITestResult.SKIP) {
			test.log(Status.SKIP, "Test Case SKIPPED IS " + result.getName());
		} else if (result.getStatus() == ITestResult.SUCCESS) {
			test.log(Status.PASS, "Test Case PASSED IS " + result.getName());
		}

	}

	// screenshot method
	public static String getScreenshot(WebDriver driver, String screenshotName) throws IOException {
		String dateName = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
		TakesScreenshot ts = (TakesScreenshot) driver;
		File source = ts.getScreenshotAs(OutputType.FILE);

		// after execution, you could see a folder "FailedTestsScreenshots" under src
		// folder
		String destination = System.getProperty("user.dir") + "/Screenshots/" + screenshotName + dateName + ".png";
		File finalDestination = new File(destination);
		FileUtils.copyFile(source, finalDestination);
		return destination;
	}
}
