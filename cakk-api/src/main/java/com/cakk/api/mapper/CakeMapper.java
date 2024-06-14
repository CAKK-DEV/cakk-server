package com.cakk.api.mapper;

import java.util.List;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.cakk.api.dto.response.cake.CakeImageListResponse;
import com.cakk.api.dto.response.like.HeartCakeImageListResponse;
import com.cakk.domain.mysql.dto.param.cake.CakeImageResponseParam;
import com.cakk.domain.mysql.dto.param.like.HeartCakeImageResponseParam;

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

	public static HeartCakeImageListResponse supplyHeartCakeImageListResponseBy(final List<HeartCakeImageResponseParam> cakeImages) {
		final int size = cakeImages.size();

		return HeartCakeImageListResponse.builder()
			.cakeImages(cakeImages)
			.lastCakeHeartId(cakeImages.isEmpty() ? null : cakeImages.get(size - 1).cakeHeartId())
			.size(cakeImages.size())
			.build();
	}
}
