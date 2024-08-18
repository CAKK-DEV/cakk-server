package com.cakk.common.enums;

public enum VerificationStatus {

	UNREQUESTED(0),
	APPROVED(1),
	REJECTED(2),
	PENDING(3);

	private final Integer code;

	VerificationStatus(Integer code) {
		this.code = code;
	}

	public Integer getCode() {
		return code;
	}

	public boolean isCandidate() {
		return code == 3;
	}

	public boolean isNotCandidate() {
		return  code != 3;
	}

	public boolean isApproved() {
		return code == 1;
	}

	public boolean isRejected() {
		return code == 2;
	}

	public VerificationStatus makeApproved() {
		return VerificationStatus.APPROVED;
	}

	public VerificationStatus makePending() {
		return VerificationStatus.PENDING;
	}
}

