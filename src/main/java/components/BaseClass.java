package components;

import static io.restassured.RestAssured.given;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import org.testng.ITestResult;
import org.testng.annotations.*;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.graphQL.reports.extentReports;

import io.restassured.response.Response;

/*
 * Usage :It is main class which handles all browser related and some common actions
 * 
 * Author : Venu Thota (venu.t@comakeit.com)
 */
public class BaseClass extends extentReports {

	protected String env, uri = "";
	protected String source_auth_token, x_auth_token;
	protected String spa_x_auth_token, spa_userName, spa_Password;
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
		String fpath = System.getProperty("user.dir") + "//src//test//resources//application.properties";
		fis = new FileInputStream(fpath);

		test = report.startTest(getTestCaseName(testMethod));

		config.load(fis);
		env = config.getProperty("env");
		uri = config.getProperty(env + "_uri");
		source_auth_token = config.getProperty("source-auth-token");
		x_auth_token = config.getProperty("x-auth-token");
		spa_x_auth_token = config.getProperty("spa_x_auth_token");
		spa_userName = config.getProperty("spa_username");
		spa_Password = config.getProperty("spa_password");

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
				+ testMethod.getName() + " :- </span> ";

	}

	/*
	 * Usage : This method is to load the query data from the properties file
	 * 
	 * Author : Venu Thota (venu.t@comakeit.com)
	 */
	public String getRequestBody(String key) {

		config = new Properties();
		String fpath = System.getProperty("user.dir") + "//src//test//java//testData//request.properties";
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

	public long getCurrentUnixTimestamp() {
		return System.currentTimeMillis() / 1000L;
		// return unixTime + "";
	}

	public long geFutureUnixTimestamp(int hours) {

		long ft = System.currentTimeMillis() + hours * 60 * 60 * 1000;

		return ft / 1000L;

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

	/*
	 * Usage : To get random States from list of States
	 * 
	 * Author : Venu Thota (venu.t@comakeit.com)
	 */
	public String getRandomState() {
		return Constants.STATES[new Random().nextInt(Constants.STATES.length)];
	}

	/*
	 * Usage : To get random US Phone number from list of US Phone numbers
	 *
	 * Author : Venu Thota (venu.thota@xebia.com)
	 */
	public String getRandomUSPhoneNumber() {

		String[] us_ph_numbers = Constants.US_PHONE_NUMBERS;
		Random random = new Random();
		int index = random.nextInt(us_ph_numbers.length);

		return us_ph_numbers[index].replace("+", "");
	}

	/*
	 * Usage : To generate random Vehicle make from list of Makes
	 *
	 * Author : Venu Thota (venu.thota@xebia.com)
	 */
	public String getRandom_Vehicle_Make() {
		String[] vehicle_makes = Constants.VEHICLE_MAKE;
		Random random = new Random();
		int index = random.nextInt(vehicle_makes.length);

		return vehicle_makes[index];
	}

	/*
	 * Usage : To generate random Vehicle color from list of Colors
	 *
	 * Author : Venu Thota (venu.thota@xebia.com)
	 */
	public String getRandom_Vehicle_Color() {
		String[] vehicle_colors = Constants.VEHICLE_COLOR;
		Random random = new Random();
		int index = random.nextInt(vehicle_colors.length);

		return vehicle_colors[index];
	}

	/*
	 * Usage : To generate random Vehicle Type from list of Types
	 *
	 * Author : Venu Thota (venu.thota@xebia.com)
	 */
	public String getRandom_Vehicle_Type() {
		String[] vehicle_types = Constants.VEHICLE_TYPE;
		Random random = new Random();
		int index = random.nextInt(vehicle_types.length);

		return vehicle_types[index];
	}

	public Response hit_GraphQL_EndPoint(String request_Payload, String auth_token) {
		RestAssured.baseURI = uri;

		stepInfo("API URI");
		passStep(uri);

		stepInfo("Request Payload");
		passStep(request_Payload);

		Response resp = null;
		if (auth_token == null) {
			resp = given().log().all().contentType("application/json").headers("source-auth-token", source_auth_token)
					.body(request_Payload).when().log().all().post("/graphql");
		} else {
			resp = given().log().all().contentType("application/json").headers("source-auth-token", source_auth_token)
					.headers("X-Auth-Token", auth_token).body(request_Payload).when().log().all().post("/graphql");
		}
		stepInfo("Response Body");
		passStep(resp.asPrettyString());
		return resp;
	}

	public static String getStripeToken() {
		String baseUrl = "https://api.stripe.com/v1/tokens";

		// Create the payload
		Map<String, String> payload = new HashMap<>();
		payload.put("card[number]", "4242424242424242");
		payload.put("card[exp_month]", "12");
		payload.put("card[exp_year]", "35");
		payload.put("card[cvc]", "902");
		payload.put("card[address_zip]", "502319");

		// Send the POST request and return the response
		RestAssured.baseURI = baseUrl;
		Response response = given().contentType("application/x-www-form-urlencoded")
				.header("Authorization", "Bearer pk_test_sIFJLPzcwOqaBTSHOix8RGHX").formParams(payload).post();

		// Check if the request was successful
		if (response.getStatusCode() == 200) {
			// Extract token from response
			String token = response.jsonPath().getString("id");
			System.out.println("Token: " + token);
			return token;
		} else {
			System.err.println("Error: " + response.getStatusCode() + " - " + response.getStatusLine());
			return null;
		}
	}

}
