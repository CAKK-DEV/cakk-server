package com.cakk.api.mapper;

import static java.util.Objects.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.cakk.api.dto.request.operation.ShopOperationParam;
import com.cakk.api.dto.request.shop.CreateShopRequest;
import com.cakk.api.dto.response.like.HeartCakeShopListResponse;
import com.cakk.api.dto.response.shop.CakeShopByMapResponse;
import com.cakk.api.dto.response.shop.CakeShopByMineResponse;
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

	public static CakeShop supplyCakeShopBy(final CreateShopRequest request) {
		return CakeShop.builder()
			.shopName(request.shopName())
			.shopBio(request.shopBio())
			.shopDescription(request.shopDescription())
			.shopAddress(request.shopAddress())
			.location(PointMapper.supplyPointBy(request.latitude(), request.longitude()))
			.build();
	}

	public static BusinessInformation supplyBusinessInformationBy(final CreateShopRequest request, final CakeShop cakeShop) {
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
		final List<CakeShopOperation> cakeShopOperations = new ArrayList<>();

		operationDays.forEach(operationParam -> cakeShopOperations
			.add(CakeShopOperation.builder()
				.operationDay(operationParam.operationDay())
				.operationStartTime(operationParam.operationStartTime())
				.operationEndTime(operationParam.operationEndTime())
				.cakeShop(cakeShop)
				.build()));

		return cakeShopOperations;
	}

	public static CakeShopSimpleResponse cakeShopSimpleResponseFromParam(final CakeShopSimpleParam param) {
		return CakeShopSimpleResponse.builder()
			.cakeShopId(param.cakeShopId())
			.thumbnailUrl(param.thumbnailUrl())
			.cakeShopName(param.cakeShopName())
			.cakeShopBio(param.cakeShopBio())
			.build();
	}

	public static CakeShopDetailResponse cakeShopDetailResponseFromParam(final CakeShopDetailParam param) {
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

	public static CakeShopInfoResponse supplyCakeShopInfoResponseBy(final CakeShopInfoParam param) {
		final Double longitude = param.point().getX();
		final Double latitude = param.point().getY();
		List<CakeShopOperationParam> cakeShopOperationParams = param.shopOperationDays();

		for (final CakeShopOperationParam cakeShopOperationParam : cakeShopOperationParams) {
			if (isEmptyCakeShopOperation(cakeShopOperationParam)) {
				cakeShopOperationParams = new ArrayList<>();
				break;
			}
		}

		return new CakeShopInfoResponse(
			param.shopAddress(),
			latitude,
			longitude,
			cakeShopOperationParams
		);
	}

	public static CakeShopByMapResponse supplyCakeShopByMapResponseBy(final List<CakeShopByLocationParam> params) {
		return new CakeShopByMapResponse(params.stream()
			.map(ShopMapper::supplyCakeShopLocationResponseParamBy)
			.toList());
	}

	public static CakeShopLocationResponseParam supplyCakeShopLocationResponseParamBy(final CakeShopByLocationParam param) {
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

	public static CakeShopSearchResponseParam supplyCakeShopSearchResponseParamListBy(final CakeShopBySearchParam param) {
		return new CakeShopSearchResponseParam(
			param.getCakeShopId(),
			param.getThumbnailUrl(),
			param.getCakeShopName(),
			param.getCakeShopBio(),
			param.getCakeImageUrls(),
			param.getOperationDays()
		);
	}

	public static CakeShopSearchResponse supplyCakeShopSearchResponseBy(final List<CakeShopBySearchParam> cakeShops) {
		final int size = cakeShops.size();
		final List<CakeShopSearchResponseParam> cakeShopSearchResponseParams = cakeShops.stream()
			.map(ShopMapper::supplyCakeShopSearchResponseParamListBy)
			.toList();

		return CakeShopSearchResponse.builder()
			.cakeShops(cakeShopSearchResponseParams)
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
			.toList();
	}

	public static CakeShopBySearchParam supplyCakeShopBySearchParamBy(final CakeShop cakeShop) {
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

	public static CakeShopOwnerResponse supplyCakeShopOwnerResponseBy(final Boolean isOwned) {
		return new CakeShopOwnerResponse(isOwned);
	}

	public static CakeShopCreateResponse supplyCakeShopCreateResponseBy(final CakeShop cakeShop) {
		return new CakeShopCreateResponse(cakeShop.getId());
	}

	public static CakeShopByMineResponse supplyCakeShopByMineResponseBy(final List<BusinessInformation> result) {
		if (result.isEmpty()) {
			return new CakeShopByMineResponse(false, null);
		}
		return new CakeShopByMineResponse(
			true,
			result.stream().findAny().get().getCakeShop().getId()
		);
	}

	private static boolean isEmptyCakeShopOperation(final CakeShopOperationParam cakeShopOperationParam) {
		return isNull(cakeShopOperationParam.operationDay())
			|| isNull(cakeShopOperationParam.operationStartTime())
			|| isNull(cakeShopOperationParam.operationEndTime());
	}
}
