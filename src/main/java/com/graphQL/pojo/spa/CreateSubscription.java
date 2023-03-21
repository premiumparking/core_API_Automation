package com.graphQL.pojo.spa;

public class CreateSubscription {

	String operationName;
	SubsrptionVariables variables;
	String query;

	public String getOperationName() {
		return operationName;
	}

	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}

	public SubsrptionVariables getVariables() {
		return variables;
	}

	public void setVariables(SubsrptionVariables variables) {
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
