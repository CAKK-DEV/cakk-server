package com.cakk.infrastructure.persistence.bo.user;

import com.cakk.common.enums.VerificationStatus;
import com.cakk.infrastructure.persistence.entity.user.BusinessInformation;
import com.cakk.infrastructure.persistence.param.user.CertificationParam;
import com.cakk.infrastructure.persistence.shop.CertificationEvent;

public interface VerificationPolicy {

	boolean isCandidate(VerificationStatus verificationStatus);

	VerificationStatus approveToBusinessOwner(VerificationStatus verificationStatus);

	CertificationEvent requestCertificationBusinessOwner(BusinessInformation businessInformation, CertificationParam param);
}

