package com.graphQL.pojo.textpay;

import java.util.List;

public class ParkingCostVariable {
	boolean isCheckout;
	String parking_time_type;
	int location_id;
	String payment_method_type;
	String source;
	boolean apply_wallet_credit;
	List<ParkingLotsVariable> parking_lots;
	List<PartnerProduct> partner_product_prices;
	List<PeekTravelProduct> peek_travel_products;
	List<PartnerProductKinds> partner_product_kinds;
	Integer parking_time_rate_id;
	int minutes;

	public boolean isCheckout() {
		return isCheckout;
	}

	public void setCheckout(boolean isCheckout) {
		this.isCheckout = isCheckout;
	}

	public String getParking_time_type() {
		return parking_time_type;
	}

	public void setParking_time_type(String parking_time_type) {
		this.parking_time_type = parking_time_type;
	}

	public int getLocation_id() {
		return location_id;
	}

	public void setLocation_id(int location_id) {
		this.location_id = location_id;
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

	public boolean isApply_wallet_credit() {
		return apply_wallet_credit;
	}

	public void setApply_wallet_credit(boolean apply_wallet_credit) {
		this.apply_wallet_credit = apply_wallet_credit;
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

	public List<PeekTravelProduct> getPeek_travel_products() {
		return peek_travel_products;
	}

	public void setPeek_travel_products(List<PeekTravelProduct> peek_travel_products) {
		this.peek_travel_products = peek_travel_products;
	}

	public List<PartnerProductKinds> getPartner_product_kinds() {
		return partner_product_kinds;
	}

	public void setPartner_product_kinds(List<PartnerProductKinds> partner_product_kinds) {
		this.partner_product_kinds = partner_product_kinds;
	}

	public Integer getParking_time_rate_id() {
		return parking_time_rate_id;
	}

	public void setParking_time_rate_id(Integer parking_time_rate_id) {
		this.parking_time_rate_id = parking_time_rate_id;
	}

	public int getMinutes() {
		return minutes;
	}

	public void setMinutes(int minutes) {
		this.minutes = minutes;
	}

}
