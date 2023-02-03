package com.graphQL.pojo;

import java.util.List;

public class LocationVariable {

	String name;
	List<String> sources;
	String timestamp;
	String Enum;

	public String getEnum() {
		return Enum;
	}

	public void setEnum(String anEnum) {
		Enum = anEnum;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

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
