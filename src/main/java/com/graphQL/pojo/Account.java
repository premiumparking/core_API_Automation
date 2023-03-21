package com.graphQL.pojo;

import java.util.List;

public class Account {

	String email;
	String password;
	String first_name;
	String last_name;
	String auth_token;
	List<PaymentMethod> payment_methods;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public String getAuth_token() {
		return auth_token;
	}

	public void setAuth_token(String auth_token) {
		this.auth_token = auth_token;
	}

	public List<PaymentMethod> getPayment_methods() {
		return payment_methods;
	}

	public void setPayment_methods(List<PaymentMethod> payment_methods) {
		this.payment_methods = payment_methods;
	}

}
