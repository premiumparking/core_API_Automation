package textpay;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import com.google.gson.Gson;
import com.graphQL.pojo.CreateParking_Card;
import com.graphQL.pojo.CreateParking_PromoCode;

import components.BaseClass;
import components.Constants;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class Create_APIs extends BaseClass {

	Gson gson = new Gson();

	private String createParking_PromoCode = getRequestBody("createParking_PromoCode");
	private String createParking_Card = getRequestBody("createParking_Card");
	private String createParkingQuery_Card = getRequestBody("createParking");
	

	@Test()
	public void TextPay_TC_01_Purchase_Session_WithPromoCode() {

		for (int i = 1; i <= 5; i++) {
			//CreateParking_PromoCode parking = LoadJsonData.getParkingObject(createParkingQuery);
			CreateParking_PromoCode parking = gson.fromJson(createParking_PromoCode, CreateParking_PromoCode.class);

			// Setting the test data
			parking.getVariables().setSource(Constants.TEXTPAY);
			parking.getVariables().setPromo_code(Constants.PROMO_100);
			parking.getVariables().setLocation_id(getRandomLocation_Ids());
			parking.getVariables().getParking_lots().get(0).getVehicles().get(0)
					.setLicense_plate(getRandomLicencePlate());
			parking.getVariables().setMinutes(120);

			//String request_Payload = LoadJsonData.convertToJSON(parking);
			String request_Payload = gson.toJson(parking);

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

			System.out.println("******* Order Number : "+order_id);
			System.out.println("***"+i+" **** Order Number : "+order_id);
			passStep("Confirmation Order Number  : " + order_id);
			passStep("Promo code  : " + promoCode);
			passStep("Rate Name  : " + rateName);

			//assertEquals(rateName, "2 Hour");
			assertEquals(promoCode, Constants.PROMO_100);
	}

	}
	
	//@Test()
	public void TextPay_TC_02_Purchase_Session_WithCard() {

		//for (int i = 1; i < 10; i++) {
			//CreateParking_PromoCode parking = LoadJsonData.getParkingObject(createParkingQuery);
			CreateParking_Card parking = gson.fromJson(createParking_Card, CreateParking_Card.class);

			// Setting the test data
			parking.getVariables().setSource(Constants.TEXTPAY);			
			parking.getVariables().setLocation_id(101);
			parking.getVariables().getParking_lots().get(0).getVehicles().get(0)
					.setLicense_plate(getRandomLicencePlate());
			parking.getVariables().setMinutes(120);

			//String request_Payload = LoadJsonData.convertToJSON(parking);
			String request_Payload = gson.toJson(parking);

			RestAssured.baseURI = uri;
			stepInfo("Request Payload");

			passStep(request_Payload);

			Response resp = given().log().all().contentType("application/json")
					.headers("source-auth-token", source_auth_token).header("x-auth-token", x_auth_token)
					.body(request_Payload).when().log().all().post("/graphql");
			stepInfo("Response Body");
			passStep(resp.asString());
			System.out.println(resp.asString());
			

			stepInfo("Response Validation");

			passStep("Received Status code : " + resp.getStatusCode());
			assertEquals(resp.getStatusCode(), 200);
			JsonPath j = new JsonPath(resp.asString());

			String order_id = j.getString("data.create_parking.order_number_id");
			String promoCode = j.getString("data.create_parking.promo_code");
			String rateName = j.getString("data.create_parking.last_payment.rate_name");

			System.out.println("******* Order Number : "+order_id);
			passStep("Confirmation Order Number  : " + order_id);
			passStep("Promo code  : " + promoCode);
			passStep("Rate Name  : " + rateName);

			assertEquals(rateName, "2 Hour");
			assertEquals(promoCode, Constants.PROMO_100);
	//	}

	}

}
