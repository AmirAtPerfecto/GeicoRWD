package testNG;
  
import java.io.IOException;
import java.net.MalformedURLException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

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

		long timerHome = 0,
			 timerSearchResult= 0; 
		driver.get("http://www.google.com");
		timerHome = PerfectoUtils.ocrTextCheckAndGetUXTimer(driver, "images", 99, 10);
		applitoolsHelper.checkWindow("Google");
		try {
			final String searchKey = "Perfecto Mobile";
			WebElement element = driver.findElement(By.name("q"));
			element.sendKeys(searchKey);
			element.submit();
			timerSearchResult = PerfectoUtils.ocrTextCheckAndGetUXTimer(driver, "testing", 99, 10);
//			(new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
//				public Boolean apply(WebDriver d) {
//					return d.getTitle().toLowerCase().startsWith(searchKey.toLowerCase());
//				}
//			});
			applitoolsHelper.checkWindow("Google- search Perfecto");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("Done: searchGoogle");
		System.out.println("########### ========>>>>>>>> Google timers: ");
		System.out.println("Google Home on: "+ driver.getCapabilities().getCapability("platformName")+ " time: " +timerHome);
		System.out.println("Google search result on: "+ driver.getCapabilities().getCapability("platformName")+ " time: " +timerSearchResult);
	}

	// Test Method, navigate to Geico and get insurance quote
	@Test
	public void geicoInsurance() throws MalformedURLException {

		long timerHome = 0,
			 timerProvideDetails= 0, 
			 timerGetQuote = 0;
		driver.get("http://www.geico.com");
		timerHome = PerfectoUtils.ocrTextCheckAndGetUXTimer(driver, "Homeowners", 99, 10);
		
		applitoolsHelper.checkWindow("Gaico");

		try{
			driver.findElement(By.id("auto")).click();
			driver.findElement(By.id("zip")).sendKeys("01434");
			driver.findElement(By.id("submitButton")).click();
			timerProvideDetails = PerfectoUtils.ocrTextCheckAndGetUXTimer(driver, "first", 99, 10);
			//driver.findElement(By.id("btnSubmit")).click();
			driver.findElement(By.xpath("//*[@id= 'CustomerForm:firstName']")).sendKeys("Dan");
			driver.findElement(By.xpath("//*[@id= 'CustomerForm:lastName']")).sendKeys("Kaligiery");
			driver.findElement(By.xpath("//*[@id= 'CustomerForm:customerMailingAddress']")).sendKeys("Woburn");

			driver.findElement(By.id("CustomerForm:birthMonth")).sendKeys("8");
			driver.findElement(By.id("CustomerForm:birthDay")).sendKeys("3");
			driver.findElement(By.id("CustomerForm:birthYear")).sendKeys("1981");

			driver.findElement(By.id("CustomerForm:continueBtn")).click();
			timerGetQuote = PerfectoUtils.ocrTextCheckAndGetUXTimer(driver, "model", 99, 10);
			applitoolsHelper.checkWindow("Geico get quote");

			//driver.findElement(By.id("btnSubmit"));
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("Done: geicoInsurance");
		System.out.println("########### ========>>>>>>>> Geico timers: ");
		System.out.println("Geico Home on: "+ driver.getCapabilities().getCapability("platformName")+ " time: " +timerHome);
		System.out.println("Geico get details on: "+ driver.getCapabilities().getCapability("platformName")+ " time: " +timerProvideDetails);
		System.out.println("Geico get Vehicle details on: "+ driver.getCapabilities().getCapability("platformName")+ " time: " +timerGetQuote);
		
		
		
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
