package com.graphQL.pojo.spa;

public class AutoCompleteVariable {

	Boolean withStreetAddress;
	String query;
	Double latitude, longitude;
	Long radius;

	public Boolean getWithStreetAddress() {
		return withStreetAddress;
	}

	public void setWithStreetAddress(Boolean withStreetAddress) {
		this.withStreetAddress = withStreetAddress;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Long getRadius() {
		return radius;
	}

	public void setRadius(Long radius) {
		this.radius = radius;
	}

}
