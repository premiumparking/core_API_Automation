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

public class Sign_In extends BaseClass {

	Gson gson = new Gson();

	private String spa_signIn = getRequestBody("spa_signIn");

	@Test
	public Account signIn() {

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

		String x_auth_token = j.getString("data.login.auth_token");
		Account account = new Account();
		System.out.println("****************" + x_auth_token);
		account.setAuth_token(x_auth_token);
		account.setPayment_methods(j.getList("data.login.payment_methods"));
		return account;

	}

}
