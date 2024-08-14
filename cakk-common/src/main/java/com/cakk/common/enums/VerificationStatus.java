package com.cakk.common.enums;

public enum VerificationStatus {

	PENDING(0),
	APPROVED(1),
	REJECTED(2);

	private final Integer code;

	VerificationStatus(Integer code) {
		this.code = code;
	}

	public Integer getCode() {
		return code;
	}

	public boolean isCandidate() {
		return code == 0;
	}

	public static VerificationStatus makeApproved() {
		return VerificationStatus.APPROVED;
	}
}

