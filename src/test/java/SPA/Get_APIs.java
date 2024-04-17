package SPA;

import org.testng.annotations.Test;
import org.testng.AssertJUnit;
import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

import com.graphQL.pojo.GetSpecificState;
import com.graphQL.pojo.GetStates;
import org.testng.annotations.Test;

import com.google.gson.Gson;
import com.graphQL.pojo.GetLocationPageData;
import com.graphQL.pojo.GetLocationRates;

import components.BaseClass;
import components.Constants;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import java.util.*;

public class Get_APIs extends BaseClass {

	Gson gson = new Gson();
	private String getLocationPageData = getRequestBody("getLocationData");
	private String getLocationRates = getRequestBody("getLocationRates");
	private String getStatesBody = getRequestBody("getStates");
	private String getSpecificStateBody = getRequestBody("getState");

	@Test()
	public void TextPay_TC_01_getLocationPageData() {

		GetLocationPageData getLocPage = gson.fromJson(getLocationPageData, GetLocationPageData.class);

		// Setting the test data
		getLocPage.getVariables().setName(getRandomLocation());
		getLocPage.getVariables().getSources().set(0, Constants.TEXTPAY);

		String request_Payload = gson.toJson(getLocPage);

		RestAssured.baseURI = uri;
		stepInfo("Request Payload");

		passStep(request_Payload);

		Response resp = given().log().all().contentType("application/json")
				.headers("source-auth-token", source_auth_token).header("x-auth-token", x_auth_token)
				.body(request_Payload).when().log().all().post("/graphql");
		stepInfo("Response Body");
		passStep(resp.asString());

		stepInfo("Response Validation");

		passStep("Received Status code : " + resp.getStatusCode());
		AssertJUnit.assertEquals(resp.getStatusCode(), 200);
		JsonPath j = new JsonPath(resp.asString());

		passStep("Location Id  : " + j.getString("data.location.id"));
		passStep("Location Name  : " + j.getString("data.location.name"));
		passStep("Location Friendly_name  : " + j.getString("data.location.friendly_name"));
		passStep("Location Address  : " + j.getString("data.location.address"));

	}

	@Test()
	public void TextPay_TC_02_getLocationRates() {

		GetLocationRates getLocRates = gson.fromJson(getLocationRates, GetLocationRates.class);

		// Setting the test data
		getLocRates.getVariables().setName(getRandomLocation());
		getLocRates.getVariables().setTimestamp(getCurrentUnixTimestamp());

		String request_Payload = gson.toJson(getLocRates);

		RestAssured.baseURI = uri;
		stepInfo("Request Payload");

		passStep(request_Payload);

		Response resp = given().log().all().contentType("application/json")
				.headers("source-auth-token", source_auth_token).header("x-auth-token", x_auth_token)
				.body(request_Payload).when().log().all().post("/graphql");
		stepInfo("Response Body");
		passStep(resp.asString());

		stepInfo("Response Validation");

		passStep("Received Status code : " + resp.getStatusCode());
		AssertJUnit.assertEquals(resp.getStatusCode(), 200);
		JsonPath j = new JsonPath(resp.asString());
		
		// Getting rate groups list
		List<Object> rg_list = j.getList("data.location.rate_groups");
		passStep("Found " + rg_list.size() + " rate group(s)");
		for (int i = 0; i < rg_list.size(); i++) {
			stepInfo("Rate group " + (i + 1) + " data");
			passStep("Rate Group Id  : " + j.getString("data.location.rate_groups[" + i + "].id"));
			passStep("Rate Group Title  : " + j.getString("data.location.rate_groups[" + i + "].title"));
			
			// Getting rates list
			List<Object> rate_List = j.getList("data.location.rate_groups[" + i + "].rates");
			passStep("Found " + rate_List.size() + " rate(s)");
			for (int rj = 0; rj < rate_List.size(); rj++) {
				stepInfo("Rate " + (rj + 1) + " data");
				passStep("Rate Kind  : " + j.getString("data.location.rate_groups[" + i + "].rates[" + rj + "].kind"));
				passStep("Rate Name  : " + j.getString("data.location.rate_groups[" + i + "].rates[" + rj + "].name"));
				passStep("Rate Pre_Tax price  : "
						+ j.getString("data.location.rate_groups[" + i + "].rates[" + rj + "].pre_tax_price"));
				passStep(
						"Rate Price  : " + j.getString("data.location.rate_groups[" + i + "].rates[" + rj + "].price"));
				passStep("Rate Visibility  : "
						+ j.getString("data.location.rate_groups[" + i + "].rates[" + rj + "].visibility"));
			}

		}

	}

	@Test()
	public void TextPay_TC_03_getStates() {

		GetStates getStates = gson.fromJson(getStatesBody, GetStates.class);

		String request_Payload = gson.toJson(getStates);

		RestAssured.baseURI = uri;
		stepInfo("Request Payload");

		passStep(request_Payload);

		Response resp = given().log().all().contentType("application/json")
				.headers("source-auth-token", source_auth_token).header("x-auth-token", x_auth_token)
				.body(request_Payload).when().log().all().post("/graphql");
		stepInfo("Response Body");
		passStep(resp.asString());

		stepInfo("Response Validation");

		passStep("Received Status code : " + resp.getStatusCode());
		AssertJUnit.assertEquals(resp.getStatusCode(), 200);
		JsonPath j = new JsonPath(resp.asString());

		passStep("State abbreviation  : " + j.getString("data.states.abbreviation"));
		passStep("State name  : " + j.getString("data.states.name"));
		passStep("State description  : " + j.getString("data.states.description"));

	}

	@Test()
	public void TextPay_TC_04_getSpecificState() {

//		GetSpecificState getSpecificState = gson.fromJson(getSpecificStateBody, GetSpecificState.class);
//		getSpecificState.getVariables().setEnum("LOUISIANA");

		String request_Payload = gson.toJson(getSpecificStateBody);

		RestAssured.baseURI = uri;
		stepInfo("Request Payload");

		passStep(request_Payload);

		Response resp = given().log().all().contentType("application/json")
				.headers("source-auth-token", source_auth_token).header("x-auth-token", x_auth_token)
				.body(request_Payload).when().log().all().post("/graphql");
		stepInfo("Response Body");
		passStep(resp.asString());

		stepInfo("Response Validation");

		passStep("Received Status code : " + resp.getStatusCode());
		AssertJUnit.assertEquals(resp.getStatusCode(), 200);
		JsonPath j = new JsonPath(resp.asString());

		passStep("State abbreviation  : " + j.getString("data.state.abbreviation"));
		passStep("State name  : " + j.getString("data.state.name"));
		passStep("State description  : " + j.getString("data.state.description"));

	}

}
