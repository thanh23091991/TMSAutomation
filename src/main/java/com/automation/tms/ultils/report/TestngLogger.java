package com.automation.tms.ultils.report;

import org.testng.Reporter;

public class TestngLogger {
	/**
	 * This method is used to write test result into TestNGReport.html. If test
	 * failed, capture a screenshot.
	 * 
	 * @author Automation team
	 * @param Description The message is logged into report
	 * @param Status      Test result, True if test passed, False if test failed
	 * @throws Exception Throw Exception if writing result to report unsuccessfully.
	 */
	public static void writeResult(String description, Boolean Status) throws Exception {
		try {
			if (Status) {
				Reporter.log(description + "," + "<span style='color:green'> PASS </span>" + "<br>");
			} else {
				// String ScreenshotPathHtmlTag = Common.takeScreenshot();
				String ScreenshotPathHtmlTag = "";
				Reporter.log(description + "," + "<span style='color:red'> FAIL </span>" + ScreenshotPathHtmlTag);
			}
		} catch (Exception e) {
			throw new Exception("Have an error when writing result to the testng report!!!");
		}
	}
	
	/**
	 * This method is used to write a log message into the Testng report.
	 * 
	 * @author Automation team
	 * @param description The log message
	 * @throws Exception Throw Exception if writing log to report unsuccessfully.
	 */
	public static void writeLog(String description) throws Exception {
		try {
			Reporter.log("<b>" + description + "</b>" + "<br>");
		} catch (Exception e) {
			throw new Exception("Have an error when writing log to the testng report!!!");
		}
	}
	
	/**
	 * This method is used to write log into testng report with a screenshot
	 * 
	 * @author Automation team
	 * @param description The log message
	 * @throws Exception Throw Exception if writing log to report unsuccessfully.
	 */
	public static void writeLogWithScreenshot(String screenShotPath) throws Exception {
		try {
			String strHtmlTag = "<a href=\"file:///" + screenShotPath + "\"><img src=\"file:///" + screenShotPath
					+ "\" alt=\"\"" + "height='50' width='50'/> </a>" + "<br>";
			Reporter.log("<br>");
			Reporter.log("<font color='red'> Screenshot: </font>" + strHtmlTag);
		} catch (Exception e) {
		}
	}
}
