package com.graphQL.utility;

import com.google.gson.Gson;
import com.graphQL.pojo.Parking;

public class LoadJsonData {

	public static Parking getParkingObject(String query) {

		// Creating a Gson Object
		Gson gson = new Gson();

		Parking parking = gson.fromJson(query, Parking.class);

		// return object
		return parking;
	}

	public static String convertToJSON(Parking parking) {

		// Creating a Gson Object
		Gson gson = new Gson();

		// return object
		return new Gson().toJson(parking);
	}

}
