package com.graphQL.pojo.textpay;

import java.util.List;

public class CreateParkingVariable {

	int minutes;
	String promo_code;
	boolean apply_wallet_credit;
	int location_id;
	String parking_time_type;
	long card_id;
	boolean save_payment_method;
	String payment_method_type;
	String source;
	List<ParkingLotsVariable> parking_lots;
	List<PartnerProduct> partner_product_prices;
	String token;

	public int getMinutes() {
		return minutes;
	}

	public void setMinutes(int minutes) {
		this.minutes = minutes;
	}

	public boolean isApply_wallet_credit() {
		return apply_wallet_credit;
	}

	public void setApply_wallet_credit(boolean apply_wallet_credit) {
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

	public long getCard_id() {
		return card_id;
	}

	public void setCard_id(long card_id) {
		this.card_id = card_id;
	}

	public boolean isSave_payment_method() {
		return save_payment_method;
	}

	public void setSave_payment_method(boolean save_payment_method) {
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

	public List<ParkingLotsVariable> getParking_lots() {
		return parking_lots;
	}

	public void setParking_lots(List<ParkingLotsVariable> parking_lots) {
		this.parking_lots = parking_lots;
	}

	public List<PartnerProduct> getPartner_product_prices() {
		return partner_product_prices;
	}

	public void setPartner_product_prices(List<PartnerProduct> partner_product_prices) {
		this.partner_product_prices = partner_product_prices;
	}

	public String getPromo_code() {
		return promo_code;
	}

	public void setPromo_code(String promo_code) {
		this.promo_code = promo_code;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
