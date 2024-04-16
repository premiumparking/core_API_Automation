package com.graphQL.pojo.textpay;

public class Query {
	String operationName;
	Object variables;
	String query;

	public String getOperationName() {
		return operationName;
	}

	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}

	public Object getVariables() {
		return variables;
	}

	public void setVariables(Object variables) {
		this.variables = variables;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

}
