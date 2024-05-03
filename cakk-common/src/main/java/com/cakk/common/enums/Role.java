package com.cakk.common.enums;

public enum Role {

	ADMIN, MERCHANT, USER;

	public String getSecurityRole() {
		return "ROLE_" + this;
	}
}
