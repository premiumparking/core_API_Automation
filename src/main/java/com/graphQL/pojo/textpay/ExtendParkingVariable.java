package com.graphQL.pojo.textpay;

import java.util.List;

public class ExtendParkingVariable {

	long order_number_id;
	int minutes;
	boolean apply_wallet_credit;
	boolean save_payment_method;
	String payment_method_type;
	String source;
	String token;
	List<PartnerProduct> partner_product_prices;

	public long getOrder_number_id() {
		return order_number_id;
	}

	public void setOrder_number_id(long order_number_id) {
		this.order_number_id = order_number_id;
	}

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

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public List<PartnerProduct> getPartner_product_prices() {
		return partner_product_prices;
	}

	public void setPartner_product_prices(List<PartnerProduct> partner_product_prices) {
		this.partner_product_prices = partner_product_prices;
	}

}
