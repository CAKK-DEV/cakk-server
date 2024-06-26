package com.cakk.domain.mysql.repository.writer;

import java.util.List;

import lombok.RequiredArgsConstructor;

import com.cakk.domain.mysql.annotation.Writer;
import com.cakk.domain.mysql.entity.shop.CakeShop;
import com.cakk.domain.mysql.entity.shop.CakeShopOperation;
import com.cakk.domain.mysql.entity.user.BusinessInformation;
import com.cakk.domain.mysql.repository.jpa.BusinessInformationJpaRepository;
import com.cakk.domain.mysql.repository.jpa.CakeShopJpaRepository;
import com.cakk.domain.mysql.repository.jpa.CakeShopOperationJpaRepository;

@Writer
@RequiredArgsConstructor
public class CakeShopWriter {

	private final BusinessInformationJpaRepository businessInformationJpaRepository;
	private final CakeShopJpaRepository cakeShopJpaRepository;
	private final CakeShopOperationJpaRepository cakeShopOperationJpaRepository;

	public CakeShop createCakeShop(CakeShop cakeShop, List<CakeShopOperation> cakeShopOperations, BusinessInformation businessInformation) {
		final CakeShop result = cakeShopJpaRepository.save(cakeShop);
		cakeShopOperationJpaRepository.saveAll(cakeShopOperations);
		businessInformationJpaRepository.save(businessInformation);
		return result;
	}
}
