package com.graphQL.pojo.spa;

public class ResetPassword {

	Boolean generate_jwt_token;
	String new_password;
	String token;

	public Boolean getGenerate_jwt_token() {
		return generate_jwt_token;
	}

	public void setGenerate_jwt_token(Boolean generate_jwt_token) {
		this.generate_jwt_token = generate_jwt_token;
	}

	public String getNew_password() {
		return new_password;
	}

	public void setNew_password(String new_password) {
		this.new_password = new_password;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
