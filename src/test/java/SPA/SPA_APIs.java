package SPA;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.Test;

import com.google.gson.Gson;
import com.graphQL.pojo.Account;
import com.graphQL.pojo.spa.LocationVariable;
import com.graphQL.pojo.spa.MarketPageData;
import com.graphQL.pojo.spa.MarketVenue;
import com.graphQL.pojo.spa.OrderData;
import com.graphQL.pojo.spa.Quote;
import com.graphQL.pojo.spa.ResetPassword;
import com.graphQL.pojo.spa.AutoCompleteVariable;
import com.graphQL.pojo.spa.ConfirmationPageData;
import com.graphQL.pojo.spa.EventData;
import com.graphQL.pojo.spa.LocationCurrentData;
import com.graphQL.pojo.spa.LocationSpace;
import com.graphQL.pojo.spa.SignInVariable;
import com.graphQL.pojo.spa.SignUpVariable;
import com.graphQL.pojo.spa.StaticPage;
import com.graphQL.pojo.spa.UpcomingEvents;
import com.graphQL.pojo.spa.VenueData;
import com.graphQL.pojo.textpay.CreateParkingVariable;
import com.graphQL.pojo.textpay.ExtendParkingVariable;
import com.graphQL.pojo.textpay.LoginInfoVariable;
import com.graphQL.pojo.textpay.ParkingCostVariable;
import com.graphQL.pojo.textpay.ParkingLotsVariable;
import com.graphQL.pojo.textpay.Query;
import com.graphQL.pojo.textpay.SendPhoneVerificationCodeVariable;
import com.graphQL.pojo.textpay.Vehicles;
import com.graphQL.pojo.textpay.VerifyPhoneNumberVariable;

import components.BaseClass;
import components.Constants;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class SPA_APIs extends BaseClass {

	Gson gson = new Gson();

	Account account = new Account();
	String auth_token, location_id, profile_id, vehicle_id, rate_id, minutes, card_id, order_number_id = null;
	long payment_cardId = 0l;

	Response response = null;
	String phone_number = getRandomUSPhoneNumber();
	String location_name = getRandomLocation();

	@Test(groups = "smoke", priority = 1)
	public void TC_01_GetLoginInfo_with_email() {
		System.out.println("================= TC_01_GetLoginInfo_with_email =================");

		Query query = gson.fromJson(getRequestBody("getLoginInfo"), Query.class);
		LoginInfoVariable loginInfoVariable = new LoginInfoVariable();
		loginInfoVariable.setLogin(spa_userName);
		query.setVariables(loginInfoVariable);

		String request_Payload = gson.toJson(query);

		response = hit_GraphQL_EndPoint(request_Payload, null);

		stepInfo("Response Validation");

		JsonPath j = new JsonPath(response.asString());

		passStep("Login Type : " + j.getString("data.login_info.login_type"));
		passStep("Login Value : " + j.getString("data.login_info.login_value"));
		passStep("Password required : " + j.getString("data.login_info.password_required"));

		assertEquals(j.getString("data.login_info.login_type"), "email", "mismatch of login_type..");
		assertEquals(j.getString("data.login_info.login_value"), spa_userName, "mismatch of email..");
		System.out.println("========================= END =========================");

	}

	@Test(groups = "smoke", priority = 2)
	public void TC_02_GetLoginInfo_with_phone() {
		System.out.println("================= TC_02_GetLoginInfo_with_phone =================");

		Query query = gson.fromJson(getRequestBody("getLoginInfo"), Query.class);
		LoginInfoVariable loginInfoVariable = new LoginInfoVariable();
		loginInfoVariable.setLogin(phone_number);
		query.setVariables(loginInfoVariable);

		String request_Payload = gson.toJson(query);

		response = hit_GraphQL_EndPoint(request_Payload, null);

		stepInfo("Response Validation");
		JsonPath j = new JsonPath(response.asString());

		passStep("Login Type : " + j.getString("data.login_info.login_type"));
		passStep("Login Value : " + j.getString("data.login_info.login_value"));
		passStep("Password required : " + j.getString("data.login_info.password_required"));

		assertEquals(j.getString("data.login_info.login_type"), "phone", "mismatch of login_type..");
		assertEquals(j.getString("data.login_info.login_value"), phone_number, "mismatch of email..");
		System.out.println("========================= END =========================");

	}

	@Test(priority = 3)
	public void TC_03_signIn() {

		Query query = gson.fromJson(getRequestBody("signin"), Query.class);

		SignInVariable signInVariable = new SignInVariable();

		// Setting the test data
		signInVariable.setSource(Constants.WEB);
		signInVariable.setGenerate_jwt_token(false);
		signInVariable.setLogin(spa_userName);
		signInVariable.setPassword(spa_Password);
		String request_Payload = gson.toJson(query);

		RestAssured.baseURI = uri;
		stepInfo("Request Payload");

		passStep(request_Payload);
		response = hit_GraphQL_EndPoint(request_Payload, null);

		stepInfo("Response Validation");
		JsonPath j = new JsonPath(response.asString());

		auth_token = j.getString("data.login.auth_token");
		assertNotNull(auth_token, "auth_token is null");
		payment_cardId = j.getLong("data.login.payment_methods[0].entity_id");

		System.out.println("*** Authentication Token : " + x_auth_token);
		System.out.println("*** Payment Card ID : " + payment_cardId);
		System.out.println("===============================================");

	}

	@Test(priority = 4)
	public void TC_04_getFooterData() {

		Query query = gson.fromJson(getRequestBody("getFooterData"), Query.class);
		String request_Payload = gson.toJson(query);

		RestAssured.baseURI = uri;
		stepInfo("Request Payload");

		passStep(request_Payload);

		response = hit_GraphQL_EndPoint(request_Payload, null);

		stepInfo("Response Validation");
		JsonPath j = new JsonPath(response.asString());

		List<Object> market_list = j.getList("data.markets");
		passStep("Received " + market_list.size() + " markets for footer data");

		assertNotNull(market_list, "markets list is null");

		if (market_list.size() > 0) {
			for (int market = 0; market < market_list.size(); market++) {
				stepInfo("Market " + (market + 1) + " details");
				passStep("market id " + j.getString("data.markets[" + market + "].id"));
				passStep("market slug " + j.getString("data.markets[" + market + "].slug"));
				passStep("market name " + j.getString("data.markets[" + market + "].name"));
			}
		}

		System.out.println("===============================================");

	}

	@Test(priority = 5)
	public void TC_05_getMarketsListData() {

		Query query = gson.fromJson(getRequestBody("getMarketsListData"), Query.class);
		String request_Payload = gson.toJson(query);

		RestAssured.baseURI = uri;
		stepInfo("Request Payload");

		passStep(request_Payload);

		response = hit_GraphQL_EndPoint(request_Payload, null);

		stepInfo("Response Validation");

		JsonPath j = new JsonPath(response.asString());
		List<Object> market_list = j.getList("data.markets");
		passStep("Received " + market_list.size() + " markets");

		assertNotNull(market_list, "markets list is null");

		if (market_list.size() > 0) {
			for (int market = 0; market < market_list.size(); market++) {
				stepInfo("Market " + (market + 1) + " details");
				passStep("market id " + j.getString("data.markets[" + market + "].id"));
				passStep("market slug " + j.getString("data.markets[" + market + "].slug"));
				passStep("market name " + j.getString("data.markets[" + market + "].name"));
				passStep("market latitude " + j.getString("data.markets[" + market + "].latitude"));
				passStep("market longitude " + j.getString("data.markets[" + market + "].longitude"));
			}
		}

		System.out.println("===============================================");

	}

	@Test(groups = "smoke", priority = 6)
	public void TC_06_sendPhoneVerificationCode() {
		System.out.println("================= TC_02_sendPhoneVerificationCode =================");
		Query query = gson.fromJson(getRequestBody("sendPhoneVerificationCode"), Query.class);
		SendPhoneVerificationCodeVariable sendPhoneVerificationCodeVariable = new SendPhoneVerificationCodeVariable();
		sendPhoneVerificationCodeVariable.setPhone(phone_number);
		query.setVariables(sendPhoneVerificationCodeVariable);

		String request_Payload = gson.toJson(query);

		response = hit_GraphQL_EndPoint(request_Payload, null);

		stepInfo("Response Validation");

		JsonPath j = new JsonPath(response.asString());

		passStep("next_resend_delay : " + j.getString("data.send_phone_verification_code.next_resend_delay"));
		passStep("Phone_number : " + j.getString("data.send_phone_verification_code.phone"));
		assertEquals(j.getString("data.send_phone_verification_code.next_resend_delay"), "30",
				"mismatch of next_resend_delay..");
		assertEquals(j.getString("data.send_phone_verification_code.phone"), phone_number,
				"mismatch of phone_number..");
		System.out.println("========================= END =========================");

	}

	@Test(groups = "smoke", priority = 7)
	public void TC_07_verifyPhoneNumber() {

		System.out.println("================= TC_03_verifyPhoneNumber =================");
		// Setting the test data
		Query query = gson.fromJson(getRequestBody("verifyPhoneNumber"), Query.class);
		VerifyPhoneNumberVariable verifyPhoneNumberVariable = new VerifyPhoneNumberVariable();
		verifyPhoneNumberVariable.setPhone(phone_number);
		verifyPhoneNumberVariable.setCode(Constants.DEFAULT_OTP);
		query.setVariables(verifyPhoneNumberVariable);

		String request_Payload = gson.toJson(query);

		response = hit_GraphQL_EndPoint(request_Payload, null);

		stepInfo("Response Validation");

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

	@Test(dependsOnMethods = "TC_03_signIn", priority = 8)
	public void TC_08_getCurrentProfile() {

		System.out.println("================= TC_04_currentProfile =================");
		// Setting the test data
		Query query = gson.fromJson(getRequestBody("currentProfile"), Query.class);
		String request_Payload = gson.toJson(query);

		response = hit_GraphQL_EndPoint(request_Payload, auth_token);

		stepInfo("Response Validation");

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

	@Test(dependsOnMethods = "TC_03_signIn", priority = 9)
	public void TC_09_autocompleteSearchForAllCities() {

		System.out.println("================= TC_08_autocompleteSearchForAllCities =================");

		Query query = gson.fromJson(getRequestBody("autocompleteSearchForAllCities"), Query.class);
		AutoCompleteVariable autoCompleteVariable = new AutoCompleteVariable();
		autoCompleteVariable.setWithStreetAddress(true);
		autoCompleteVariable.setQuery("10");

		query.setVariables(autoCompleteVariable);
		String request_Payload = gson.toJson(query);

		response = hit_GraphQL_EndPoint(request_Payload, auth_token);

		stepInfo("Response Validation");

		JsonPath j = new JsonPath(response.asString());

		List<Object> city_result_list = j.getList("data.autocomplete_search");
		passStep("Received " + city_result_list.size() + " records");
		assertNotNull(city_result_list, "city_result_list list is null");

		if (city_result_list.size() > 0) {
			for (int city = 0; city < city_result_list.size(); city++) {
				stepInfo("city " + (city + 1) + " details");
				passStep("description " + j.getString("data.autocomplete_search[" + city + "].description"));
				passStep("id " + j.getString("data.autocomplete_search[" + city + "].id"));
				passStep("latitude " + j.getString("data.autocomplete_search[" + city + "].latitude"));
				passStep("longitude " + j.getString("data.autocomplete_search[" + city + "].longitude"));
				passStep("name " + j.getString("data.autocomplete_search[" + city + "].name"));
				passStep("slug " + j.getString("data.autocomplete_search[" + city + "].slug"));
				passStep("type " + j.getString("data.autocomplete_search[" + city + "].type"));

			}
		}
		System.out.println("========================= END =========================");

	}

	@Test(dependsOnMethods = "TC_03_signIn", priority = 10)
	public void TC_10_getLocation() {

		System.out.println("================= TC_10_getLocation =================");

		Query query = gson.fromJson(getRequestBody("getLocation"), Query.class);
		LocationVariable locationVariable = new LocationVariable();
		locationVariable.setSlug("P101");

		query.setVariables(locationVariable);
		String request_Payload = gson.toJson(query);

		response = hit_GraphQL_EndPoint(request_Payload, auth_token);

		stepInfo("Response Validation");
		JsonPath j = new JsonPath(response.asString());

		passStep("id : " + j.getString("data.location.id"));
		passStep("name : " + j.getString("data.location.name"));
		passStep("reservation_instructions : " + j.getString("data.location.reservation_instructions"));

		assertNotNull(j.getString("data.location.name"), "P0101");

		System.out.println("========================= END =========================");

	}

	@Test(dependsOnMethods = "TC_03_signIn", priority = 11)
	public void TC_11_getLocationCurrentData() {

		System.out.println("================= TC_11_getLocationCurrentData =================");

		Query query = gson.fromJson(getRequestBody("getLocationCurrentData"), Query.class);
		LocationCurrentData locationCurrentDataVariable = new LocationCurrentData();
		locationCurrentDataVariable.setSlug("P101");
		locationCurrentDataVariable.setTimestamp(getCurrentUnixTimestamp());

		query.setVariables(locationCurrentDataVariable);
		String request_Payload = gson.toJson(query);

		response = hit_GraphQL_EndPoint(request_Payload, auth_token);

		stepInfo("Response Validation");
		JsonPath j = new JsonPath(response.asString());

		location_id = j.getString("data.location.id");
		List<Object> rate_Groups = j.getList("data.location.rate_groups");
		passStep("Rate groups count :" + rate_Groups.size());

		assertNotNull(rate_Groups, "rate_Groups list is null");

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

	@Test(priority = 12)
	public void TC_12_getVehicleEnums() {

		System.out.println("================= TC_12_getVehicleEnums =================");
		Query query = gson.fromJson(getRequestBody("getVehicleEnums"), Query.class);
		String request_Payload = gson.toJson(query);

		response = hit_GraphQL_EndPoint(request_Payload, null);

		stepInfo("Response Validation");

		JsonPath j = new JsonPath(response.asString());

		List<Object> enumValues = j.getList("data.states.enumValues");
		passStep("Received " + enumValues.size() + " States");
		assertNotNull(enumValues, "enumValues list is null");

		if (enumValues.size() > 0) {
			for (int state = 0; state < enumValues.size(); state++) {
				stepInfo("State " + (state + 1) + " details");
				passStep("name " + j.getString("data.states.enumValues[" + state + "].name"));
				passStep("description " + j.getString("data.states.enumValues[" + state + "].description"));

			}
		}
		System.out.println("========================= END =========================");

	}

	@Test(priority = 13)
	public void TC_13_getMarketPageData() {

		System.out.println("================= TC_13_getMarketPageData =================");
		Query query = gson.fromJson(getRequestBody("getMarketPageData"), Query.class);

		MarketPageData mpd = new MarketPageData();
		mpd.setSlug("new-orleans");
		mpd.setPopularVenuesLimit(6);
		mpd.setVenuesInGroupsLimit(8);

		query.setVariables(mpd);

		String request_Payload = gson.toJson(query);

		response = hit_GraphQL_EndPoint(request_Payload, null);

		stepInfo("Response Validation");

		JsonPath j = new JsonPath(response.asString());

		passStep("Market name " + j.getString("data.market.name"));
		assertEquals(j.getString("data.market.name"), "New Orleans", "mismatch of market name..");

		System.out.println("========================= END =========================");

	}

	@Test(priority = 14)
	public void TC_14_getVenueData() {

		System.out.println("================= TC_13_getVenueData =================");
		Query query = gson.fromJson(getRequestBody("getVenueData"), Query.class);

		VenueData vd = new VenueData();
		vd.setSlug("new-orleans");
		vd.setVenueSlug("harrahs-new-orleans-casino");
		vd.setNearbyLocationsLimit(6);

		query.setVariables(vd);

		String request_Payload = gson.toJson(query);

		response = hit_GraphQL_EndPoint(request_Payload, null);

		stepInfo("Response Validation");

		JsonPath j = new JsonPath(response.asString());

		passStep("Market name " + j.getString("data.market.name"));
		passStep("slug " + j.getString("data.market.slug"));
		assertEquals(j.getString("data.market.name"), "New Orleans", "mismatch of market name..");

		System.out.println("========================= END =========================");

	}

	@Test(priority = 15)
	public void TC_15_getUpcomingEvents() {

		System.out.println("================= TC_15_getUpcomingEvents =================");
		Query query = gson.fromJson(getRequestBody("getUpcomingEvents"), Query.class);

		UpcomingEvents upd = new UpcomingEvents();
		upd.setMarketSlug("new-orleans");
		upd.setVenueSlug("harrahs-new-orleans-casino");
		upd.setLimit(12);
		upd.setStartsAtFrom(getCurrentUnixTimestamp());

		query.setVariables(upd);

		String request_Payload = gson.toJson(query);

		response = hit_GraphQL_EndPoint(request_Payload, null);

		stepInfo("Response Validation");

		JsonPath j = new JsonPath(response.asString());

		passStep("Market name " + j.getString("data.market.name"));
		assertEquals(j.getString("data.market.name"), "New Orleans", "mismatch of market name..");

		System.out.println("========================= END =========================");

	}

	@Test(priority = 16)
	public void TC_16_getFeaturesForSearchFilter() {

		System.out.println("================= TC_16_getFeaturesForSearchFilter =================");
		Query query = gson.fromJson(getRequestBody("getFeaturesForSearchFilter"), Query.class);

		String request_Payload = gson.toJson(query);

		response = hit_GraphQL_EndPoint(request_Payload, null);

		stepInfo("Response Validation");

		JsonPath j = new JsonPath(response.asString());

		passStep("Features count " + j.getList("data.features").size());
		assertNotNull(j.getList("data.features"), "features list is null");

		System.out.println("========================= END =========================");

	}

	@Test(priority = 17)
	public void TC_17_getSearchPageData() {

		System.out.println("================= TC_17_getSearchPageData =================");
		Query query = gson.fromJson(getRequestBody("getSearchPageData"), Query.class);

		String request_Payload = gson.toJson(query);

		response = hit_GraphQL_EndPoint(request_Payload, null);

		stepInfo("Response Validation");

		JsonPath j = new JsonPath(response.asString());

		passStep("Locations count " + j.getList("data.locations").size());
		assertNotNull(j.getList("data.locations"), "location list is null");

		System.out.println("========================= END =========================");

	}

	@Test(priority = 18)
	public void TC_18_getLocationFeatures() {

		System.out.println("================= TC_18_getLocationFeatures =================");
		Query query = gson.fromJson(getRequestBody("getLocationFeatures"), Query.class);

		String request_Payload = gson.toJson(query);

		response = hit_GraphQL_EndPoint(request_Payload, null);

		stepInfo("Response Validation");

		JsonPath j = new JsonPath(response.asString());

		passStep("Locations Id " + j.getString("data.location.id"));
		assertNotNull(j.getString("data.location.id"), "location.id is null");

		System.out.println("========================= END =========================");

	}

	@Test(priority = 19, dependsOnMethods = "TC_11_getLocationCurrentData")
	public void TC_19_getParkingCostByTime() {

		System.out.println("================= TC_19_getParkingCostByTime =================");
		Query query = gson.fromJson(getRequestBody("getParkingCostByTime"), Query.class);

		ParkingCostVariable parkingCostByTime = new ParkingCostVariable();
		ParkingLotsVariable parkingLot = new ParkingLotsVariable();

		Vehicles vehicle = new Vehicles();
		vehicle.setLicense_plate(getRandomLicencePlate());
		vehicle.setState(getRandomState());

		List<Vehicles> pl_vehicles = new ArrayList<>();
		pl_vehicles.add(vehicle);

		parkingLot.setVehicles(pl_vehicles);

		List<ParkingLotsVariable> parkingLots = new ArrayList<>();
		parkingLots.add(parkingLot);

		parkingCostByTime.setParking_lots(parkingLots);

		parkingCostByTime.setCheckout(true);
		parkingCostByTime.setLocation_id(Integer.parseInt(location_id));
		parkingCostByTime.setParking_time_type(Constants.RESERVATION);
		parkingCostByTime.setSource(Constants.WEB);
		parkingCostByTime.setPayment_method_type(Constants.CARD);
		parkingCostByTime.setApply_wallet_credit(true);
		parkingCostByTime.setStarts_at(getCurrentUnixTimestamp());
		parkingCostByTime.setExpires_at(getCurrentUnixTimestamp() + 86400); // 24 Hrs
		parkingCostByTime.setMinutes(null);
		query.setVariables(parkingCostByTime);

		String request_Payload = gson.toJson(query);

		response = hit_GraphQL_EndPoint(request_Payload, null);

		stepInfo("Response Validation");

		JsonPath j = new JsonPath(response.asString());
		assertNotNull(j.getString("data.parking_cost_by_time.total"), "total is null");

		passStep("total " + j.getString("data.parking_cost_by_time.total"));

		System.out.println("========================= END =========================");

	}

	@Test(priority = 20, dependsOnMethods = "TC_11_getLocationCurrentData")
	public void TC_20_getParkingCostByRate() {

		System.out.println("================= TC_20_getParkingCostByRate =================");
		Query query = gson.fromJson(getRequestBody("getParkingCostByRate"), Query.class);

		ParkingCostVariable parkingCostByRate = new ParkingCostVariable();
		ParkingLotsVariable parkingLot = new ParkingLotsVariable();

		Vehicles vehicle = new Vehicles();
		vehicle.setLicense_plate(getRandomLicencePlate());
		vehicle.setState(getRandomState());

		List<Vehicles> pl_vehicles = new ArrayList<>();
		pl_vehicles.add(vehicle);

		parkingLot.setVehicles(pl_vehicles);

		List<ParkingLotsVariable> parkingLots = new ArrayList<>();
		parkingLots.add(parkingLot);

		parkingCostByRate.setParking_lots(parkingLots);

		parkingCostByRate.setLocation_id(Integer.parseInt(location_id));
		parkingCostByRate.setParking_time_type(Constants.SUBSCRIPTION);
		parkingCostByRate.setSource(Constants.WEB);
		parkingCostByRate.setPayment_method_type(Constants.CARD);
		parkingCostByRate.setApply_wallet_credit(true);
		parkingCostByRate.setStarts_at(getCurrentUnixTimestamp());
		parkingCostByRate.setExpires_at(null);
		parkingCostByRate.setMinutes(null);
		parkingCostByRate.setParking_time_rate_id(678);
		query.setVariables(parkingCostByRate);

		String request_Payload = gson.toJson(query);

		response = hit_GraphQL_EndPoint(request_Payload, null);

		stepInfo("Response Validation");

		JsonPath j = new JsonPath(response.asString());
		assertNotNull(j.getString("data.parking_cost_by_rate.total"), "total is null");
		passStep("total " + j.getString("data.parking_cost_by_rate.total"));

		System.out.println("========================= END =========================");

	}

	@Test(priority = 21, dependsOnMethods = "TC_11_getLocationCurrentData")
	public void TC_21_getParkingCostByDuration() {

		System.out.println("================= TC_21_getParkingCostByDuration =================");
		Query query = gson.fromJson(getRequestBody("getParkingCostByDuration"), Query.class);

		ParkingCostVariable parkingCostByDuration = new ParkingCostVariable();
		ParkingLotsVariable parkingLot = new ParkingLotsVariable();

		Vehicles vehicle = new Vehicles();
		vehicle.setLicense_plate(getRandomLicencePlate());
		vehicle.setState(getRandomState());

		List<Vehicles> pl_vehicles = new ArrayList<>();
		pl_vehicles.add(vehicle);

		parkingLot.setVehicles(pl_vehicles);

		List<ParkingLotsVariable> parkingLots = new ArrayList<>();
		parkingLots.add(parkingLot);

		parkingCostByDuration.setParking_lots(parkingLots);

		parkingCostByDuration.setLocation_id(Integer.parseInt(location_id));
		parkingCostByDuration.setParking_time_type(Constants.SESSION);
		parkingCostByDuration.setSource(Constants.WEB);
		parkingCostByDuration.setPayment_method_type(Constants.CARD);
		parkingCostByDuration.setApply_wallet_credit(true);
		parkingCostByDuration.setStarts_at(null);
		parkingCostByDuration.setExpires_at(null);
		parkingCostByDuration.setMinutes(720);
		parkingCostByDuration.setParking_time_rate_id(null);
		query.setVariables(parkingCostByDuration);

		String request_Payload = gson.toJson(query);

		response = hit_GraphQL_EndPoint(request_Payload, null);

		stepInfo("Response Validation");

		JsonPath j = new JsonPath(response.asString());

		passStep("minutes " + j.getString("data.parking_cost_by_duration.minutes"));
		assertNotNull(j.getString("data.parking_cost_by_duration.minutes"), "minutes is null");

		System.out.println("========================= END =========================");

	}

	@Test(groups = "smoke", priority = 22)
	public void TC_22_sendPhoneVerificationCode() {
		System.out.println("================= TC_22_sendPhoneVerificationCode =================");
		Query query = gson.fromJson(getRequestBody("sendPhoneVerificationCode"), Query.class);
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

	@Test(groups = "smoke", priority = 23)
	public void TC_23_sendResetPasswordNotification() {
		System.out.println("================= TC_23_sendResetPasswordNotification =================");

		Query query = gson.fromJson(getRequestBody("sendResetPasswordNotification"), Query.class);
		LoginInfoVariable loginInfoVariable = new LoginInfoVariable();
		loginInfoVariable.setLogin(spa_userName);
		query.setVariables(loginInfoVariable);

		String request_Payload = gson.toJson(query);

		response = hit_GraphQL_EndPoint(request_Payload, null);

		stepInfo("Response Validation");

		JsonPath j = new JsonPath(response.asString());
		assertNotNull(j.getString("data.send_reset_password_notification.sending_service"), "sending_service is null");

		passStep("sending_service : " + j.getString("data.send_reset_password_notification.sending_service"));

		assertEquals(j.getString("data.send_reset_password_notification.sending_service"), "email",
				"mismatch of sending_service..");
		System.out.println("========================= END =========================");

	}

	@Test(groups = "smoke", priority = 23)
	public void TC_24_resetPassword() {
		System.out.println("================= TC_24_resetPassword =================");

		Query query = gson.fromJson(getRequestBody("resetPassword"), Query.class);
		ResetPassword resetPassword = new ResetPassword();

		resetPassword.setGenerate_jwt_token(false);
		resetPassword.setNew_password(getRandomUSPhoneNumber());
		resetPassword.setToken("QL1-moUDzHH94eUnuWbt");

		query.setVariables(resetPassword);

		String request_Payload = gson.toJson(query);

		response = hit_GraphQL_EndPoint(request_Payload, null);

		System.out.println("========================= END =========================");

	}

	@Test(groups = "smoke", priority = 25)
	public void TC_25_getStaticPageData() {
		System.out.println("================= TC_25_getStaticPageData =================");

		Query query = gson.fromJson(getRequestBody("getStaticPageData"), Query.class);

		StaticPage sp = new StaticPage();
		sp.setPageSlug("glideparcs");
		query.setVariables(sp);

		String request_Payload = gson.toJson(query);

		response = hit_GraphQL_EndPoint(request_Payload, null);

		stepInfo("Response Validation");

		JsonPath j = new JsonPath(response.asString());
		assertNotNull(j.getString("data.static_page.meta_title"), "meta_title is null");

		passStep("meta_title : " + j.getString("data.static_page.meta_title"));
		passStep("meta_description : " + j.getString("data.static_page.meta_description"));

		System.out.println("========================= END =========================");

	}

	@Test(groups = "smoke", priority = 26)
	public void TC_26_logout() {
		System.out.println("================= TC_26_logout =================");

		Query query = gson.fromJson(getRequestBody("logout"), Query.class);

		String request_Payload = gson.toJson(query);

		response = hit_GraphQL_EndPoint(request_Payload, null);

		System.out.println("========================= END =========================");

	}

	@Test(priority = 27)
	public void TC_27_autocompleteSearchByCoordinates() {

		System.out.println("================= TC_27_autocompleteSearchByCoordinates =================");

		Query query = gson.fromJson(getRequestBody("autocompleteSearchByCoordinates"), Query.class);

		String request_Payload = gson.toJson(query);

		response = hit_GraphQL_EndPoint(request_Payload, auth_token);

		stepInfo("Response Validation");

		JsonPath j = new JsonPath(response.asString());

		List<Object> city_result_list = j.getList("data.autocomplete_search");
		passStep("Received " + city_result_list.size() + " records");
		assertNotNull(city_result_list, "City list is null");

		if (city_result_list.size() > 0) {
			for (int city = 0; city < city_result_list.size(); city++) {
				stepInfo("city " + (city + 1) + " details");
				passStep("description " + j.getString("data.autocomplete_search[" + city + "].description"));
				passStep("id " + j.getString("data.autocomplete_search[" + city + "].id"));
				passStep("latitude " + j.getString("data.autocomplete_search[" + city + "].latitude"));
				passStep("longitude " + j.getString("data.autocomplete_search[" + city + "].longitude"));
				passStep("name " + j.getString("data.autocomplete_search[" + city + "].name"));
				passStep("slug " + j.getString("data.autocomplete_search[" + city + "].slug"));
				passStep("type " + j.getString("data.autocomplete_search[" + city + "].type"));

			}
		}
		System.out.println("========================= END =========================");

	}

	@Test(priority = 28)
	public void TC_28_getAllMarkets() {

		System.out.println("================= TC_28_getAllMarkets =================");

		Query query = gson.fromJson(getRequestBody("getAllMarkets"), Query.class);

		String request_Payload = gson.toJson(query);

		response = hit_GraphQL_EndPoint(request_Payload, auth_token);

		stepInfo("Response Validation");

		JsonPath j = new JsonPath(response.asString());

		List<Object> market_result_list = j.getList("data.markets");
		passStep("Received " + market_result_list.size() + " markets");
		assertNotNull(j.getList("data.markets"), "markets list is null");

		if (market_result_list.size() > 0) {
			for (int market = 0; market < market_result_list.size(); market++) {
				stepInfo("market " + (market + 1) + " details");
				passStep("id " + j.getString("data.markets[" + market + "].id"));
				passStep("slug " + j.getString("data.markets[" + market + "].slug"));
				passStep("name " + j.getString("data.markets[" + market + "].name"));

			}
		}
		System.out.println("========================= END =========================");

	}

	@Test(groups = "smoke", priority = 29)
	public void TC_29_getCheckoutPageData() {
		System.out.println("================= TC_29_getCheckoutPageData =================");

		Query query = gson.fromJson(getRequestBody("getCheckoutPageData"), Query.class);

		String request_Payload = gson.toJson(query);

		response = hit_GraphQL_EndPoint(request_Payload, null);

		stepInfo("Response Validation");

		JsonPath j = new JsonPath(response.asString());

		passStep("id : " + j.getString("data.location.id"));
		passStep("reservation_instructions : " + j.getString("data.location.reservation_instructions"));
		assertNotNull(j.getString("data.location.id"), "id is null");

		System.out.println("========================= END =========================");

	}

	//@Test(groups = "smoke", priority = 30)
	public void TC_30_getEvent() {
		System.out.println("================= TC_30_getEvent =================");

		Query query = gson.fromJson(getRequestBody("getEvent"), Query.class);

		String request_Payload = gson.toJson(query);

		response = hit_GraphQL_EndPoint(request_Payload, null);

		stepInfo("Response Validation");

		JsonPath j = new JsonPath(response.asString());

		passStep("id : " + j.getString("data.venue_event.id"));
		passStep("name : " + j.getString("data.venue_event.name"));
		assertNotNull(j.getString("data.venue_event.id"), "id is null");

		System.out.println("========================= END =========================");

	}

	//@Test(groups = "smoke", priority = 31)
	public void TC_31_getEventData() {
		System.out.println("================= TC_31_getEventData =================");

		Query query = gson.fromJson(getRequestBody("getEventData"), Query.class);

		EventData ed = new EventData();
		ed.setSlug("new-orleans");
		ed.setVenueSlug("caesars-superdome");
		ed.setNearbyLocationsLimit(6);
		ed.setEventSlug("test-future-event");

		query.setVariables(ed);

		String request_Payload = gson.toJson(query);

		response = hit_GraphQL_EndPoint(request_Payload, null);

		stepInfo("Response Validation");

		JsonPath j = new JsonPath(response.asString());

		passStep("market id : " + j.getString("data.market.id"));
		passStep("market name : " + j.getString("data.market.name"));
		assertNotNull(j.getString("data.market.id"), "market id is null");

		passStep("venue id : " + j.getString("data.market.venue.id"));
		passStep("venue name : " + j.getString("data.market.venue.name"));

		passStep("event id : " + j.getString("data.market.venue.venue_event.id"));
		passStep("event name : " + j.getString("data.market.venue.venue_event.name"));

		System.out.println("========================= END =========================");

	}

	@Test(groups = "smoke", priority = 32)
	public void TC_32_signUp() {
		System.out.println("================= TC_32_signUp =================");

		Query query = gson.fromJson(getRequestBody("signup"), Query.class);

		String email = getCurrentUnixTimestamp() + "@test.com";
		SignUpVariable signUpVariable = new SignUpVariable();
		signUpVariable.setGenerate_jwt_token(false);
		signUpVariable.setRecaptcha_token(recaptcha_token);
		signUpVariable.setLogin(email);
		signUpVariable.setPassword(email);
		signUpVariable.setSource(Constants.WEB);

		query.setVariables(signUpVariable);

		String request_Payload = gson.toJson(query);

		response = hit_GraphQL_EndPoint(request_Payload, null);

		stepInfo("Response Validation");

		JsonPath j = new JsonPath(response.asString());

		passStep("id : " + j.getString("data.register.id"));
		passStep("email : " + j.getString("data.register.email"));
		assertEquals(j.getString("data.register.email"), email, "email mismatch");

		System.out.println("========================= END =========================");

	}

	@Test(groups = "smoke", dependsOnMethods = "TC_11_getLocationCurrentData", priority = 33)
	public void TC_33_createParking_with_Card() {

		System.out.println("================= TC_33_createParking_with_Card =================");

		Query query = gson.fromJson(getRequestBody("createParking"), Query.class);
		String token = null;

		CreateParkingVariable createParkingVariable = new CreateParkingVariable();
		Vehicles vehicle = new Vehicles();
		ParkingLotsVariable parkingLot = new ParkingLotsVariable();

		vehicle.setMake(getRandom_Vehicle_Make().toUpperCase());
		vehicle.setColor(getRandom_Vehicle_Color().toUpperCase());
		vehicle.setBody_type(getRandom_Vehicle_Type().toUpperCase());
		parkingLot.setUnknown_vehicle(true);

		token = getStripeToken();
		createParkingVariable.setToken(token);

		List<Vehicles> pl_vehicles = new ArrayList<>();
		pl_vehicles.add(vehicle);

		parkingLot.setVehicles(pl_vehicles);

		List<ParkingLotsVariable> parkingLots = new ArrayList<>();
		parkingLots.add(parkingLot);

		createParkingVariable.setMinutes(60);
		createParkingVariable.setApply_wallet_credit(true);
		createParkingVariable.setLocation_id(Integer.parseInt(location_id));
		createParkingVariable.setParking_time_type(Constants.SESSION);

		createParkingVariable.setSave_payment_method(true);
		createParkingVariable.setSource(Constants.WEB);
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

	@Test(groups = "smoke", dependsOnMethods = "TC_33_createParking_with_Card", priority = 34)
	public void TC_34_Extend_Existing_Parking() {

		System.out.println("================= TC_34_Extend_Existing_Parking =================");

		Query query = gson.fromJson(getRequestBody("extendParking"), Query.class);

		ExtendParkingVariable extendParkingVariable = new ExtendParkingVariable();

		extendParkingVariable.setMinutes(60);
		extendParkingVariable.setOrder_number_id(Long.parseLong(order_number_id));
		extendParkingVariable.setApply_wallet_credit(true);
		extendParkingVariable.setSave_payment_method(true);
		extendParkingVariable.setSource(Constants.WEB);
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

	//@Test(groups = "smoke", priority = 35)
	public void TC_35_getConfirmationPageData() {

		System.out.println("================= TC_35_getConfirmationPageData =================");

		Query query = gson.fromJson(getRequestBody("getConfirmationPageData"), Query.class);

		ConfirmationPageData cpd = new ConfirmationPageData();

		cpd.setOrder_number_id(Integer.parseInt(991369+""));
		query.setVariables(cpd);

		String request_Payload = gson.toJson(query);

		response = hit_GraphQL_EndPoint(request_Payload, auth_token);

		stepInfo("Response Validation");

		JsonPath j = new JsonPath(response.asString());
		passStep("id :" + j.getString("data.me.id"));
		assertEquals(j.getString("data.me.id"), order_number_id, "order number id mismatch");

		System.out.println("========================= END =========================");

	}

	@Test(groups = "smoke", priority = 36)
	public void TC_36_checkCondoNumber() {

		System.out.println("================= TC_36_checkCondoNumber =================");

		Query query = gson.fromJson(getRequestBody("checkCondoNumber"), Query.class);

		String request_Payload = gson.toJson(query);

		response = hit_GraphQL_EndPoint(request_Payload, auth_token);

		stepInfo("Response Validation");

		JsonPath j = new JsonPath(response.asString());
		passStep("name :" + j.getString("data.unit_number.name"));
		assertEquals(j.getString("data.unit_number.name"), "PPFSTEST", "name mismatch");

		System.out.println("========================= END =========================");

	}

	@Test(groups = "smoke", priority = 37)
	public void TC_37_getLocationFloors() {

		System.out.println("================= TC_36_getLocationFloors =================");

		Query query = gson.fromJson(getRequestBody("getLocationFloors"), Query.class);

		String request_Payload = gson.toJson(query);

		response = hit_GraphQL_EndPoint(request_Payload, auth_token);

		stepInfo("Response Validation");

		JsonPath j = new JsonPath(response.asString());
		passStep("id :" + j.getString("data.location.id"));
		List<Object> floors = j.getList("data.location.floors");
		assertTrue(floors.size() > 0, "Floors size is 0");
		passStep("Floors size is " + floors.size());
		for (int floor = 0; floor < floors.size(); floor++) {
			passStep("Floor " + (floor + 1));
			passStep("title : " + j.getString("data.location.floors[" + floor + "].title"));
			passStep("min_price : " + j.getString("data.location.floors[" + floor + "].min_price"));
			passStep("zlevel : " + j.getString("data.location.floors[" + floor + "].zlevel"));

		}

		System.out.println("========================= END =========================");

	}
	
	@Test(groups = "smoke", priority = 38)
	public void TC_38_getLocationSpaces() {

		System.out.println("================= TC_38_getLocationSpaces =================");

		Query query = gson.fromJson(getRequestBody("getLocationSpaces"), Query.class);
		
		LocationSpace ls = new LocationSpace();
		ls.setSlug("P373");
		ls.setZlevel(1);
		
		query.setVariables(ls);
		

		String request_Payload = gson.toJson(query);

		response = hit_GraphQL_EndPoint(request_Payload, auth_token);

		stepInfo("Response Validation");

		JsonPath j = new JsonPath(response.asString());
		passStep("id :" + j.getString("data.location.id"));
		List<Object> spaces = j.getList("data.location.spaces");
		assertTrue(spaces.size() > 0, "spaces size is 0");
		passStep("spaces size is " + spaces.size());
		for (int space = 0; space < spaces.size(); space++) {
			passStep("Space " + (space + 1));
			passStep("id : " + j.getString("data.location.spaces[" + space + "].id"));
			passStep("occupied : " + j.getBoolean("data.location.spaces[" + space + "].occupied"));
			passStep("pre_tax_price : " + j.getString("data.location.spaces[" + space + "].pre_tax_price"));
			passStep("title : " + j.getString("data.location.spaces[" + space + "].title"));
			passStep("kind_text : " + j.getString("data.location.spaces[" + space + "].kind_text"));
			passStep("description : " + j.getString("data.location.spaces[" + space + "].description"));

		}

		System.out.println("========================= END =========================");

	}
	
	@Test(groups = "smoke", priority = 39)
	public void TC_39_checkLogin() {

		System.out.println("================= TC_38_checkLogin =================");

		Query query = gson.fromJson(getRequestBody("checkLogin"), Query.class);
		
		String request_Payload = gson.toJson(query);

		response = hit_GraphQL_EndPoint(request_Payload, auth_token);

		stepInfo("Response Validation");
		

		System.out.println("========================= END =========================");

	}
	
	@Test(groups = "smoke", priority = 40)
	public void TC_40_updateProfile() {

		System.out.println("================= TC_40_updateProfile =================");

		Query query = gson.fromJson(getRequestBody("updateProfile"), Query.class);
		
		String request_Payload = gson.toJson(query);

		response = hit_GraphQL_EndPoint(request_Payload, auth_token);
		stepInfo("Response Validation");
		

		System.out.println("========================= END =========================");

	}
	
	
	@Test(groups = "smoke", priority = 41)
	public void TC_41_getHeaderData() {

		System.out.println("================= TC_40_getHeaderData =================");

		Query query = gson.fromJson(getRequestBody("getHeaderData"), Query.class);
		
		String request_Payload = gson.toJson(query);

		response = hit_GraphQL_EndPoint(request_Payload, auth_token);
		stepInfo("Response Validation");
		
		JsonPath j = new JsonPath(response.asString());
		List<Object> headers = j.getList("data.static_page_groups");
		assertTrue(headers.size() > 0, "headers size is 0");		
		passStep("headers size is " + headers.size());
		
		for (int header = 0; header < headers.size(); header++) {
			passStep("header " + (header + 1));
			passStep("title : " + j.getString("data.static_page_groups[" + header + "].title"));
			passStep("slug : " + j.getString("data.static_page_groups[" + header + "].slug"));

		}
		

		System.out.println("========================= END =========================");

	}
	

	@Test(groups = "smoke", priority = 41)
	public void TC_41_getContactPageData() {

		System.out.println("================= TC_41_getContactPageData =================");

		Query query = gson.fromJson(getRequestBody("getContactPageData"), Query.class);
		
		String request_Payload = gson.toJson(query);

		response = hit_GraphQL_EndPoint(request_Payload, auth_token);
		stepInfo("Response Validation");
		
		JsonPath j = new JsonPath(response.asString());

		List<Object> market_list = j.getList("data.markets");
		passStep("Received " + market_list.size() + " markets for footer data");

		assertNotNull(market_list, "markets list is null");

		if (market_list.size() > 0) {
			for (int market = 0; market < market_list.size(); market++) {
				stepInfo("Market " + (market + 1) + " details");
				passStep("market id " + j.getString("data.markets[" + market + "].id"));
				passStep("market slug " + j.getString("data.markets[" + market + "].slug"));
				passStep("market name " + j.getString("data.markets[" + market + "].name"));
			}
		}
		

		System.out.println("========================= END =========================");

	}
	

	@Test(groups = "smoke", priority = 42)
	public void TC_42_getMarketVenuesData() {

		System.out.println("================= TC_42_getMarketVenuesData =================");

		Query query = gson.fromJson(getRequestBody("getMarketVenuesData"), Query.class);
		
		MarketVenue mv = new MarketVenue();
		mv.setLimit(7);
		mv.setOffset(5);
		mv.setMarketSlug("new-orleans");
		
		query.setVariables(mv);
		
		String request_Payload = gson.toJson(query);

		response = hit_GraphQL_EndPoint(request_Payload, auth_token);
		stepInfo("Response Validation");
		
		JsonPath j = new JsonPath(response.asString());

		List<Object> venues = j.getList("data.market.venues");
		passStep("Received " + venues.size() + " venues for footer data");

		assertNotNull(venues, "venues list is null");

		if (venues.size() > 0) {
			for (int venue = 0; venue < venues.size(); venue++) {
				stepInfo("Venue " + (venue + 1) + " details");
				passStep("address " + j.getString("data.market.venues[" + venue + "].address"));
				passStep("name " + j.getString("data.market.venues[" + venue + "].name"));
				passStep("id " + j.getString("data.market.venues[" + venue + "].id"));
			}
		}
		

		System.out.println("========================= END =========================");

	}
	
	@Test(groups = "smoke", priority = 43)
	public void TC_43_quote() {

		System.out.println("================= TC_43_quote =================");

		Query query = gson.fromJson(getRequestBody("quote"), Query.class);
		
		Quote quote = new Quote();
		quote.setAddress("Test Address");
		quote.setCity("Hyderabad");
		quote.setEmail(spa_userName);
		quote.setFacilityType("RETAIL");
		quote.setName("New_Location_"+getTimestamp());
		quote.setPhone(getRandomUSPhoneNumber());
		quote.setSpaceCount("500");
		quote.setStartDate("LESS_THAN_THREE_MONTHS");
		quote.setFloors(2);
		quote.setGates(true);
		quote.setNewConstruction(true);
		
		query.setVariables(quote);
		
		String request_Payload = gson.toJson(query);

		response = hit_GraphQL_EndPoint(request_Payload, auth_token);
		stepInfo("Response Validation");
		JsonPath j = new JsonPath(response.asString());
		passStep("email : "+j.getString("data.create_instant_quote.email"));
		assertEquals(j.getString("data.create_instant_quote.email"),spa_userName, "email mismatch");
		
	
		System.out.println("========================= END =========================");

	}
	
	@Test(groups = "smoke", priority = 44)
	public void TC_44_contact() {

		System.out.println("================= TC_44_contact =================");

		Query query = gson.fromJson(getRequestBody("contact"), Query.class);		
		
		String request_Payload = gson.toJson(query);

		response = hit_GraphQL_EndPoint(request_Payload, auth_token);
		stepInfo("Response Validation");
		JsonPath j = new JsonPath(response.asString());
		passStep("email : "+j.getString("data.request_contact.email"));
		assertEquals(j.getString("data.request_contact.email"),"venu.t@comakeit.com", "email mismatch");
		
	
		System.out.println("========================= END =========================");

	}
	
	@Test(groups = "smoke", priority = 45, dependsOnMethods = "TC_33_createParking_with_Card")
	public void TC_45_OrderData() {

		System.out.println("================= TC_45_OrderData =================");

		Query query = gson.fromJson(getRequestBody("OrderData"), Query.class);		
		OrderData od = new OrderData();
		od.setOrderNumber(Integer.parseInt(order_number_id));
		query.setVariables(od);
		String request_Payload = gson.toJson(query);

		response = hit_GraphQL_EndPoint(request_Payload, auth_token);
		stepInfo("Response Validation");
		JsonPath j = new JsonPath(response.asString());
		passStep("location_id : "+j.getString("data.me.parkings.location_id"));
		assertNotNull(j.getString("data.me.parkings.location_id"),"Loction id is null");
		
	
		System.out.println("========================= END =========================");

	}
}
