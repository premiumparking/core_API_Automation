package com.graphQL.pojo;

public class GetStates {
    String operationName;
    LocationVariable variables;
    String query;

    public String getOperationName() {
        return operationName;
    }
    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }
    public LocationVariable getVariables() {
        return variables;
    }
    public void setVariables(LocationVariable variables) {
        this.variables = variables;
    }
    public String getQuery() {
        return query;
    }
    public void setQuery(String query) {
        this.query = query;
    }
}
