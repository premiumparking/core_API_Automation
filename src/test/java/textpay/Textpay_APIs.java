package textpay;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.Test;

import com.google.gson.Gson;
import com.graphQL.pojo.textpay.CreateParkingVariable;
import com.graphQL.pojo.textpay.ExtendParkingVariable;
import com.graphQL.pojo.textpay.LocationPageDataVariable;
import com.graphQL.pojo.textpay.LoginInfoVariable;
import com.graphQL.pojo.textpay.ParkingCostVariable;
import com.graphQL.pojo.textpay.ParkingLotsVariable;
import com.graphQL.pojo.textpay.Query;
import com.graphQL.pojo.textpay.SendPhoneVerificationCodeVariable;
import com.graphQL.pojo.textpay.Vehicles;
import com.graphQL.pojo.textpay.VerifyPhoneNumberVariable;
import com.graphQL.pojo.textpay.hotel.LocationRatesVariable;

import components.BaseClass;
import components.Constants;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class Textpay_APIs extends BaseClass {

	Gson gson = new Gson();

	String auth_token, location_id, profile_id, vehicle_id, rate_id, minutes, card_id, order_number_id = null;

	private String getLoginInfo = getRequestBody("getLoginInfo");
	private String sendPhoneVerificationCode = getRequestBody("sendPhoneVerificationCode");
	private String verifyPhoneNumber = getRequestBody("verifyPhoneNumber");
	private String currentProfile = getRequestBody("currentProfile");
	private String getLocationPageData = getRequestBody("getLocationPageData");
	private String personalVehicles = getRequestBody("personalVehicles");
	private String getLocationRates = getRequestBody("getLocationRates");
	private String getStates = getRequestBody("getStates");
	private String getParkingCostByRate = getRequestBody("getParkingCostByRate");
	private String getParkingCostByDuration = getRequestBody("getParkingCostByDuration");
	private String personalDataForCheckout = getRequestBody("personalDataForCheckout");
	private String createParking = getRequestBody("createParking");
	private String extendParking = getRequestBody("extendParking");

	String phone_number = getRandomUSPhoneNumber();
	String location_name = getRandomLocation();

	Response response = null;

	@Test(groups = "smoke", priority = 1)
	public void TC_01_GetLoginInfo() {
		System.out.println("================= TC_01_GetLoginInfo =================");

		Query query = gson.fromJson(getLoginInfo, Query.class);
		LoginInfoVariable loginInfoVariable = new LoginInfoVariable();
		loginInfoVariable.setLogin(phone_number);
		query.setVariables(loginInfoVariable);

		String request_Payload = gson.toJson(query);

		response = hit_GraphQL_EndPoint(request_Payload, null);

		stepInfo("Response Validation");

		passStep("Received Status code : " + response.getStatusCode());
		assertEquals(response.getStatusCode(), 200);
		JsonPath j = new JsonPath(response.asString());

		passStep("Login Type : " + j.getString("data.login_info.login_type"));
		passStep("Login Value : " + j.getString("data.login_info.login_value"));
		passStep("Password required : " + j.getString("data.login_info.password_required"));

		assertEquals(j.getString("data.login_info.login_type"), "phone", "mismatch of login_type..");
		assertEquals(j.getString("data.login_info.login_value"), phone_number, "mismatch of phone_number..");
		System.out.println("========================= END =========================");

	}

	@Test(groups = "smoke", dependsOnMethods = "TC_01_GetLoginInfo", priority = 2)
	public void TC_02_sendPhoneVerificationCode() {
		System.out.println("================= TC_02_sendPhoneVerificationCode =================");
		Query query = gson.fromJson(sendPhoneVerificationCode, Query.class);
		SendPhoneVerificationCodeVariable sendPhoneVerificationCodeVariable = new SendPhoneVerificationCodeVariable();
		sendPhoneVerificationCodeVariable.setPhone(phone_number);
		query.setVariables(sendPhoneVerificationCodeVariable);

		String request_Payload = gson.toJson(query);

		response = hit_GraphQL_EndPoint(request_Payload, null);

		stepInfo("Response Validation");

		passStep("Received Status code : " + response.getStatusCode());
		assertEquals(response.getStatusCode(), 200);
		JsonPath j = new JsonPath(response.asString());

		passStep("next_resend_delay : " + j.getString("data.send_phone_verification_code.next_resend_delay"));
		passStep("Phone_number : " + j.getString("data.send_phone_verification_code.phone"));
		assertEquals(j.getString("data.send_phone_verification_code.next_resend_delay"), "30",
				"mismatch of next_resend_delay..");
		assertEquals(j.getString("data.send_phone_verification_code.phone"), phone_number,
				"mismatch of phone_number..");
		System.out.println("========================= END =========================");

	}

	@Test(groups = "smoke", dependsOnMethods = "TC_02_sendPhoneVerificationCode", priority = 3)
	public void TC_03_verifyPhoneNumber() {

		System.out.println("================= TC_03_verifyPhoneNumber =================");
		// Setting the test data
		Query query = gson.fromJson(verifyPhoneNumber, Query.class);
		VerifyPhoneNumberVariable verifyPhoneNumberVariable = new VerifyPhoneNumberVariable();
		verifyPhoneNumberVariable.setPhone(phone_number);
		verifyPhoneNumberVariable.setCode(Constants.DEFAULT_OTP);
		query.setVariables(verifyPhoneNumberVariable);

		String request_Payload = gson.toJson(query);

		response = hit_GraphQL_EndPoint(request_Payload, null);

		stepInfo("Response Validation");

		passStep("Received Status code : " + response.getStatusCode());
		assertEquals(response.getStatusCode(), 200);
		JsonPath j = new JsonPath(response.asString());

		auth_token = j.getString("data.verify_phone_number.auth_token");
		profile_id = j.getString("data.verify_phone_number.id");

		passStep("auth_token : " + auth_token);
		passStep("profile_id : " + profile_id);
		passStep("profile_email : " + j.getString("data.verify_phone_number.email"));
		passStep("profile_phone : " + phone_number);
		assertNotNull(auth_token, "Auth token is null");
		assertNotNull(profile_id, "Profile id is null");
		assertEquals(j.getString("data.verify_phone_number.phone"), phone_number, "mismatch of phone_number..");
		System.out.println("========================= END =========================");

	}

	@Test(groups = "smoke", dependsOnMethods = "TC_03_verifyPhoneNumber", priority = 4)
	public void TC_04_getCurrentProfile() {

		System.out.println("================= TC_04_currentProfile =================");
		// Setting the test data
		Query query = gson.fromJson(currentProfile, Query.class);
		String request_Payload = gson.toJson(query);

		response = hit_GraphQL_EndPoint(request_Payload, auth_token);

		stepInfo("Response Validation");

		passStep("Received Status code : " + response.getStatusCode());
		assertEquals(response.getStatusCode(), 200);
		JsonPath j = new JsonPath(response.asString());

		passStep("auth_token : " + j.getString("data.me.auth_token"));
		passStep("profile_id : " + j.getString("data.me.id"));
		passStep("profile_email : " + j.getString("data.me.email"));
		passStep("profile_phone : " + j.getString("data.me.phone"));

		assertNotNull(j.getString("data.me.id"), "Profile id is null");
		assertEquals(j.getString("data.me.id"), profile_id, "mismatch of profile_id..");
		assertEquals(j.getString("data.me.phone"), phone_number, "mismatch of phone_number..");
		assertEquals(j.getString("data.me.auth_token"), auth_token, "mismatch of auth_token..");
		System.out.println("========================= END =========================");

	}

	@Test(groups = "smoke", dependsOnMethods = "TC_03_verifyPhoneNumber", priority = 5)
	public void TC_05_getLocationPageData_on_TextPay() {

		System.out.println("================= TC_05_getLocationPageData_on_TextPay =================");
		// Setting the test data
		Query query = gson.fromJson(getLocationPageData, Query.class);

		LocationPageDataVariable locationPageDataVariable = new LocationPageDataVariable();
		locationPageDataVariable.setName(location_name);
		List<String> sources = new ArrayList<>();
		sources.add(Constants.TEXTPAY);
		locationPageDataVariable.setSources(sources);

		query.setVariables(locationPageDataVariable);

		String request_Payload = gson.toJson(query);

		response = hit_GraphQL_EndPoint(request_Payload, auth_token);

		stepInfo("Response Validation");

		passStep("Received Status code : " + response.getStatusCode());
		assertEquals(response.getStatusCode(), 200);
		JsonPath j = new JsonPath(response.asString());
		passStep("Location Id : " + j.getString("data.location.id"));
		passStep("Location Name : " + j.getString("data.location.name"));
		passStep("Location Address : " + j.getString("data.location.address"));
		assertNotNull(j.getString("data.location.id"), "Lolcation id is null");
		assertNotNull(j.getString("data.location.name"), "Lolcation name is null");
		System.out.println("========================= END =========================");

	}

	@Test(groups = "smoke", dependsOnMethods = "TC_03_verifyPhoneNumber", priority = 6)
	public void TC_06_getLocationPageData_on_CameraPay() {

		System.out.println("================= TC_06_getLocationPageData_on_CameraPay =================");
		// Setting the test data
		Query query = gson.fromJson(getLocationPageData, Query.class);

		LocationPageDataVariable locationPageDataVariable = new LocationPageDataVariable();
		locationPageDataVariable.setName(location_name);
		List<String> sources = new ArrayList<>();
		sources.add(Constants.CAMERAPAY);
		locationPageDataVariable.setSources(sources);

		query.setVariables(locationPageDataVariable);

		String request_Payload = gson.toJson(query);

		response = hit_GraphQL_EndPoint(request_Payload, auth_token);

		stepInfo("Response Validation");

		passStep("Received Status code : " + response.getStatusCode());
		assertEquals(response.getStatusCode(), 200);
		JsonPath j = new JsonPath(response.asString());
		passStep("Location Id : " + j.getString("data.location.id"));
		passStep("Location Name : " + j.getString("data.location.name"));
		passStep("Location Address : " + j.getString("data.location.address"));
		assertNotNull(j.getString("data.location.id"), "Lolcation id is null");
		assertNotNull(j.getString("data.location.name"), "Lolcation name is null");
		System.out.println("========================= END =========================");

	}

	@Test(groups = "smoke", dependsOnMethods = "TC_03_verifyPhoneNumber", priority = 7)
	public void TC_07_getPersonalVehicles() {

		System.out.println("================= TC_07_getPersonalVehicles =================");
		// Setting the test data
		Query query = gson.fromJson(personalVehicles, Query.class);
		String request_Payload = gson.toJson(query);

		RestAssured.baseURI = uri;
		stepInfo("Request Payload");

		response = hit_GraphQL_EndPoint(request_Payload, auth_token);

		stepInfo("Response Validation");

		passStep("Received Status code : " + response.getStatusCode());
		assertEquals(response.getStatusCode(), 200);
		JsonPath j = new JsonPath(response.asString());

		passStep("Profile Id : " + j.getString("data.me.id"));
		List<Object> vehicles = j.getList("data.me.vehicles");
		passStep("Vehicles count :" + vehicles.size());
		if (vehicles.size() > 0) {
			vehicle_id = j.getString("data.me.vehicles[0].id");
			passStep("First vehile id " + vehicle_id);
		}

		assertEquals(j.getString("data.me.id"), profile_id);

		System.out.println("========================= END =========================");

	}

	@Test(groups = "smoke", dependsOnMethods = "TC_03_verifyPhoneNumber", priority = 8)
	public void TC_08_getLocationRates() {

		System.out.println("================= TC_08_getLocationRates =================");
		// Setting the test data
		Query query = gson.fromJson(getLocationRates, Query.class);

		LocationRatesVariable locationRatesVariable = new LocationRatesVariable();
		locationRatesVariable.setName(location_name);
		locationRatesVariable.setTimestamp(getCurrentUnixTimestamp());

		query.setVariables(locationRatesVariable);

		String request_Payload = gson.toJson(query);

		RestAssured.baseURI = uri;
		stepInfo("Request Payload");

		response = hit_GraphQL_EndPoint(request_Payload, auth_token);

		stepInfo("Response Validation");

		passStep("Received Status code : " + response.getStatusCode());
		assertEquals(response.getStatusCode(), 200);
		JsonPath j = new JsonPath(response.asString());

		location_id = j.getString("data.location.id");
		List<Object> rate_Groups = j.getList("data.location.rate_groups");
		passStep("Rate groups count :" + rate_Groups.size());

		if (rate_Groups.size() > 0) {
			if (j.getList("data.location.rate_groups[0].rates").size() > 0) {
				rate_id = j.getString("data.location.rate_groups[0].rates[0].id");
				minutes = j.getString("data.location.rate_groups[0].rates[0].minutes");
			}
		}

		for (int rg = 0; rg < rate_Groups.size(); rg++) {
			stepInfo("rate group name : " + j.getString("data.location.rate_groups[" + rg + "].title"));
			List<Object> rates = j.getList("data.location.rate_groups[" + rg + "].rates");
			passStep("rates count : " + rates.size());

			for (int r = 0; r < rates.size(); r++) {
				stepInfo("rate title : "
						+ j.getString("data.location.rate_groups[" + rg + "].rates[" + r + "].descriptive_title"));
				passStep("rate id : " + j.getString("data.location.rate_groups[" + rg + "].rates[" + r + "].id"));
				passStep("rate name : " + j.getString("data.location.rate_groups[" + rg + "].rates[" + r + "].name"));
				passStep("rate kind : " + j.getString("data.location.rate_groups[" + rg + "].rates[" + r + "].kind"));
				passStep("minutes : " + j.getString("data.location.rate_groups[" + rg + "].rates[" + r + "].minutes"));
				passStep("pre_tax_price : "
						+ j.getString("data.location.rate_groups[" + rg + "].rates[" + r + "].pre_tax_price"));
				passStep("tax_price : "
						+ j.getString("data.location.rate_groups[" + rg + "].rates[" + r + "].tax_price"));
				passStep("price : " + j.getString("data.location.rate_groups[" + rg + "].rates[" + r + "].price"));
			}
		}

		System.out.println("========================= END =========================");

	}

	@Test(groups = "smoke", dependsOnMethods = "TC_03_verifyPhoneNumber", priority = 9)
	public void TC_09_getStates() {

		System.out.println("================= TC_09_getStates =================");
		// Setting the test data
		Query query = gson.fromJson(getStates, Query.class);

		String request_Payload = gson.toJson(query);

		RestAssured.baseURI = uri;
		stepInfo("Request Payload");

		response = hit_GraphQL_EndPoint(request_Payload, auth_token);

		stepInfo("Response Validation");

		passStep("Received Status code : " + response.getStatusCode());
		assertEquals(response.getStatusCode(), 200);
		JsonPath j = new JsonPath(response.asString());

		List<Object> sates = j.getList("data.states");
		passStep("States count received :" + sates.size());
		for (int st = 0; st < sates.size(); st++) {

			passStep("state name : " + j.getString("data.states[" + st + "].name"));
			passStep("state description : " + j.getString("data.states[" + st + "].description"));
		}

		System.out.println("========================= END =========================");

	}

	@Test(groups = "smoke", dependsOnMethods = { "TC_07_getPersonalVehicles", "TC_08_getLocationRates" }, priority = 10)
	public void TC_10_getParkingCostByRate_On_TextPay() {

		System.out.println("================= TC_10_getParkingCostByRate_On_TextPay =================");
		// Setting the test data
		Query query = gson.fromJson(getParkingCostByRate, Query.class);

		ParkingCostVariable parkingCostByRateVariable = new ParkingCostVariable();
		Vehicles vehicle = new Vehicles();
		ParkingLotsVariable parkingLot = new ParkingLotsVariable();

		if (vehicle_id == null) {
			vehicle.setMake(getRandom_Vehicle_Make().toUpperCase());
			vehicle.setColor(getRandom_Vehicle_Color().toUpperCase());
			vehicle.setBody_type(getRandom_Vehicle_Type().toUpperCase());
			parkingLot.setUnknown_vehicle(true);
		} else
			vehicle.setId(Integer.parseInt(vehicle_id));

		List<Vehicles> pl_vehicles = new ArrayList<>();
		pl_vehicles.add(vehicle);

		parkingLot.setVehicles(pl_vehicles);

		List<ParkingLotsVariable> parkingLots = new ArrayList<>();
		parkingLots.add(parkingLot);

		parkingCostByRateVariable.setParking_lots(parkingLots);

		parkingCostByRateVariable.setCheckout(true);
		parkingCostByRateVariable.setLocation_id(Integer.parseInt(location_id));
		parkingCostByRateVariable.setParking_time_type(Constants.SESSION);
		parkingCostByRateVariable.setSource(Constants.TEXTPAY);
		parkingCostByRateVariable.setPayment_method_type(Constants.CARD);
		parkingCostByRateVariable.setApply_wallet_credit(true);
		if (rate_id != null)
			parkingCostByRateVariable.setParking_time_rate_id(Integer.parseInt(rate_id));
		else
			assertNotNull(rate_id, "No rate available for location " + location_name + ". Please configure the rates");

		query.setVariables(parkingCostByRateVariable);

		String request_Payload = gson.toJson(query);

		RestAssured.baseURI = uri;
		stepInfo("Request Payload");

		response = hit_GraphQL_EndPoint(request_Payload, auth_token);

		stepInfo("Response Validation");

		passStep("Received Status code : " + response.getStatusCode());
		assertEquals(response.getStatusCode(), 200);

		System.out.println("========================= END =========================");

	}

	@Test(groups = "smoke", dependsOnMethods = { "TC_07_getPersonalVehicles", "TC_08_getLocationRates" }, priority = 11)
	public void TC_11_getParkingCostByRate_On_CameraPay() {

		System.out.println("================= TC_11_getParkingCostByRate_On_CameraPay =================");
		// Setting the test data
		Query query = gson.fromJson(getParkingCostByRate, Query.class);

		ParkingCostVariable parkingCostByRateVariable = new ParkingCostVariable();
		Vehicles vehicle = new Vehicles();
		ParkingLotsVariable parkingLot = new ParkingLotsVariable();

		if (vehicle_id == null) {
			vehicle.setMake(getRandom_Vehicle_Make().toUpperCase());
			vehicle.setColor(getRandom_Vehicle_Color().toUpperCase());
			vehicle.setBody_type(getRandom_Vehicle_Type().toUpperCase());
			parkingLot.setUnknown_vehicle(true);
		} else
			vehicle.setId(Integer.parseInt(vehicle_id));

		List<Vehicles> pl_vehicles = new ArrayList<>();
		pl_vehicles.add(vehicle);

		parkingLot.setVehicles(pl_vehicles);

		List<ParkingLotsVariable> parkingLots = new ArrayList<>();
		parkingLots.add(parkingLot);

		parkingCostByRateVariable.setParking_lots(parkingLots);

		parkingCostByRateVariable.setCheckout(true);
		parkingCostByRateVariable.setLocation_id(Integer.parseInt(location_id));
		parkingCostByRateVariable.setParking_time_type(Constants.SESSION);
		parkingCostByRateVariable.setSource(Constants.CAMERAPAY);
		parkingCostByRateVariable.setPayment_method_type(Constants.CARD);
		parkingCostByRateVariable.setApply_wallet_credit(true);
		if (rate_id != null)
			parkingCostByRateVariable.setParking_time_rate_id(Integer.parseInt(rate_id));
		else
			assertNotNull(rate_id, "No rate available for location " + location_name + ". Please configure the rates");

		query.setVariables(parkingCostByRateVariable);

		String request_Payload = gson.toJson(query);

		RestAssured.baseURI = uri;
		stepInfo("Request Payload");

		response = hit_GraphQL_EndPoint(request_Payload, auth_token);

		stepInfo("Response Validation");

		passStep("Received Status code : " + response.getStatusCode());
		assertEquals(response.getStatusCode(), 200);

		System.out.println("========================= END =========================");

	}

	@Test(groups = "smoke", dependsOnMethods = { "TC_07_getPersonalVehicles", "TC_08_getLocationRates" }, priority = 12)
	public void TC_12_getParkingCostByDuration_on_Textpay() {

		System.out.println("================= TC_12_getParkingCostByDuration_on_Textpay =================");
		// Setting the test data
		Query query = gson.fromJson(getParkingCostByDuration, Query.class);

		ParkingCostVariable parkingCostByDurationVariable = new ParkingCostVariable();
		ParkingLotsVariable parkingLot = new ParkingLotsVariable();
		Vehicles vehicle = new Vehicles();
		if (vehicle_id == null) {
			vehicle.setMake(getRandom_Vehicle_Make().toUpperCase());
			vehicle.setColor(getRandom_Vehicle_Color().toUpperCase());
			vehicle.setBody_type(getRandom_Vehicle_Type().toUpperCase());
			parkingLot.setUnknown_vehicle(true);
		} else
			vehicle.setId(Integer.parseInt(vehicle_id));

		List<Vehicles> pl_vehicles = new ArrayList<>();
		pl_vehicles.add(vehicle);

		parkingLot.setVehicles(pl_vehicles);

		List<ParkingLotsVariable> parkingLots = new ArrayList<>();
		parkingLots.add(parkingLot);

		parkingCostByDurationVariable.setParking_lots(parkingLots);

		parkingCostByDurationVariable.setCheckout(true);
		parkingCostByDurationVariable.setLocation_id(Integer.parseInt(location_id));
		parkingCostByDurationVariable.setParking_time_type(Constants.SESSION);
		parkingCostByDurationVariable.setSource(Constants.TEXTPAY);
		parkingCostByDurationVariable.setPayment_method_type(Constants.CARD);
		parkingCostByDurationVariable.setApply_wallet_credit(true);
		parkingCostByDurationVariable.setMinutes(Integer.parseInt(minutes));

		query.setVariables(parkingCostByDurationVariable);

		String request_Payload = gson.toJson(query);

		RestAssured.baseURI = uri;
		stepInfo("Request Payload");

		response = hit_GraphQL_EndPoint(request_Payload, auth_token);

		stepInfo("Response Validation");

		passStep("Received Status code : " + response.getStatusCode());
		assertEquals(response.getStatusCode(), 200);
		JsonPath j = new JsonPath(response.asString());
		passStep("minutes :" + j.getString("data.parking_cost_by_duration.minutes"));
		assertEquals(j.getString("data.parking_cost_by_duration.minutes"), minutes);

		System.out.println("========================= END =========================");

	}

	@Test(groups = "smoke", dependsOnMethods = { "TC_07_getPersonalVehicles", "TC_08_getLocationRates" }, priority = 13)
	public void TC_13_getParkingCostByDuration_on_Camerapay() {

		System.out.println("================= TC_13_getParkingCostByDuration_on_Camerapay =================");
		// Setting the test data
		Query query = gson.fromJson(getParkingCostByDuration, Query.class);

		ParkingCostVariable parkingCostByDurationVariable = new ParkingCostVariable();
		ParkingLotsVariable parkingLot = new ParkingLotsVariable();
		Vehicles vehicle = new Vehicles();
		if (vehicle_id == null) {
			vehicle.setMake(getRandom_Vehicle_Make().toUpperCase());
			vehicle.setColor(getRandom_Vehicle_Color().toUpperCase());
			vehicle.setBody_type(getRandom_Vehicle_Type().toUpperCase());
			parkingLot.setUnknown_vehicle(true);
		} else
			vehicle.setId(Integer.parseInt(vehicle_id));

		List<Vehicles> pl_vehicles = new ArrayList<>();
		pl_vehicles.add(vehicle);

		parkingLot.setVehicles(pl_vehicles);

		List<ParkingLotsVariable> parkingLots = new ArrayList<>();
		parkingLots.add(parkingLot);

		parkingCostByDurationVariable.setParking_lots(parkingLots);

		parkingCostByDurationVariable.setCheckout(true);
		parkingCostByDurationVariable.setLocation_id(Integer.parseInt(location_id));
		parkingCostByDurationVariable.setParking_time_type(Constants.SESSION);
		parkingCostByDurationVariable.setSource(Constants.CAMERAPAY);
		parkingCostByDurationVariable.setPayment_method_type(Constants.CARD);
		parkingCostByDurationVariable.setApply_wallet_credit(true);
		parkingCostByDurationVariable.setMinutes(Integer.parseInt(minutes));

		query.setVariables(parkingCostByDurationVariable);

		String request_Payload = gson.toJson(query);

		RestAssured.baseURI = uri;
		stepInfo("Request Payload");

		response = hit_GraphQL_EndPoint(request_Payload, auth_token);

		stepInfo("Response Validation");

		passStep("Received Status code : " + response.getStatusCode());
		assertEquals(response.getStatusCode(), 200);
		JsonPath j = new JsonPath(response.asString());
		passStep("minutes :" + j.getString("data.parking_cost_by_duration.minutes"));
		assertEquals(j.getString("data.parking_cost_by_duration.minutes"), minutes);

		System.out.println("========================= END =========================");

	}

	@Test(groups = "smoke", dependsOnMethods = "TC_03_verifyPhoneNumber", priority = 14)
	public void TC_14_getpersonalDataForCheckout() {

		System.out.println("================= TC_14_getpersonalDataForCheckout =================");
		// Setting the test data
		Query query = gson.fromJson(personalDataForCheckout, Query.class);

		String request_Payload = gson.toJson(query);

		RestAssured.baseURI = uri;
		stepInfo("Request Payload");

		response = hit_GraphQL_EndPoint(request_Payload, auth_token);

		stepInfo("Response Validation");

		passStep("Received Status code : " + response.getStatusCode());
		assertEquals(response.getStatusCode(), 200);
		JsonPath j = new JsonPath(response.asString());

		passStep("id: " + j.getString("data.me.id"));
		assertEquals(profile_id, j.getString("data.me.id"), "profie id mismatch");
		assertEquals(phone_number, j.getString("data.me.phone"), "phone number mismatch");
		passStep("phone number : " + j.getString("data.me.phone"));
		List<Object> paymentmethods = j.getList("data.me.payment_methods");
		if (paymentmethods.size() > 0)
			card_id = j.getString("data.me.payment_methods[0].entity_id");

		passStep("avaialble payment methods :" + paymentmethods.size());
		for (int pm = 0; pm < paymentmethods.size(); pm++) {
			stepInfo("Payment method : " + (pm + 1));
			passStep("entity_id : " + j.getString("data.me.payment_methods[" + pm + "].entity_id"));
			passStep("name : " + j.getString("data.me.payment_methods[" + pm + "].name"));
			passStep("last4 : " + j.getString("data.me.payment_methods[" + pm + "].last4"));

		}

		System.out.println("========================= END =========================");

	}

	@Test(groups = "smoke", dependsOnMethods = { "TC_08_getLocationRates",
			"TC_14_getpersonalDataForCheckout" }, priority = 15)
	public void TC_15_createParking_with_Card_On_textPay() {

		System.out.println("================= TC_15_createParking_with_Card_On_textPay =================");
		// Setting the test data
		Query query = gson.fromJson(createParking, Query.class);
		String token = null;

		CreateParkingVariable createParkingVariable = new CreateParkingVariable();
		Vehicles vehicle = new Vehicles();
		ParkingLotsVariable parkingLot = new ParkingLotsVariable();

		vehicle.setMake(getRandom_Vehicle_Make().toUpperCase());
		vehicle.setColor(getRandom_Vehicle_Color().toUpperCase());
		vehicle.setBody_type(getRandom_Vehicle_Type().toUpperCase());
		parkingLot.setUnknown_vehicle(true);

		if (card_id == null) {
			token = getStripeToken();
			createParkingVariable.setToken(token);
		} else
			createParkingVariable.setCard_id(Long.parseLong(card_id));

		List<Vehicles> pl_vehicles = new ArrayList<>();
		pl_vehicles.add(vehicle);

		parkingLot.setVehicles(pl_vehicles);

		List<ParkingLotsVariable> parkingLots = new ArrayList<>();
		parkingLots.add(parkingLot);

		createParkingVariable.setMinutes(Integer.parseInt(minutes));
		createParkingVariable.setApply_wallet_credit(true);
		createParkingVariable.setLocation_id(Integer.parseInt(location_id));
		createParkingVariable.setParking_time_type(Constants.SESSION);

		createParkingVariable.setSave_payment_method(true);
		createParkingVariable.setSource(Constants.TEXTPAY);
		createParkingVariable.setPayment_method_type(Constants.CARD);

		createParkingVariable.setParking_lots(parkingLots);

		query.setVariables(createParkingVariable);

		String request_Payload = gson.toJson(query);

		RestAssured.baseURI = uri;
		stepInfo("Request Payload");

		response = hit_GraphQL_EndPoint(request_Payload, auth_token);

		stepInfo("Response Validation");

		passStep("Received Status code : " + response.getStatusCode());
		assertEquals(response.getStatusCode(), 200);
		JsonPath j = new JsonPath(response.asString());

		order_number_id = j.getString("data.create_parking.order_number_id");
		passStep("order_number_id :" + order_number_id);
		assertNotNull(j.getString("data.create_parking.order_number_id"), "order_number_id is null");

		System.out.println("========================= END =========================");

	}

	@Test(groups = "smoke", dependsOnMethods = { "TC_08_getLocationRates",
			"TC_14_getpersonalDataForCheckout" }, priority = 16)
	public void TC_16_createParking_with_Card_On_CameraPay() {

		System.out.println("================= TC_16_createParking_with_Card_On_CameraPay =================");
		// Setting the test data
		Query query = gson.fromJson(createParking, Query.class);
		String token = null;

		CreateParkingVariable createParkingVariable = new CreateParkingVariable();
		Vehicles vehicle = new Vehicles();
		ParkingLotsVariable parkingLot = new ParkingLotsVariable();

		vehicle.setMake(getRandom_Vehicle_Make().toUpperCase());
		vehicle.setColor(getRandom_Vehicle_Color().toUpperCase());
		vehicle.setBody_type(getRandom_Vehicle_Type().toUpperCase());
		parkingLot.setUnknown_vehicle(true);

		if (card_id == null) {
			token = getStripeToken();
			createParkingVariable.setToken(token);
		} else
			createParkingVariable.setCard_id(Long.parseLong(card_id));

		List<Vehicles> pl_vehicles = new ArrayList<>();
		pl_vehicles.add(vehicle);

		parkingLot.setVehicles(pl_vehicles);

		List<ParkingLotsVariable> parkingLots = new ArrayList<>();
		parkingLots.add(parkingLot);

		createParkingVariable.setMinutes(Integer.parseInt(minutes));
		createParkingVariable.setApply_wallet_credit(true);
		createParkingVariable.setLocation_id(Integer.parseInt(location_id));
		createParkingVariable.setParking_time_type(Constants.SESSION);

		createParkingVariable.setSave_payment_method(true);
		createParkingVariable.setSource(Constants.CAMERAPAY);
		createParkingVariable.setPayment_method_type(Constants.CARD);

		createParkingVariable.setParking_lots(parkingLots);

		query.setVariables(createParkingVariable);

		String request_Payload = gson.toJson(query);

		RestAssured.baseURI = uri;
		stepInfo("Request Payload");

		response = hit_GraphQL_EndPoint(request_Payload, auth_token);

		stepInfo("Response Validation");

		passStep("Received Status code : " + response.getStatusCode());
		assertEquals(response.getStatusCode(), 200);
		JsonPath j = new JsonPath(response.asString());

		order_number_id = j.getString("data.create_parking.order_number_id");
		passStep("order_number_id :" + order_number_id);
		assertNotNull(j.getString("data.create_parking.order_number_id"), "order_number_id is null");

		System.out.println("========================= END =========================");

	}

	@Test(groups = "smoke", dependsOnMethods = "TC_08_getLocationRates", priority = 17)
	public void TC_17_createParking_with_new_Vehicle_and_New_Card_On_Textpay() {

		System.out.println(
				"================= TC_17_createParking_with_new_Vehicle_and_New_Card_On_Textpay =================");

		Query query = gson.fromJson(createParking, Query.class);
		String token = null;

		CreateParkingVariable createParkingVariable = new CreateParkingVariable();
		Vehicles vehicle = new Vehicles();
		ParkingLotsVariable parkingLot = new ParkingLotsVariable();

		vehicle.setMake(getRandom_Vehicle_Make().toUpperCase());
		vehicle.setColor(getRandom_Vehicle_Color().toUpperCase());
		vehicle.setBody_type(getRandom_Vehicle_Type().toUpperCase());
		vehicle.setId(null);
		parkingLot.setUnknown_vehicle(true);

		token = getStripeToken();
		createParkingVariable.setToken(token);

		List<Vehicles> pl_vehicles = new ArrayList<>();
		pl_vehicles.add(vehicle);

		parkingLot.setVehicles(pl_vehicles);

		List<ParkingLotsVariable> parkingLots = new ArrayList<>();
		parkingLots.add(parkingLot);

		createParkingVariable.setMinutes(Integer.parseInt(minutes));
		createParkingVariable.setApply_wallet_credit(true);
		createParkingVariable.setLocation_id(Integer.parseInt(location_id));
		createParkingVariable.setParking_time_type(Constants.SESSION);

		createParkingVariable.setSave_payment_method(true);
		createParkingVariable.setSource(Constants.TEXTPAY);
		createParkingVariable.setPayment_method_type(Constants.CARD);

		createParkingVariable.setParking_lots(parkingLots);

		query.setVariables(createParkingVariable);

		String request_Payload = gson.toJson(query);

		RestAssured.baseURI = uri;
		stepInfo("Request Payload");

		response = hit_GraphQL_EndPoint(request_Payload, auth_token);

		stepInfo("Response Validation");

		passStep("Received Status code : " + response.getStatusCode());
		assertEquals(response.getStatusCode(), 200);
		JsonPath j = new JsonPath(response.asString());
		passStep("order_number_id :" + j.getString("data.create_parking.order_number_id"));
		assertNotNull(j.getString("data.create_parking.order_number_id"), "order_number_id is null");

		System.out.println("========================= END =========================");

	}

	@Test(groups = "smoke", dependsOnMethods = "TC_08_getLocationRates", priority = 18)
	public void TC_18_createParking_with_new_Vehicle_and_New_Card_On_Camerapay() {

		System.out.println(
				"================= TC_18_createParking_with_new_Vehicle_and_New_Card_On_Camerapay =================");

		Query query = gson.fromJson(createParking, Query.class);
		String token = null;

		CreateParkingVariable createParkingVariable = new CreateParkingVariable();
		Vehicles vehicle = new Vehicles();
		ParkingLotsVariable parkingLot = new ParkingLotsVariable();

		vehicle.setMake(getRandom_Vehicle_Make().toUpperCase());
		vehicle.setColor(getRandom_Vehicle_Color().toUpperCase());
		vehicle.setBody_type(getRandom_Vehicle_Type().toUpperCase());
		vehicle.setId(null);
		parkingLot.setUnknown_vehicle(true);

		token = getStripeToken();
		createParkingVariable.setToken(token);

		List<Vehicles> pl_vehicles = new ArrayList<>();
		pl_vehicles.add(vehicle);

		parkingLot.setVehicles(pl_vehicles);

		List<ParkingLotsVariable> parkingLots = new ArrayList<>();
		parkingLots.add(parkingLot);

		createParkingVariable.setMinutes(Integer.parseInt(minutes));
		createParkingVariable.setApply_wallet_credit(true);
		createParkingVariable.setLocation_id(Integer.parseInt(location_id));
		createParkingVariable.setParking_time_type(Constants.SESSION);

		createParkingVariable.setSave_payment_method(true);
		createParkingVariable.setSource(Constants.CAMERAPAY);
		createParkingVariable.setPayment_method_type(Constants.CARD);

		createParkingVariable.setParking_lots(parkingLots);

		query.setVariables(createParkingVariable);

		String request_Payload = gson.toJson(query);

		RestAssured.baseURI = uri;
		stepInfo("Request Payload");

		response = hit_GraphQL_EndPoint(request_Payload, auth_token);

		stepInfo("Response Validation");

		passStep("Received Status code : " + response.getStatusCode());
		assertEquals(response.getStatusCode(), 200);
		JsonPath j = new JsonPath(response.asString());
		passStep("order_number_id :" + j.getString("data.create_parking.order_number_id"));
		assertNotNull(j.getString("data.create_parking.order_number_id"), "order_number_id is null");

		System.out.println("========================= END =========================");

	}

	@Test(groups = "smoke", dependsOnMethods = { "TC_08_getLocationRates" }, priority = 19)
	public void TC_19_createParking_with_new_Vehicle_with_Promocode_On_TextPay() {

		System.out.println(
				"================= TC_19_createParking_with_new_Vehicle_with_Promocode_On_TextPay =================");
		// Setting the test data
		Query query = gson.fromJson(createParking, Query.class);

		CreateParkingVariable createParkingVariable = new CreateParkingVariable();

		Vehicles vehicle = new Vehicles();
		vehicle.setMake(getRandom_Vehicle_Make().toUpperCase());
		vehicle.setColor(getRandom_Vehicle_Color().toUpperCase());
		vehicle.setBody_type(getRandom_Vehicle_Type().toUpperCase());
		vehicle.setId(null);

		List<Vehicles> pl_vehicles = new ArrayList<>();
		pl_vehicles.add(vehicle);

		ParkingLotsVariable parkingLot = new ParkingLotsVariable();
		parkingLot.setVehicles(pl_vehicles);
		parkingLot.setUnknown_vehicle(true);

		List<ParkingLotsVariable> parkingLots = new ArrayList<>();
		parkingLots.add(parkingLot);

		createParkingVariable.setMinutes(Integer.parseInt(minutes));
		createParkingVariable.setApply_wallet_credit(true);
		createParkingVariable.setLocation_id(Integer.parseInt(location_id));
		createParkingVariable.setParking_time_type(Constants.SESSION);
		createParkingVariable.setPromo_code(Constants.PPFSTEST);
		createParkingVariable.setSave_payment_method(true);
		createParkingVariable.setSource(Constants.TEXTPAY);
		createParkingVariable.setPayment_method_type(Constants.CARD);

		createParkingVariable.setParking_lots(parkingLots);

		query.setVariables(createParkingVariable);

		String request_Payload = gson.toJson(query);

		RestAssured.baseURI = uri;
		stepInfo("Request Payload");

		response = hit_GraphQL_EndPoint(request_Payload, auth_token);

		stepInfo("Response Validation");

		passStep("Received Status code : " + response.getStatusCode());
		assertEquals(response.getStatusCode(), 200);
		JsonPath j = new JsonPath(response.asString());
		order_number_id = j.getString("data.create_parking.order_number_id");
		passStep("order_number_id :" + order_number_id);
		passStep("order_total :" + j.getString("data.create_parking.order_total"));
		passStep("promo_code :" + j.getString("data.create_parking.promo_code"));
		assertNotNull(j.getString("data.create_parking.order_number_id"), "order_number_id is null");
		assertEquals(j.getString("data.create_parking.order_total"), "0.0");
		assertEquals(j.getString("data.create_parking.promo_code"), Constants.PPFSTEST);

		System.out.println("========================= END =========================");

	}

	@Test(groups = "smoke", dependsOnMethods = { "TC_08_getLocationRates" }, priority = 20)
	public void TC_20_createParking_with_new_Vehicle_with_Promocode_On_CameraPay() {

		System.out.println(
				"================= TC_19_createParking_with_new_Vehicle_with_Promocode_On_TextPay =================");
		// Setting the test data
		Query query = gson.fromJson(createParking, Query.class);

		CreateParkingVariable createParkingVariable = new CreateParkingVariable();

		Vehicles vehicle = new Vehicles();
		vehicle.setMake(getRandom_Vehicle_Make().toUpperCase());
		vehicle.setColor(getRandom_Vehicle_Color().toUpperCase());
		vehicle.setBody_type(getRandom_Vehicle_Type().toUpperCase());
		vehicle.setId(null);

		List<Vehicles> pl_vehicles = new ArrayList<>();
		pl_vehicles.add(vehicle);

		ParkingLotsVariable parkingLot = new ParkingLotsVariable();
		parkingLot.setVehicles(pl_vehicles);
		parkingLot.setUnknown_vehicle(true);

		List<ParkingLotsVariable> parkingLots = new ArrayList<>();
		parkingLots.add(parkingLot);

		createParkingVariable.setMinutes(Integer.parseInt(minutes));
		createParkingVariable.setApply_wallet_credit(true);
		createParkingVariable.setLocation_id(Integer.parseInt(location_id));
		createParkingVariable.setParking_time_type(Constants.SESSION);
		createParkingVariable.setPromo_code(Constants.PPFSTEST);
		createParkingVariable.setSave_payment_method(true);
		createParkingVariable.setSource(Constants.CAMERAPAY);
		createParkingVariable.setPayment_method_type(Constants.CARD);

		createParkingVariable.setParking_lots(parkingLots);

		query.setVariables(createParkingVariable);

		String request_Payload = gson.toJson(query);

		RestAssured.baseURI = uri;
		stepInfo("Request Payload");

		response = hit_GraphQL_EndPoint(request_Payload, auth_token);

		stepInfo("Response Validation");

		passStep("Received Status code : " + response.getStatusCode());
		assertEquals(response.getStatusCode(), 200);
		JsonPath j = new JsonPath(response.asString());
		order_number_id = j.getString("data.create_parking.order_number_id");
		passStep("order_number_id :" + order_number_id);
		passStep("order_total :" + j.getString("data.create_parking.order_total"));
		passStep("promo_code :" + j.getString("data.create_parking.promo_code"));
		assertNotNull(j.getString("data.create_parking.order_number_id"), "order_number_id is null");
		assertEquals(j.getString("data.create_parking.order_total"), "0.0");
		assertEquals(j.getString("data.create_parking.promo_code"), Constants.PPFSTEST);

		System.out.println("========================= END =========================");

	}

	@Test(groups = "smoke", dependsOnMethods = "TC_19_createParking_with_new_Vehicle_with_Promocode_On_TextPay", priority = 21)
	public void TC_21_Extend_Existing_Parking_On_TextPay() {

		System.out.println("================= TC_21_Extend_Existing_Parking_On_TextPay =================");

		Query query = gson.fromJson(extendParking, Query.class);

		ExtendParkingVariable extendParkingVariable = new ExtendParkingVariable();

		extendParkingVariable.setMinutes(60);
		extendParkingVariable.setOrder_number_id(Long.parseLong(order_number_id));
		extendParkingVariable.setApply_wallet_credit(true);
		extendParkingVariable.setSave_payment_method(true);
		extendParkingVariable.setSource(Constants.TEXTPAY);
		extendParkingVariable.setPayment_method_type(Constants.CARD);
		extendParkingVariable.setToken(getStripeToken());

		query.setVariables(extendParkingVariable);

		String request_Payload = gson.toJson(query);

		response = hit_GraphQL_EndPoint(request_Payload, auth_token);

		stepInfo("Response Validation");

		passStep("Received Status code : " + response.getStatusCode());
		assertEquals(response.getStatusCode(), 200);
		JsonPath j = new JsonPath(response.asString());
		passStep("order_number_id :" + j.getString("data.extend_parking.order_number_id"));
		assertEquals(j.getString("data.extend_parking.order_number_id"), order_number_id);

		System.out.println("========================= END =========================");

	}

	@Test(groups = "smoke", dependsOnMethods = "TC_20_createParking_with_new_Vehicle_with_Promocode_On_CameraPay", priority = 22)
	public void TC_22_Extend_Existing_Parking_On_CameraPay() {

		System.out.println("================= TC_22_Extend_Existing_Parking_On_CameraPay =================");

		Query query = gson.fromJson(extendParking, Query.class);

		ExtendParkingVariable extendParkingVariable = new ExtendParkingVariable();

		extendParkingVariable.setMinutes(60);
		extendParkingVariable.setOrder_number_id(Long.parseLong(order_number_id));
		extendParkingVariable.setApply_wallet_credit(true);
		extendParkingVariable.setSave_payment_method(true);
		extendParkingVariable.setSource(Constants.CAMERAPAY);
		extendParkingVariable.setPayment_method_type(Constants.CARD);
		extendParkingVariable.setToken(getStripeToken());

		query.setVariables(extendParkingVariable);

		String request_Payload = gson.toJson(query);

		response = hit_GraphQL_EndPoint(request_Payload, auth_token);

		stepInfo("Response Validation");

		passStep("Received Status code : " + response.getStatusCode());
		assertEquals(response.getStatusCode(), 200);
		JsonPath j = new JsonPath(response.asString());
		passStep("order_number_id :" + j.getString("data.extend_parking.order_number_id"));
		assertEquals(j.getString("data.extend_parking.order_number_id"), order_number_id);

		System.out.println("========================= END =========================");

	}

}
