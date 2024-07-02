package com.cakk.api.mapper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.cakk.api.dto.response.cake.CakeDetailResponse;
import com.cakk.api.dto.response.cake.CakeImageListResponse;
import com.cakk.api.dto.response.like.HeartCakeImageListResponse;
import com.cakk.domain.mysql.dto.param.cake.CakeDetailParam;
import com.cakk.domain.mysql.dto.param.cake.CakeImageResponseParam;
import com.cakk.domain.mysql.dto.param.like.HeartCakeImageResponseParam;
import com.cakk.domain.mysql.dto.param.tag.TagParam;
import com.cakk.domain.mysql.entity.cake.Cake;

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

		return HeartCakeImageListResponse.builder()
			.cakeImages(cakeImages)
			.lastCakeHeartId(cakeImages.isEmpty() ? null : cakeImages.get(size - 1).cakeHeartId())
			.size(cakeImages.size())
			.build();
	}

	public static CakeDetailResponse cakeDetailResponseFromParam(CakeDetailParam param) {
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

	private static boolean isEmptyTag(TagParam tagParam) {
		return tagParam.tagId() == null || tagParam.tagName() == null;
	}
}
