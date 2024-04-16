package com.graphQL.reports;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.testng.annotations.*;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

/*
 * Usage :It is a main class which will handle start, end test and creates HTML report upon execution.
 * 
 * Author : Venu Thota (venu.t@comakeit.com)
 */
public class extentReports {

	protected static ExtentTest test;
	public static ExtentReports report;

	/*
	 * Usage :To Start test
	 * 
	 * Author : Venu Thota (venu.t@comakeit.com)
	 */
	@BeforeSuite
	public static void startTest() {

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		String d = dateFormat.format(date).toString();
		String timeStamp = new SimpleDateFormat("HHmmss").format(new Date());
		String reportName = "API_Automation_Result_" + timeStamp;

		report = new ExtentReports(
				System.getProperty("user.dir") + "//TestResults//" + d + "//" + reportName + ".html");
		// test =
		// report.startTest(Thread.currentThread().getStackTrace()[1].getMethodName().toString());

	}

	/*
	 * Usage :To end test
	 * 
	 * Author : Venu Thota (venu.t@comakeit.com)
	 */
	@AfterMethod
	public static void endTest() {
		report.endTest(test);
		report.flush();
	}

	/*
	 * Usage :To insert a step as pass in the HTML report
	 * 
	 * Author : Venu Thota (venu.t@comakeit.com)
	 */
	public void passStep(String stepinfo) {
		test.log(LogStatus.PASS, stepinfo);
		System.out.println(stepinfo);
	}

	/*
	 * Usage :To insert a step as fail in the HTML report
	 * 
	 * Author : Venu Thota (venu.t@comakeit.com)
	 */
	public void failStep(String stepinfo) {
		test.log(LogStatus.FAIL, stepinfo);
		System.out.println(stepinfo);
	}

	/*
	 * Usage :To insert a info step in the HTML report
	 * 
	 * Author : Venu Thota (venu.t@comakeit.com)
	 */
	public void stepInfo(String stepinfo) {
		test.log(LogStatus.INFO, "<b>" + stepinfo + "</b>");
		System.out.println("-------------"+stepinfo+"-------------");
	}
}
