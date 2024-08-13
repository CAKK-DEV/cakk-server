package com.cakk.domain.mysql.bo;

import com.cakk.common.enums.VerificationStatus;
import com.cakk.domain.mysql.entity.user.User;

public interface VerificationPolicy {

	boolean isCandidate(User user, VerificationStatus verificationStatus);

	VerificationStatus approvedBusinessOwner(User user);
}
