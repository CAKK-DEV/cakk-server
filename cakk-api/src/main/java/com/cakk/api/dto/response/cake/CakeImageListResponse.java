package com.cakk.api.dto.response.cake;

import java.util.List;

import lombok.Builder;

import com.cakk.domain.dto.param.cake.CakeImageResponseParam;

@Builder
public record CakeImageListResponse(

	List<CakeImageResponseParam> cakeImages,
	Long lastCakeId,
	int size
) {

	public static CakeImageListResponse from(List<CakeImageResponseParam> cakeImages) {
		int size = cakeImages.size();

		return CakeImageListResponse.builder()
			.cakeImages(cakeImages)
			.lastCakeId(cakeImages.isEmpty() ? null : cakeImages.get(size - 1).cakeId())
			.size(cakeImages.size())
			.build();
	}
}
