package com.cakk.domain.mysql.bo;

import org.springframework.stereotype.Component;

import com.cakk.common.enums.VerificationStatus;
import com.cakk.domain.mysql.entity.user.User;

@Component
public class DefaultVerificationPolicy implements VerificationPolicy {

	@Override
	public boolean isCandidate(User user, VerificationStatus verificationStatus) {
		return !user.isBusinessOwner() && verificationStatus == VerificationStatus.PENDING;
	}

	@Override
	public VerificationStatus approveToBusinessOwner(User user) {
		user.upgradedBusinessOwner();
		return VerificationStatus.APPROVED;
	}
}
