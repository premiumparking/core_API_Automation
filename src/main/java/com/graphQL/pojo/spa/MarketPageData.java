package com.graphQL.pojo.spa;

public class MarketPageData {

	String slug;
	int popularVenuesLimit, venuesInGroupsLimit;

	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

	public int getPopularVenuesLimit() {
		return popularVenuesLimit;
	}

	public void setPopularVenuesLimit(int popularVenuesLimit) {
		this.popularVenuesLimit = popularVenuesLimit;
	}

	public int getVenuesInGroupsLimit() {
		return venuesInGroupsLimit;
	}

	public void setVenuesInGroupsLimit(int venuesInGroupsLimit) {
		this.venuesInGroupsLimit = venuesInGroupsLimit;
	}

}
