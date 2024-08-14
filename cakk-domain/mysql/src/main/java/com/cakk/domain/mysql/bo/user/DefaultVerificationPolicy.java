package com.cakk.domain.mysql.bo.user;

import org.springframework.stereotype.Component;

import com.cakk.common.enums.VerificationStatus;
import com.cakk.domain.mysql.entity.user.User;

@Component
public class DefaultVerificationPolicy implements VerificationPolicy {

	@Override
	public boolean isCandidate(User user, VerificationStatus verificationStatus) {
		return !user.isBusinessOwner() && verificationStatus.isCandidate();
	}

	@Override
	public VerificationStatus approveToBusinessOwner(User user) {
		user.upgradedBusinessOwner();
		return VerificationStatus.makeApproved();
	}
}

