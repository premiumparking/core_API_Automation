package com.graphQL.pojo.textpay;

import java.util.List;

public class ParkingLotsVariable {
	List<Vehicles> vehicles;
	boolean unknown_vehicle;

	public List<Vehicles> getVehicles() {
		return vehicles;
	}

	public void setVehicles(List<Vehicles> vehicles) {
		this.vehicles = vehicles;
	}

	public boolean isUnknown_vehicle() {
		return unknown_vehicle;
	}

	public void setUnknown_vehicle(boolean unknown_vehicle) {
		this.unknown_vehicle = unknown_vehicle;
	}

}
