package com.graphQL.pojo;

public class CreateParking_PromoCode {

	String operationName;
	Variable_PromoCode variables;
	String query;

	public String getOperationName() {
		return operationName;
	}

	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}

	public Variable_PromoCode getVariables() {
		return variables;
	}

	public void setVariables(Variable_PromoCode variables) {
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
