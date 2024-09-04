package com.cakk.domain.mysql.facade.shop;

import java.util.List;

import lombok.RequiredArgsConstructor;

import com.cakk.domain.mysql.annotation.DomainFacade;
import com.cakk.domain.mysql.entity.shop.CakeShop;
import com.cakk.domain.mysql.entity.shop.CakeShopLink;
import com.cakk.domain.mysql.entity.shop.CakeShopOperation;
import com.cakk.domain.mysql.entity.user.BusinessInformation;
import com.cakk.domain.mysql.repository.jpa.CakeShopJpaRepository;

@RequiredArgsConstructor
@DomainFacade
public class CakeShopManagerFacade {

	private final CakeShopJpaRepository cakeShopJpaRepository;

	public CakeShop createCakeShop(
		final CakeShop cakeShop,
		final List<CakeShopOperation> cakeShopOperations,
		final BusinessInformation businessInformation,
		final List<CakeShopLink> cakeShopLinks
	) {
		cakeShop.addShopOperationDays(cakeShopOperations);
		cakeShop.addShopLinks(cakeShopLinks);
		cakeShop.registerBusinessInformation(businessInformation);

		return cakeShopJpaRepository.save(cakeShop);
	}
}
