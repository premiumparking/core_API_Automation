package com.graphQL.pojo.textpay.hotel;

import java.util.List;

public class LocationPageDataVariables {

	String name;
	List<String> sources;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getSources() {
		return sources;
	}

	public void setSources(List<String> sources) {
		this.sources = sources;
	}

}
