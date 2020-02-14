package com.automation.tms.web;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.automation.tms.ultils.Common;
import com.automation.tms.ultils.Constant;
import com.automation.tms.ultils.report.HtmlReporter;
import com.automation.tms.ultils.report.Log;
import com.automation.tms.ultils.report.TestngLogger;



public class WebDriverMethod extends WebDriverFactory {
	private static final String DEFAULT_ELEMENT_SEPARATOR = " --- ";
	public WebDriverMethod() throws Exception {
		super();
	}
	
	public WebDriverMethod(String browser, String platform) throws Exception {
		super(browser, platform);
	}

	public WebElement highlightElement(WebElement element) {
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		jse.executeScript("arguments[0].style.border='4px solid red'", element);
		return element;
	}

	/**
	 * This method is used to navigate the browser to the url
	 * 
	 * @author Automation team
	 * @param url the url of website
	 * @return None
	 * @throws Exception The exception is thrown if the driver can't navigate to the
	 *                   url
	 */
	public void openUrl(String url) throws Exception {

		try {
			driver.get(url);
			Log.info("Navigated to the url : [" + url + "]");
			TestngLogger.writeResult("Navigated to the url : [" + url + "]", true);
			HtmlReporter.pass("Navigated to the url : [" + url + "]", takeScreenshot());

		} catch (Exception e) {
			Log.error("Can't navigate to the url : [" + url + "]");
			Log.error(e.getMessage());
			TestngLogger.writeResult("Can't navigate to the url : [" + url + "]", false);
			TestngLogger.writeLog(e.getMessage());
			HtmlReporter.fail("Can't navigate to the url : [" + url + "]", e, takeScreenshot());
			throw (e);

		}
	}
	
	/**
	 * This method is used to capture a screenshot then write to the TestNG Logger
	 * 
	 * @author Automation team
	 * 
	 * @return A html tag that reference to the image, it's attached to the
	 *         report.html
	 * @throws Exception
	 */
	public String takeScreenshot() {

		String failureImageFileName = new SimpleDateFormat(Constant.TIME_STAMP_3)
				.format(new GregorianCalendar().getTime()) + "." + Constant.SCREENSHOT_FORMAT;
		try {
			if (driver != null) {
				String strImagePath = Common.correctPath(Common.strWebScreenshotFolder + failureImageFileName);
				File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

				FileUtils.copyFile(scrFile, new File(strImagePath));
				TestngLogger.writeLogWithScreenshot(strImagePath);
				return strImagePath;
			}
			return "";
		} catch (Exception e) {
			return "";
		}

	}
	
	/**
	 * This method is used to send keys into a text box without cleaning before.
	 * 
	 * @author Automation team
	 * @param elementName        The name of text box
	 * @param byWebElementObject The by object of text box element
	 * @param keysToSend         The keys are sent
	 * @throws Exception The exception is throws if sending keys not success
	 */
	public void sendkeys(String element, String keysToSend) throws Exception {
		String elementName = getElementName(element);
		try {

			findElement(element).sendKeys(keysToSend);
			Log.info("Keys [" + keysToSend + "] are sent to the element: [" + elementName + "]");
			TestngLogger.writeResult("Keys [" + keysToSend + "] are sent to the element: [" + elementName + "]", true);
			HtmlReporter.pass("Keys [" + keysToSend + "] are sent to the element: [" + elementName + "]",
					takeScreenshot());

		} catch (Exception e) {

			Log.error("Can't sendkeys to the element: [" + elementName + "]");
			Log.error(e.toString());
			TestngLogger.writeResult("Can't sendkeys to the element: [" + elementName + "]", false);
			TestngLogger.writeLog(e.toString());
			HtmlReporter.fail("Can't sendkeys to the element: [" + elementName + "]", e, takeScreenshot());
			throw (e);

		}
	}
	
	public String getElementName(String element) {
		return element.split(DEFAULT_ELEMENT_SEPARATOR)[1];
	}
	
	/**
	 * Get a web element object
	 * 
	 * @param by The By locator object of element
	 * @return The WebElement object
	 * @throws Exception
	 */
	public WebElement findElement(String elementInfo) throws Exception {
		WebElement element = null;
		String elementName = getElementName(elementInfo);
		String[] extract = getElemenLocator(elementInfo).split("=", 2);
		String by = extract[0];
		String value = extract[1];
		try {
			waitForPageLoad();
			if (by.equalsIgnoreCase("id")) {
				element = driver.findElement(By.id(value));
			} else if (by.equalsIgnoreCase("xpath")) {
				element = driver.findElement(By.xpath(value));
			} else if (by.equalsIgnoreCase("class")) {
				element = driver.findElement(By.className(value));
			} else if (by.equalsIgnoreCase("css")) {
				element = driver.findElement(By.cssSelector(value));
			} else if (by.equalsIgnoreCase("linkText")) {
				element = driver.findElement(By.linkText(value));
			} else if (by.equalsIgnoreCase("name")) {
				element = driver.findElement(By.name(value));
			} else if (by.equalsIgnoreCase("partialLinkText")) {
				element = driver.findElement(By.partialLinkText(value));
			} else if (by.equalsIgnoreCase("tag")) {
				element = driver.findElement(By.tagName(value));
			}
			highlightElement(element);
			// Log.info("The element : " + by + " is found.");
			// TestngLogger.writeLog("The element : " + by + " is found.");
		} catch (Exception e) {
			Log.error("The element : [" + elementName + "] located by : [" + getElemenLocator(elementInfo)
					+ "] isn't found. : " + e);
			TestngLogger.writeResult("The element : [" + elementName + "] located by : ["
					+ getElemenLocator(elementInfo) + "] isn't found. : " + e.toString(), false);
			HtmlReporter.fail("The element : [" + elementName + "] located by : [" + getElemenLocator(elementInfo)
					+ "] isn't found", e, takeScreenshot());
			throw (e);
		}
		return element;
	}
	
	public String getElemenLocator(String element) {
		return element.split(DEFAULT_ELEMENT_SEPARATOR)[0];
	}
	
	/**
	 * This method is used to wait for the page load
	 * 
	 * @author Automation team
	 * @param
	 * @return None
	 * @throws Exception
	 */

	public void waitForPageLoad() {

		WebDriverWait wait = new WebDriverWait(driver, Constant.DEFAULT_WAITTIME_SECONDS);

		// Wait for Javascript to load
		ExpectedCondition<Boolean> jsLoad = new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return ((JavascriptExecutor) driver).executeScript("return document.readyState").toString()
						.equals("complete");
			}
		};
		// JQuery Wait
		ExpectedCondition<Boolean> jQueryLoad = new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return (Long) ((JavascriptExecutor) driver).executeScript("return jQuery.active") == 0;
			}
		};

		// Angular Wait
		final String angularReadyScript = "return angular.element(document).injector().get('$http').pendingRequests.length";
		ExpectedCondition<Boolean> angularLoad = new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return (Long) ((JavascriptExecutor) driver).executeScript(angularReadyScript) == 0;
			}
		};

		wait.until(jsLoad);
		wait.until(jQueryLoad);
		// wait.until(angularLoad);
	}
}
