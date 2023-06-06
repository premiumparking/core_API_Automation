package SPA;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import com.google.gson.Gson;
import com.graphQL.pojo.Account;
import com.graphQL.pojo.spa.CreateReservation;
import com.graphQL.pojo.spa.CreateSession;
import com.graphQL.pojo.spa.CreateSubscription;
import com.graphQL.pojo.spa.SignIn;

import components.BaseClass;
import components.Constants;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class Create_APIs extends BaseClass {

	Gson gson = new Gson();
	Sign_In sign_in = new Sign_In();
	Account account = new Account();
	String auth_token = null;
	long payment_cardId = 0l;

	private String spa_signIn = getRequestBody("spa_signIn");
	private String spa_signOut = getRequestBody("spa_signOut");
	private String spa_create_session = getRequestBody("spa_create_session");
	private String spa_create_reservation = getRequestBody("spa_create_reservation");
	private String spa_create_subscription = getRequestBody("spa_create_subscription");

	
	public void signIn() {

		SignIn signIn = gson.fromJson(spa_signIn, SignIn.class);

		// Setting the test data
		signIn.getVariables().setSource(Constants.WEB);
		signIn.getVariables().setGenerate_jwt_token(false);
		signIn.getVariables().setLogin(spa_userName);
		signIn.getVariables().setPassword(spa_Password);

		// String request_Payload = LoadJsonData.convertToJSON(parking);
		String request_Payload = gson.toJson(signIn);

		RestAssured.baseURI = uri;
		stepInfo("Request Payload");

		passStep(request_Payload);

		Response resp = given().log().all().contentType("application/json")
				.headers("source-auth-token", source_auth_token).body(request_Payload).when().log().all()
				.post("/graphql");
		stepInfo("Response Body");
		passStep(resp.asString());

		stepInfo("Response Validation");

		passStep("Received Status code : " + resp.getStatusCode());
		assertEquals(resp.getStatusCode(), 200);
		JsonPath j = new JsonPath(resp.asString());
		
		//System.out.println(resp.asPrettyString());

		auth_token = j.getString("data.login.auth_token");
		payment_cardId = j.getLong("data.login.payment_methods[0].entity_id");

		System.out.println("*** Authentication Token : " + x_auth_token);
		System.out.println("*** Payment Card ID : " + payment_cardId);
		System.out.println("===============================================");

	}

	
	 @Test
	public void SPA_TC_01_Purchase_Reservation() {
		
		signIn();
		for (int i = 1; i <= 2000; i++) {
			CreateReservation parking = gson.fromJson(spa_create_reservation, CreateReservation.class);

			// Setting the test data
			parking.getVariables().setStarts_at(geFutureUnixTimestamp(0));
			parking.getVariables().setExpires_at(geFutureUnixTimestamp(48));
			parking.getVariables().setSource(Constants.WEB);
			parking.getVariables().setParking_time_type(Constants.RESERVATION);
			parking.getVariables().setCard_id(payment_cardId);
			parking.getVariables().setLocation_id(getRandomLocation_Ids());
			parking.getVariables().getParking_lots().get(0).getVehicles().get(0)
					.setLicense_plate(getRandomLicencePlate());
			parking.getVariables().getParking_lots().get(0).getVehicles().get(0).setState(getRandomState());
			parking.getVariables().setSave_payment_method(true);

			// String request_Payload = LoadJsonData.convertToJSON(parking);
			String request_Payload = gson.toJson(parking);

			RestAssured.baseURI = uri;
			stepInfo("Request Payload");

			passStep(request_Payload);

			Response resp = given().contentType("application/json")
					.headers("source-auth-token", source_auth_token).header("x-auth-token", auth_token)
					.body(request_Payload).when().post("/graphql");
			stepInfo("Response Body");
			passStep(resp.asString());

			stepInfo("Response Validation");

			passStep("Received Status code : " + resp.getStatusCode());
			assertEquals(resp.getStatusCode(), 200);
			JsonPath j = new JsonPath(resp.asString());

			String order_id = j.getString("data.create_parking.order_number_id");
			String rateName = j.getString("data.create_parking.last_payment.rate_name");
			String oder_Total = j.getString("data.create_parking.order_total");
			String order_total_without_discount = j.getString("data.create_parking.order_total_without_discount");
			String discount_amount = j.getString("data.create_parking.discount_amount");

			System.out.println(i + "*** Reservation Order Number : " + order_id);
			passStep("Confirmation Order Number  : " + order_id);
			passStep("Rate Name  : " + rateName);
			passStep("Total amount  : " + oder_Total);
			passStep("Total without discount  : " + order_total_without_discount);
			passStep("Discount Amount  : " + discount_amount);

		}
	}

	@Test()
	public void SPA_TC_02_Purchase_Suscription() {
		signIn();
		for (int i = 1; i <= 5; i++) {
			CreateSubscription parking = gson.fromJson(spa_create_subscription, CreateSubscription.class);

			// Setting the test data
			parking.getVariables().setStarts_at(getCurrentUnixTimestamp());
			parking.getVariables().setParking_time_rate_id(57);
			parking.getVariables().setSource(Constants.WEB);
			parking.getVariables().setParking_time_type(Constants.SUBSCRIPTION);
			parking.getVariables().setCard_id(payment_cardId);
			parking.getVariables().setSave_payment_method(true);
			parking.getVariables().setLocation_id(getRandomLocation_Ids());
			parking.getVariables().getParking_lots().get(0).getVehicles().get(0)
					.setLicense_plate(getRandomLicencePlate());
			parking.getVariables().getParking_lots().get(0).getVehicles().get(0).setState(getRandomState());
			parking.getVariables().getParking_lots().get(0).getVehicles().get(1)
					.setLicense_plate(getRandomLicencePlate());
			parking.getVariables().getParking_lots().get(0).getVehicles().get(1).setState(getRandomState());
			parking.getVariables().getParking_lots().get(0).getVehicles().get(2)
					.setLicense_plate(getRandomLicencePlate());
			parking.getVariables().getParking_lots().get(0).getVehicles().get(2).setState(getRandomState());

			// String request_Payload = LoadJsonData.convertToJSON(parking);
			String request_Payload = gson.toJson(parking);

			RestAssured.baseURI = uri;
			stepInfo("Request Payload");

			passStep(request_Payload);

			Response resp = given().contentType("application/json")
					.headers("source-auth-token", source_auth_token).header("x-auth-token", auth_token)
					.body(request_Payload).when().post("/graphql");
			stepInfo("Response Body");
			passStep(resp.asString());

			stepInfo("Response Validation");

			passStep("Received Status code : " + resp.getStatusCode());
			assertEquals(resp.getStatusCode(), 200);
			JsonPath j = new JsonPath(resp.asString());

			String order_id = j.getString("data.create_parking.order_number_id");
			String rateName = j.getString("data.create_parking.last_payment.rate_name");
			String oder_Total = j.getString("data.create_parking.order_total");
			String order_total_without_discount = j.getString("data.create_parking.order_total_without_discount");
			String discount_amount = j.getString("data.create_parking.discount_amount");

			System.out.println(i + "*** Subscription Order Number : " + order_id);
			passStep("Confirmation Order Number  : " + order_id);
			passStep("Rate Name  : " + rateName);
			passStep("Total amount  : " + oder_Total);
			passStep("Total without discount  : " + order_total_without_discount);
			passStep("Discount Amount  : " + discount_amount);
		}
	}

	//@Test(dependsOnMethods = { "signIn" })
	
	@Test
	public void SPA_TC_03_Purchase_Session() {
		signIn();
		for (int i = 1; i <= 5; i++) {
			CreateSession parking = gson.fromJson(spa_create_session, CreateSession.class);

			// Setting the test data
			parking.getVariables().setMinutes(Constants._5HRS);
			parking.getVariables().setSource(Constants.WEB);
			parking.getVariables().setParking_time_type(Constants.SESSION);
			//parking.getVariables().setCard_id(payment_cardId);
			parking.getVariables().setCard_id(payment_cardId);
			parking.getVariables().setSave_payment_method(true);
			parking.getVariables().setLocation_id(getRandomLocation_Ids());
			parking.getVariables().getParking_lots().get(0).getVehicles().get(0)
					.setLicense_plate(getRandomLicencePlate());
			parking.getVariables().getParking_lots().get(0).getVehicles().get(0).setState(getRandomState());

			// String request_Payload = LoadJsonData.convertToJSON(parking);
			String request_Payload = gson.toJson(parking);

			RestAssured.baseURI = uri;
			stepInfo("Request Payload");

			passStep(request_Payload);

			Response resp = given().contentType("application/json")
					.headers("source-auth-token", source_auth_token).header("x-auth-token", auth_token)
					.body(request_Payload).when().post("/graphql");
			stepInfo("Response Body");
			passStep(resp.asString());

			stepInfo("Response Validation");

			passStep("Received Status code : " + resp.getStatusCode());
			assertEquals(resp.getStatusCode(), 200);
			JsonPath j = new JsonPath(resp.asString());

			String order_id = j.getString("data.create_parking.order_number_id");
			String rateName = j.getString("data.create_parking.last_payment.rate_name");
			String oder_Total = j.getString("data.create_parking.order_total");
			String order_total_without_discount = j.getString("data.create_parking.order_total_without_discount");
			String discount_amount = j.getString("data.create_parking.discount_amount");

			System.out.println(i + "*** Session Order Number : " + order_id);
			passStep("Confirmation Order Number  : " + order_id);
			passStep("Rate Name  : " + rateName);
			passStep("Total amount  : " + oder_Total);
			passStep("Total without discount  : " + order_total_without_discount);
			passStep("Discount Amount  : " + discount_amount);

		}
	}
	/*
	 * @AfterMethod public void SPA_LogOut() {
	 * 
	 * RestAssured.baseURI = uri; stepInfo("Request Payload");
	 * 
	 * passStep(spa_signOut);
	 * 
	 * Response resp = given().log().all().contentType("application/json")
	 * .headers("source-auth-token", source_auth_token).header("x-auth-token",
	 * auth_token) .body(spa_signOut).when().log().all().post("/graphql");
	 * stepInfo("Response Body"); passStep(resp.asString());
	 * 
	 * stepInfo("Response Validation");
	 * 
	 * passStep("Received Status code : " + resp.getStatusCode());
	 * 
	 * JsonPath j = new JsonPath(resp.asString());
	 * 
	 * 
	 * System.out.println("******* Order Number : " + resp.asString());
	 * 
	 * }
	 */
}
