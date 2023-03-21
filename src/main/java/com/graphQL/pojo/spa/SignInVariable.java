package com.graphQL.pojo.spa;

public class SignInVariable {

	Boolean generate_jwt_token;
	String source;
	String login;
	String password;
	public Boolean getGenerate_jwt_token() {
		return generate_jwt_token;
	}
	public void setGenerate_jwt_token(Boolean generate_jwt_token) {
		this.generate_jwt_token = generate_jwt_token;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	

}
