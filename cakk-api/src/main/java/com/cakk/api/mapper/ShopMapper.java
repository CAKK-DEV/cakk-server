package com.cakk.api.mapper;

import static com.cakk.common.utils.SetUtilsKt.*;
import static java.util.Objects.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.cakk.api.dto.request.link.UpdateLinkRequest;
import com.cakk.api.dto.request.operation.UpdateShopOperationRequest;
import com.cakk.api.dto.request.shop.PromotionRequest;
import com.cakk.api.dto.request.shop.UpdateShopRequest;
import com.cakk.api.dto.request.user.CertificationRequest;
import com.cakk.core.dto.param.shop.CreateShopParam;
import com.cakk.core.dto.param.shop.PromotionParam;
import com.cakk.core.dto.param.shop.ShopOperationParam;
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
import com.cakk.domain.mysql.bo.shop.CakeShopByLocationParam;
import com.cakk.domain.mysql.bo.shop.CakeShopBySearchParam;
import com.cakk.domain.mysql.dto.param.like.HeartCakeShopResponseParam;
import com.cakk.domain.mysql.dto.param.link.UpdateLinkParam;
import com.cakk.domain.mysql.dto.param.operation.UpdateShopOperationParam;
import com.cakk.domain.mysql.dto.param.shop.CakeShopDetailParam;
import com.cakk.domain.mysql.dto.param.shop.CakeShopInfoParam;
import com.cakk.domain.mysql.dto.param.shop.CakeShopLocationResponseParam;
import com.cakk.domain.mysql.dto.param.shop.CakeShopOperationParam;
import com.cakk.domain.mysql.dto.param.shop.CakeShopSearchResponseParam;
import com.cakk.domain.mysql.dto.param.shop.CakeShopSimpleParam;
import com.cakk.domain.mysql.dto.param.shop.CakeShopUpdateParam;
import com.cakk.domain.mysql.dto.param.user.CertificationParam;
import com.cakk.domain.mysql.entity.cake.Cake;
import com.cakk.domain.mysql.entity.shop.CakeShop;
import com.cakk.domain.mysql.entity.shop.CakeShopLink;
import com.cakk.domain.mysql.entity.shop.CakeShopOperation;
import com.cakk.domain.mysql.entity.user.BusinessInformation;
import com.cakk.domain.mysql.entity.user.User;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ShopMapper {

	public static CakeShop supplyCakeShopBy(final CreateShopParam param) {
		return CakeShop.builder()
			.shopName(param.getShopName())
			.shopBio(param.getShopBio())
			.shopDescription(param.getShopDescription())
			.shopAddress(param.getShopAddress())
			.location(PointMapper.supplyPointBy(param.getLatitude(), param.getLongitude()))
			.build();
	}

	public static BusinessInformation supplyBusinessInformationBy(final CreateShopParam param, final CakeShop cakeShop) {
		return BusinessInformation.builder()
			.businessNumber(param.getBusinessNumber())
			.cakeShop(cakeShop)
			.build();
	}

	public static List<CakeShopOperation> supplyCakeShopOperationsBy(
		final CakeShop cakeShop,
		final List<ShopOperationParam> operationDays
	) {
		final List<CakeShopOperation> cakeShopOperations = new ArrayList<>();

		operationDays.forEach(operationParam -> cakeShopOperations
			.add(CakeShopOperation.builder()
				.operationDay(operationParam.getOperationDay())
				.operationStartTime(operationParam.getOperationStartTime())
				.operationEndTime(operationParam.getOperationEndTime())
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
		cakeShops.forEach(it -> keepOnlyNElements(it.cakeImageUrls(), 4));

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

	public static CreateShopParam supplyCreateShopParamBy(final CreateShopRequest request) {
		return new CreateShopParam(
			request.businessNumber(),
			request.operationDays(),
			request.shopName(),
			request.shopBio(),
			request.shopDescription(),
			request.shopAddress(),
			request.latitude(),
			request.longitude(),
			request.links()
		);
	}

	public static PromotionParam supplyPromotionParamBy(final PromotionRequest request) {
		return new PromotionParam(
			request.userId(),
			request.cakeShopId()
		);
	}

	public static CakeShopUpdateParam supplyCakeShopUpdateParamBy(
		final UpdateShopRequest request,
		final User user,
		final Long cakeShopId) {
		return CakeShopUpdateParam.builder()
			.user(user)
			.cakeShopId(cakeShopId)
			.thumbnailUrl(request.thumbnailUrl())
			.shopName(request.shopName())
			.shopBio(request.shopBio())
			.shopDescription(request.shopDescription())
			.build();
	}

	public static CertificationParam supplyCertificationParamBy(final CertificationRequest request, final User user) {
		return CertificationParam.builder()
			.businessRegistrationImageUrl(request.businessRegistrationImageUrl())
			.idCardImageUrl(request.idCardImageUrl())
			.cakeShopId(request.cakeShopId())
			.emergencyContact(request.emergencyContact())
			.message(request.message())
			.user(user)
			.build();
	}

	public static UpdateLinkParam supplyUpdateLinkParamBy(
		final UpdateLinkRequest request,
		final User user,
		final Long cakeShopId) {
		List<CakeShopLink> cakeShopLinks = new ArrayList<>();
		CakeShopLink instagramLink = LinkMapper.supplyCakeShopLinkByInstagram(request.instagram());
		CakeShopLink kakaoLink = LinkMapper.supplyCakeShopLinkByKakao(request.kakao());
		CakeShopLink webLink = LinkMapper.supplyCakeShopLinkByWeb(request.web());

		addLink(cakeShopLinks, instagramLink);
		addLink(cakeShopLinks, kakaoLink);
		addLink(cakeShopLinks, webLink);

		return new UpdateLinkParam(
			user,
			cakeShopId,
			cakeShopLinks
		);
	}

	public static UpdateShopOperationParam supplyUpdateShopOperationParamBy(
		final UpdateShopOperationRequest request,
		final User user,
		final Long cakeShopId
	) {
		final List<CakeShopOperation> cakeShopOperations = ShopOperationMapper
			.supplyCakeShopOperationListBy(request.operationDays());

		return new UpdateShopOperationParam(
			cakeShopOperations,
			user,
			cakeShopId
		);
	}

	private static boolean isEmptyCakeShopOperation(final CakeShopOperationParam cakeShopOperationParam) {
		return isNull(cakeShopOperationParam.operationDay())
			|| isNull(cakeShopOperationParam.operationStartTime())
			|| isNull(cakeShopOperationParam.operationEndTime());
	}

	private static void addLink(List<CakeShopLink> cakeShopLinks, CakeShopLink cakeShopLink) {
		if (nonNull(cakeShopLink)) {
			cakeShopLinks.add(cakeShopLink);
		}
	}
}
