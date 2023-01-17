package textpay;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import com.google.gson.Gson;
import com.graphQL.pojo.CreateParking;
import com.graphQL.pojo.GetLocationPageData;

import components.BaseClass;
import components.Constants;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class Get_APIs extends BaseClass {

	Gson gson = new Gson();
	private String getLocationPageData = getRequestBody("getLocationData");

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
		assertEquals(resp.getStatusCode(), 200);
		JsonPath j = new JsonPath(resp.asString());

		passStep("Location Id  : " + j.getString("data.location.id"));
		passStep("Location Name  : " + j.getString("data.location.name"));
		passStep("Location Friendly_name  : " + j.getString("data.location.friendly_name"));
		passStep("Location Address  : " + j.getString("data.location.address"));

	}

}
