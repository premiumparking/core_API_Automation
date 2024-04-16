package com.graphQL.pojo.textpay;

public class VerifyPhoneNumberVariable {
    String phone;
    String code;
    boolean generate_jwt_token;


    
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public boolean isGenerate_jwt_token() {
		return generate_jwt_token;
	}

	public void setGenerate_jwt_token(boolean generate_jwt_token) {
		this.generate_jwt_token = generate_jwt_token;
	}
   
   
}
