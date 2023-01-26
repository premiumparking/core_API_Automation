package com.graphQL.pojo;

import java.util.List;

public class Variable_Card {

	int minutes;
	String token;
	Boolean apply_wallet_credit;
	int location_id;
	String parking_time_type;
	Boolean save_payment_method;
	String payment_method_type;
	String source;
	List<String> partner_product_prices;
	List<Vehicle> parking_lots;
	public int getMinutes() {
		return minutes;
	}
	public void setMinutes(int minutes) {
		this.minutes = minutes;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public Boolean getApply_wallet_credit() {
		return apply_wallet_credit;
	}
	public void setApply_wallet_credit(Boolean apply_wallet_credit) {
		this.apply_wallet_credit = apply_wallet_credit;
	}
	public int getLocation_id() {
		return location_id;
	}
	public void setLocation_id(int location_id) {
		this.location_id = location_id;
	}
	public String getParking_time_type() {
		return parking_time_type;
	}
	public void setParking_time_type(String parking_time_type) {
		this.parking_time_type = parking_time_type;
	}
	public Boolean getSave_payment_method() {
		return save_payment_method;
	}
	public void setSave_payment_method(Boolean save_payment_method) {
		this.save_payment_method = save_payment_method;
	}
	public String getPayment_method_type() {
		return payment_method_type;
	}
	public void setPayment_method_type(String payment_method_type) {
		this.payment_method_type = payment_method_type;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public List<String> getPartner_product_prices() {
		return partner_product_prices;
	}
	public void setPartner_product_prices(List<String> partner_product_prices) {
		this.partner_product_prices = partner_product_prices;
	}
	public List<Vehicle> getParking_lots() {
		return parking_lots;
	}
	public void setParking_lots(List<Vehicle> parking_lots) {
		this.parking_lots = parking_lots;
	}


}
