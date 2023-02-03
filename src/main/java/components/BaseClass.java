package components;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Random;

import org.testng.ITestResult;
import org.testng.annotations.*;

import com.graphQL.reports.extentReports;

/*
 * Usage :It is main class which handles all browser related and some common actions
 * 
 * Author : Venu Thota (venu.t@comakeit.com)
 */
public class BaseClass extends extentReports {

	protected String uri = "";
	protected String source_auth_token, x_auth_token;
	Properties config;
	FileInputStream fis;

	/*
	 * Usage : This method is to load data from application.properties files
	 * 
	 * Author : Venu Thota (venu.t@comakeit.com)
	 */
	@BeforeMethod
	public void loadProperties(Method testMethod) throws IOException {

		config = new Properties();
		String fpath = System.getProperty("user.dir") + "\\src\\test\\resources\\application.properties";
		fis = new FileInputStream(fpath);

		test = report.startTest(getTestCaseName(testMethod));

		config.load(fis);
		uri = config.getProperty("URI");
		source_auth_token = config.getProperty("source-auth-token");
		x_auth_token = config.getProperty("x-auth-token");

	}

	/*
	 * Usage : This method is create a 4 digit random number
	 * 
	 * Author : Venu Thota (venu.t@comakeit.com)
	 */
	public String get4DigitRandomNumber() {
		Random random = new Random();
		return String.format("%04d", random.nextInt(10000));
	}

	/*
	 * Usage : This method is to capture the test case name which will include in
	 * the HTML report
	 * 
	 * Author : Venu Thota (venu.t@comakeit.com)
	 */
	public String getTestCaseName(Method testMethod) {

		String name = testMethod.getDeclaringClass().getTypeName();
		String className = name.substring(name.lastIndexOf(".") + 1);

		return "<span style='color:#d64161;'>" + className + " : </span> " + "<span style='color:#4040a1;'>"
				+ testMethod.getName() + " : </span> ";

	}

	/*
	 * Usage : This method is to load the query data from the properties file
	 * 
	 * Author : Venu Thota (venu.t@comakeit.com)
	 */
	public String getRequestBody(String key) {

		config = new Properties();
		String fpath = System.getProperty("user.dir") + "\\src\\test\\java\\testData\\request.properties";
		try {
			fis = new FileInputStream(fpath);
			try {
				config.load(fis);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return config.getProperty(key);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	/*
	 * Usage : This method is to capture the test result
	 * 
	 * Author : Venu Thota (venu.t@comakeit.com)
	 */
	@AfterMethod
	public void getResult(ITestResult result) throws IOException {
		if (result.getStatus() == ITestResult.FAILURE) {
			stepInfo("Failed Due to below exception : ");
			failStep(result.getThrowable().toString());
		} else if (result.getStatus() == ITestResult.SUCCESS) {
			passStep("Testcase passed");
		}
	}

	/*
	 * Usage : To get the current timestamp
	 * 
	 * Author : Venu Thota (venu.t@comakeit.com)
	 */
	public String getTimestamp() {
		return new SimpleDateFormat("HHmmss").format(new Date());
	}

	public long getUnixTimestamp() {
		return System.currentTimeMillis() / 1000L;
		//return unixTime + "";
	}

	/*
	 * Usage : To get the random Lattitude
	 * 
	 * Author : Venu Thota (venu.t@comakeit.com)
	 */
	public static double getRandomLatitude() {
		double latitude = (Math.random() * 180.0) - 90.0;
		return (double) Math.round(latitude * 10000d) / 10000d;

	}

	/*
	 * Usage : To get the random Longitude
	 * 
	 * Author : Venu Thota (venu.t@comakeit.com)
	 */
	public static double getRandomLongitude() {
		double longitude = (Math.random() * 360.0) - 180.0;
		return (double) Math.round(longitude * 10000d) / 10000d;

	}

	/*
	 * Usage : To generate 5 digit random licence plate which is used purchase a
	 * space
	 * 
	 * Author : Venu Thota (venu.t@comakeit.com)
	 */
	public String getRandomLicencePlate() {
		String abc = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		Random random = new Random();
		char letter1 = abc.charAt(random.nextInt(abc.length()));
		char letter2 = abc.charAt(random.nextInt(abc.length()));
		return letter1 + "" + String.format("%03d", random.nextInt(1000)).concat(letter2 + "");
	}

	/*
	 * Usage : To get random location name from list of Locations names
	 * 
	 * Author : Venu Thota (venu.t@comakeit.com)
	 */
	public String getRandomLocation() {

		String[] locations = Constants.LOCATIONS;
		Random random = new Random();
		int index = random.nextInt(locations.length);

		return locations[index];
	}

	/*
	 * Usage : To get random location ids from list of Locations
	 * 
	 * Author : Venu Thota (venu.t@comakeit.com)
	 */
	public int getRandomLocation_Ids() {

		int[] locations = Constants.LOCATION_IDS;
		Random random = new Random();
		int index = random.nextInt(locations.length);

		return locations[index];
	}

}
