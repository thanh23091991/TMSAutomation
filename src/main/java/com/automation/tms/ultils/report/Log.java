package com.automation.tms.ultils.report;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.core.LoggerContext;

import com.automation.tms.ultils.Common;
import com.automation.tms.ultils.Constant;


public class Log {
	private static Logger getLogger() {
		String strConfigFilePath = Common.correctPath(Common.strWorkspacepath + Constant.configLogFilePath);
		File file = new File(strConfigFilePath);
		LoggerContext context = (LoggerContext) LogManager.getContext(false);
		context.setConfigLocation(file.toURI());
		Logger log = LogManager.getLogger();
		//get log by Thread
		//String logFileName = "log_"+ Thread.currentThread().getName() + "_" +Thread.currentThread().getId();
		
		//TuNN6
		// is it better if we logged by browser name and platform name??
		// is it better if we put logged output in difference folder by execute time
		// due to case that there're many test case in one time execution -> more ez to trace
		// due to case that execute multiple browser on multiple platform in one time execution
		// example: log/30-10-2018 21345/ log_chrome_win64bit , log_firefox_win1064bit
		String logFileName = "log_"+ Thread.currentThread().getName();
		//
		ThreadContext.put("ROUTINGKEY", logFileName);
		return log;
	}
	/**
	 * This method is used at the start of test case, help for developer can
	 * trace log easier.
	 * 
	 * @author Automation team
	 * @param sTestCaseName
	 *            The name of test case
	 */
	public static void startTestCase(String sTestCaseName) {

		getLogger().info("****************************************************************************************");

		getLogger().info("****************************************************************************************");

		getLogger().info("$$$$$$$$$$$$$$$$$$$$$                 " + sTestCaseName
				+ "       $$$$$$$$$$$$$$$$$$$$$$$$$");

		getLogger().info("****************************************************************************************");

		getLogger().info("****************************************************************************************");

	}

	/**
	 * This method is used at the end of test case, help for developer can trace
	 * log easier.
	 * 
	 * @author Automation team
	 * @param sTestCaseName
	 *            The name of test case
	 */
	public static void endTestCase(String sTestCaseName) {

		getLogger().info("XXXXXXXXXXXXXXXXXXXXXXX             " + "-E---N---D-"
				+ "             XXXXXXXXXXXXXXXXXXXXXX");

		getLogger().info("X");

		getLogger().info("X");

		getLogger().info("X");

		getLogger().info("X");

	}

	/**
	 * Writing information message to log file
	 * 
	 * @author Automation team
	 * @param info
	 *            The information meesage
	 */
	public static void info(String info) {

		getLogger().info(info);

	}

	/**
	 * Writing warning message to log file
	 * 
	 * @author Automation team
	 * @param warn
	 *            The warning message
	 */
	public static void warn(String warn) {

		getLogger().warn(warn);

	}

	/**
	 * Writing error message to log file
	 * 
	 * @author Automation team
	 * @param error
	 *            The error message
	 */
	public static void error(String error) {

		getLogger().error(error);

	}

	/**
	 * Writing fatal message to log file
	 * 
	 * @author Automation team
	 * @param fatal
	 *            The fatal message
	 */
	public static void fatal(String fatal) {

		getLogger().fatal(fatal);

	}

	/**
	 * Writing debug message to log file
	 * 
	 * @author Automation team
	 * @param debug
	 *            The debug message
	 */
	public static void debug(String debug) {

		getLogger().debug(debug);

	}
}
