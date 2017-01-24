package testNG;

import org.openqa.selenium.remote.RemoteWebDriver;

import com.applitools.eyes.BatchInfo;
import com.applitools.eyes.Eyes;
import com.applitools.eyes.TestFailedException;
import com.applitools.eyes.TestResults;

public final class ApplitoolsHelper {
	private Eyes eyes;
	private static BatchInfo batch;

	public static void setBatch(String batchName) {
		//Added support for Jenkins plugin, if running from Jenkins. 
		String jenkinsBatchId = System.getenv("APPLITOOLS_BATCH_ID");
		String key = System.getenv().get("APPLITOOLS_API_KEY");

		if (null != jenkinsBatchId)
			batch.setId(jenkinsBatchId);
		else
			if (null != key) 
				batch = new BatchInfo(batchName);
		
	}

	public static BatchInfo getBatchInfo() {
		return batch;
	}

	public ApplitoolsHelper(RemoteWebDriver driver, String appName, String testName) {
		String key = System.getenv().get("APPLITOOLS_API_KEY");
		if (null != key) {

			eyes = new Eyes();
			// This is your api key, make sure you use it in all your tests.
			eyes.setApiKey(key);

			// Start visual testing with browser viewport set to 1024x768.
			// Make sure to use the returned driver from this point on.
			if (!eyes.getIsOpen()) {
				//it is recommended to set the batch before opening eyes.
				eyes.setBatch(batch);  
				eyes.open(driver, appName, testName);
			}
		}

	}

	public String close() {
		if (null != eyes)
			try {
				//eyes.close() will throw an exception if there are differences between the 
				//current and the baseline. eyes.close(false); will enable the code to continue to the next line.
				TestResults tr = eyes.close(false);
				return tr.getUrl();
			} catch (TestFailedException e) {
				String s = e.getTestResults().getUrl();
				return s;
			} finally {
				eyes.abortIfNotClosed();
			}
		return null;
	}

	public void checkWindow(String tag) {
		if (null != eyes && eyes.getIsOpen())
			eyes.checkWindow(tag);
	}
}
