package com.automation.tms.ultils.report;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;

import com.automation.tms.ultils.ThreadController;
import com.aventstack.extentreports.AnalysisStrategy;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class HtmlReporter {
	private static ExtentReports _report;

	private static HashMap<String, ExtentTest> extentTestMap = new HashMap<String, ExtentTest>();

	public static ExtentReports getReporter(String filename) throws UnknownHostException {
		if (_report == null)
			_report = createInstance(filename);

		_report.setSystemInfo("Application", "Data Driven Framework");
		_report.setSystemInfo("Os Name", System.getProperty("os.name"));
		_report.setSystemInfo("Os Version", System.getProperty("os.version"));
		_report.setSystemInfo("Os Architecture", System.getProperty("os.arch"));
		_report.setSystemInfo("Host", InetAddress.getLocalHost().getHostName());
		_report.setSystemInfo("Username", System.getProperty("user.name"));

		// Tests view
		_report.setAnalysisStrategy(AnalysisStrategy.CLASS);

		return _report;

	}
	
	/**
	 * To create an reporter instance
	 * 
	 * @param fileName
	 *            The report's name
	 * @return
	 */
	public static ExtentReports createInstance(String fileName) {
		File reportsDir = new File(fileName);
    	if (!reportsDir.exists()) {
			reportsDir.getParentFile().mkdirs();
		}
		ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(reportsDir);
		// String configFile = Common.correctPath(Common.strWorkspacepath +
		// "/src/main/resource/config/extent-config.xml");
		// htmlReporter.loadXMLConfig(configFile);
		htmlReporter.config().setDocumentTitle("Report");
		htmlReporter.config().setTestViewChartLocation(ChartLocation.BOTTOM);
		htmlReporter.config().setChartVisibilityOnOpen(false);
		htmlReporter.config().setTheme(Theme.STANDARD);
		//htmlReporter.config().setDocumentTitle(fileName);
		htmlReporter.config().setEncoding("utf-8");
		htmlReporter.config().setReportName("Report");
		htmlReporter.setAppendExisting(false);

		ExtentReports report = new ExtentReports();
		report.attachReporter(htmlReporter);

		return report;
	}
	
	/**
	 * To get the ExtentTest session to write report
	 * 
	 * @return ExtentTest session
	 */
	public static synchronized ExtentTest getTest() {
		if (getNode() == null) {
			return getParent();
		} else {
			return getNode();
		}
	}
	
	/**
	 * To get the ExtentTest's node session to write report
	 * 
	 * @return ExtentTest session
	 */
	public static synchronized ExtentTest getNode() {
		return extentTestMap.get("node_" + Thread.currentThread().getId());
	}
	
	/**
	 * To get the ExtentTest's parent session to write report
	 * 
	 * @return ExtentTest session
	 */

	public static synchronized ExtentTest getParent() {
		return extentTestMap.get("test_" + Thread.currentThread().getId());
	}
	
	/**
	 * To write a passed step to report with screenshot
	 * 
	 * @param strDescription
	 *            The Step's description
	 * @param strScreenshotPath
	 *            The screenshot's path
	 * @throws IOException
	 *             If the screenshot doesn't exist
	 */
	public static void pass(String strDescription, String strScreenshotPath) {

		try {
			if (strDescription.equalsIgnoreCase("")) {
				getTest().pass(strDescription);
			} else {

				if (ThreadController.cucumberExtentFormatter.get() != null && 
						ThreadController.cucumberExtentFormatter.get().equalsIgnoreCase("Y")) {
					ThreadController.scenario.get().write("pass;" + strDescription + ";" + strScreenshotPath);
				} else {
					String[] paths = strScreenshotPath.split("\\u005c");
					strScreenshotPath = "./Screenshots/" + paths[paths.length-1];
					getTest().pass(strDescription).addScreenCaptureFromPath(strScreenshotPath);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.info("Can't write to html report, initialize it first");
		}
	}
	
	/**
	 * To write a failed step to report with screenshot and throwable stacktrace
	 * 
	 * @param strDescription
	 *            The Step's description
	 * @param e
	 *            Throwable object
	 * @param strScreenshotPath
	 *            The screenshot's path
	 * @throws IOException
	 *             If the screenshot doesn't exist
	 */
	public static void fail(String strDescription, Throwable e, String strScreenshotPath) {

		try {
			if (strScreenshotPath.equalsIgnoreCase("")) {
				getTest().fail(strDescription).fail(e);
			} else {

				if (ThreadController.cucumberExtentFormatter.get() != null && 
						ThreadController.cucumberExtentFormatter.get().equalsIgnoreCase("Y")) {
					ThreadController.scenario.get()
							.write("fail;" + strDescription + ";" + throwableToString(e) + ";" + strScreenshotPath);
				} else {
					String[] paths = strScreenshotPath.split("\\u005c");
					strScreenshotPath = "./Screenshots/" + paths[paths.length-1];
					getTest().fail(strDescription).fail(e).addScreenCaptureFromPath(strScreenshotPath);
				}
			}
		} catch (Exception ex) {
			Log.info("Can't write to html report, initialize it first");
		}

	}
	
	public static String throwableToString(Throwable e) throws Exception {
		try (StringWriter sw = new StringWriter(); PrintWriter pw = new PrintWriter(sw);) {
			e.printStackTrace(pw);

			return sw.toString();
		}
	}
}
