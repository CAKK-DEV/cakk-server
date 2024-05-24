package com.cakk.domain.repository.reader;

import static java.util.Objects.*;

import lombok.RequiredArgsConstructor;

import com.cakk.common.enums.ReturnCode;
import com.cakk.common.exception.CakkException;
import com.cakk.domain.annotation.Reader;
import com.cakk.domain.dto.param.shop.CakeShopDetailParam;
import com.cakk.domain.dto.param.shop.CakeShopSimpleParam;
import com.cakk.domain.entity.shop.CakeShop;
import com.cakk.domain.entity.user.BusinessInformation;
import com.cakk.domain.repository.jpa.BusinessInformationJpaRepository;
import com.cakk.domain.repository.jpa.CakeShopJpaRepository;
import com.cakk.domain.repository.query.CakeShopQueryRepository;

@Reader
@RequiredArgsConstructor
public class CakeShopReader {

	private final CakeShopJpaRepository cakeShopJpaRepository;
	private final CakeShopQueryRepository cakeShopQueryRepository;
	private final BusinessInformationJpaRepository businessInformationJpaRepository;

	public CakeShop findById(Long cakeShopId) {
		return cakeShopJpaRepository.findById(cakeShopId).orElseThrow(() -> new CakkException(ReturnCode.NOT_EXIST_CAKE_SHOP));
	}

	public CakeShopSimpleParam searchSimpleById(final Long cakeShopId) {
		final CakeShopSimpleParam response = cakeShopQueryRepository.searchSimpleById(cakeShopId);

		if (isNull(response)) {
			throw new CakkException(ReturnCode.NOT_EXIST_CAKE_SHOP);
		}

		return response;
	}

	public CakeShopDetailParam searchDetailById(final Long cakeShopId) {
		final CakeShopDetailParam response = cakeShopQueryRepository.searchDetailById(cakeShopId);

		if (isNull(response)) {
			throw new CakkException(ReturnCode.NOT_EXIST_CAKE_SHOP);
		}

		return response;
	}

	public BusinessInformation findBusinessInformationWithShop(Long cakeShopId) {
		return businessInformationJpaRepository
			.findBusinessInformationWithCakeShop(cakeShopId)
			.orElseThrow(() -> new CakkException(ReturnCode.NOT_EXIST_CAKE_SHOP));
	}

	public BusinessInformation findBusinessInformationByCakeShopId(Long cakeShopId) {
		return businessInformationJpaRepository.findBusinessInformationByCakeShopId(cakeShopId)
			.orElseThrow(() -> new CakkException(ReturnCode.NOT_EXIST_CAKE_SHOP));
	}

}
