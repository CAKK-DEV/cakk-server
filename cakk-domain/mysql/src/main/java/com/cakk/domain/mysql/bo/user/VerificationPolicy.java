package com.cakk.domain.mysql.bo.user;

import com.cakk.common.enums.VerificationStatus;

public interface VerificationPolicy {

	boolean isCandidate(VerificationStatus verificationStatus);

	VerificationStatus approveToBusinessOwner();
}

