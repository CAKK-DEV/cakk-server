package com.cakk.api.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.cakk.api.dto.request.shop.CreateShopRequest;
import com.cakk.domain.entity.shop.CakeShop;
import com.cakk.domain.entity.user.BusinessInformation;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ShopMapper {

	public static CakeShop supplyCakeShopBy(CreateShopRequest request) {
		return CakeShop.builder()
			.shopName(request.shopName())
			.shopBio(request.shopBio())
			.shopDescription(request.shopDescription())
			.latitude(request.latitude())
			.longitude(request.longitude())
			.build();
	}

	public static BusinessInformation supplyBusinessInformationBy(CreateShopRequest request, CakeShop cakeShop) {
		return BusinessInformation.builder()
			.businessNumber(request.businessNumber())
			.cakeShop(cakeShop)
			.operationDay(request.operationDay())
			.startTime(request.startTime())
			.endTime(request.endTime())
			.build();
	}

	public static BusinessInformation supplyBusinessInformationBy() {
		return BusinessInformation.builder()
			.build();
	}
}
