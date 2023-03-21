package com.graphQL.pojo;

public class PaymentMethod {
	String entity_uid;
	String entity_id;
	String entity_type;
	String name;
	String last4;
	String __typename;

	public String getEntity_uid() {
		return entity_uid;
	}

	public void setEntity_uid(String entity_uid) {
		this.entity_uid = entity_uid;
	}

	public String getEntity_id() {
		return entity_id;
	}

	public void setEntity_id(String entity_id) {
		this.entity_id = entity_id;
	}

	public String getEntity_type() {
		return entity_type;
	}

	public void setEntity_type(String entity_type) {
		this.entity_type = entity_type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLast4() {
		return last4;
	}

	public void setLast4(String last4) {
		this.last4 = last4;
	}

	public String get__typename() {
		return __typename;
	}

	public void set__typename(String __typename) {
		this.__typename = __typename;
	}

}
