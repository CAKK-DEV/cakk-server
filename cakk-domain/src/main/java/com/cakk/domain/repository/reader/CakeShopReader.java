package com.cakk.domain.repository.reader;

import com.cakk.common.enums.ReturnCode;
import com.cakk.common.exception.CakkException;
import com.cakk.domain.annotation.Reader;
import com.cakk.domain.entity.shop.CakeShop;
import com.cakk.domain.entity.user.BusinessInformation;
import com.cakk.domain.repository.jpa.BusinessInformationJpaRepository;
import com.cakk.domain.repository.jpa.CakeShopJpaRepository;

import lombok.RequiredArgsConstructor;

@Reader
@RequiredArgsConstructor
public class CakeShopReader {

	private final CakeShopJpaRepository cakeShopJpaRepository;
	private final BusinessInformationJpaRepository businessInformationJpaRepository;

	public CakeShop findById(Long cakeShopId) {
		return cakeShopJpaRepository.findById(cakeShopId).orElseThrow(() -> new CakkException(ReturnCode.NOT_EXIST_CAKE_SHOP));
	}

	public BusinessInformation findBusinessInformationByShopId(Long cakeShopId) {
		return businessInformationJpaRepository
			.findBusinessInformationByCakeShopId(cakeShopId)
			.orElseThrow(() -> new CakkException(ReturnCode.NOT_EXIST_CAKE_SHOP));
	}
}
