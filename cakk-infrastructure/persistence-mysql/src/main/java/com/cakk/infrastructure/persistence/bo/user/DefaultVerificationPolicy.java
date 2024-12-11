package com.cakk.infrastructure.persistence.bo.user;

import org.springframework.stereotype.Component;

import com.cakk.common.enums.ReturnCode;
import com.cakk.common.enums.VerificationStatus;
import com.cakk.common.exception.CakkException;
import com.cakk.infrastructure.persistence.entity.user.BusinessInformationEntity;
import com.cakk.infrastructure.persistence.param.user.CertificationParam;
import com.cakk.infrastructure.persistence.shop.CertificationEvent;

@Component
public class DefaultVerificationPolicy implements VerificationPolicy {

	@Override
	public boolean isCandidate(VerificationStatus verificationStatus) {
		return verificationStatus.isCandidate();
	}

	@Override
	public VerificationStatus approveToBusinessOwner(VerificationStatus verificationStatus) {
		validateVerificationStatusPending(verificationStatus);
		return verificationStatus.makeApproved();
	}

	@Override
	public CertificationEvent requestCertificationBusinessOwner(
		BusinessInformationEntity businessInformationEntity,
		CertificationParam param
	) {
		validateRequestCertificationBusinessOwner(businessInformationEntity);
		return businessInformationEntity.registerCertificationInformation(param);
	}

	private void validateRequestCertificationBusinessOwner(BusinessInformationEntity businessInformationEntity) {
		if (businessInformationEntity.isImPossibleRequestCertification()) {
			throw new CakkException(ReturnCode.CAKE_SHOP_CERTIFICATED_ISSUE);
		}
	}

	private void validateVerificationStatusPending(VerificationStatus verificationStatus) {
		if (verificationStatus.isNotCandidate()) {
			throw new CakkException(ReturnCode.CAKE_SHOP_CERTIFICATED_ISSUE);
		}
	}
}

