package textpay;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import com.graphQL.pojo.Parking;
import com.graphQL.utility.BaseClass;
import com.graphQL.utility.LoadJsonData;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class Create_APIs extends BaseClass {

	LoadJsonData loadJSONdata = new LoadJsonData();
	private String createParking = getRequestBody("createParking");

	@Test()
	public void TC_01_Create_Parking_using_TextPay() {

		Parking parking = LoadJsonData.getParkingObject(createParking);

		// Change of request payload
		parking.getVariables().getParking_lots().get(0).getVehicles().get(0).setLicense_plate(getRandomLicencePlate());
		parking.getVariables().setMinutes(120); // 2 hours
		String request_Payload = LoadJsonData.convertToJSON(parking);

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

		String order_id = j.getString("data.create_parking.order_number_id");
		String promoCode = j.getString("data.create_parking.promo_code");
		String rateName = j.getString("data.create_parking.last_payment.rate_name");

		passStep("Confirmation Order Number  : " + order_id);
		passStep("Promo code  : " + promoCode);
		passStep("Rate Name  : " + rateName);

		assertEquals(rateName, "2 Hour");
		assertEquals(promoCode, "PROMO100");

	}

}
