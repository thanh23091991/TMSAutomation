package com.automation.tms.web;

import java.io.File;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;

import com.automation.tms.ultils.Common;
import com.automation.tms.ultils.Constant;
import com.automation.tms.ultils.report.HtmlReporter;
import com.automation.tms.ultils.report.Log;
import com.automation.tms.ultils.report.TestngLogger;

public class WebDriverFactory {
	public static class BrowserType {
		public static final String IE = "IE";
		public static final String FIREFOX = "Firefox";
		public static final String CHROME = "Chrome";
		public static final String EDGE = "Edge";
		public static final String SAFARI = "Safari";
		public static final String REMOTE = "Remote";
	}
	
	protected WebDriver driver;
	
	public WebDriverFactory() throws Exception {
		setDriver();
	}

	public WebDriverFactory(String browserName, String platform) throws Exception {
		setDriver(browserName, platform);
	}
	/**
	 * This method is used to open a webdriver, it's used for selenium grid as well
	 * 
	 * @author Automation team
	 * @param None
	 * @return None
	 * @throws Exception The method throws an exeption when browser is invalid or
	 *                   can't start webdriver
	 */
	private void setDriver(String browser, String platform) throws Exception {
		String strRemoteFlag = Common.getConfigValue("RemoteFlag");
		String strNodeUrl = Common.getConfigValue("NodeURL");
		String strRemoteBrowser = Common.getConfigValue("RemoteBrowser");

		DesiredCapabilities capabilities = null;
		try {
			if (strRemoteFlag.equalsIgnoreCase("Y")) { 
				if (browser.equalsIgnoreCase(BrowserType.FIREFOX)) {
					capabilities = DesiredCapabilities.firefox();
					capabilities.setCapability("browserName", browser);
					capabilities.setCapability("platform", platform);
				} else if (browser.equalsIgnoreCase(BrowserType.CHROME)) {
					capabilities = DesiredCapabilities.chrome();
					capabilities.setCapability("browserName", browser);
					capabilities.setCapability("platform", platform);
				} else if (strRemoteBrowser.equalsIgnoreCase(BrowserType.IE)) {
					capabilities = DesiredCapabilities.internetExplorer();
					capabilities.setCapability("browserName", browser);
					capabilities.setCapability("platform", platform);
				} else if (strRemoteBrowser.equalsIgnoreCase(BrowserType.SAFARI)) {
					capabilities = DesiredCapabilities.safari();
					capabilities.setCapability("browserName", browser);
					capabilities.setCapability("platform", platform);
				} else if (strRemoteBrowser.equalsIgnoreCase(BrowserType.EDGE)) {
					capabilities = DesiredCapabilities.edge();
					capabilities.setCapability("browserName", browser);
					capabilities.setCapability("platform", platform);
				} else {
					throw new Exception("The given Browser is not available  OS: " + platform + " ,"
							+ " Remote Browser: " + browser);
				}
				
				driver = new RemoteWebDriver(new URL(strNodeUrl + "/wd/hub"), capabilities);
				Log.info("Starting remote webdriver for: OS: " + platform + " ," + " Remote Browser: " + browser);
				TestngLogger.writeResult(
						"Starting remote webdriver for: OS: " + platform + " ," + " Remote Browser: " + browser, true);
			} else {
				setExecutableDriver(browser);

				if (browser.equalsIgnoreCase(BrowserType.FIREFOX)) {

					FirefoxOptions options = new FirefoxOptions();
					options.addPreference("security.insecure_password.ui.enabled", false);
					options.addPreference("security.insecure_field_warning.contextual.enabled", false);
					driver = new FirefoxDriver(options);

				} else if (browser.equalsIgnoreCase(BrowserType.CHROME)) {
//					DesiredCapabilities caps = DesiredCapabilities.chrome();
//					HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
//					chromePrefs.put("profile.default_content_settings.popups", 0);
//					chromePrefs.put("download.prompt_for_download", "false");
//					chromePrefs.put("safebrowsing.enabled", "true");
//					ChromeOptions options = new ChromeOptions();
//					options.setExperimentalOption("prefs", chromePrefs);
//					options.addArguments("safebrowsing-disable-download-protection");
//					options.addArguments("--no-sandbox");
//					caps.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
//					caps.setCapability(ChromeOptions.CAPABILITY, options);
//					driver = new ChromeDriver(caps);
					DesiredCapabilities caps = DesiredCapabilities.chrome();

					ChromeDriverService service = new ChromeDriverService.Builder()
					                            .usingDriverExecutable(new File("/usr/local/chromedriver"))
					                            .usingAnyFreePort()
					                            .build();
					ChromeOptions options = new ChromeOptions();
					options.merge(caps);    
					driver = new ChromeDriver(service, options);

				} else if (browser.equalsIgnoreCase(BrowserType.IE)) {

					capabilities = DesiredCapabilities.internetExplorer();
					capabilities.setCapability(InternetExplorerDriver.IGNORE_ZOOM_SETTING, true);
					driver = new InternetExplorerDriver(capabilities);

				} else if (browser.equalsIgnoreCase(BrowserType.SAFARI)) {

					driver = new SafariDriver();

				} else if (browser.equalsIgnoreCase(BrowserType.EDGE)) {

					EdgeOptions options = new EdgeOptions();
					options.setPageLoadStrategy("eager");
					driver = new EdgeDriver(options);

				} else {
					throw new Exception("The given local browser is not available: " + browser);
				}

				Log.info("Starting Webdriver, Browser: " + browser);
				TestngLogger.writeResult("Starting Webdriver, Browser: " + browser, true);
			}	
		} catch (Exception e) {
			Log.error("Can't start the webdriver for " + browser + "\n" + e);
			TestngLogger.writeResult("Can't starting Webdriver!!! : " + browser + "\n" + e, false);
			throw (e);
		}
		driver.manage().timeouts().implicitlyWait(Constant.IMPLICIT_WAIT_TIME, TimeUnit.SECONDS);
		setBrowserSize();
	}
	
	/**
	 * This method is used to open a webdriver, it's used for selenium grid as well
	 * 
	 * @author Automation team
	 * @param None
	 * @return None
	 * @throws Exception The method throws an exeption when browser is invalid or
	 *                   can't start webdriver
	 */
	private void setDriver() throws Exception {

		String strRemoteFlag = Common.getConfigValue("RemoteFlag");
		String strNodeUrl = Common.getConfigValue("NodeURL");
		String strRemoteBrowser = Common.getConfigValue("RemoteBrowser");
		String strRemoteBrowserVersion = Common.getConfigValue("RemoteBrowserVersion");
		String strRemoteOS = Common.getConfigValue("RemoteOS");

		String strLocalBrowser = Common.getConfigValue("LocalBrowser");

		DesiredCapabilities capabilities = null;

		try {

			if (strRemoteFlag.equalsIgnoreCase("Y")) {

				if (strRemoteBrowser.equalsIgnoreCase(BrowserType.FIREFOX)) {
					capabilities = DesiredCapabilities.firefox();
					capabilities.setCapability("version", strRemoteBrowserVersion);
					if (strRemoteOS.equalsIgnoreCase("VISTA")) {
						capabilities.setCapability("platform", Platform.VISTA);
					}
				} else if (strRemoteBrowser.equalsIgnoreCase(BrowserType.CHROME)) {
					capabilities = DesiredCapabilities.chrome();
					capabilities.setCapability("version", strRemoteBrowserVersion);
					if (strRemoteOS.equalsIgnoreCase("LINUX")) {
						capabilities.setCapability("platform", Platform.LINUX);
					}
				} else if (strRemoteBrowser.equalsIgnoreCase(BrowserType.IE)) {
					capabilities = DesiredCapabilities.internetExplorer();
					capabilities.setCapability("version", strRemoteBrowserVersion);
					if (strRemoteOS.equalsIgnoreCase("WINDOWS")) {
						capabilities.setCapability("platform", Platform.WINDOWS);
					}
				} else {
					throw new Exception(
							"The given Browser is not available  OS: " + strRemoteOS + " ," + " Remote Browser: "
									+ strRemoteBrowser + ", Remote Browser Version: " + strRemoteBrowserVersion);
				}

				driver = new RemoteWebDriver(new URL(strNodeUrl + "/wd/hub"), capabilities);
				Log.info("Starting remote webdriver for: OS: " + strRemoteOS + " ," + " Remote Browser: "
						+ strRemoteBrowser + ", Remote Browser Version: " + strRemoteBrowserVersion);
				TestngLogger
						.writeResult(
								"Starting remote webdriver for: OS: " + strRemoteOS + " ," + " Remote Browser: "
										+ strRemoteBrowser + ", Remote Browser Version: " + strRemoteBrowserVersion,
								true);

			} else {

				setExecutableDriver();

				if (strLocalBrowser.equalsIgnoreCase(BrowserType.FIREFOX)) {

					FirefoxOptions options = new FirefoxOptions();
					options.addPreference("security.insecure_password.ui.enabled", false);
					options.addPreference("security.insecure_field_warning.contextual.enabled", false);
					driver = new FirefoxDriver(options);

				} else if (strLocalBrowser.equalsIgnoreCase(BrowserType.CHROME)) {

//					DesiredCapabilities caps = DesiredCapabilities.chrome();
//					HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
//					chromePrefs.put("profile.default_content_settings.popups", 0);
//					chromePrefs.put("download.prompt_for_download", "false");
//					chromePrefs.put("safebrowsing.enabled", "true");
//					ChromeOptions options = new ChromeOptions();
//					options.setExperimentalOption("prefs", chromePrefs);
//					options.addArguments("safebrowsing-disable-download-protection");
//					caps.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
//					caps.setCapability(ChromeOptions.CAPABILITY, options);
//					driver = new ChromeDriver(caps);
					DesiredCapabilities caps = DesiredCapabilities.chrome();

					ChromeDriverService service = new ChromeDriverService.Builder()
					                            .usingDriverExecutable(new File("/usr/local/chromedriver"))
					                            .usingAnyFreePort()
					                            .build();
					ChromeOptions options = new ChromeOptions();
					options.merge(caps);    

				} else if (strLocalBrowser.equalsIgnoreCase(BrowserType.IE)) {

					capabilities = DesiredCapabilities.internetExplorer();
					capabilities.setCapability(InternetExplorerDriver.IGNORE_ZOOM_SETTING, true);
					driver = new InternetExplorerDriver(capabilities);

				} else if (strLocalBrowser.equalsIgnoreCase(BrowserType.SAFARI)) {

					driver = new SafariDriver();

				} else if (strLocalBrowser.equalsIgnoreCase(BrowserType.EDGE)) {

					EdgeOptions options = new EdgeOptions();
					options.setPageLoadStrategy("eager");
					driver = new EdgeDriver(options);

				} else {
					throw new Exception("The given local browser is not available: " + strLocalBrowser);
				}

				Log.info("Starting Webdriver, Browser: " + strLocalBrowser);
				TestngLogger.writeResult("Starting Webdriver, Browser: " + strLocalBrowser, true);
			}

		} catch (Exception e) {

			Log.error("Can't start the webdriver!!! : " + e);
			TestngLogger.writeResult("Can't starting Webdriver!!! : " + e, false);
			throw (e);

		}
		driver.manage().timeouts().implicitlyWait(Constant.IMPLICIT_WAIT_TIME, TimeUnit.SECONDS);
		setBrowserSize();

	}

	
	/**
	 * To configure the executable path corresponding to the environment
	 * 
	 * @throws Exception
	 */
	public void setExecutableDriver(String browserName) throws Exception {

		String strLocalOS = System.getProperty("os.name");
		String strDriverPath = "";
		
		// Linux
		if (strLocalOS.contains("Linux")) {

			strDriverPath = Common.correctPath(Common.getProjectPath() + Constant.driverFolder + "//Linux//");

			if (browserName.equalsIgnoreCase(BrowserType.CHROME)) {

				File file = new File(Common.correctPath(strDriverPath + "chromedriver"));
				System.setProperty("webdriver.chrome.driver", file.getAbsolutePath());

			} else if (browserName.equalsIgnoreCase(BrowserType.FIREFOX)) {

				File file = new File(Common.correctPath(strDriverPath + "geckodriver"));
				System.setProperty("webdriver.gecko.driver", file.getAbsolutePath());

			} else {
				throw new Exception("Linux doesn't support this browser [" + browserName + "]");
			}
		}
		// Windows
		else if (strLocalOS.contains("Windows")) {

			strDriverPath = Common.correctPath(Common.getProjectPath() + Constant.driverFolder + "//Windows//");

			if (browserName.equalsIgnoreCase(BrowserType.CHROME)) {

				File file = new File(Common.correctPath(strDriverPath + "chromedriver.exe"));
				System.setProperty("webdriver.chrome.driver",  file.getAbsolutePath());

			} else if (browserName.equalsIgnoreCase(BrowserType.FIREFOX)) {

				File file = new File(Common.correctPath(strDriverPath + "geckodriver.exe"));
				System.setProperty("webdriver.gecko.driver", file.getAbsolutePath());

			} else if (browserName.equalsIgnoreCase(BrowserType.IE)) {

				File file = new File(Common.correctPath(strDriverPath + "IEDriverServer.exe"));
				System.setProperty("webdriver.ie.driver", file.getAbsolutePath());

			} else if (browserName.equalsIgnoreCase(BrowserType.EDGE)) {

				File file = new File(Common.correctPath(strDriverPath + "MicrosoftWebDriver.exe"));
				System.setProperty("webdriver.edge.driver", file.getAbsolutePath());

			} else {
				throw new Exception("Windows doesn't support this browser [" + browserName + "]");
			}
		}
		// MAC OS
		else if (strLocalOS.contains("mac")) {

			strDriverPath = Common.correctPath(Common.getProjectPath() + Constant.driverFolder + "//Mac//");

			if (strLocalOS.equalsIgnoreCase(BrowserType.SAFARI)) {

				System.setProperty("webdriver.safari.noinstall", "true");
				SafariOptions options = new SafariOptions();
				options.setUseTechnologyPreview(true);
				driver = new SafariDriver(options);

			} else {
				throw new Exception("MAC doesn't support this browser [" + browserName + "]");
			}
		}
		// Others
		else {
			throw new Exception("Selenium doesn't support this OS [" + strLocalOS + "]");
		}
	}
	
	/**
	 * To configure the executable path corresponding to the environment
	 * 
	 * @throws Exception
	 */
	public void setExecutableDriver() throws Exception {

		String strLocalOS = System.getProperty("os.name");
		String strLocalBrowser = Common.getConfigValue("LocalBrowser");
		String strDriverPath = "";

		// Linux
		if (strLocalOS.contains("Linux")) {

			strDriverPath = Common.correctPath(Common.getProjectPath() + Constant.driverFolder + "//Linux//");

			if (strLocalBrowser.equalsIgnoreCase(BrowserType.CHROME)) {

				File file = new File(Common.correctPath(strDriverPath + "chromedriver"));
				System.setProperty("webdriver.chrome.driver", file.getAbsolutePath());

			} else if (strLocalBrowser.equalsIgnoreCase(BrowserType.FIREFOX)) {

				File file = new File(Common.correctPath(strDriverPath + "geckodriver"));
				System.setProperty("webdriver.gecko.driver", file.getAbsolutePath());

			} else {
				throw new Exception("Linux doesn't support this browser [" + strLocalBrowser + "]");
			}
		}
		// Windows
		else if (strLocalOS.contains("Windows")) {

			strDriverPath = Common.correctPath(Common.getProjectPath() + Constant.driverFolder + "//Windows//");

			if (strLocalBrowser.equalsIgnoreCase(BrowserType.CHROME)) {

				File file = new File(Common.correctPath(strDriverPath + "chromedriver.exe"));
				System.setProperty("webdriver.chrome.driver", file.getAbsolutePath());

			} else if (strLocalBrowser.equalsIgnoreCase(BrowserType.FIREFOX)) {

				File file = new File(Common.correctPath(strDriverPath + "geckodriver.exe"));
				System.setProperty("webdriver.gecko.driver", file.getAbsolutePath());

			} else if (strLocalBrowser.equalsIgnoreCase(BrowserType.IE)) {

				File file = new File(Common.correctPath(strDriverPath + "IEDriverServer.exe"));
				System.setProperty("webdriver.ie.driver", file.getAbsolutePath());

			} else if (strLocalBrowser.equalsIgnoreCase(BrowserType.EDGE)) {

				File file = new File(Common.correctPath(strDriverPath + "MicrosoftWebDriver.exe"));
				System.setProperty("webdriver.edge.driver", file.getAbsolutePath());

			} else {
				throw new Exception("Windows doesn't support this browser [" + strLocalBrowser + "]");
			}
		}
		// MAC OS
		else if (strLocalOS.contains("mac")) {

			strDriverPath = Common.correctPath(Common.getProjectPath() + Constant.driverFolder + "//Mac//");

			if (strLocalOS.equalsIgnoreCase(BrowserType.SAFARI)) {

				System.setProperty("webdriver.safari.noinstall", "true");
				SafariOptions options = new SafariOptions();
				options.setUseTechnologyPreview(true);
				driver = new SafariDriver(options);

			} else {
				throw new Exception("MAC doesn't support this browser [" + strLocalBrowser + "]");
			}
		}
		// Others
		else {
			throw new Exception("Selenium doesn't support this OS [" + strLocalOS + "]");
		}
	}
	
	/**
	 * To set the Browser size depending on Testing Scope
	 */
	public void setBrowserSize() {
		try {
			String strLocalWidth = Common.getConfigValue("LocalWidth");
			String strLocalHeight = Common.getConfigValue("LocalHeight");

			// If we configured for the height and width
			if (!strLocalWidth.equalsIgnoreCase("") && !strLocalHeight.equalsIgnoreCase("")) {
				int width = Integer.parseInt(strLocalHeight);
				int height = Integer.parseInt(strLocalHeight);
				Dimension size = new Dimension(width, height);
				driver.manage().window().setSize(size);
			} else {
				driver.manage().window().maximize();
			}
		} catch (Exception e) {
		}
	}
	/**
	 * To get driver instance
	 * 
	 * @return
	 */
	public WebDriver getDriver() {
		return this.driver;
	}

	/**
	 * This method is used to close a webdriver
	 * 
	 * @author Automation team
	 * @param None
	 * @return None
	 * @throws Exception The exception is thrown when can't close the webdriver.
	 */
	public void closeDriver() throws Exception {

		try {

			if (driver != null) {
				driver.quit();
			}
		} catch (Exception e) {

			Log.error("The webdriver is not closed!!! " + e.getMessage());
			TestngLogger.writeResult("The webdriver is not closed!!! " + e.getMessage(), false);
			HtmlReporter.getTest().fail("The webdriver is not closed!!!").fail(e);
			throw (e);

		}
	}

	/**
	 * Get Browser Type
	 * 
	 * @return Browser Type
	 */
	public String getBrowserType() {

		String strBrowserType = "";

		if (driver instanceof InternetExplorerDriver) {
			strBrowserType = BrowserType.IE;
		} else if (driver instanceof FirefoxDriver) {
			strBrowserType = BrowserType.FIREFOX;
		} else if (driver instanceof ChromeDriver) {
			strBrowserType = BrowserType.CHROME;
		} else if (driver instanceof EdgeDriver) {
			strBrowserType = BrowserType.EDGE;
		} else if (driver instanceof SafariDriver) {
			strBrowserType = BrowserType.SAFARI;
		} else if (driver instanceof RemoteWebDriver) {
			strBrowserType = BrowserType.REMOTE;
		} else {
			strBrowserType = "";
		}

		return strBrowserType;
	}

}
