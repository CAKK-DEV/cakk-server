package com.cakk.domain.repository.writer;

import java.util.List;

import lombok.RequiredArgsConstructor;

import com.cakk.domain.annotation.Writer;
import com.cakk.domain.entity.shop.CakeShopOperation;
import com.cakk.domain.entity.shop.CakeShop;
import com.cakk.domain.entity.user.BusinessInformation;
import com.cakk.domain.repository.jpa.BusinessInformationJpaRepository;
import com.cakk.domain.repository.jpa.CakeShopJpaRepository;
import com.cakk.domain.repository.jpa.CakeShopOperationJpaRepository;

@Writer
@RequiredArgsConstructor
public class CakeShopWriter {

	private final BusinessInformationJpaRepository businessInformationJpaRepository;
	private final CakeShopJpaRepository cakeShopJpaRepository;
	private final CakeShopOperationJpaRepository cakeShopOperationJpaRepository;

	public void createCakeShop(CakeShop cakeShop, List<CakeShopOperation> cakeShopOperations, BusinessInformation businessInformation) {
		cakeShopJpaRepository.save(cakeShop);
		cakeShopOperationJpaRepository.saveAll(cakeShopOperations);
		businessInformationJpaRepository.save(businessInformation);
	}
}
