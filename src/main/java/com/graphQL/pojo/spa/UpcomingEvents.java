package com.graphQL.pojo.spa;

public class UpcomingEvents {

	String marketSlug, venueSlug;
	int limit;
	long startsAtFrom;

	public String getMarketSlug() {
		return marketSlug;
	}

	public void setMarketSlug(String marketSlug) {
		this.marketSlug = marketSlug;
	}

	public String getVenueSlug() {
		return venueSlug;
	}

	public void setVenueSlug(String venueSlug) {
		this.venueSlug = venueSlug;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public long getStartsAtFrom() {
		return startsAtFrom;
	}

	public void setStartsAtFrom(long startsAtFrom) {
		this.startsAtFrom = startsAtFrom;
	}

}
