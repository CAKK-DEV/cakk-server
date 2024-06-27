package com.cakk.domain.mysql.repository.writer;

import java.util.List;

import lombok.RequiredArgsConstructor;

import com.cakk.domain.mysql.annotation.Writer;
import com.cakk.domain.mysql.entity.shop.CakeShop;
import com.cakk.domain.mysql.entity.shop.CakeShopLink;
import com.cakk.domain.mysql.entity.shop.CakeShopOperation;
import com.cakk.domain.mysql.entity.user.BusinessInformation;
import com.cakk.domain.mysql.repository.jpa.BusinessInformationJpaRepository;
import com.cakk.domain.mysql.repository.jpa.CakeShopJpaRepository;
import com.cakk.domain.mysql.repository.jpa.CakeShopLinkJpaRepository;
import com.cakk.domain.mysql.repository.jpa.CakeShopOperationJpaRepository;

@Writer
@RequiredArgsConstructor
public class CakeShopWriter {

	private final BusinessInformationJpaRepository businessInformationJpaRepository;
	private final CakeShopJpaRepository cakeShopJpaRepository;
	private final CakeShopOperationJpaRepository cakeShopOperationJpaRepository;
	private final CakeShopLinkJpaRepository cakeShopLinkJpaRepository;

	public CakeShop createCakeShop(
		final CakeShop cakeShop,
		final List<CakeShopOperation> cakeShopOperations,
		final BusinessInformation businessInformation,
		final List<CakeShopLink> cakeShopLinks) {
		final CakeShop result = cakeShopJpaRepository.save(cakeShop);
		cakeShopOperationJpaRepository.saveAll(cakeShopOperations);
		businessInformationJpaRepository.save(businessInformation);
		cakeShopLinkJpaRepository.saveAll(cakeShopLinks);
		return result;
	}
}
