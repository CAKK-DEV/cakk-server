package com.cakk.domain.mysql.repository.reader;

import lombok.RequiredArgsConstructor;

import com.cakk.domain.mysql.annotation.Reader;
import com.cakk.domain.mysql.entity.user.User;
import com.cakk.domain.mysql.repository.jpa.BusinessInformationJpaRepository;

@Reader
@RequiredArgsConstructor
public class BusinessInformationReader {

	private final BusinessInformationJpaRepository businessInformationJpaRepository;

	public boolean isExistBusinessInformation(User owner, Long cakeShopId) {
		return businessInformationJpaRepository.existsBusinessInformationByUserAndCakeShop_Id(owner, cakeShopId);
	}
}
