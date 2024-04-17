package textpay.hotelParking;

import org.testng.annotations.Test;
import org.testng.AssertJUnit;
import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

import java.util.List;

import org.testng.annotations.Test;

import com.google.gson.Gson;
import com.graphQL.pojo.textpay.hotel.GetLocationPageData;
import com.graphQL.pojo.textpay.hotel.GetLocationRates;

import components.BaseClass;
import components.Constants;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class HotelParking_APIs extends BaseClass {

	Gson gson = new Gson();

	private String getLocationPageData = getRequestBody("getLocationPageData");
	private String getLocationRates = getRequestBody("getLocationRates");
	private String validateHotelGuest = getRequestBody("validateHotelGuest");
	private String createParking = getRequestBody("createParking");
	private String getConfirmationPageData = getRequestBody("getConfirmationPageData");

	@Test()
	public void TC_01_TextPay_Hotel_GetLocationPageDate() {

		GetLocationPageData getLocPageData = gson.fromJson(getLocationPageData, GetLocationPageData.class);

		// Setting the test data
		getLocPageData.getVariables().setName(Constants.HOTEL_LOCATION);
		getLocPageData.getVariables().getSources().set(0, Constants.TEXTPAY);

		String request_Payload = gson.toJson(getLocPageData);

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

		String location_id = j.getString("data.location.id");
		String location_name = j.getString("data.location.name");
		List<String> hotels = j.getList("data.location.hotels");

		System.out.println("******* Location Name : " + location_name);
		passStep("Location ID" + location_id);
		passStep("Location Name" + location_name);
		passStep("Found " + hotels.size() + " hotels");
		for (int h = 0; h < hotels.size(); h++) {
			stepInfo("Hotel " + (h + 1) + " :------------------");
			passStep("Hotel Name " + j.getString("data.location.hotels[" + h + "].name"));

		}
		AssertJUnit.assertEquals(location_name, Constants.HOTEL_LOCATION);

	}

	@Test()
	public void TC_02_TextPay_Hotel_GetLocationRates() {

		GetLocationRates getLocRates = gson.fromJson(getLocationRates, GetLocationRates.class);

		// Setting the test data
		getLocRates.getVariables().setName(Constants.HOTEL_LOCATION);
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
		//System.out.println(resp.asString());

		stepInfo("Response Validation");

		passStep("Received Status code : " + resp.getStatusCode());
		AssertJUnit.assertEquals(resp.getStatusCode(), 200);
		JsonPath j = new JsonPath(resp.asString());

		String location_id = j.getString("data.location.id");
		List<String> rate_groups = j.getList("data.location.rate_groups");

		passStep("Location ID :" + location_id);
		passStep("Found " + rate_groups.size() + " rate groups");
		if (rate_groups.size() > 0) {
			for (int rg = 0; rg < rate_groups.size(); rg++) {
				List<String> rates = j.getList("data.location.rate_groups[" + rg + "].rates");
				if (rates.size() > 0) {
					for (int r = 0; r < rates.size(); r++) {
						String rateName = j.getString("data.location.rate_groups[" + rg + "].rates[" + r + "].name");
						String kind = j.getString("data.location.rate_groups[" + rg + "].rates[" + r + "].kind");
						System.out.println(rateName + "  --- "+kind );
						System.out.println(rg + "  --- "+r );
						
						if (kind.toLowerCase().contains("hotel")) {
							//String kind = j.getString("data.location.rate_groups[" + rg + "].rates[" + r + "].kind");
							String minutes = j.getString("data.location.rate_groups[" + rg + "].rates[" + r + "].minutes");
							String descriptive_title = j.getString("data.location.rate_groups[" + rg + "].rates[" + r + "].descriptive_title");

							stepInfo("Rate info :-----------------------");
							passStep("Hotel Rate name : " + rateName);
							passStep("kind : " + kind);
							passStep("minutes : " + minutes);
							passStep("descriptive title : " + descriptive_title);
						}
					}
				}
			}
		}

	}

}
