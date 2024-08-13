package com.cakk.common.enums;

public enum VerificationStatus {
	PENDING(0),
	APPROVED(1),
	REJECTED(2);

	private final Integer code;

	VerificationStatus(Integer code) {
		this.code = code;
	}

	public static boolean isCandidate(VerificationStatus verificationStatus) {
		return verificationStatus == PENDING;
	}

	public Integer getCode() {
		return code;
	}
}
