package com.graphQL.pojo;

public class CreateParking {

	String operationName;
	CreateParkingVariable variables;
	String query;

	public String getOperationName() {
		return operationName;
	}

	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}

	public CreateParkingVariable getVariables() {
		return variables;
	}

	public void setVariables(CreateParkingVariable variables) {
		this.variables = variables;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	@Override
	public String toString() {
		return "{operationName=" + operationName + ", variables=" + variables + ", query=" + query + "}";
	}
	
	

}
