package com.cakk.api.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.cakk.api.dto.request.operation.ShopOperationParam;
import com.cakk.api.dto.request.shop.CreateShopRequest;
import com.cakk.api.dto.response.like.HeartCakeShopListResponse;
import com.cakk.api.dto.response.shop.CakeShopByMapResponse;
import com.cakk.api.dto.response.shop.CakeShopCreateResponse;
import com.cakk.api.dto.response.shop.CakeShopDetailResponse;
import com.cakk.api.dto.response.shop.CakeShopInfoResponse;
import com.cakk.api.dto.response.shop.CakeShopOwnerResponse;
import com.cakk.api.dto.response.shop.CakeShopSearchResponse;
import com.cakk.api.dto.response.shop.CakeShopSimpleResponse;
import com.cakk.common.utils.SetUtils;
import com.cakk.domain.mysql.dto.param.like.HeartCakeShopResponseParam;
import com.cakk.domain.mysql.dto.param.shop.CakeShopByLocationParam;
import com.cakk.domain.mysql.dto.param.shop.CakeShopBySearchParam;
import com.cakk.domain.mysql.dto.param.shop.CakeShopDetailParam;
import com.cakk.domain.mysql.dto.param.shop.CakeShopInfoParam;
import com.cakk.domain.mysql.dto.param.shop.CakeShopLocationResponseParam;
import com.cakk.domain.mysql.dto.param.shop.CakeShopOperationParam;
import com.cakk.domain.mysql.dto.param.shop.CakeShopSearchResponseParam;
import com.cakk.domain.mysql.dto.param.shop.CakeShopSimpleParam;
import com.cakk.domain.mysql.entity.cake.Cake;
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
			.shopAddress(request.shopAddress())
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
		final CakeShop cakeShop,
		final List<ShopOperationParam> operationDays
	) {
		List<CakeShopOperation> cakeShopOperations = new ArrayList<>();

		operationDays.forEach(operationParam -> cakeShopOperations
			.add(CakeShopOperation.builder()
			.operationDay(operationParam.operationDay())
			.operationStartTime(operationParam.operationStartTime())
			.operationEndTime(operationParam.operationEndTime())
			.cakeShop(cakeShop)
			.build()));

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

	public static CakeShopByMapResponse supplyCakeShopByMapResponseBy(List<CakeShopByLocationParam> params) {
		return new CakeShopByMapResponse(params
			.stream()
			.map(ShopMapper::supplyCakeShopLocationResponseParamBy)
			.collect(Collectors.toList()));
	}

	public static CakeShopLocationResponseParam supplyCakeShopLocationResponseParamBy(CakeShopByLocationParam param) {
		return new CakeShopLocationResponseParam(
			param.getCakeShopId(),
			param.getThumbnailUrl(),
			param.getCakeShopName(),
			param.getCakeShopBio(),
			param.getCakeImageUrls(),
			param.getLongitude(),
			param.getLatitude()
		);
	}

	public static CakeShopSearchResponseParam supplyCakeShopSearchResponseParamListBy(CakeShopBySearchParam param) {
		return new CakeShopSearchResponseParam(
			param.getCakeShopId(),
			param.getThumbnailUrl(),
			param.getCakeShopName(),
			param.getCakeShopBio(),
			param.getCakeImageUrls(),
			param.getOperationDays()
		);
	}

	public static CakeShopSearchResponse supplyCakeShopSearchResponseBy(List<CakeShopBySearchParam> cakeShops) {
		final int size = cakeShops.size();

		return CakeShopSearchResponse.builder()
			.cakeShops(cakeShops
				.stream()
				.map(ShopMapper::supplyCakeShopSearchResponseParamListBy)
				.collect(Collectors.toList()))
			.lastCakeShopId(cakeShops.isEmpty() ? null : cakeShops.get(size - 1).getCakeShopId())
			.size(cakeShops.size())
			.build();
	}

	public static HeartCakeShopListResponse supplyHeartCakeShopListResponseBy(final List<HeartCakeShopResponseParam> cakeShops) {
		final int size = cakeShops.size();
		cakeShops.forEach(it -> SetUtils.keepOnlyNElements(it.cakeImageUrls(), 4));

		return HeartCakeShopListResponse.builder()
			.cakeShops(cakeShops)
			.lastCakeShopHeartId(cakeShops.isEmpty() ? null : cakeShops.get(size - 1).cakeShopHeartId())
			.size(size)
			.build();
	}

	public static List<CakeShopBySearchParam> supplyCakeShopBySearchParamListBy(List<CakeShop> cakeShops) {
		return cakeShops.stream()
			.map(ShopMapper::supplyCakeShopBySearchParamBy)
			.collect(Collectors.toList());
	}

	public static CakeShopBySearchParam supplyCakeShopBySearchParamBy(CakeShop cakeShop) {
		return CakeShopBySearchParam.builder()
			.cakeShopId(cakeShop.getId())
			.thumbnailUrl(cakeShop.getThumbnailUrl())
			.cakeShopName(cakeShop.getShopName())
			.cakeShopBio(cakeShop.getShopBio())
			.cakeImageUrls(cakeShop.getCakes().stream().map(Cake::getCakeImageUrl).collect(Collectors.toSet()))
			.operationDays(cakeShop.getCakeShopOperations()
				.stream()
				.map(cakeShopOperation -> new CakeShopOperationParam(
					cakeShopOperation.getOperationDay(),
					cakeShopOperation.getOperationStartTime(),
					cakeShopOperation.getOperationEndTime())
				).collect(Collectors.toSet())
			).build();
	}

	public static CakeShopOwnerResponse supplyCakeShopOwnerResponseBy(Boolean isOwned) {
		return new CakeShopOwnerResponse(isOwned);
	}

	public static CakeShopCreateResponse supplyCakeShopCreateResponseBy(CakeShop cakeShop) {
		return new CakeShopCreateResponse(cakeShop.getId());
	}
}
