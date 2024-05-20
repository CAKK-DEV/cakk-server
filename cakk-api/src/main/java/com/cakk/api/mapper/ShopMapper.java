package com.cakk.api.mapper;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.cakk.api.dto.request.shop.CreateShopRequest;
import com.cakk.common.enums.DayOfWeek;
import com.cakk.domain.entity.cake.CakeShopOperation;
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
			.build();
	}

	public static BusinessInformation supplyBusinessInformationBy() {
		return BusinessInformation.builder()
			.build();
	}

	public static List<CakeShopOperation> supplyCakeShopOperationsBy(
		CakeShop cakeShop,
		List<DayOfWeek> operationsDays,
		List<LocalTime> startTimes,
		List<LocalTime> endTimes) {
		List<CakeShopOperation> cakeShopOperations = new ArrayList<>();

		for (int i = 0; i < operationsDays.size(); i++) {
			cakeShopOperations.add(CakeShopOperation.builder()
				.operationDay(operationsDays.get(i))
				.operationStartTime(startTimes.get(i))
				.operationEndTime(endTimes.get(i))
				.cakeShop(cakeShop)
				.build());
		}

		return cakeShopOperations;
	}

}
