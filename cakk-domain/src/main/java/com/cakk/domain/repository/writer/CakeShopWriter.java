package com.cakk.domain.repository.writer;

import com.cakk.domain.annotation.Writer;
import com.cakk.domain.entity.shop.CakeShop;
import com.cakk.domain.entity.user.BusinessInformation;
import com.cakk.domain.repository.jpa.BusinessInformationJpaRepository;
import com.cakk.domain.repository.jpa.CakeShopJpaRepository;

import lombok.RequiredArgsConstructor;

@Writer
@RequiredArgsConstructor
public class CakeShopWriter {

	private final BusinessInformationJpaRepository businessInformationJpaRepository;
	private final CakeShopJpaRepository cakeShopJpaRepository;

	public void createCakeShop(CakeShop cakeShop, BusinessInformation businessInformation) {
		cakeShopJpaRepository.save(cakeShop);
		businessInformationJpaRepository.save(businessInformation);
	}
}
