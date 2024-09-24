package com.cakk.api.mapper;

import static java.util.Objects.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.cakk.api.dto.request.cake.CakeCreateRequest;
import com.cakk.api.dto.request.cake.CakeSearchByCategoryRequest;
import com.cakk.api.dto.request.cake.CakeSearchByLocationRequest;
import com.cakk.api.dto.request.cake.CakeSearchByShopRequest;
import com.cakk.api.dto.request.cake.CakeSearchByViewsRequest;
import com.cakk.api.dto.request.cake.CakeUpdateRequest;
import com.cakk.api.dto.response.cake.CakeDetailResponse;
import com.cakk.api.dto.response.cake.CakeImageListResponse;
import com.cakk.core.dto.param.cake.CakeCreateParam;
import com.cakk.core.dto.param.cake.CakeSearchByCategoryParam;
import com.cakk.core.dto.param.cake.CakeSearchByShopParam;
import com.cakk.core.dto.param.cake.CakeSearchByViewsParam;
import com.cakk.core.dto.param.cake.CakeSearchParam;
import com.cakk.core.dto.param.cake.CakeUpdateParam;
import com.cakk.core.dto.response.like.HeartCakeImageListResponse;
import com.cakk.domain.mysql.dto.param.cake.CakeDetailParam;
import com.cakk.domain.mysql.dto.param.cake.CakeImageResponseParam;
import com.cakk.domain.mysql.dto.param.like.HeartCakeImageResponseParam;
import com.cakk.domain.mysql.dto.param.tag.TagParam;
import com.cakk.domain.mysql.entity.cake.Cake;
import com.cakk.domain.mysql.entity.user.User;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CakeMapper {

	public static CakeImageListResponse supplyCakeImageListResponse(final List<CakeImageResponseParam> cakeImages) {
		final int size = cakeImages.size();

		return CakeImageListResponse.builder()
			.cakeImages(cakeImages)
			.lastCakeId(cakeImages.isEmpty() ? null : cakeImages.get(size - 1).cakeId())
			.size(cakeImages.size())
			.build();
	}

	public static CakeImageListResponse supplyCakeImageListResponse(
		final List<CakeImageResponseParam> cakeImages,
		final List<Long> cakeIds
	) {
		final List<CakeImageResponseParam> sortedCakeImages = new ArrayList<>();

		for (long cakeId : cakeIds) {
			cakeImages.stream()
				.filter(cakeImage -> cakeImage.cakeId() == cakeId)
				.findFirst()
				.ifPresent(sortedCakeImages::add);
		}

		return CakeImageListResponse.builder()
			.cakeImages(sortedCakeImages)
			.lastCakeId(null)
			.size(sortedCakeImages.size())
			.build();
	}

	public static HeartCakeImageListResponse supplyHeartCakeImageListResponseBy(final List<HeartCakeImageResponseParam> cakeImages) {
		final int size = cakeImages.size();

		return new HeartCakeImageListResponse(
			cakeImages,
			cakeImages.isEmpty() ? null : cakeImages.get(size - 1).cakeHeartId(),
			cakeImages.size()
		);
	}

	public static CakeDetailResponse cakeDetailResponseFromParam(final CakeDetailParam param) {
		Set<TagParam> tags = param.tags();

		for (TagParam tagParam : tags) {
			if (isEmptyTag(tagParam)) {
				tags = new HashSet<>();
				break;
			}
		}

		return CakeDetailResponse.builder()
			.cakeShopId(param.cakeShopId())
			.cakeShopName(param.cakeShopName())
			.cakeImageUrl(param.cakeImageUrl())
			.shopBio(param.shopBio())
			.cakeCategories(param.cakeCategories())
			.tags(tags)
			.build();
	}

	public static Cake supplyCakeBy(String cakeImageUrl) {
		return Cake.builder()
			.cakeImageUrl(cakeImageUrl)
			.build();
	}

	public static CakeSearchByCategoryParam supplyCakeSearchByCategoryParamBy(final CakeSearchByCategoryRequest dto) {
		return new CakeSearchByCategoryParam(
			dto.getCakeId(),
			dto.getCategory(),
			dto.getPageSize()
		);
	}

	public static CakeSearchParam supplyCakeSearchParamBy(final CakeSearchByLocationRequest dto) {
		return new CakeSearchParam(
			dto.getCakeId(),
			dto.getKeyword(),
			PointMapper.supplyPointBy(dto.getLatitude(), dto.getLongitude()),
			dto.getPageSize()
		);
	}

	public static CakeSearchByShopParam supplyCakeSearchByShopParamBy(final CakeSearchByShopRequest dto) {
		return new CakeSearchByShopParam(
			dto.getCakeId(),
			dto.getCakeShopId(),
			dto.getPageSize()
		);
	}

	public static CakeSearchByViewsParam supplyCakeSearchByViewsParamBy(final CakeSearchByViewsRequest dto) {
		return new CakeSearchByViewsParam(
			dto.getOffset(),
			dto.getPageSize()
		);
	}

	public static CakeCreateParam supplyCakeCreateParamBy(final CakeCreateRequest dto, final User user, final Long cakeShopId) {
		return new CakeCreateParam(
			CakeMapper.supplyCakeBy(dto.getCakeImageUrl()),
			CakeDesignCategoryMapper.supplyCakeCategoryListBy(dto.getCakeDesignCategories()),
			dto.getTagNames(),
			user,
			cakeShopId
		);
	}

	public static CakeUpdateParam supplyCakeUpdateParamBy(final CakeUpdateRequest dto, final User owner, final Long cakeId) {
		return new CakeUpdateParam(
			owner,
			cakeId,
			dto.getCakeImageUrl(),
			CakeDesignCategoryMapper.supplyCakeCategoryListBy(dto.getCakeDesignCategories()),
			dto.getTagNames()
		);
	}

	private static boolean isEmptyTag(TagParam tagParam) {
		return isNull(tagParam.tagId()) || isNull(tagParam.tagName());
	}
}
