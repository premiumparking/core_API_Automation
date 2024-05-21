package com.graphQL.pojo.spa;

public class EventData {

	String slug, venueSlug,eventSlug;
	int nearbyLocationsLimit;

	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

	public String getVenueSlug() {
		return venueSlug;
	}

	public void setVenueSlug(String venueSlug) {
		this.venueSlug = venueSlug;
	}

	public int getNearbyLocationsLimit() {
		return nearbyLocationsLimit;
	}

	public void setNearbyLocationsLimit(int nearbyLocationsLimit) {
		this.nearbyLocationsLimit = nearbyLocationsLimit;
	}

	public String getEventSlug() {
		return eventSlug;
	}

	public void setEventSlug(String eventSlug) {
		this.eventSlug = eventSlug;
	}
	
	

}
