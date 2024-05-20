package com.cakk.common.enums;

public enum Days {

	MON(0), TUE(1), WED(2), THU(3), FRI(4), SAT(5), SUN(6);

	private Integer code;

	private Days(Integer code) {
		this.code = code;
	}

	public Integer getCode() {
		return code;
	}

}
