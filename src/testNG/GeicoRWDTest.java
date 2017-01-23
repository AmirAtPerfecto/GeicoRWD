package testNG;

import org.testng.annotations.Test;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;

import java.io.IOException;
import java.net.MalformedURLException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterSuite;

public class GeicoRWDTest {
	RemoteWebDriver driver;
	ApplitoolsHelper applitoolsHelper;
	// Create Remote WebDriver based on testng.xml configuration
	@Parameters({ "platformName", "platformVersion", "browserName", "browserVersion", "screenResolution" })
	@BeforeTest
	public void beforeTest(String platformName, String platformVersion, String browserName, String browserVersion, String screenResolution) throws MalformedURLException {
		driver = Utils.getRemoteWebDriver(platformName, platformVersion, browserName, browserVersion, screenResolution);        
		applitoolsHelper  = new ApplitoolsHelper(driver, "Geico RWD", "Geico Test");
	}
	
	// Test Method, navigate to google and perform search
	@Test
	public void searchGoogle() throws MalformedURLException {				

		driver.get("http://www.google.com");
		applitoolsHelper.checkWindow("Google");
		try {
			final String searchKey = "Perfecto Mobile";
			WebElement element = driver.findElement(By.name("q"));
			element.sendKeys(searchKey);
			element.submit();
			(new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
				public Boolean apply(WebDriver d) {
					return d.getTitle().toLowerCase().startsWith(searchKey.toLowerCase());
				}
			});
			applitoolsHelper.checkWindow("Google- search Perfecto");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("Done: searchGoogle");
	}

	// Test Method, navigate to Geico and get insurance quote
	@Test
	public void geicoInsurance() throws MalformedURLException {

		driver.get("http://www.geico.com");
		applitoolsHelper.checkWindow("Gaico");

		try{
			driver.findElement(By.id("auto")).click();
			driver.findElement(By.id("zip")).sendKeys("01434");
			driver.findElement(By.id("submitButton")).click();
			//driver.findElement(By.id("btnSubmit")).click();
			driver.findElement(By.xpath("//*[@id= 'CustomerForm:firstName']")).sendKeys("Dan");
			driver.findElement(By.xpath("//*[@id= 'CustomerForm:lastName']")).sendKeys("Kaligiery");
			driver.findElement(By.xpath("//*[@id= 'CustomerForm:customerMailingAddress']")).sendKeys("Woburn");

			driver.findElement(By.id("CustomerForm:birthMonth")).sendKeys("8");
			driver.findElement(By.id("CustomerForm:birthDay")).sendKeys("3");
			driver.findElement(By.id("CustomerForm:birthYear")).sendKeys("1981");

			driver.findElement(By.id("CustomerForm:continueBtn")).click();
			applitoolsHelper.checkWindow("Geico get quote");

			//driver.findElement(By.id("btnSubmit"));
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("Done: geicoInsurance");
	}

	@AfterTest
	public void afterTest() throws IOException {
		String reportURL = (String)driver.getCapabilities().getCapability("testGridReportUrl");
		System.out.println("########### ========>>>>>>>> Report URL: "+ reportURL); 
		try{
			String applitoolsReportURL = applitoolsHelper.close();
			System.out.println("########### ========>>>>>>>> Applitools Report URL: "+ applitoolsReportURL); 
		}

		finally {
			// Abort test in case of an unexpected error.
			driver.close();
			driver.quit();	
		}

	}	

	@BeforeClass
	public void beforeClass() {
	}

	@BeforeSuite
	public void beforeSuite() {
		ApplitoolsHelper.setBatch("Geico RWD Test");
	}

	@AfterSuite
	public void afterSuite() {
	}

}
