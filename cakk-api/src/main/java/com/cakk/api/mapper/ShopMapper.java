package com.cakk.api.mapper;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.cakk.api.dto.request.shop.CreateShopRequest;
import com.cakk.api.dto.response.shop.CakeShopDetailResponse;
import com.cakk.api.dto.response.shop.CakeShopInfoResponse;
import com.cakk.api.dto.response.shop.CakeShopSimpleResponse;
import com.cakk.common.enums.Days;
import com.cakk.domain.mysql.dto.param.shop.CakeShopDetailParam;
import com.cakk.domain.mysql.dto.param.shop.CakeShopInfoParam;
import com.cakk.domain.mysql.dto.param.shop.CakeShopSimpleParam;
import com.cakk.domain.mysql.entity.shop.CakeShop;
import com.cakk.domain.mysql.entity.shop.CakeShopOperation;
import com.cakk.domain.mysql.entity.user.BusinessInformation;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ShopMapper {

	public static CakeShop supplyCakeShopBy(CreateShopRequest request) {

		return CakeShop.builder()
			.shopName(request.shopName())
			.shopBio(request.shopBio())
			.shopDescription(request.shopDescription())
			.location(PointMapper.supplyPointBy(request.latitude(), request.longitude()))
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
			.cakeShop(CakeShop.builder().build())
			.build();
	}

	public static List<CakeShopOperation> supplyCakeShopOperationsBy(
		CakeShop cakeShop,
		List<Days> operationsDays,
		List<LocalTime> startTimes,
		List<LocalTime> endTimes
	) {
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

	public static CakeShopSimpleResponse cakeShopSimpleResponseFromParam(CakeShopSimpleParam param) {
		return CakeShopSimpleResponse.builder()
			.cakeShopId(param.cakeShopId())
			.thumbnailUrl(param.thumbnailUrl())
			.cakeShopName(param.cakeShopName())
			.cakeShopBio(param.cakeShopBio())
			.build();
	}

	public static CakeShopDetailResponse cakeShopDetailResponseFromParam(CakeShopDetailParam param) {
		return CakeShopDetailResponse.builder()
			.cakeShopId(param.cakeShopId())
			.cakeShopName(param.shopName())
			.thumbnailUrl(param.thumbnailUrl())
			.cakeShopBio(param.shopBio())
			.cakeShopDescription(param.shopDescription())
			.operationDays(param.operationDays())
			.links(param.links())
			.build();
	}

	public static CakeShopInfoResponse supplyCakeShopInfoResponseBy(CakeShopInfoParam param) {
		Double longitude = param.point().getX();
		Double latitude = param.point().getY();

		return new CakeShopInfoResponse(
			param.shopAddress(),
			latitude,
			longitude,
			param.shopOperationDays()
		);
	}
}
