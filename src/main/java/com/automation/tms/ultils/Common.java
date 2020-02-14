package com.automation.tms.ultils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Properties;

public class Common {
	
	// Setting Path
	public static String strWorkspacepath = System.getProperty("user.dir");
	// dd-MM-yyyy
	public static String date = new SimpleDateFormat(Constant.TIME_STAMP_5)
				.format(new GregorianCalendar().getTime());
	public static String strWebReportFolder = correctPath(strWorkspacepath
			+ Constant.webReportFolder);
	// Daily Report Folder: ./reports/web/dd-MM-yyyy HHmmss
		public static String strWebDailyReportFolder = correctPath(strWebReportFolder + "\\" + date);
	// Daily Screenshot Folder: ./reports/web/dd-MM-yyyy HHmmss/Screenshot
	public static String strWebScreenshotFolder = correctPath(strWebDailyReportFolder
			+ "\\Screenshots\\");
	/**
	 * This method is used to read the configuration file
	 * 
	 * @author Automation team
	 * @param strKey
	 *            The key in the configuration file
	 * @return The value of key
	 * @throws Exception
	 */
	public static String getConfigValue(String strKey) throws Exception {

		Properties prop = new Properties();
		InputStream input = null;
		String configFile = correctPath(strWorkspacepath + Constant.configFilePath);
		
		input = new FileInputStream(configFile);
		prop.load(input);
		
		return prop.getProperty(strKey);
	}
	
	/**
	 * This method is used to get the project path
	 * 
	 * @author Automation team
	 * @return The absolute path of project
	 */
	public static String getProjectPath() {

		return strWorkspacepath;

	}
	
	/**
	 * Correct the file path based on the OS system type
	 * 
	 * @param path
	 *            the path to file
	 * @throws Exception
	 */
	public static String correctPath(String path){
		
		return path.replaceAll("\\\\|/", "\\" + System.getProperty("file.separator"));

	}
}
