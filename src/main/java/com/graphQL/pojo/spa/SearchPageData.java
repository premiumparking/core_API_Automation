package com.graphQL.pojo.spa;

public class SearchPageData {

	boolean exceptCruise;
	long expiresAt, startsAt;
	double latitude, longitude, neLatitude, neLongitude, swLatitude, swLongitude;
	int mapZoom;
	String name, rateType;

	public boolean isExceptCruise() {
		return exceptCruise;
	}

	public void setExceptCruise(boolean exceptCruise) {
		this.exceptCruise = exceptCruise;
	}

	public long getExpiresAt() {
		return expiresAt;
	}

	public void setExpiresAt(long expiresAt) {
		this.expiresAt = expiresAt;
	}

	public long getStartsAt() {
		return startsAt;
	}

	public void setStartsAt(long startsAt) {
		this.startsAt = startsAt;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getNeLatitude() {
		return neLatitude;
	}

	public void setNeLatitude(double neLatitude) {
		this.neLatitude = neLatitude;
	}

	public double getNeLongitude() {
		return neLongitude;
	}

	public void setNeLongitude(double neLongitude) {
		this.neLongitude = neLongitude;
	}

	public double getSwLatitude() {
		return swLatitude;
	}

	public void setSwLatitude(double swLatitude) {
		this.swLatitude = swLatitude;
	}

	public double getSwLongitude() {
		return swLongitude;
	}

	public void setSwLongitude(double swLongitude) {
		this.swLongitude = swLongitude;
	}

	public int getMapZoom() {
		return mapZoom;
	}

	public void setMapZoom(int mapZoom) {
		this.mapZoom = mapZoom;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRateType() {
		return rateType;
	}

	public void setRateType(String rateType) {
		this.rateType = rateType;
	}

}
