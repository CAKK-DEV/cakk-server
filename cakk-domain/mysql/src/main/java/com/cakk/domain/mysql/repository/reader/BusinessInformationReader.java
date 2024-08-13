package com.cakk.domain.mysql.repository.reader;

import java.util.List;

import lombok.RequiredArgsConstructor;

import com.cakk.domain.mysql.annotation.Reader;
import com.cakk.domain.mysql.bo.user.VerificationStatus;
import com.cakk.domain.mysql.entity.user.BusinessInformation;
import com.cakk.domain.mysql.entity.user.User;
import com.cakk.domain.mysql.repository.jpa.BusinessInformationJpaRepository;

@Reader
@RequiredArgsConstructor
public class BusinessInformationReader {

	private final BusinessInformationJpaRepository businessInformationJpaRepository;

	public boolean isExistBusinessInformation(final User owner, final Long cakeShopId) {
		return businessInformationJpaRepository.existsBusinessInformationByUserAndCakeShop_Id(owner, cakeShopId);
	}

	public List<BusinessInformation> findAllWithCakeShopByUser(final User owner) {
		return businessInformationJpaRepository.findAllWithCakeShopByUser(owner);
	}

	public List<BusinessInformation> findAllCakeShopBusinessOwnerCandidates() {
		return businessInformationJpaRepository.findAllCakeShopBusinessOwnerCandidates(VerificationStatus.PENDING);
	}
}
