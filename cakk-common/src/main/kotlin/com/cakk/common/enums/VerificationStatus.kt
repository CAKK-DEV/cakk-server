package com.cakk.common.enums

enum class VerificationStatus(@JvmField val code: Int) {
	UNREQUESTED(0),
	APPROVED(1),
	REJECTED(2),
	PENDING(3);

	val isCandidate: Boolean
		get() = code == 3

	val isNotCandidate: Boolean
		get() = code != 3

	val isApproved: Boolean
		get() = code == 1

	val isRejected: Boolean
		get() = code == 2

	fun makeApproved(): VerificationStatus {
		return APPROVED
	}

	fun makePending(): VerificationStatus {
		return PENDING
	}
}

