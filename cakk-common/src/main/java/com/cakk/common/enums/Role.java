package com.cakk.common.enums;

public enum Role {

	ADMIN, BUSINESS_OWNER, USER;

	public String getSecurityRole() {
		return "ROLE_" + this;
	}
}
