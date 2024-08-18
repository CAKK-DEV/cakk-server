package com.cakk.domain.mysql.bo.user;

import com.cakk.common.enums.VerificationStatus;
import com.cakk.domain.mysql.dto.param.user.CertificationParam;
import com.cakk.domain.mysql.entity.user.BusinessInformation;
import com.cakk.domain.mysql.event.shop.CertificationEvent;

public interface VerificationPolicy {

	boolean isCandidate(VerificationStatus verificationStatus);

	VerificationStatus approveToBusinessOwner(VerificationStatus verificationStatus);

	CertificationEvent requestCertificationBusinessOwner(BusinessInformation businessInformation, CertificationParam param);
}

