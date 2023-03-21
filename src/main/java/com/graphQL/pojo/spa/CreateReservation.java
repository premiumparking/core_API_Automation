package com.graphQL.pojo.spa;

public class CreateReservation {

	String operationName;
	ReservationVariables variables;
	String query;

	public String getOperationName() {
		return operationName;
	}

	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}

	public ReservationVariables getVariables() {
		return variables;
	}

	public void setVariables(ReservationVariables variables) {
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
