package com.graphQL.pojo.textpay.hotel;

public class GetLocationRates {
    String operationName;
    LocationRatesVariable variables;
    String query;
    public String getOperationName() {
        return operationName;
    }
    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }
    public LocationRatesVariable getVariables() {
        return variables;
    }
    public void setVariables(LocationRatesVariable variables) {
        this.variables = variables;
    }
    public String getQuery() {
        return query;
    }
    public void setQuery(String query) {
        this.query = query;
    }
}
