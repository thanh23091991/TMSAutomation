package com.automation.tms.ultils;

import java.util.HashMap;

import com.automation.tms.web.WebDriverMethod;
import cucumber.api.Scenario;

public class ThreadController {
	public static ThreadLocal<String> browser = new ThreadLocal<String>();
	public static ThreadLocal<String> platform = new ThreadLocal<String>();
	public static ThreadLocal<WebDriverMethod> driver = new ThreadLocal<WebDriverMethod>();
	public static ThreadLocal<Scenario> scenario = new ThreadLocal<Scenario>();
	public static ThreadLocal<String> cucumberExtentFormatter = new ThreadLocal<String>();
	public static ThreadLocal<HashMap<String, String>> threadInfo = new ThreadLocal<HashMap<String, String>>();
	
	public static void setThreadState(String platform, String browserName) {
		Thread.currentThread().setName(platform + "_" + browserName);
        ThreadController.cucumberExtentFormatter.set("Y");
	}
}
